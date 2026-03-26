import { useState } from "react"; // Uvozimo useState za upravljanje stanjem komponente
import { NavBar } from "./NavBar"; // Uvozimo NavBar komponentu za navigaciju
import { Footer } from "./Footer"; // Uvozimo Footer komponentu za podnožje stranice

import pattern from "../slike/pattern4.jpg"; // Uvozimo sliku za pozadinu

import { useEffect } from "react"; // Uvozimo useEffect za sporedne efekte

// Glavna Registracija komponenta
export function Registracija(){

    // Definisanje stanja za svako polje forme pomoću useState hooka
    const[ime,setIme]=useState("");
    const[prezime,setPrezime]=useState("");
    const[email,setEmail]=useState("");
    const[telefon,setTelefon]=useState("");
    const[adresa,setAdresa]=useState("");
    const[lozinka,setLozinka]=useState(""); // Lozinka
    const[potvrdaLozinke,setPotvrda]=useState(""); // Potvrda lozinke
    const[datumRodjenja,setDatumRodjenja]=useState(); // Datum rođenja
    const [gradovi, setGradovi] = useState([]); // Stanje za listu gradova dobijenih sa servera
    const [idGrad, setGradId] = useState(); // Stanje za izabrani ID grada

    // useEffect hook za dohvatanje liste gradova sa servera kada se komponenta montira
    useEffect(() => {
        fetch("http://localhost:8080/grad/all", { // GET zahtev za sve gradove
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        })
        .then(response => response.json()) // Parsiranje JSON odgovora
        .then(data => setGradovi(data.data.values)) // Postavljanje dobijenih gradova u stanje
        .catch(error => console.error("Greška prilikom učitavanja gradova:", error)); // Hvatanje grešaka
    }, []); // Prazni niz [] znači da se efekat izvršava samo jednom na montiranju

    // Asinhrona funkcija za obradu podnošenja forme
    const handleSubmit = async (e) => {
        e.preventDefault(); // Sprečava podrazumevano ponašanje forme 
        // Provera da li se lozinke poklapaju
        if (lozinka !== potvrdaLozinke) {
            alert("Lozinke se ne poklapaju!");
            return; // Prekida izvršavanje ako se lozinke ne poklapaju
        }

        // Kreiranje objekta sa podacima korisnika za slanje na backend
        const userData = {
            ime,
            prezime,
            email,
            telefon,
            adresa,
            lozinka, 
            datumRodjenja,
            grad: { id: parseInt(idGrad) } // Objekat za grad sa ID-jem, parsiran u integer
        };

        console.log("Šaljem podatke:", userData); // Konzoli izlaz podataka koji se šalju

        try {
            // Izvršavanje POST zahteva za registraciju korisnika
            const response = await fetch("http://localhost:8080/guests/register", {
                method: "POST", // Tip zahteva je POST
                headers: { "Content-Type": "application/json" }, // Tip sadržaja je JSON
                body: JSON.stringify(userData), // Podaci se šalju kao JSON string
            });

            // Provera da li je odgovor sa servera uspešan
            if (response.ok) {
                alert("Uspešno registrovani!"); // Obaveštenje o uspešnoj registraciji
            } else {
                // Obrada grešaka sa backenda
                const data = await response.json();
                console.error("Greška sa backendom:", data);
                alert(`Greška: ${data.data?.value || data.message || "Neuspešna registracija"}`);
            }
        } catch (error) {
            // Hvatanje grešaka u slučaju problema sa mrežom ili serverom
            console.log("Greška", error);
            alert("Server nije dostupan.");
        }
    };

    // JSX koji definiše izgled Registracija komponente
    return(
        <>
        {/* Renderujemo NavBar na vrhu stranice */}
        <NavBar />
        {/* Glavni kontejner sa pozadinskom slikom i centriranim sadržajem */}
        <div style={{ backgroundImage: `url(${pattern})`, // Postavljanje pozadinske slike
            backgroundSize: "cover", // Slika pokriva ceo kontejner
            backgroundPosition: "center", // Slika je centrirana
            minHeight: "100vh", // Minimalna visina 100% visine viewport-a
            display: "flex", // Koristimo Flexbox za raspored
            justifyContent: "center", // Centriranje horizontalno
            alignItems: "center", // Centriranje vertikalno
            flexDirection: "column"}}> {/* Elementi se ređaju vertikalno */}
            {/* Div za stilizovanu formu */}
            <div className="forma">
                {/* Forma za registraciju, onSubmit poziva handleSubmit funkciju */}
                <form onSubmit={handleSubmit}>
                    {/* Polje za unos imena */}
                    <span className="formaSpan">
                        Unesite ime
                        <input type="text" onChange={(e)=>setIme(e.target.value)} required/>
                    </span>
                    {/* Polje za unos prezimena */}
                    <span className="formaSpan">
                        Unesite prezime
                        <input type="text" onChange={(e)=>setPrezime(e.target.value)} required />
                    </span>
                    {/* Polje za unos e-maila */}
                    <span className="formaSpan">
                        Unesite email
                        <input type="email" onChange={(e)=>setEmail(e.target.value)} required />
                    </span>
                    {/* Polje za unos lozinke */}
                    <span className="formaSpan">
                        Unesite lozinku
                        <input type="password" onChange={(e)=>setLozinka(e.target.value)} required/>
                    </span>
                    {/* Polje za potvrdu lozinke */}
                    <span className="formaSpan">
                        Potvrdite lozinku
                        <input type="password" onChange={(e)=>setPotvrda(e.target.value)} required/>
                    </span>
                    {/* Polje za unos telefona */}
                    <span className="formaSpan">
                        Unesite telefon
                        <input type="text" onChange={(e)=>setTelefon(e.target.value)} required />
                    </span>
                    {/* Polje za unos adrese */}
                    <span className="formaSpan">
                        Unesite adresu
                        <input type="text" onChange={(e)=>setAdresa(e.target.value)} required />
                    </span>
                    {/* Polje za unos datuma rođenja */}
                    <span className="formaSpan">
                        Unesite datum rođenja
                        <input type="date" onChange={(e)=>setDatumRodjenja(e.target.value)} required />
                    </span>
                    {/* Select box za izbor grada */}
                    <span className="formaSpan">
                        Izaberite grad
                        <select onChange={(e) => setGradId(e.target.value)} required>
                            <option value="">-- Izaberite grad --</option> {/* Podrazumevana opcija */}
                            {/* Mapiranje liste gradova u <option> elemente */}
                            {gradovi && gradovi.map((grad) => (
                                <option key={grad.id} value={grad.id}>
                                    {grad.name}
                                </option>
                            ))}
                        </select>
                    </span>
                    {/* Dugme za slanje forme */}
                    <button type="submit" className="btnNav" style={{marginTop:"20px",border: "none",fontSize:"18px",width:"160px",height:"50px"}}>Napravi nalog</button>
                </form>
            </div>
        </div>
        {/* Renderujemo Footer na dnu stranice */}
        <Footer />
        </>
    )
}