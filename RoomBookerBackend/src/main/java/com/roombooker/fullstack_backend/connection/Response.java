package com.roombooker.fullstack_backend.connection;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

// Ova anotacija osigurava da Jackson (za JSON serijalizaciju) ignoriše polja koja su null.
// To znači da ako 'data' polje nije postavljeno, neće se pojaviti u JSON odgovoru.
@JsonInclude(NON_NULL)
public class Response {

    private String message; // Poruka za klijenta (npr. "Uspešno obrisano")
    private Map<?, ?> data;    // Opcioni podaci koji se vraćaju klijentu (npr. objekat, lista)
    private HttpStatus status; // HTTP status kod odgovora (npr. 200 OK, 400 BAD_REQUEST)

    // Konstruktor za odgovore bez dodatnih podataka.
    public Response(String message, HttpStatus httpStatus) {
        this.message = message;
        this.status = httpStatus;
    }

    // Konstruktor za odgovore koji uključuju podatke.
    public Response(String message, Map<?, ?> data, HttpStatus httpStatus) {
        this.message = message;
        this.data = data;
        this.status = httpStatus;
    }

    // Getter za poruku.
    public String getMessage() {
        return message;
    }

    // Getter za podatke.
    public Object getData() {
        return data;
    }

    // Nedostaju getteri za 'status', ali su često potrebni za potpunu funkcionalnost
    // i da bi Jackson serijalizovao 'status' polje.
    // public HttpStatus getStatus() { return status; }
}