package com.roombooker.fullstack_backend.controller;

import com.roombooker.fullstack_backend.connection.HttpResponse;
import com.roombooker.fullstack_backend.connection.Response;
import com.roombooker.fullstack_backend.dto.GostDTO;
import com.roombooker.fullstack_backend.service.GostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

// Omogućava Cross-Origin Resource Sharing (CORS) sa frontendom na localhost:3000.
@CrossOrigin("http://localhost:3000")
// Označava klasu kao REST kontroler, automatski serijalizuje povratne vrednosti u JSON/XML.
@RestController
// Definiše osnovnu URL putanju za sve metode u ovom kontroleru.
@RequestMapping("/guests") // Osnovna ruta za goste
public class GostController {

    private final GostService gostService; // Instanca servisne klase za poslovnu logiku.

    // Konstruktor za injekciju GostService-a.
    public GostController(GostService gostService) {
        this.gostService = gostService;
    }

    // Endpoint za dodavanje novog gosta (registracija). Prima GostDTO iz tela zahteva.
    @PostMapping("/add")
    public ResponseEntity<Response> addGuest(@RequestBody GostDTO gost) {
        String result = gostService.addGuest(gost); // Poziva servis za dodavanje gosta.

        // Logika za određivanje HTTP statusa na osnovu poruke servisa.
        if ("Gost sa ovim emailom već postoji!".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict ako email već postoji.
                    .body(HttpResponse.getResponseWithData(result, Map.of("value", result), HttpStatus.CONFLICT));
        }

        if ("Gost je uspešno dodat!".equals(result)) {
            return ResponseEntity.status(HttpStatus.OK) // 200 OK ako je uspešno.
                    .body(HttpResponse.getResponseWithData(result, Map.of("value", result), HttpStatus.OK));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST) // 400 Bad Request za ostale greške.
                .body(HttpResponse.getResponseWithData(result, Map.of("value", result), HttpStatus.BAD_REQUEST));
    }

    // Endpoint za pregled svih gostiju.
    @GetMapping("/all")
    public ResponseEntity<Response> getAll() {
        List<GostDTO> gosti = gostService.getAll(); // Dohvaća sve goste putem servisa.
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Gosti su uspešno pronađeni!",
                        Map.of("values", gosti), // Vraća listu gostiju.
                        HttpStatus.OK
                ));
    }

    // Endpoint za pronalaženje gosta po emailu. Prima email iz URL putanje.
    @GetMapping("/getGuest/{email}")
    public ResponseEntity<Response> getGuestByEmail(@PathVariable("email") String email) {
        GostDTO gost = gostService.getGuestByEmail(email); // Dohvaća gosta po emailu putem servisa.
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Gost je pronađen!",
                        Map.of("value", gost), // Vraća pronađenog gosta.
                        HttpStatus.OK
                ));
    }

    // Endpoint za izmenu podataka gosta. Prima ažurirani GostDTO iz tela zahteva.
    @PostMapping("/update")
    public ResponseEntity<Response> updateGuest(@RequestBody GostDTO gost) {
        String result = gostService.updateGuest(gost); // Poziva servis za ažuriranje gosta.
        // Određuje HTTP status na osnovu poruke iz servisa.
        HttpStatus status = "Podaci o gostu su uspešno ažurirani!".equals(result) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(HttpResponse.getResponseWithData(result, Map.of("value", result), status));
    }
 
    // Endpoint za brisanje gosta po emailu. Prima email iz URL putanje.
    @DeleteMapping("/delete/{email}") 
    public ResponseEntity<Response> deleteGuest(@PathVariable String email) { // @PathVariable!
    	
        String result = gostService.deleteGuest(email); // Poziva servis za brisanje gosta.
        // Određuje HTTP status na osnovu poruke iz servisa.
        HttpStatus status = "Gost je uspešno obrisan!".equals(result) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(HttpResponse.getResponseWithData(result, Map.of("value", result), status));
    }

    // Endpoint za pretragu gostiju po imenu ili prezimenu. Prima string za pretragu.
    @GetMapping("/search/{searchRequest}")
    public ResponseEntity<Response> searchGuests(@PathVariable("searchRequest") String searchRequest) {
        List<GostDTO> gosti = gostService.searchGuests(searchRequest); // Pretraga putem servisa.
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Pretraga gostiju izvršena.",
                        Map.of("values", gosti), // Vraća listu pronađenih gostiju.
                        HttpStatus.OK
                ));
    }

    // Endpoint za login gosta. Prima email i lozinku iz URL putanje.
    @GetMapping("/login/{email}/{password}")
    public ResponseEntity<Response> login(@PathVariable("email") String email, @PathVariable("password") String password) {
        GostDTO gost = gostService.getGuestByEmail(email); // Pronalazi gosta po emailu.

        if (gost == null) {
            // Neuspešan login ako korisnik ne postoji.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // 401 Unauthorized.
                .body(HttpResponse.getResponse("Neuspešan login - ne postoji korisnik sa datim emailom.", HttpStatus.UNAUTHORIZED));
        }

        // Provera lozinke.
        if (!gost.getLozinka().equals(password)) {
            // Neuspešan login ako je lozinka pogrešna.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // 401 Unauthorized.
                .body(HttpResponse.getResponse("Neuspešan login - pogrešna lozinka.", HttpStatus.UNAUTHORIZED));
        }

        // Uspešan login.
        return ResponseEntity.ok(
            HttpResponse.getResponseWithData(
                "Uspešan login",
                Map.of("guest", gost), // Vraća podatke o ulogovanom gostu.
                HttpStatus.OK
            )
        );
    }

    // Endpoint za registraciju gosta. Prima GostDTO iz tela zahteva.
    // Slično kao `/add`, ali možda ima specifičnu logiku za registraciju.
    @PostMapping("/register")
    public ResponseEntity<Response> registerGuest(@RequestBody GostDTO gost) {
        String result = gostService.registerGuest(gost); // Poziva servis za registraciju.
        // Određuje HTTP status na osnovu poruke iz servisa.
        HttpStatus status = "Gost je uspešno dodat!".equals(result) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(HttpResponse.getResponseWithData(result, Map.of("value", result), status));
    }
}