package com.example.smartsave.dataClasses

import java.io.Serializable
import java.time.LocalDate


data class Konto(
    val kontonr: Int,
    val blz: String,
    val bic: String,
    val iban: String,
    val bemerkung: String,
    val kontoart: String
): Serializable{
    var umsatzList: List<Umsatz> = listOf()
    var kontostand = 0.0

    //TODO Ausgaben richtig berechnen
    fun getAusgabenByKategorie(month: Month, kategorie: Kategorie): Double {
        var umsatzSumme = 0.0
        val selectedDate = LocalDate.of(month.year, month.month, 1)

        for (umsatz in umsatzList) {
            if (umsatz.hasAssignedEinzelumsatz()) {
                for (einzelumsatz in umsatz.einzelumsatzListe) {
                    if (einzelumsatz.datum.isAfter(selectedDate) && einzelumsatz.kategorie.id == kategorie.id && einzelumsatz.betrag < 0) {
                        umsatzSumme += -einzelumsatz.betrag
                    }
                }
            } else if (umsatz.datum.isAfter(selectedDate) && umsatz.kategorie.id == kategorie.id && umsatz.betrag < 0) {
                umsatzSumme += -umsatz.betrag
                }
            }

        return umsatzSumme
    }

    fun getAusgaben(month: Month, kategorienliste: List<Kategorie>): Double {
        var umsatzSumme = 0.0

        for (kategorie in kategorienliste) {
            umsatzSumme += this.getAusgabenByKategorie(month, kategorie)
        }

        return umsatzSumme
    }

    fun calcKontostand(): Double {
        var kontostandSumme = 0.0

        for (umsatz in umsatzList) {
            kontostandSumme += umsatz.betrag
        }
        return kontostandSumme
    }

}