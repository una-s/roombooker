import { NavBarRecepcionar } from "./NavBarRecepcionar";
import { Footer } from "./Footer";
import pattern from "../slike/pattern4.jpg";
import { useEffect, useState } from "react";

export function PravljenjeRezervacije() {
    const [rezervacije, setRezervacije] = useState([]);
    const [notification, setNotification] = useState(""); // za prikaz poruke
    const recepcionarId = parseInt(document.cookie.split("userId=")[1]?.split(";")[0]);


    useEffect(() => {
        fetch("http://localhost:8080/reservations/getReservationStatus/PENDING")
            .then((res) => res.json())
            .then((data) => {
                if (data?.data?.values) {
                    setRezervacije(data.data.values);
                }
            })
            .catch((err) => console.error("Greška pri učitavanju rezervacija:", err));
    }, []);

    async function promeniStatus(rezervacija, noviStatus) {
        try {
            const izmenjenaRezervacija = {
                ...rezervacija,
                statusRezervacije: noviStatus,
                recepcionar: { idRecepcionar: recepcionarId } // dodaj ID recepcionara
            };


            const res = await fetch("http://localhost:8080/reservations/update", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(izmenjenaRezervacija),
            });

            if (res.ok) {
                setRezervacije((prev) =>
                    prev.map((r) =>
                        r.idRezervacija === rezervacija.idRezervacija
                            ? { ...r, statusRezervacije: noviStatus }
                            : r
                    )
                );

                // Prikaz poruke
                setNotification(`Rezervacija ${noviStatus === "CONFIRMED" ? "odobrena" : "otkazana"}!`);
                setTimeout(() => setNotification(""), 3000); // nestaje posle 3 sekunde
            } else {
                alert("Neuspešno ažuriranje rezervacije!");
            }
        } catch (err) {
            console.error("Greška pri ažuriranju rezervacije:", err);
            alert("Greška pri komunikaciji sa serverom.");
        }
    }

    return (
        <>
            <div
                style={{
                    minHeight: "100vh",
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "space-between",
                }}
            >
                <NavBarRecepcionar />

                {/* Poruka obaveštenja */}
                {notification && (
                    <div
                        style={{
                            position: "fixed",
                            top: "80px",
                            left: "50%",
                            transform: "translateX(-50%)",
                            backgroundColor: "#4caf50",
                            color: "white",
                            padding: "10px 20px",
                            borderRadius: "5px",
                            zIndex: 1000,
                            boxShadow: "0 2px 10px rgba(0,0,0,0.3)",
                        }}
                    >
                        {notification}
                    </div>
                )}

                <div
                    style={{
                        backgroundImage: `url(${pattern})`,
                        backgroundSize: "cover",
                        backgroundPosition: "center",
                        minHeight: "100vh",
                        padding: "40px",
                    }}
                >
                    <div
                        style={{
                            display: "flex",
                            flexWrap: "wrap",
                            gap: "20px",
                            justifyContent: "flex-start",
                            padding: "20px",
                        }}
                    >
                        {rezervacije.map((rezervacija) => (
                            <div
                                key={rezervacija.idRezervacija}
                                style={{
                                    background: "#ffffffff",
                                    margin: "10px 0",
                                    padding: "15px",
                                    borderRadius: "8px",
                                    color: "black",
                                    width: "250px",
                                    boxShadow: "0 2px 10px rgba(0,0,0,0.2)",
                                    display: "flex",
                                    flexDirection: "column",
                                    gap: "5px",
                                }}
                            >
                                <p>Pocetak: {rezervacija.datumPrijave}</p>
                                <p>Kraj: {rezervacija.datumOdjave}</p>
                                <p>Broj osoba: {rezervacija.brojOsoba}</p>
                                <p>Iznos: {rezervacija.ukupanIznos} din</p>
                                <p>Broj sobe: {rezervacija.soba.brojSobe}</p>
                                <p>Sprat: {rezervacija.soba.sprat}</p>
                                <p>Tip: {rezervacija.soba.tipSobe.naziv}</p>
                                <p>Cena po noći: {rezervacija.soba.tipSobe.cenaPoNoci} din</p>
                                <p>{rezervacija.soba.tipSobe.opis}</p>
                                <p>Zahtev podneo: {rezervacija.gost.email}</p>

                                <div
                                    style={{
                                        display: "flex",
                                        gap: "10px",
                                        justifyContent: "space-between",
                                        marginTop: "10px",
                                    }}
                                >
                                    <button
                                        style={{
                                            flex: 1,
                                            padding: "5px",
                                            borderRadius: "5px",
                                            border: "none",
                                            backgroundColor: "#4CAF50",
                                            color: "white",
                                            cursor: "pointer",
                                        }}
                                        onClick={() => promeniStatus(rezervacija, "CONFIRMED")}
                                    >
                                        Odobri
                                    </button>
                                    <button
                                        style={{
                                            flex: 1,
                                            padding: "5px",
                                            borderRadius: "5px",
                                            border: "none",
                                            backgroundColor: "#f44336",
                                            color: "white",
                                            cursor: "pointer",
                                        }}
                                        onClick={() => promeniStatus(rezervacija, "CANCELLED")}
                                    >
                                        Odbij
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
            <Footer />
        </>
    );
}
