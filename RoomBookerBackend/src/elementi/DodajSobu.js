import { useState } from "react"; // Importovanje React hook-a useState za upravljanje stanjem komponente
import { NavBarRecepcionar } from "./NavBarRecepcionar"; // Importovanje NavBarRecepcionar komponente
import { Footer } from "./Footer"; // Importovanje Footer komponente
import pattern from "../slike/pattern4.jpg"; // Importovanje slike za pozadinu

// Glavna komponenta za dodavanje sobe
export function DodajSobu() {
  // Deklaracija state varijabli pomocu useState hook-a
  const [brojSobe, setBrojSobe] = useState(""); // Stanje za broj sobe, inicijalno prazan string
  const [sprat, setSprat] = useState(""); // Stanje za sprat sobe, inicijalno prazan string
  const [tipSobe, setTipSobe] = useState(""); // Stanje za tip sobe, inicijalno prazan string
  const [dostupna, setDostupna] = useState(false); // Stanje za dostupnost sobe, inicijalno false (nedostupna)
  const [slika, setSlika] = useState(null); // Stanje za odabranu sliku, inicijalno null

  // Funkcija koja se poziva pri submitu forme
  const handleSubmit = async (e) => {
    e.preventDefault(); // Sprečava podrazumevano ponašanje forme (reload stranice)

    // Validacija forme: Provera da li su popunjena obavezna polja
    if (!brojSobe || !sprat || !tipSobe) {
      alert("Molimo popunite sve podatke!"); // Upozorenje korisniku
      return; // Prekida izvršavanje funkcije
    }

    const formData = new FormData(); // Kreiranje FormData objekta za slanje podataka na server (uključujući fajlove)

    // Dodavanje slike u FormData objekat, ako je slika odabrana
    if (slika) {
      formData.append("file", slika); // 'file' je ime parametra koje backend očekuje za sliku
    }

    // Kreiranje objekta sobaData sa podacima o sobi
    const sobaData = {
      brojSobe: brojSobe,
      sprat: sprat,
      // tipSobe se šalje kao objekat sa idTipSobe, pretpostavljajući da backend očekuje ID
      tipSobe: { idTipSobe: tipSobe },
      dostupna: dostupna,
    };

    // Dodavanje sobaData objekta u FormData kao JSON Blob
    // 'soba' je ime parametra koje backend očekuje za soba DTO
    formData.append(
      "soba",
      new Blob([JSON.stringify(sobaData)], { type: "application/json" })
    );

    try {
      // Slanje POST zahteva na backend za dodavanje sobe
      const response = await fetch("http://localhost:8080/rooms/add", {
        method: "POST", // HTTP metoda
        body: formData, // Telo zahteva je FormData objekat
        // NAPOMENA: Za FormData NIJE potrebno ručno postavljati 'Content-Type' header,
        // browser će automatski postaviti 'multipart/form-data' sa odgovarajućim boundary-jem.
      });

      // Provera da li je odgovor sa servera uspešan (status 2xx)
      if (!response.ok) {
        // Ako odgovor nije OK, baca se greška
        throw new Error("Greška pri dodavanju sobe");
      }

      const data = await response.text(); // Čitanje odgovora sa servera kao teksta
      alert(data); // Prikazivanje odgovora korisniku (npr. "Soba uspešno dodata!")

      // Resetovanje stanja forme nakon uspešnog dodavanja
      setBrojSobe("");
      setSprat("");
      setTipSobe("");
      setDostupna(false);
      setSlika(null);
    } catch (error) {
      // Hvatanje i obrada eventualnih grešaka tokom fetch poziva
      console.error(error); // Prikazivanje greške u konzoli
      alert("Greška pri dodavanju sobe"); // Prikazivanje generičke poruke o grešci korisniku
    }
  };

  // Funkcija koja se poziva kada se promeni odabrani fajl (slika)
  const handleFileChange = (e) => {
    setSlika(e.target.files[0]); // Postavlja prvu odabranu sliku u state
  };

  // JSX koji se renderuje (struktura i izgled komponente)
  return (
    <div
      style={{
        minHeight: "100vh", // Minimalna visina elementa da zauzme ceo prozor
        display: "flex", // Omogućava flexbox raspored
        flexDirection: "column", // Elementi se slažu vertikalno
        justifyContent: "space-between", // Raspoređuje prostor između elemenata
      }}
    >
      <NavBarRecepcionar /> {/* Renderovanje navigacionog bara za recepcionare */}

      <div
        style={{
          flex: 1, // Omogućava da ovaj div zauzme sav preostali vertikalni prostor
          backgroundImage: `url(${pattern})`, // Postavljanje pozadinske slike
          backgroundSize: "cover", // Slika pokriva ceo prostor
          backgroundPosition: "center", // Slika centrirana
          display: "flex",
          justifyContent: "center", // Centriranje sadržaja horizontalno
          alignItems: "center", // Centriranje sadržaja vertikalno
        }}
      >
        <form
          onSubmit={handleSubmit} // Funkcija koja se poziva pri submitu forme
          style={{
            background: "rgba(255,255,255,0.95)", // Poluprovidna bela pozadina forme
            padding: "40px", // Unutrašnji razmak
            borderRadius: "12px", // Zaobljeni ćoškovi
            display: "flex",
            flexDirection: "column",
            gap: "20px", // Razmak između elemenata unutar forme
            width: "400px", // Fiksna širina forme
            boxShadow: "0 4px 12px rgba(0,0,0,0.2)", // Senka oko forme
          }}
        >
          <h2 style={{ textAlign: "center" }}>Dodaj novu sobu</h2>{" "}
          {/* Naslov forme */}
          {/* Input polje za broj sobe */}
          <input
            type="number" // Tip inputa je broj
            placeholder="Broj sobe" // Tekst koji se prikazuje kada je polje prazno
            value={brojSobe} // Vrednost inputa je vezana za 'brojSobe' state
            onChange={(e) => setBrojSobe(e.target.value)} // Ažurira 'brojSobe' state pri promeni
            required // Obavezno polje
            style={{
              height: "40px",
              borderRadius: "8px",
              padding: "0 10px",
              fontSize: "16px",
            }}
          />
          {/* Input polje za sprat */}
          <input
            type="number" // Tip inputa je broj
            placeholder="Sprat"
            value={sprat}
            onChange={(e) => setSprat(e.target.value)}
            required
            style={{
              height: "40px",
              borderRadius: "8px",
              padding: "0 10px",
              fontSize: "16px",
            }}
          />
          {/* Select (padajući meni) za odabir tipa sobe */}
          <select
            value={tipSobe}
            onChange={(e) => setTipSobe(e.target.value)}
            required
            style={{
              height: "40px",
              borderRadius: "8px",
              padding: "0 10px",
              fontSize: "16px",
            }}
          >
            <option value="">Izaberite tip sobe</option> {/* Podrazumevana opcija */}
            <option value={1}>Standard</option> {/* Opcije sa odgovarajućim ID-jevima */}
            <option value={2}>Apartman</option>
            <option value={3}>Poslovna</option>
            <option value={4}>Luksuzna</option>
          </select>
          {/* Checkbox za dostupnost sobe */}
          <label
            style={{
              display: "flex",
              alignItems: "center",
              gap: "10px",
              fontSize: "16px",
            }}
          >
            <input
              type="checkbox" // Tip inputa je checkbox
              checked={dostupna} // Povezuje se sa 'dostupna' state-om
              onChange={(e) => setDostupna(e.target.checked)} // Ažurira 'dostupna' state
              style={{ width: "20px", height: "20px" }}
            />
            Dostupna odmah {/* Tekst pored checkboxa */}
          </label>
          {/* Input polje za odabir fajla (slike) */}
          <input
            type="file" // Tip inputa je fajl
            accept="image/*" // Prihvata samo slike
            onChange={handleFileChange} // Funkcija koja se poziva pri promeni fajla
            style={{ borderRadius: "8px", padding: "5px" }}
          />
          {/* Dugme za submit forme */}
          <button
            type="submit" // Tip dugmeta je submit
            className="btnNav" // CSS klasa za stilizovanje
            style={{
              height: "45px",
              borderRadius: "8px",
              fontSize: "16px",
            }}
          >
            Dodaj sobu {/* Tekst na dugmetu */}
          </button>
        </form>
      </div>

      <Footer /> {/* Renderovanje komponente Footer */}
    </div>
  );
}