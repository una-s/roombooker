import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { NavBarRecepcionar } from "./NavBarRecepcionar";
import { Footer } from "./Footer";

export function SobaDetalji() {
    const { idSoba } = useParams();
    const [rezervacije, setRezervacije] = useState([]);
    const [soba, setSoba] = useState(null);
    const [gosti, setGosti] = useState([]); // lista svih gostiju
    const [odabraniGostId, setOdabraniGostId] = useState(""); // ID iz dropdown-a
    const [datumOd, setDatumOd] = useState("");
    const [datumDo, setDatumDo] = useState("");
    const [brojOsoba, setBrojOsoba] = useState("");

    // Funkcija za čitanje cookie-a po imenu
    const getCookie = (name) => {
        const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
        return match ? match[2] : null;
    };

    const recepcionarId = parseInt(getCookie("userId")); // ID ulogovanog recepcionara

    // Učitavanje sobe
    useEffect(() => {
        fetch(`http://localhost:8080/rooms/${idSoba}`)
            .then((res) => res.json())
            .then((data) => {
                if (data?.data?.value) setSoba(data.data.value);
            })
            .catch((err) => console.error("Greška pri učitavanju sobe:", err));
    }, [idSoba]);

    // Učitavanje rezervacija za sobu
    useEffect(() => {
        fetch(`http://localhost:8080/reservations/getReservationsForRoom/${idSoba}`)
            .then((res) => res.json())
            .then((data) => {
                if (data?.data?.values) setRezervacije(data.data.values);
            })
            .catch((err) => console.error("Greška pri učitavanju rezervacija za sobu:", err));
    }, [idSoba]);

    // Učitavanje gostiju za dropdown
    useEffect(() => {
        fetch("http://localhost:8080/guests/all") // pretpostavljam da postoji endpoint za sve goste
            .then((res) => res.json())
            .then((data) => {
                if (data?.data?.values) setGosti(data.data.values);
            })
            .catch((err) => console.error("Greška pri učitavanju gostiju:", err));
    }, []);

    // Dodavanje nove rezervacije
    const handleAddReservation = async (e) => {
        e.preventDefault();

        if (!soba) {
            alert("Podaci o sobi nisu učitani.");
            return;
        }

        if (!odabraniGostId) {
            alert("Morate odabrati gosta!");
            return;
        }

        const datumP = new Date(datumOd);
        const datumO = new Date(datumDo);
        const brojNoci = Math.ceil((datumO - datumP) / (1000 * 60 * 60 * 24));

        if (brojNoci <= 0) {
            alert("Datum odjave mora biti posle datuma prijave!");
            return;
        }

        const broj = parseInt(brojOsoba);
        const ukupanIznos = soba.tipSobe?.cenaPoNoci * brojNoci * broj;

        const odabraniGost = gosti.find(g => g.idGost === parseInt(odabraniGostId));

        const novaRez = {
            datumPrijave: datumOd,
            datumOdjave: datumDo,
            brojOsoba: broj,
            statusRezervacije: "CONFIRMED",
            gost: { idGost: odabraniGost.idGost, email: odabraniGost.email },
            soba: { idSoba: parseInt(idSoba) },
            recepcionar: { idRecepcionar: recepcionarId },
            ukupanIznos,
            datumKreiranja: new Date().toISOString().split("T")[0],
        };

        try {
            const response = await fetch(
                "http://localhost:8080/reservations/createByReceptionist",
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(novaRez),
                }
            );

            const data = await response.json();
            if (!response.ok) {
                alert("Greška pri kreiranju rezervacije: " + (data.message || "Nepoznata greška"));
                return;
            }

            alert(data.message || "Rezervacija je uspešno dodata!");
            setRezervacije((prev) => [...prev, novaRez]);
            setOdabraniGostId("");
            setDatumOd("");
            setDatumDo("");
            setBrojOsoba("");
        } catch (err) {
            console.error("Greška:", err);
            alert("Greška pri kreiranju rezervacije!");
        }
    };

    return (
        <>
            <NavBarRecepcionar />
            <div style={{ padding: "40px" }}>
                <h2>Rezervacije za sobu #{idSoba}</h2>

                {rezervacije.length > 0 ? (
                    <table style={{
                        width: "100%",
                        borderCollapse: "collapse",
                        marginTop: "20px",
                        textAlign: "left"
                    }}>
                        <thead>
                            <tr style={{ backgroundColor: "#e7b7ffff" }}>
                                <th style={{ padding: "8px", border: "1px solid #ccc", width: "10%" }}>ID</th>
                                <th style={{ padding: "8px", border: "1px solid #ccc", width: "20%" }}>Gost</th>
                                <th style={{ padding: "8px", border: "1px solid #ccc", width: "15%" }}>Datum prijave</th>
                                <th style={{ padding: "8px", border: "1px solid #ccc", width: "15%" }}>Datum odjave</th>
                                <th style={{ padding: "8px", border: "1px solid #ccc", width: "10%" }}>Broj osoba</th>
                                <th style={{ padding: "8px", border: "1px solid #ccc", width: "15%" }}>Ukupan iznos (RSD)</th>
                                <th style={{ padding: "8px", border: "1px solid #ccc", width: "10%" }}>Status</th>
                                <th style={{ padding: "8px", border: "1px solid #ccc", width: "15%" }}>Recepcionar</th>
                            </tr>
                        </thead>
                        <tbody>
                            {rezervacije.map((r) => (
                                <tr key={r.idRezervacija}>
                                    <td style={{ padding: "8px", border: "1px solid #ccc" }}>{r.idRezervacija}</td>
                                    <td style={{ padding: "8px", border: "1px solid #ccc" }}>{r.gost?.email || "Nepoznat"}</td>
                                    <td style={{ padding: "8px", border: "1px solid #ccc" }}>{r.datumPrijave}</td>
                                    <td style={{ padding: "8px", border: "1px solid #ccc" }}>{r.datumOdjave}</td>
                                    <td style={{ padding: "8px", border: "1px solid #ccc" }}>{r.brojOsoba}</td>
                                    <td style={{ padding: "8px", border: "1px solid #ccc" }}>{r.ukupanIznos?.toFixed(2) || "-"}</td>
                                    <td style={{ padding: "8px", border: "1px solid #ccc" }}>{r.statusRezervacije}</td>
                                    <td style={{ padding: "8px", border: "1px solid #ccc" }}>{r.recepcionar?.idRecepcionar || "Nepoznat"}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                ) : (
                    <p>Nema rezervacija za ovu sobu.</p>
                )}

                <hr style={{ margin: "30px 0" }} />

                <h3>Kreiraj novu rezervaciju</h3>
                <form onSubmit={handleAddReservation}>
                    <label>Odaberi gosta:</label>
                    <select
                        value={odabraniGostId}
                        onChange={(e) => setOdabraniGostId(e.target.value)}
                        required
                        style={{ display: "block", marginBottom: "10px", width: "300px" }}
                    >
                        <option value="">-- Odaberi gosta --</option>
                        {gosti.map((g) => (
                            <option key={g.idGost} value={g.idGost}>
                                {g.email}
                            </option>
                        ))}
                    </select>

                    <label>Datum prijave:</label>
                    <input
                        type="date"
                        value={datumOd}
                        onChange={(e) => setDatumOd(e.target.value)}
                        required
                        style={{ display: "block", marginBottom: "10px" }}
                    />

                    <label>Datum odjave:</label>
                    <input
                        type="date"
                        value={datumDo}
                        onChange={(e) => setDatumDo(e.target.value)}
                        required
                        style={{ display: "block", marginBottom: "10px" }}
                    />

                    <label>Broj osoba:</label>
                    <input
                        type="number"
                        value={brojOsoba}
                        onChange={(e) => setBrojOsoba(e.target.value)}
                        required
                        style={{ display: "block", marginBottom: "10px", width: "100px" }}
                    />

                    {datumOd && datumDo && brojOsoba && soba && (
                        <p style={{ marginTop: "10px", fontWeight: "bold" }}>
                            Ukupan iznos:{" "}
                            {(soba.tipSobe.cenaPoNoci *
                                Math.ceil((new Date(datumDo) - new Date(datumOd)) / (1000 * 60 * 60 * 24)) *
                                brojOsoba
                            ).toFixed(2)}{" "}
                            RSD
                        </p>
                    )}

                    <button
                        type="submit"
                        style={{
                            backgroundColor: "#b84aff",
                            color: "white",
                            border: "none",
                            padding: "8px 15px",
                            borderRadius: "6px",
                            cursor: "pointer",
                        }}
                    >
                        Dodaj rezervaciju
                    </button>
                </form>
            </div>
            <Footer />
        </>
    );
}
