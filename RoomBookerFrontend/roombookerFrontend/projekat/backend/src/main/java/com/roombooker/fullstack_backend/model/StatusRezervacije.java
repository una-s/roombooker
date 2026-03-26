package com.roombooker.fullstack_backend.model;

public enum StatusRezervacije {
CONFIRMED, //svi uslovi za rezervaciju su ispunjeni
CANCELLED, //ako gost ili hotel otkaze pre predivdjenog vremena za check-in
CHECKED_IN, //gost je dosao u sobu
CHECKED_OUT, //gost je napustio sobu ~ completed
PENDING //nisu svi uslovi ispunjeni da bi recepcionar potvrdio rezervaciju
}
