import { useEffect, useState } from "react";
import { NavBarRecepcionar } from "./NavBarRecepcionar";
import pattern from "../slike/pattern4.jpg";

export function RezervacijeRecepcionar() {
    const [rezervacije, setRezervacije] = useState([]);
    const [filter, setFilter] = useState("ALL");


    const handleGenerateConfirmation = (rezervacija) => {
        // Za sada samo alert sa osnovnim informacijama
        alert(
            `Potvrda rezervacije:\n` +
            `Rezervacija #${rezervacija.idRezervacija}\n` +
            `Gost: ${rezervacija.gost?.ime} ${rezervacija.gost?.prezime}\n` +
            `Soba: ${rezervacija.soba?.brojSobe}\n` +
            `Datum prijave: ${rezervacija.datumPrijave}\n` +
            `Datum odjave: ${rezervacija.datumOdjave}\n` +
            `Broj osoba: ${rezervacija.brojOsoba}\n` +
            `Ukupan iznos: ${rezervacija.ukupanIznos} RSD\n` +
            `Status: ${rezervacija.statusRezervacije}`
        );

        // Kasnije ovde možeš dodati generisanje PDF-a ili modal sa potvrdom
    };


    useEffect(() => {
        fetch("http://localhost:8080/reservations/all")
            .then(res => res.json())
            .then(data => {
                if (data && data.data && data.data.values) {
                    setRezervacije(data.data.values);
                }
            })
            .catch(err => console.error("Greška pri učitavanju rezervacija:", err));
    }, []);

    const handleDelete = async (idRezervacija) => {
        if (!window.confirm("Da li ste sigurni da želite da obrišete ovu rezervaciju?")) return;

        try {
            const response = await fetch("http://localhost:8080/reservations/delete", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(idRezervacija),
            });
            const data = await response.json();
            if (response.ok) {
                alert("Rezervacija uspešno obrisana!");
                setRezervacije(prev => prev.filter(r => r.idRezervacija !== idRezervacija));
            } else {
                alert("Greška pri brisanju rezervacije: " + data.message);
            }
        } catch (err) {
            console.error(err);
        }
    };

    const filtriraneRezervacije = filter === "ALL" //ako je status all prikazi sve u suprotnom samo filtrirane
        ? rezervacije
        : rezervacije.filter(r => r.statusRezervacije === filter);

    const statusi = ["ALL", "CONFIRMED", "CHECKED_IN", "CHECKED_OUT", "CANCELLED"];

    return (
        <>
            <NavBarRecepcionar />
            <div style={{
                backgroundImage: `url(${pattern})`,
                backgroundSize: "cover",
                backgroundPosition: "center",
                minHeight: "100vh",
                padding: "40px",
            }}>
                <h2 style={{
                    textAlign: "center",
                    color: "white",
                    backgroundColor: "rgba(215, 96, 255, 0.6)",
                    padding: "15px 30px",
                    borderRadius: "10px",
                    display: "inline-block",
                }}>
                    Pregled rezervacija
                </h2>

                {/* Filter dugmad */}
                <div style={{ display: "flex", justifyContent: "center", marginBottom: "20px" }}>
                    {statusi.map(s => (
                        <button
                            key={s}
                            onClick={() => setFilter(s)}
                            style={{
                                margin: "0 8px",
                                padding: "8px 16px",
                                backgroundColor: filter === s ? "#ce7affff" : "#ffffffff",
                                color: filter === s ? "white" : "black",
                                border: "none",
                                borderRadius: "6px",
                                cursor: "pointer",
                            }}
                        >
                            {s === "ALL" ? "Sve" : s.replace("_", " ")} {/*ispis u dugmetu*/}
                        </button>
                    ))}
                </div>

                {/* Kartice rezervacija */}
                <div style={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
                    gap: "20px",
                    padding: "0 20px",
                }}>
                    {filtriraneRezervacije.length > 0 ? (
                        filtriraneRezervacije.map(rez => (
                            <div key={rez.idRezervacija} style={{
                                backgroundColor: "#f7f9fc",
                                borderRadius: "10px",
                                padding: "20px",
                                boxShadow: "0px 2px 8px rgba(0,0,0,0.1)",
                            }}>
                                <h3>Rezervacija #{rez.idRezervacija}</h3>
                                <p><strong>Gost:</strong> {rez.gost?.ime} {rez.gost?.prezime}</p>
                                <p><strong>Soba:</strong> {rez.soba?.brojSobe}</p>
                                <p><strong>Datum prijave:</strong> {rez.datumPrijave}</p>
                                <p><strong>Datum odjave:</strong> {rez.datumOdjave}</p>
                                <p><strong>Status:</strong> {rez.statusRezervacije}</p>
                                <p><strong>Ukupan iznos:</strong> {rez.ukupanIznos} RSD</p>

                                <div style={{ display: "flex", justifyContent: "flex-end", marginTop: "10px" }}>
                              {/*react prop*/}<button onClick={() => handleDelete(rez.idRezervacija)} style={{
                                        backgroundColor: "#fff174ff",
                                        color: "black",
                                        border: "none",
                                        borderRadius: "6px",
                                        padding: "6px 12px",
                                        cursor: "pointer",
                                    }}>Obriši</button>
                                    <button
                                        onClick={() => handleGenerateConfirmation(rez)}
                                        style={{
                                            backgroundColor: "#fff174ff",
                                            color: "black",
                                            border: "none",
                                            borderRadius: "6px",
                                            padding: "6px 12px",
                                            cursor: "pointer",
                                            marginLeft: "8px" // razmak između dugmadi
                                        }}
                                    >
                                        Generiši potvrdu
                                    </button>

                                </div>
                            </div>
                        ))
                    ) : (
                        <p style={{ textAlign: "center", width: "100%" }}>
                            Nema rezervacija za odabrani status.
                        </p>
                    )}
                </div>
            </div>
        </>
    );
}
