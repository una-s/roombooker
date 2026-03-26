package com.roombooker.fullstack_backend.controller;

import com.roombooker.fullstack_backend.connection.HttpResponse;
import com.roombooker.fullstack_backend.connection.Response;
import com.roombooker.fullstack_backend.dto.GostDTO;
import com.roombooker.fullstack_backend.dto.RezervacijaDTO;
import com.roombooker.fullstack_backend.dto.SobaDTO;
import com.roombooker.fullstack_backend.model.StatusRezervacije;
import com.roombooker.fullstack_backend.service.GostService;
import com.roombooker.fullstack_backend.service.RezervacijaService;
import com.roombooker.fullstack_backend.service.SobaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/reservations")
public class RezervacijaController {

    private final RezervacijaService rezervacijaService;

    public RezervacijaController(RezervacijaService rezervacijaService) {
        this.rezervacijaService = rezervacijaService;
    }

    // Dodavanje rezervacije
    @PostMapping("/add")
    public ResponseEntity<Response> addReservation(@RequestBody RezervacijaDTO rezervacija) {
        String result = rezervacijaService.addRezervacija(rezervacija);
        HttpStatus status = "Rezervacija je uspesno kreirana!".equals(result) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(HttpResponse.getResponseWithData(result, Map.of("value", result), status));
    }
    @PostMapping("/createByReceptionist") // od strane recepcionarra
    public ResponseEntity<Response> createByReceptionist(@RequestBody RezervacijaDTO dto) {
        String result = rezervacijaService.saveByReceptionist(dto);
        HttpStatus status = result.contains("uspešno") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(HttpResponse.getResponseWithData(result, Map.of("value", result), status));
    }


    // Pregled svih rezervacija
    @GetMapping("/all")
    public ResponseEntity<Response> getAllReservations() {
        List<RezervacijaDTO> lista = rezervacijaService.getAllRezervacije();
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Sve rezervacije su pronadjene.",
                        Map.of("values", lista),
                        HttpStatus.OK
                ));
    }

    // Dohvatanje rezervacije po ID-u
    @GetMapping("/getReservation/{id}")
    public ResponseEntity<Response> getReservationById(@PathVariable("id") int id) {
        RezervacijaDTO rezervacija = rezervacijaService.getRezervacija(id);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Rezervacija je pronadjena.",
                        Map.of("value", rezervacija),
                        HttpStatus.OK
                ));
    }

    // Izmena rezervacije
    @PostMapping("/update")
    public ResponseEntity<Response> updateReservation(@RequestBody RezervacijaDTO rezervacija) {
        String result = rezervacijaService.updateRezervacija(rezervacija);
        HttpStatus status = "Rezervacija ažurirana uspešno!".equals(result) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(HttpResponse.getResponseWithData(result, Map.of("value", result), status));
    }

    // Brisanje rezervacije
    @PostMapping("/delete")
    public ResponseEntity<Response> deleteReservation(@RequestBody int rezervacijaId) {
        String result = rezervacijaService.deleteRezervacija(rezervacijaId);
        HttpStatus status = "Rezervacija obrisana uspešno!".equals(result) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(HttpResponse.getResponseWithData(result, Map.of("value", result), status));
    }

    // Dohvatanje rezervacija za odredjenog gosta
    @PostMapping("/getGuestsReservations")
    public ResponseEntity<Response> getReservationsForGuest(@RequestBody GostDTO gostDTO) {
        List<RezervacijaDTO> lista = rezervacijaService.getRezervacijeZaGosta(gostDTO);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Rezervacije za gosta su pronadjene.",
                        Map.of("values", lista),
                        HttpStatus.OK
                ));
    }

    // Dohvatanje rezervacija po statusu
    @GetMapping("/getReservationStatus/{status}")
    public ResponseEntity<Response> getReservationsByStatus(@PathVariable("status") StatusRezervacije status) {
        List<RezervacijaDTO> lista = rezervacijaService.getRezervacijaStatus(status);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Rezervacije po statusu su pronadjene.",
                        Map.of("values", lista),
                        HttpStatus.OK
                ));
    }

    // Dohvatanje rezervacija za sobu (provera slobodnih termina)
    @GetMapping("/getReservationsForRoom/{idSoba}")
    public ResponseEntity<Response> getReservationsForRoom(@PathVariable("idSoba") int idSoba) {
        List<RezervacijaDTO> lista = rezervacijaService.getRezervacijeZaSobu(idSoba);
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Rezervacije za sobu su pronadjene.",
                        Map.of("values", lista),
                        HttpStatus.OK
                ));
    }
}
