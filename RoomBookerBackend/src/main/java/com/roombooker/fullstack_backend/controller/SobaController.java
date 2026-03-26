package com.roombooker.fullstack_backend.controller;

// Importi su izostavljeni prema zahtevu

import com.roombooker.fullstack_backend.connection.HttpResponse;
import com.roombooker.fullstack_backend.connection.Response;
import com.roombooker.fullstack_backend.dto.SobaDTO;
import com.roombooker.fullstack_backend.service.SobaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

// Annotation @CrossOrigin omogućava zahtevima sa specificiranog domena (frontend aplikacije)
// da pristupaju resursima ovog kontrolera. Ovde je to React aplikacija na portu 3000.
@CrossOrigin("http://localhost:3000")
// Annotation @RestController kombinuje @Controller i @ResponseBody,
// što znači da metode ovog kontrolera direktno vraćaju podatke (JSON/XML) a ne poglede.
@RestController
// Annotation @RequestMapping definiše osnovnu rutu za sve endpoint-e u ovom kontroleru.
// Svi endpoint-i unutar ove klase će počinjati sa "/rooms".
@RequestMapping("/rooms")
public class SobaController {

    // Finalno polje za SobaService, koje će biti injektovano preko konstruktora.
    private final SobaService sobaService;

    // Konstruktor za injekciju zavisnosti (Dependency Injection) SobaService-a.
    // Spring automatski pronalazi i injektuje instancu SobaService-a.
    public SobaController(SobaService sobaService) {
        this.sobaService = sobaService;
    }

   //Definise putanju do foldera za upload fajlova.
    @Value("${upload.folder:C:/Users/Asus/Desktop/fullstack-backend/uploads/}")
    private String uploadFolder;

