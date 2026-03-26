package com.roombooker.fullstack_backend.connection;

import org.springframework.http.HttpStatus;
import java.util.Map;

// Pomoćna klasa za kreiranje standardizovanih HTTP Response objekata.
public class HttpResponse {

    /**
     * Kreira odgovor sa porukom, podacima i HTTP statusom.
     * Koristi se za vraćanje podataka klijentu (npr. rezultat pretrage).
     */
    public static Response getResponseWithData(String message, Map<?, ?> data, HttpStatus httpStatus) {
        return new Response(message, data, httpStatus);
    }

    /**
     * Kreira odgovor samo sa porukom i HTTP statusom.
     * Koristi se za jednostavne potvrde (npr. uspeh/neuspjeh operacije).
     */
    public static Response getResponse(String message, HttpStatus httpStatus) {
        return new Response(message, httpStatus);
    }

}