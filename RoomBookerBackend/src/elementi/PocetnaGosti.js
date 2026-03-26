import { useState, useEffect } from "react"; // Uvozimo useState za upravljanje stanjem i useEffect za sporedne efekte
import { NavBar } from "./NavBar"; // Uvozimo NavBar komponentu za navigaciju
import { Footer } from "./Footer"; // Uvozimo Footer komponentu za podnožje stranice
import pattern from "../slike/pattern4.jpg"; // Uvozimo sliku za pozadinu

// Glavna PocetnaGosti komponenta
export function PocetnaGosti() {
    // Stanja za prikaz i filtriranje soba
    const [sobe, setSobe] = useState([]); // Svi podaci o sobama
    const [filterSlobodne, setFilterSlobodne] = useState(false); // Filter za prikaz samo dostupnih soba
    const [filterTip, setFilterTip] = useState(""); // Filter po tipu sobe
    const [tipoviSoba, setTipoviSoba] = useState([]); // Lista svih dostupnih tipova soba

    // Stanja za modal (dijalog) i podatke za rezervaciju
    const [showModal, setShowModal] = useState(false); // Kontroliše vidljivost modala
    const [izabranaSoba, setIzabranaSoba] = useState(null); // Soba koju je korisnik izabrao za rezervaciju
    const [datumPrijave, setDatumPrijave] = useState(""); // Datum prijave za rezervaciju
    const [datumOdjave, setDatumOdjave] = useState(""); // Datum odjave za rezervaciju
    const [brojOsoba, setBrojOsoba] = useState(1); // Broj osoba za rezervaciju

    // Dohvatanje ID-a trenutno ulogovanog korisnika iz kolačića
    // Koristi split() i ?. za sigurno pristupanje vrednosti
    const userId = parseInt(document.cookie.split("userId=")[1]?.split(";")[0]); // trenutni gost

    // Funkcija za izračunavanje ukupne cene rezervacije
    const izracunajUkupno = () => {
        if (!datumPrijave || !datumOdjave) return 0; // Ako datumi nisu uneti, cena je 0
        const start = new Date(datumPrijave);
        const end = new Date(datumOdjave);
        const diffTime = end - start; // Razlika u milisekundama
        const brojNoci = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); // Broj noći (zaokruženo na više)

        const cenaPoNoci = izabranaSoba?.tipSobe?.cenaPoNoci || 0; // Cena po noći za izabranu sobu
        return cenaPoNoci * brojNoci * brojOsoba; // Ukupna cena
    };

    // useEffect hook za učitavanje svih soba i tipova soba prilikom prvog renderovanja komponente
    useEffect(() => {
        fetch("http://localhost:8080/rooms/all") // GET zahtev za sve sobe
            .then((res) => res.json())
            .then((data) => {
                if (data?.data?.values) setSobe(data.data.values); // Postavljamo sobe u stanje

                // Ekstraktujemo jedinstvene tipove soba iz dobijenih podataka
                const tipovi = Array.from(new Set(data.data.values.map(s => s.tipSobe?.naziv))).filter(Boolean);
                setTipoviSoba(tipovi); // Postavljamo tipove soba u stanje
            })
            .catch((err) => console.error("Greška pri učitavanju soba:", err)); // Hvatanje grešaka
    }, []); // Prazan niz [] znači da se efekat pokreće samo jednom (na montiranju)

    // Filtriranje soba na osnovu postavljenih filtera
    const filtriraneSobe = sobe.filter((soba) => {
        const slobodna = !filterSlobodne || soba.dostupna; // Proverava dostupnost (ako je filter uključen)
        const tip = !filterTip || (soba.tipSobe?.naziv === filterTip); // Proverava tip sobe (ako je filter izabran)
        return slobodna && tip; // Vraća sobe koje zadovoljavaju oba kriterijuma
    });

    // Funkcija koja se poziva kada korisnik klikne na neku sobu
    const handleSobaClick = (soba) => {
        if(!soba.dostupna){ // Proverava da li je soba dostupna pre otvaranja modala
            alert("Soba je nedostupna!");
            return;
        }
        setIzabranaSoba(soba); // Postavlja izabranu sobu
        setDatumPrijave(""); // Resetuje datume i broj osoba za novi zahtev
        setDatumOdjave("");
        setBrojOsoba(1);
        setShowModal(true); // Prikazuje modal
    };

    // Funkcija za slanje zahteva za rezervaciju na server
    const handlePosaljiZahtev = async () => {

        if (!datumPrijave || !datumOdjave) { // Provera unesenih datuma
            alert("Unesite datume prijave i odjave.");
            return;
        }
        if(!userId || isNaN(userId)){ // Provera da li je korisnik ulogovan
            alert("Morate biti ulogovani!");
            return;
        }
        // Kreiranje objekta sa podacima za rezervaciju (DTO)
        const rezervacijaDTO = {
            datumPrijave,
            datumOdjave,
            brojOsoba,
            statusRezervacije: "PENDING", // Status rezervacije je "na čekanju"
            gost: { idGost: userId }, // Podaci o gostu (samo ID)
            soba: { idSoba: izabranaSoba.idSoba }, // Podaci o sobi (samo ID)
            recepcionar: null, // Recepcionar je null jer je zahtev kreirao gost
            ukupanIznos: izracunajUkupno(), // Izračunata ukupna cena
            datumKreiranja: new Date().toISOString().split("T")[0] // Trenutni datum kreiranja
        };

        try {
            // Slanje POST zahteva za dodavanje rezervacije
            const res = await fetch("http://localhost:8080/reservations/add", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(rezervacijaDTO) // Slanje podataka kao JSON string
            });

            const data = await res.json(); // Parsiranje JSON odgovora sa servera
            if (res.ok) { // Ako je odgovor uspešan
                alert("Zahtev za rezervaciju je poslat!");
                setShowModal(false); // Zatvara modal
            } else { // Ako je došlo do greške
                alert("Neuspešno slanje zahteva!");
                console.error("Greška sa servera:", data);
            }
        } catch (err) { // Hvatanje grešaka u komunikaciji sa serverom
            console.error(err);
            alert("Greška pri komunikaciji sa serverom.");
        }
    };

    // JSX koji definiše izlaz komponente
    return (
        <>
            <NavBar /> {/* Prikaz NavBar komponente */}

            {/* Glavni kontejner sa pozadinom i sadržajem stranice */}
            <div
                style={{
                    backgroundImage: `url(${pattern})`, // Postavljanje pozadinske slike
                    backgroundSize: "cover", // Slika pokriva ceo kontejner
                    backgroundPosition: "center", // Slika je centrirana
                    minHeight: "100vh", // Minimalna visina celog viewport-a
                    padding: "40px", // Unutrašnji razmak
                }}
            >
                {/* Naslov "Pregled soba" */}
                <h2
                    style={{
                        textAlign: "center",
                        color: "white",
                        backgroundColor: "rgba(215, 96, 255, 0.6)", // Ljubičasta pozadina sa transparentnošću
                        padding: "15px 30px",
                        borderRadius: "10px",
                        display: "inline-block", // Omogućava da se pozadina primeni samo na tekst
                    }}
                >
                    Pregled soba
                </h2>

                {/* Sekcija za filtere */}
                <div style={{ margin: "20px 0", display: "flex", gap: "20px", justifyContent: "center" }}>

                    <label style={{fontSize:"15px",marginTop:"5px"}}>
                         <input
                            type="checkbox"
                            checked={filterSlobodne} // Kontrolisana komponenta, vezana za stanje
                            onChange={() => setFilterSlobodne(!filterSlobodne)} // Meni stanje pri promeni
                        />{" "}
                        Prikaži samo dostupne
                    </label>


                    <select value={filterTip} style={{width:"250px",height:"35px",textAlign:"center",borderRadius:"10px",fontSize:"15px"}} onChange={(e) => setFilterTip(e.target.value)}>
                        <option value="">Svi tipovi soba</option> {/* Podrazumevana opcija */}
                        {/* Mapiranje liste tipova soba u <option> elemente */}
                        {tipoviSoba.map((tip, idx) => (
                            <option key={idx} value={tip}>{tip}</option>
                        ))}
                    </select>
                </div>

                {/* Lista soba */}
                <div style={{ display: "flex", flexWrap: "wrap", gap: "20px", justifyContent: "center" }}>
                    {/* Mapiranje filtriranih soba za prikaz */}
                    {filtriraneSobe.map((soba) => (
                        <div
                            key={soba.idSoba} // Jedinstveni ključ za svaki element u listi
                            style={{
                                backgroundColor: "rgba(255,255,255,0.9)", // Bela pozadina sa transparentnošću
                                width: "250px",
                                borderRadius: "15px",
                                padding: "15px",
                                textAlign: "center",
                                boxShadow: "0 2px 10px rgba(0,0,0,0.2)", // Senka za vizuelni efekat
                                cursor: "pointer" // Pokazivač miša kao ruka kada se pređe preko sobe
                            }}
                            onClick={() => handleSobaClick(soba)} // Poziva funkciju kada se klikne na sobu
                        >
                            {/* Prikaz slike sobe ako postoji */}
                            {soba.slika && (
                                <img
                                    src={`http://localhost:8080/uploads/${soba.slika.split("/").pop()}`} // Generisanje URL-a slike
                                    alt="Slika sobe"
                                    style={{ width: "100%", height: "150px", borderRadius: "10px", objectFit: "cover" }}
                                />
                            )}
                            <h3>{soba.naziv}</h3>
                            <p>Broj sobe: {soba.brojSobe}</p>
                            <p>Tip sobe: {soba.tipSobe?.naziv || "Nepoznat"}</p> {/* Prikaz tipa sobe ili "Nepoznat" */}
                            <p>Cena po noći: {soba.tipSobe?.cenaPoNoci || 0} RSD</p>
                            <p>Status: {soba.dostupna ? "Dostupna" : "Nedostupna"}</p>
                        </div>
                    ))}
                </div>
            </div>

            {/* MODAL ZA ZAHTEV ZA REZERVACIJU */}
            {showModal && izabranaSoba && ( // Prikazuje modal samo ako je showModal true i soba izabrana
                <div style={{
                    position: "fixed", top: 0, left: 0, width: "100%", height: "100%",
                    backgroundColor: "rgba(0,0,0,0.6)", display: "flex", // Tamna transparentna pozadina modala
                    justifyContent: "center", alignItems: "center" // Centriranje sadržaja modala
                }}>
                    <div style={{ backgroundColor: "white", padding: "20px", borderRadius: "10px", width: "400px" }}>
                        <h3>Zahtev za rezervaciju - {izabranaSoba.naziv}</h3> {/* Naslov modala */}

                        <label>Datum prijave:</label>
                        <input type="date" value={datumPrijave} onChange={(e) => setDatumPrijave(e.target.value)} style={{ width: "100%", marginBottom: "10px" }} />

                        <label>Datum odjave:</label>
                        <input type="date" value={datumOdjave} onChange={(e) => setDatumOdjave(e.target.value)} style={{ width: "100%", marginBottom: "10px" }} />

                        <label>Broj osoba:</label>
                        <input type="number" value={brojOsoba} min={1} onChange={(e) => setBrojOsoba(parseInt(e.target.value))} style={{ width: "100%", marginBottom: "10px" }} />

                        {/* Dugmad unutar modala */}
                        <div style={{ display: "flex", justifyContent: "flex-end", gap: "10px" }}>
                            <button onClick={() => setShowModal(false)} style={{ backgroundColor: "gray", color: "white", border: "none", borderRadius: "5px", padding: "5px 10px" }}>Otkaži</button>
                            <button onClick={handlePosaljiZahtev} style={{ backgroundColor: "purple", color: "white", border: "none", borderRadius: "5px", padding: "5px 10px" }}>Pošalji zahtev</button>
                        </div>
                    </div>
                </div>
            )}

            <Footer /> {/* Prikaz Footer komponente */}
        </>
    );
}