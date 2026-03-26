package com.roombooker.fullstack_backend.controller;

import com.roombooker.fullstack_backend.connection.HttpResponse;
import com.roombooker.fullstack_backend.connection.Response;
import com.roombooker.fullstack_backend.service.GradService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin("http://localhost:3000") 
@RestController
@RequestMapping("/grad") // osnovna ruta za grad
public class GradController {

    private final GradService gradService;

    public GradController(GradService gradService) {
        this.gradService = gradService;
    }

    // Dohvatanje svih gradova
    @GetMapping("/all")
    public ResponseEntity<Response> getAll() {
        return ResponseEntity.ok(
                HttpResponse.getResponseWithData(
                        "Uspešno pronađeni svi gradovi!",
                        Map.of("values", gradService.getAll()),
                        HttpStatus.OK
                )
        );
    }
}
