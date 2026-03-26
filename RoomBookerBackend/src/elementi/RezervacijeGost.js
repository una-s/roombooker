import { useEffect, useState } from "react";
import { NavBar } from "./NavBar";
import pattern from "../slike/pattern4.jpg";

export function RezervacijeGost() {
    const [rezervacije, setRezervacije] = useState([]);

    // ID ulogovanog gosta iz cookie-ja
    const userId = parseInt(document.cookie.split("userId=")[1]?.split(";")[0]);


    useEffect(() => {
        if (!userId) return;

        // Dohvati sve rezervacije samo za ulogovanog gosta
        fetch("http://localhost:8080/reservations/getGuestsReservations", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ idGost: userId })
        })
        .then(res => res.json())
        .then(data => {
            if (data?.data?.values) {
                setRezervacije(data.data.values);
            }
        })
        .catch(err => console.error("Greška pri učitavanju rezervacija:", err));
    }, [userId]);

    return (
        <>
            <NavBar />
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
                    Moje rezervacije
                </h2>

                <div style={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
                    gap: "20px",
                    padding: "0 20px",
                }}>
                    {rezervacije.length > 0 ? rezervacije.map(rez => (
                        <div key={rez.idRezervacija} style={{
                            backgroundColor: "#f7f9fc",
                            borderRadius: "10px",
                            padding: "20px",
                            boxShadow: "0px 2px 8px rgba(0,0,0,0.1)",
                        }}>
                            <h3>Rezervacija #{rez.idRezervacija}</h3>
                            <p><strong>Soba:</strong> {rez.soba?.brojSobe}</p>
                            <p><strong>Datum prijave:</strong> {rez.datumPrijave}</p>
                            <p><strong>Datum odjave:</strong> {rez.datumOdjave}</p>
                            <p><strong>Status:</strong> {rez.statusRezervacije}</p>
                            <p><strong>Broj osoba:</strong> {rez.brojOsoba}</p>
                            <p><strong>Ukupan iznos:</strong> {rez.ukupanIznos} RSD</p>
                        </div>
                    )) : (
                        <p style={{ textAlign: "center", width: "100%" }}>
                            Nemate rezervacija.
                        </p>
                    )}
                </div>
            </div>
        </>
    );
}
