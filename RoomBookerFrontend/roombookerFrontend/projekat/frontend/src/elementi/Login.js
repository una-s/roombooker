import { NavBar } from "./NavBar"; // Uvozimo NavBar komponentu za navigaciju
import { Footer } from "./Footer"; // Uvozimo Footer komponentu za podnožje stranice
import { useState, useRef, useEffect } from "react"; // Uvozimo React hookove: useState za stanje, useRef za reference, useEffect za sporedne efekte
import pattern from "../slike/pattern4.jpg"; // Uvozimo sliku pozadine
import { useNavigate } from "react-router-dom"; // Uvozimo useNavigate hook za programsku navigaciju

// Glavna Login komponenta
export function Login() {
  // Definisanje stanja za e-mail i lozinku pomoću useState hooka
  const [email, setEmail] = useState("");
  const [lozinka, setLozinka] = useState("");

  // useRef za kreiranje reference na DOM element (u ovom slučaju, polje za e-mail)
  const imeRef = useRef(null);

  // useNavigate za dobijanje funkcije za navigaciju
  const navigate = useNavigate();

  // useEffect se izvršava nakon prvog renderovanja komponente
  useEffect(() => {
    // Fokusiranje na input polje za e-mail čim se stranica učita
    imeRef.current.focus();
  }, []);

  // Asinhrona funkcija za obradu login zahteva
  // Prima događaj (e) i ulogu korisnika (role - "gost" ili "admin")
  const handleSubmit = async (e, role) => {
    e.preventDefault(); // sprečava podrazumevano ponašanje forme 

    // Provera da li su e-mail i lozinka uneti
    if (!email || !lozinka) {
      alert("Molimo unesite email i lozinku!");
      return; // Prekida izvršavanje funkcije ako podaci nedostaju
    }

    try {
      // Određivanje URL-a backend endpointa na osnovu uloge korisnika
      const url =
        role === "admin" // Ako je uloga "admin" (recepcionar)
          ? `http://localhost:8080/receptionists/login/${email}/${lozinka}` // URL za login recepcionara
          : `http://localhost:8080/guests/login/${email}/${lozinka}`; // URL za login gosta

      // Izvršavanje GET zahteva prema backendu
      const response = await fetch(url, { method: "GET" });

      // Provera da li je odgovor sa servera uspešan (status 2xx)
      if (response.ok) {
        const data = await response.json(); // Parsiranje JSON odgovora

        if (role === "gost") {
          // Ako je uloga "gost"
          const guest = data.data.guest; // Izvlačenje podataka o gostu
          // Postavljanje kolačića (cookies) sa e-mailom i ID-jem gosta
          document.cookie = `userEmail=${guest.email}; path=/;`;
          document.cookie = `userId=${guest.idGost}; path=/;`;
          navigate("/"); // Preusmeravanje gosta na početnu stranicu
        } else {
          // Ako je uloga "admin" (recepcionar)
          const receptionist = data.data.receptionist; // Izvlačenje podataka o recepcionaru
          // Postavljanje kolačića sa korisničkim imenom i ID-jem recepcionara
          document.cookie = `userEmail=${receptionist.korisnickoIme}; path=/;`;
          document.cookie = `userId=${receptionist.idRecepcionar}; path=/;`;
          navigate("/recepcionar"); // Preusmeravanje recepcionara na stranicu recepcionara
        }
      } else {
        // Ako odgovor nije uspešan (npr. pogrešni podaci)
        alert("Pogrešno uneti podaci!");
      }
    } catch (error) {
      // Hvatanje grešaka u slučaju problema sa mrežom ili serverom
      console.error("Greška prilikom login-a:", error);
      alert("Server nije dostupan.");
    }
  };

  // JSX koji definiše izgled Login komponente
  return (
    <>
      {/* Renderujemo NavBar na vrhu stranice */}
      <NavBar />
      {/* Div koji služi kao glavni kontejner za pozadinu i formu */}
      <div style={{
        backgroundImage: `url(${pattern})`, // Postavljamo sliku kao pozadinu
        backgroundSize: "cover", // Slika pokriva ceo kontejner
        backgroundPosition: "center", // Slika je centrirana
        minHeight: "100vh", // Minimalna visina 100% visine viewport-a
        display: "flex",
        justifyContent: "center", // Centriranje sadržaja horizontalno
        alignItems: "center", // Centriranje sadržaja vertikalno
        flexDirection: "column", // Elementi se ređaju vertikalno
        overflowX: "hidden"
      }}>
        {/* Div za stilizovanu formu */}
        <div className="forma" style={{ width: "350px", height: "300px", padding: "30px", display: "flex", justifyContent: "center", flexDirection: "column" }}>
          <form>
            {/* Polje za unos e-maila */}
            <span className="formaSpan">
              Unesite email
              <input
                type="email"
                onChange={(e) => setEmail(e.target.value)} // Ažuriramo stanje 'email' pri svakoj promeni
                ref={imeRef} // Povezujemo referencu sa ovim input poljem
                required // Polje je obavezno
              />
            </span>

            {/* Polje za unos lozinke */}
            <span className="formaSpan">
              Unesite lozinku
              <input
                type="password" // Tip 'password' skriva uneti tekst
                onChange={(e) => setLozinka(e.target.value)} // Ažuriramo stanje 'lozinka' pri svakoj promeni
                required // Polje je obavezno
              />
            </span>

            {/* Kontejner za dugmad za prijavu */}
            <div style={{
              display: "flex",
              justifyContent: "center", // Centriranje dugmadi horizontalno
              gap: "15px", // Razmak između dugmadi
              marginTop: "20px",
              marginLeft: "15px",
            }}>
              {/* Dugme za prijavu kao gost */}
              <button
                onClick={(e) => handleSubmit(e, "gost")} // Pozivamo handleSubmit sa ulogom "gost"
                className="btnNav"
                style={{
                  flex: 1, // Zauzima raspoloživ prostor
                  marginRight: "10px",
                  border: "none",
                  fontSize: "18px",
                  width: "160px",
                  height: "50px",
                }}
              >
                Uloguj se
              </button>

              {/* Dugme za prijavu kao recepcionar */}
              <button
                onClick={(e) => handleSubmit(e, "admin")} // Pozivamo handleSubmit sa ulogom "admin"
                className="btnNav"
                style={{
                  flex: 1, // Zauzima raspoloživ prostor
                  marginRight: "10px",
                  border: "none",
                  fontSize: "18px",
                  width: "160px",
                  height: "50px",
                  paddingTop: "6px",
                  paddingBottom: "auto"
                }}
              >
                Uloguj se kao recepcionar
              </button>
            </div>
          </form>
        </div>
      </div>
      {/* Renderujemo Footer na dnu stranice */}
      <Footer />
    </>
  );
}