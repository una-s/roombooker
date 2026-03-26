import { useState, useEffect } from 'react';
import '../index.css';
import { useNavigate } from 'react-router-dom';

export function NavBarRecepcionar() {
  const navigate = useNavigate(); // hook za navigaciju između ruta
  const [userEmail, setUserEmail] = useState(""); // čuva email ulogovanog korisnika

  // Funkcija za čitanje cookie-a po imenu
  const getCookie = (name) => {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    if (match) return decodeURIComponent(match[2]); // vraća vrednost kolačića
    return null; // ako kolačić ne postoji
  };

  // useEffect se izvršava pri učitavanju komponente
  useEffect(() => {
    const email = getCookie("userEmail"); // čita cookie sa email-om korisnika
    if (email) {
      setUserEmail(email); // postavlja email u state
    }
  }, []); // prazna lista zavisnosti znači da se izvršava samo jednom

  // Funkcija za odjavu korisnika
  const handleLogout = () => {
    // Briše cookie i resetuje state
    document.cookie = "userEmail=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    setUserEmail("");
    navigate("/"); // vraća korisnika na početnu stranu
  };

  return (
    <header>
      <nav
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          width: "100%",
          padding: "0 40px",
          background: "#af65c5",
          height: "70px",
          boxSizing: "border-box"
        }}
      >
        {/* LEVA STRANA NAVBARA: dugmad za navigaciju unutar aplikacije */}
        <div style={{ display: "flex", alignItems: "center", gap: "10px", height: "100%" }}>
          <span
            className="btnNav"
            onClick={() => navigate("/dodaj_sobu")} // navigacija na dodavanje sobe
            style={{ height: "40px", display: "flex", alignItems: "center", padding: "0 15px" }}
          >
            Dodaj novu sobu
          </span>

          <span
            className="btnNav"
            onClick={() => navigate("/gosti_recepcionar")} // navigacija na listu gostiju
            style={{ height: "40px", display: "flex", alignItems: "center", padding: "0 15px" }}
          >
            Gosti
          </span>
          <span
            className="btnNav"
            onClick={() => navigate("/rezervacije")} // navigacija na rezervacije
            style={{ height: "40px", display: "flex", alignItems: "center", padding: "0 15px" }}
          >
            Rezervacije
          </span>
        </div>

        {/* DESNA STRANA NAVBARA: prikaz login/odjava i email korisnika */}
        <div style={{ display: "flex", gap: "10px", alignItems: "center", height: "100%" }}>
          {userEmail ? (
            <>
              {/* Dugme za pristup zahtevima za rezervacije */}
              <span
                className="btnNav"
                onClick={() => navigate("/pravljenje_rezervacije")}
                style={{ height: "40px", display: "flex", alignItems: "center", padding: "0 15px" }}
              >
                Zahtevi za rezervacije
              </span>
              {/* Prikaz email-a ulogovanog korisnika */}
              <span style={{ color: "white", fontSize: "16px" }}>{userEmail}</span>
              {/* Dugme za logout */}
              <span
                className="btnNav"
                onClick={handleLogout}
                style={{ height: "40px", display: "flex", alignItems: "center", padding: "0 15px" }}
              >
                Logout
              </span>
            </>
          ) : (
            <>
              {/* Prikaz dugmadi za registraciju i login ako korisnik nije ulogovan */}
              <span className="btnNav" onClick={() => navigate("/registracija")}>Register</span>
              <span className="btnNav" onClick={() => navigate("/login")}>Login</span>
            </>
          )}
        </div>
      </nav>
    </header>
  );
}
