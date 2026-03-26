package com.roombooker.fullstack_backend.controller;

import com.roombooker.fullstack_backend.connection.HttpResponse;
import com.roombooker.fullstack_backend.connection.Response;
import com.roombooker.fullstack_backend.service.TipSobeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/tip-sobe") // osnovna ruta
public class TipSobeController {

    private final TipSobeService tipSobeService;

    public TipSobeController(TipSobeService tipSobeService) {
        this.tipSobeService = tipSobeService;
    }

    //Dohvatanje svih tipova soba (koristi se za prikaz u formama ili pregled tipova)
    @GetMapping("/all")
    public ResponseEntity<Response> getAll() {
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Uspešno pronađeni svi tipovi soba!",
                        Map.of("values", tipSobeService.getAll()),
                        HttpStatus.OK
                )
        );
    }
}
