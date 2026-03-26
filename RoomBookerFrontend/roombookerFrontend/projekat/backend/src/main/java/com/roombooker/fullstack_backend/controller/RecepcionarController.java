package com.roombooker.fullstack_backend.controller;

import com.roombooker.fullstack_backend.connection.HttpResponse;
import com.roombooker.fullstack_backend.connection.Response;
import com.roombooker.fullstack_backend.dto.GostDTO;
import com.roombooker.fullstack_backend.dto.RecepcionarDTO;
import com.roombooker.fullstack_backend.service.RecepcionarService;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/receptionists")
public class RecepcionarController {

    private final RecepcionarService recepcionarService;

    public RecepcionarController(RecepcionarService recepcionarService) {
        this.recepcionarService = recepcionarService;
    }

    // Login recepcionara
    @GetMapping("/login/{email}/{password}")
    public ResponseEntity<Response> login(@PathVariable String email, @PathVariable String password) {
        RecepcionarDTO recepcionar = recepcionarService.login(email, password);
        if (recepcionar != null) {
            return ResponseEntity.ok(HttpResponse.getResponseWithData(
                "Uspesan login",
                Map.of("receptionist", recepcionar),
                HttpStatus.OK
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(HttpResponse.getResponse("Neuspesan login", HttpStatus.UNAUTHORIZED));
        }
    }

}