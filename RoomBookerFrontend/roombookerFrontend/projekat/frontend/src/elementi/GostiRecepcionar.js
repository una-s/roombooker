import { useEffect, useState } from "react";
import { Footer } from "./Footer";
import { NavBarRecepcionar } from "./NavBarRecepcionar";
import pattern from "../slike/pattern4.jpg";

export function GostiRecepcionar() {
    const [gosti, setGosti] = useState([]);
    const [izabraniGost, setIzabraniGost] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [search, setSearch] = useState("");
    const [gradovi, setGradovi] = useState([]);

    // Učitavanje gostiju iz backenda
    useEffect(() => {
        fetch("http://localhost:8080/guests/all")
            .then((res) => res.json())
            .then((data) => {
                if (data && data.data && data.data.values) {
                    setGosti(data.data.values);
                }
            })
            .catch((err) => console.error("Greška pri učitavanju gostiju:", err));
    }, []);

    // Učitavanje gradova iz backenda
    useEffect(() => {
        fetch("http://localhost:8080/grad/all")
            .then((res) => res.json())
            .then((data) => {
                if (data && data.data && data.data.values) {
                    setGradovi(data.data.values);
                }
            })
            .catch((err) => console.error("Greška pri učitavanju gradova:", err));
    }, []);

    // Brisanje gosta po email-u
    const handleDelete = async (email) => {
        //js ugradjeni dijalog: nudi opcije ok i cancel(true i false)
        if (!window.confirm("Da li ste sigurni da želite da obrišete ovog gosta?")) return;

        try {//await pauzira izvrsavanje funkcije dok se Promise ne resi, i zatim taj res ne mora da se raspakuje
            const res = await fetch(`http://localhost:8080/guests/delete/${email}`, {
                method: "DELETE",
            });

            const data = await res.json();
            //res sadrzi informacije o odgovoru backenda, res.ok moze biti true ili false
            if (res.ok) {//azuriramo state gosti, novu vrednost gosti ce ciniti
                //svi gosti koji zadovoljavaju uslov da im je email drugaciji od prosledjenog za brisanje 
                setGosti(prevGosti => prevGosti.filter(g => g.email !== email));
                alert(data.message || "Gost je uspešno obrisan!");
            } else {
                alert(data.message || "Došlo je do greške prilikom brisanja gosta.");
            }
        } catch (err) {
            console.error(err);
            alert("Došlo je do greške prilikom brisanja gosta.");
        }
    };

    // Otvaranje modala za dodavanje ili izmenu
    //mozemo proslediti ili gosta ako vrsimo izmenu ili null ukoliko dodajemo novog gosta
    const handleEdit = (gost = null) => {
        //ako postoji gost uzmi sve njegove podatke i kopiraj({...}) u novi objekat
        //ako ne postoji kreira se novi objekat s odgovarajucim pocetnim vrednostima 
        setIzabraniGost(gost ? { ...gost } : {
            ime: "", prezime: "", email: "", telefon: "", adresa: "",
            datumRodjenja: "", lozinka: "", grad: { idGrad: 1 }
        });
        setShowModal(true);
    };

    // Čuvanje gosta (dodavanje ili izmena)
    const handleSave = async () => {
        if (!izabraniGost) return;

        // Provera email-a
        const emailPostoji = gosti.some(
            (g) => g.email.toLowerCase() === izabraniGost.email.toLowerCase()
                && g.idGost !== izabraniGost.idGost
        );

        if (emailPostoji) {
            alert("Ovaj email već postoji. Unesite drugi email.");
            return;
        }

        // Provera telefona
        const telefonPostoji = gosti.some(
            (g) => g.telefon === izabraniGost.telefon
                && g.idGost !== izabraniGost.idGost
        );

        if (telefonPostoji) {
            alert("Ovaj broj telefona već postoji. Unesite drugi broj.");
            return;
        }

        const url = izabraniGost.idGost
            ? "http://localhost:8080/guests/update"
            : "http://localhost:8080/guests/add";

        const response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" }, //u kom formatu se salju podaci
            body: JSON.stringify(izabraniGost),
        });

        if (response.ok) {
            //ako ima id, odnosno radimo izmenu, onda se prolazi kroz svakog gosta
            //i kad se nadje onaj ciji je id jednak ovom koga menjamo dodamo izabranog u listu(sa forme)
            //a kad nije jednak id dodamo tog originalnog gosta
            const updatedGuests = izabraniGost.idGost
                ? gosti.map((g) => (g.idGost === izabraniGost.idGost ? izabraniGost : g))
                : [...gosti, izabraniGost];
            //ako ne postoji id gosta (dodajemo novog gosta), onda u novu listu gostiju
            //kopiramo sve stare goste i na kraj dodajemo novog
            setGosti(updatedGuests);
            setShowModal(false);
            //postavljamo nove goste i zatvaramo formu za izmenu ili dodavanje
            alert("Gost je uspešno sačuvan!");
        } else {
            alert("Došlo je do greške pri čuvanju gosta.");
        }
    };

    // Filtriranje gostiju po search inputu
    const filteredGosti = gosti.filter(
        (g) =>
            g.ime.toLowerCase().includes(search.toLowerCase()) ||
            g.prezime.toLowerCase().includes(search.toLowerCase()) ||
            g.email.toLowerCase().includes(search.toLowerCase())
    );

    return (
        <div style={{ minHeight: "100vh", display: "flex", flexDirection: "column", justifyContent: "space-between" }}>
            <NavBarRecepcionar />

            <div
                style={{
                    backgroundImage: `url(${pattern})`,
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    minHeight: "100vh",
                    padding: "40px",
                }}
            >
                <h2 style={{
                    textAlign: "center",
                    color: "white",
                    backgroundColor: "rgba(226, 108, 255, 0.7)",
                    padding: "15px 30px",
                    borderRadius: "10px",
                    display: "inline-block",
                    marginBottom: "30px",
                }}>
                    Lista gostiju
                </h2>

                <div style={{ marginBottom: "20px", textAlign: "center" }}>
                    <input
                        type="text"
                        placeholder="Pretraži goste..."
                        value={search} //u polju ce se prikazivati vrednost ove promenljvie
                        onChange={(e) => setSearch(e.target.value)} //kad god se vrednost polja promeni ovo se pozove
                        style={{ padding: "10px", width: "300px", borderRadius: "5px", border: "1px solid #ccc" }}
                    />
                    <button
                        onClick={() => handleEdit(null)}
                        style={{
                            marginLeft: "10px",
                            padding: "10px 15px",
                            borderRadius: "5px",
                            backgroundColor: "#9d3dd5ff",
                            color: "white",
                            border: "none",
                            cursor: "pointer",
                        }}
                    >
                        Dodaj gosta
                    </button>
                </div>

                <div style={{ display: "flex", flexWrap: "wrap", gap: "20px", justifyContent: "center" }}>
                    {filteredGosti.map((gost) => (//id za identifikovanje elemenata u listi
                        <div key={gost.idGost} style={{
                            backgroundColor: "rgba(255,255,255,0.9)",
                            width: "280px",
                            borderRadius: "10px",
                            padding: "15px",
                            textAlign: "left",
                            boxShadow: "0 2px 10px rgba(0,0,0,0.2)",
                        }}>
                            <p><strong>Ime:</strong> {gost.ime} {gost.prezime}</p>
                            <p><strong>Email:</strong> {gost.email}</p>
                            <p><strong>Telefon:</strong> {gost.telefon}</p>
                            <p><strong>Adresa:</strong> {gost.adresa}</p>
                            <p><strong>Grad:</strong> {gost.grad?.name || "Nepoznat"}</p>
                            <p><strong>Datum rođenja:</strong> {gost.datumRodjenja}</p>

                            <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px", marginTop: "10px" }}>
                                <button
                                    onClick={() => handleEdit(gost)}
                                    style={{ backgroundColor: "#e3dd61ff", color: "black", border: "none", padding: "5px 10px", borderRadius: "5px", cursor: "pointer" }}
                                >
                                    Izmeni
                                </button>
                                <button
                                    onClick={() => handleDelete(gost.email)}
                                    style={{ backgroundColor: "#e3dd61ff", color: "black", border: "none", padding: "5px 10px", borderRadius: "5px", cursor: "pointer" }}
                                >
                                    Obriši
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {/* MODAL ZA IZMENE I DODAVANJE */}
            {showModal && ( //ako je showmodal true prikazi ovo
                <div style={{
                    position: "fixed",
                    top: 0,
                    left: 0,
                    width: "100%",
                    height: "100%",
                    backgroundColor: "rgba(0,0,0,0.6)",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                }}>
                    <div style={{ backgroundColor: "white", padding: "20px", borderRadius: "10px", width: "400px" }}>
                        <h3>{izabraniGost.idGost ? "Izmeni gosta" : "Dodaj gosta"}</h3>

                        <label>Ime:</label>
                        <input type="text" value={izabraniGost.ime} onChange={(e) => setIzabraniGost({ ...izabraniGost, ime: e.target.value })} style={{ width: "100%", marginBottom: "10px" }} />
                       {/* uzmi sve podatke za izabranog gosta, ali za ime uzmi vrednost koju korisnik unosi */}
                        <label>Prezime:</label>
                        <input type="text" value={izabraniGost.prezime} onChange={(e) => setIzabraniGost({ ...izabraniGost, prezime: e.target.value })} style={{ width: "100%", marginBottom: "10px" }} />

                        <label>Email:</label>
                        <input type="email" value={izabraniGost.email} onChange={(e) => setIzabraniGost({ ...izabraniGost, email: e.target.value })} style={{ width: "100%", marginBottom: "10px" }} />

                        <label>Telefon:</label>
                        <input type="text" value={izabraniGost.telefon} onChange={(e) => setIzabraniGost({ ...izabraniGost, telefon: e.target.value })} style={{ width: "100%", marginBottom: "10px" }} />

                        <label>Adresa:</label>
                        <input type="text" value={izabraniGost.adresa} onChange={(e) => setIzabraniGost({ ...izabraniGost, adresa: e.target.value })} style={{ width: "100%", marginBottom: "10px" }} />

                        <label>Datum rođenja:</label>
                        <input type="date" value={izabraniGost.datumRodjenja} onChange={(e) => setIzabraniGost({ ...izabraniGost, datumRodjenja: e.target.value })} style={{ width: "100%", marginBottom: "10px" }} />

                        <label>Lozinka:</label>
                        <input type="password" value={izabraniGost.lozinka || ""} onChange={(e) => setIzabraniGost({ ...izabraniGost, lozinka: e.target.value })} style={{ width: "100%", marginBottom: "10px" }} />

                        <label>Grad:</label> {/*value je trenutno izabrana vrednost iz padajuceg menija*/}
                        <select value={izabraniGost.grad?.id || ""} onChange={(e) =>
                            setIzabraniGost({
                                ...izabraniGost,
                                grad: gradovi.find(g => g.id === parseInt(e.target.value))
                            })
                        } style={{ width: "100%", marginBottom: "10px" }}>
                            <option value="">Izaberite grad</option>{/*ako nista nije odabrano za vrednost grada ce se prikazati ovaj tekst*/}
                            {gradovi.map((g) => (
                                <option key={g.id} value={g.id}>{g.name}</option>
                            ))}{/*za mapiranje moze da se iskoristi key, znaci za svaki grad ce se za
                            odabrani id ispisati naziv grada*/}
                        </select>

                        <div style={{ display: "flex", justifyContent: "flex-end" }}>
                            <button onClick={() => setShowModal(false)} style={{ marginRight: "10px", backgroundColor: "gray", color: "white", border: "none", borderRadius: "5px", padding: "5px 10px" }}>
                                Otkaži
                            </button>
                            <button onClick={handleSave} style={{ backgroundColor: "purple", color: "white", border: "none", borderRadius: "5px", padding: "5px 10px" }}>
                                Sačuvaj
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <Footer />
        </div>
    );
}