    // SK3 – Pretraga slobodnih soba
    // Annotation @GetMapping mapira HTTP GET zahtev na putanji "/rooms/available" na ovu metodu.
    @GetMapping("/available")
    public ResponseEntity<Response> getAvailableRooms() {
        // Vraća ResponseEntity sa statusom OK i telom koje sadrži podatke o slobodnim sobama.
        // HttpResponse.getResponseWithData je pomoćna metoda za standardizovan odgovor.
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Uspešno pronađene slobodne sobe!", // Poruka za klijenta
                        Map.of("values", sobaService.getAvailableRooms()), // Podaci: lista slobodnih soba dobijena od servisa
                        HttpStatus.OK // HTTP status kod
                )
        );
    }

    // Dodatno – Pregled svih soba (koristi recepcioner)
    // Annotation @GetMapping mapira HTTP GET zahtev na putanji "/rooms/all" na ovu metodu.
    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms() {
        // Vraća ResponseEntity sa statusom OK i telom koje sadrži podatke o svim sobama.
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Uspešno pronađene sve sobe!",
                        Map.of("values", sobaService.getAllRooms()), // Podaci: lista svih soba dobijena od servisa
                        HttpStatus.OK
                )
        );
    }

    // SK6 – Dodavanje nove sobe sa slikom
    // Annotation @PostMapping mapira HTTP POST zahtev na putanji "/rooms/add" na ovu metodu.
    // Koristi @RequestPart za prihvatanje delova multipart/form-data zahteva:
    // "soba" za JSON objekat SobaDTO i "file" za MultipartFile (sliku).
    @PostMapping("/add")
    public ResponseEntity<String> addSoba(
            @RequestPart("soba") SobaDTO sobaDTO, // Prima SobaDTO objekat iz dela zahteva nazvanog "soba"
            @RequestPart(value = "file", required = false) MultipartFile file) { // Prima opcioni fajl iz dela "file"

        try {
            // Provera da li je fajl (slika) poslat i da nije prazan
            if (file != null && !file.isEmpty()) {
                
                File dir = new File(uploadFolder);
                if (!dir.exists()) {
                    dir.mkdirs(); // Kreira folder ako ne postoji
                }

                // Kreira jedinstveno ime fajla kako bi se izbeglo prepisivanje postojećih slika.
                // Kombinuje trenutno vreme (millis) i originalno ime fajla.
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String filePath = uploadFolder + fileName; // Puna putanja do fajla

                // Čuva fajl (sliku) na disk.
                file.transferTo(new File(filePath));

                // Postavlja putanju do sačuvane slike u SobaDTO objekat.
                sobaDTO.setSlika(filePath);
            }

            // Poziva servis da doda sobu u bazu podataka sa ili bez putanje do slike.
            String result = sobaService.addRoom(sobaDTO);

            // Vraća uspešan odgovor sa porukom.
            return ResponseEntity.ok(result);

        } catch (IOException e) {
            // Hvatanje IOException (greške pri I/O operacijama, npr. čuvanje fajla).
            e.printStackTrace(); // Ispis steka grešaka u konzolu
            // Vraća odgovor sa statusom 500 (INTERNAL_SERVER_ERROR) i porukom o grešci.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greška pri uploadu slike: " + e.getMessage());
        } catch (Exception e) {
            // Hvatanje ostalih generičkih izuzetaka.
            e.printStackTrace(); // Ispis steka grešaka u konzolu
            // Vraća odgovor sa statusom 400 (BAD_REQUEST) i porukom o grešci.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Greška pri dodavanju sobe: " + e.getMessage());
        }
    }

    // SK7 – Izmena podataka sobe
    // Annotation @PostMapping mapira HTTP POST zahtev na putanji "/rooms/update" na ovu metodu.
    // @RequestBody anocija govori Springu da telo HTTP zahteva (koje se očekuje da je JSON)
    // treba da se konvertuje u SobaDTO Java objekat.
    @PostMapping("/update")
    public ResponseEntity<Response> updateRoom(@RequestBody SobaDTO sobaDTO) {
        // Poziva servis za ažuriranje podataka sobe.
        String result = sobaService.updateRoom(sobaDTO);
        // Određuje HTTP status kod na osnovu poruke iz servisa.
        // Ako poruka počinje sa "Podaci o sobi su uspešno", status je OK, inače BAD_REQUEST.
        HttpStatus status = result.startsWith("Podaci o sobi su uspešno") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        // Vraća ResponseEntity sa odgovarajućim statusom i porukom.
        return ResponseEntity.status(status)
                .body(HttpResponse.getResponseWithData(result, Map.of("value", result), status));
    }

    // SK8 – Brisanje sobe
    // Annotation @PostMapping mapira HTTP POST zahtev na putanji "/rooms/delete" na ovu metodu.
    // Prima ID sobe iz tela zahteva.
    @PostMapping("/delete")
    public ResponseEntity<Response> deleteRoom(@RequestBody int idSoba) {
        // Poziva servis za brisanje sobe.
        String result = sobaService.deleteRoom(idSoba);
        // Određuje HTTP status kod na osnovu poruke iz servisa.
        HttpStatus status = result.startsWith("Soba je uspešno") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        // Vraća ResponseEntity sa odgovarajućim statusom i porukom.
        return ResponseEntity.status(status)
                .body(HttpResponse.getResponseWithData(result, Map.of("value", result), status));
    }

    // Dodatno – Dohvatanje sobe po ID-ju
    // Annotation @GetMapping sa @PathVariable mapira HTTP GET zahtev na putanji "/rooms/{id}" na ovu metodu.
    // {id} je dinamički deo URL-a koji će biti mapiran na 'idSoba' parametar metode.
    @GetMapping("/{id}")
    public ResponseEntity<Response> getRoomById(@PathVariable("id") int idSoba) {
        // Vraća ResponseEntity sa statusom OK i telom koje sadrži podatke o pronađenoj sobi.
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Uspešno pronađena soba!",
                        Map.of("value", sobaService.getRoomById(idSoba)), // Podaci: Soba objekat dobijen od servisa
                        HttpStatus.OK
                )
        );
    }
}