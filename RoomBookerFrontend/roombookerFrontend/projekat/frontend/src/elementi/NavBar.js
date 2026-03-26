import { useState, useEffect } from 'react';
import '../index.css';
import { useNavigate } from 'react-router-dom';

export function NavBar() {
  const navigate = useNavigate(); // Hook za navigaciju između ruta
  const [userEmail, setUserEmail] = useState(""); // Čuva email prijavljenog korisnika

  // Funkcija za čitanje cookie-a po imenu
  const getCookie = (name) => {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    if (match) return decodeURIComponent(match[2]);
    return null;
  };

  // Proverava cookie kada se NavBar učita
  useEffect(() => {
    const email = getCookie("userEmail");
    if (email) {
      setUserEmail(email); // Ako postoji cookie, setuje email u state
    }
  }, []);

  // Funkcija za logout
  const handleLogout = () => {
    // Briše cookie-e
    document.cookie = "userEmail=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "userId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    setUserEmail(""); // Resetuje stanje
    navigate("/"); // Preusmerava na početnu
  };

  return (
    <header>
      <nav
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          width: "100%",
          background: "#af65c5",
          padding: "20px"
        }}
      >
        <div style={{ display: "flex", gap: "10px" }}>
          {userEmail ? (
            <>
              {/* Prikaz email-a */}
              <span style={{ color: "white", fontSize: "16px", margin: "auto 0" }}>
                {userEmail}
              </span>

              {/* Dugme za moje rezervacije */}
              <span className="btnNav" onClick={() => navigate("/rezervacije-gost")} style={{ cursor: "pointer" }}>
                Moje rezervacije
              </span>

              {/* Dugme logout */}
              <span className="btnNav" onClick={handleLogout} style={{ cursor: "pointer" }}>
                Logout
              </span>
            </>
          ) : (
            <>
              {/* Dugmad za registrovanje i login */}
              <span className="btnNav" onClick={() => navigate("/registracija")}>Register</span>
              <span className="btnNav" onClick={() => navigate("/login")}>Login</span>
            </>
          )}
        </div>
      </nav>
    </header>
  );
}
