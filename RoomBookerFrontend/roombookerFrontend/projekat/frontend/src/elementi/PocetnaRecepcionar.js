import { useEffect, useState } from "react";
import { Footer } from "./Footer";
import { NavBarRecepcionar } from "./NavBarRecepcionar";
import pattern from "../slike/pattern4.jpg";
import { useNavigate } from "react-router-dom";


export function PocetnaRecepcionar() {
  const [sobe, setSobe] = useState([]);
  const [tipoviSoba, setTipoviSoba] = useState([]);
  const [izabranaSoba, setIzabranaSoba] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();


  // Ucitavanje soba iz backenda
  useEffect(() => {
    fetch("http://localhost:8080/rooms/all")
      .then((res) => res.json())
      .then((data) => {
        if (data && data.data && data.data.values) {
          setSobe(data.data.values);
        }
      })
      .catch((err) => console.error("Greška pri učitavanju soba:", err));
  }, []);

  // Ucitavanje tipova soba iz backenda
  useEffect(() => {
    fetch("http://localhost:8080/tip-sobe/all")
      .then((res) => res.json())
      .then((data) => {
        if (data && data.data && data.data.values) {
          setTipoviSoba(data.data.values);
        }
      })
      .catch((err) => console.error("Greška pri učitavanju tipova soba:", err));
  }, []);

  // Brisanje sobe
  const handleDelete = async (idSoba) => {
    if (window.confirm("Da li ste sigurni da želite da obrišete ovu sobu?")) {
      await fetch("http://localhost:8080/rooms/delete", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(idSoba),
      });
      setSobe(sobe.filter((s) => s.idSoba !== idSoba));
    }
  };

  // Otvaranje modala za izmenu
  const handleEdit = (soba) => {
    setIzabranaSoba({ ...soba });
    setError("");
    setShowModal(true);
  };

  // Čuvanje izmenjenih podataka
  const handleSave = async () => {
    const postoji = sobe.some(
      (s) =>
        s.brojSobe === parseInt(izabranaSoba.brojSobe) &&
        s.idSoba !== izabranaSoba.idSoba
    );
    if (postoji) {
      setError("Ovaj broj sobe već postoji! Unesite drugi broj.");
      return;
    }

    const response = await fetch("http://localhost:8080/rooms/update", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(izabranaSoba),
    });

    if (response.ok) {
      alert("Soba uspešno izmenjena!");
      setSobe((prev) =>
        prev.map((s) =>
          s.idSoba === izabranaSoba.idSoba ? izabranaSoba : s
        )
      );
      setShowModal(false);
    } else {
      alert("Greška pri izmeni sobe!");
    }
  };

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
      }}
    >
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
        <h2
          style={{
            textAlign: "center",
            color: "white",
            backgroundColor: "rgba(235, 120, 255, 0.6)",
            padding: "15px 30px",
            borderRadius: "10px",
            display: "inline-block",
          }}
        >
          Lista soba
        </h2>

        <div
          style={{
            display: "flex",
            flexWrap: "wrap",
            justifyContent: "center",
            gap: "20px",
            marginTop: "30px",
          }}
        >
          {sobe.map((soba) => (
            <div
              key={soba.idSoba}
              onClick={() => navigate(`/soba/${soba.idSoba}`)} // <--- DODATO
              style={{
                backgroundColor: "rgba(255, 255, 255, 1)",
                width: "250px",
                borderRadius: "15px",
                padding: "15px",
                boxShadow: "0 2px 10px rgba(0,0,0,0.2)",
                textAlign: "center",
                cursor: "pointer", // da se vidi da je klikabilno
              }}
            >
              {soba.slika && (
                <img
                  src={`http://localhost:8080/uploads/${soba.slika
                    .split("/")
                    .pop()}`}
                  alt="Slika sobe"
                  style={{
                    width: "100%",
                    height: "150px",
                    borderRadius: "10px",
                    objectFit: "cover",
                  }}
                />
              )}

              <h3>{soba.naziv}</h3>
              <p>Broj sobe: {soba.brojSobe}</p>
              <p>Tip sobe: {soba.tipSobe ? soba.tipSobe.naziv : "Nepoznat"}</p>
              <p>
                Cena po noći:{" "}
                {soba.tipSobe
                  ? soba.tipSobe.cenaPoNoci + " RSD"
                  : Math.floor(Math.random() * 5000 + 3000) + " RSD"}
              </p>

              <div style={{ marginTop: "10px" }}>
                <button
                  onClick={() => handleEdit(soba)}
                  style={{
                    marginRight: "10px",
                    backgroundColor: "#eee380ff",
                    color: "black",
                    border: "none",
                    borderRadius: "5px",
                    padding: "5px 10px",
                    cursor: "pointer",
                  }}
                >
                  Izmeni
                </button>
                <button
                  onClick={() => handleDelete(soba.idSoba)}
                  style={{
                    backgroundColor: "#eee380ff",
                    color: "black",
                    border: "none",
                    borderRadius: "5px",
                    padding: "5px 10px",
                    cursor: "pointer",
                  }}
                >
                  Obriši
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* MODAL ZA IZMENE */}
      {showModal && (
        <div
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100%",
            height: "100%",
            backgroundColor: "rgba(0,0,0,0.6)",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <div
            style={{
              backgroundColor: "white",
              padding: "20px",
              borderRadius: "10px",
              width: "400px",
            }}
          >
            <h3>Izmeni sobu</h3>

            {error && (
              <p style={{ color: "red", marginBottom: "10px" }}>{error}</p>
            )}

            <label>Broj sobe:</label>
            <input
              type="number"
              value={izabranaSoba.brojSobe}
              onChange={(e) =>
                setIzabranaSoba({
                  ...izabranaSoba,
                  brojSobe: parseInt(e.target.value),
                })
              }
              style={{ width: "100%", marginBottom: "10px" }}
            />

            <label>Dostupna:</label>
            <select
              value={izabranaSoba.dostupna ? "true" : "false"}
              onChange={(e) =>
                setIzabranaSoba({
                  ...izabranaSoba,
                  dostupna: e.target.value === "true",
                })
              }
              style={{ width: "100%", marginBottom: "10px" }}
            >
              <option value="true">Dostupna</option>
              <option value="false">Zauzeta</option>
            </select>

            <label>Tip sobe:</label>
            <select
              value={izabranaSoba.tipSobe?.idTipSobe || ""}
              onChange={(e) => {
                const id = parseInt(e.target.value);
                const izabraniTip = tipoviSoba.find((t) => t.idTipSobe === id);
                setIzabranaSoba({ ...izabranaSoba, tipSobe: izabraniTip });
              }}
              style={{ width: "100%", marginBottom: "10px" }}
            >
              <option value="">Odaberite tip sobe</option>
              {tipoviSoba.map((tip) => (
                <option key={tip.idTipSobe} value={tip.idTipSobe}>
                  {tip.naziv}
                </option>
              ))}
            </select>

            <label>Sprat:</label>
            <input
              type="number"
              value={izabranaSoba.sprat}
              onChange={(e) =>
                setIzabranaSoba({
                  ...izabranaSoba,
                  sprat: parseInt(e.target.value),
                })
              }
              style={{ width: "100%", marginBottom: "10px" }}
            />

            <div style={{ display: "flex", justifyContent: "flex-end" }}>
              <button
                onClick={() => setShowModal(false)}
                style={{
                  marginRight: "10px",
                  backgroundColor: "gray",
                  color: "white",
                  border: "none",
                  borderRadius: "5px",
                  padding: "5px 10px",
                }}
              >
                Otkaži
              </button>
              <button
                onClick={handleSave}
                style={{
                  backgroundColor: "purple",
                  color: "white",
                  border: "none",
                  borderRadius: "5px",
                  padding: "5px 10px",
                }}
              >
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
