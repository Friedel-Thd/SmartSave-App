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

    fun getAusgabenByKategorie(month: Month, kategorie: Kategorie): Double {
        var umsatzSumme = 0.0
        var selectedDate = LocalDate.of(month.year, month.month, 1)

        for (umsatz in umsatzList) {

            /*
            Log.d("umsatzdatum", "${umsatz.datum}")
            Log.d("selectedDate", "$selectedDate")
            Log.d("If", "${umsatz.datum.isAfter(selectedDate)}")
            Log.d("umsatzkatname", "${umsatz.kategorie.name}")
            Log.d("katname", "${kategorie.name}")
            Log.d("Ifkat", "${(umsatz.kategorie.name == kategorie.name)}")
            */

            if (umsatz.datum.isAfter(selectedDate) && (umsatz.kategorie.name == kategorie.name) && umsatz.betrag < 0) {

                if(umsatz.hasAssignedEinzelumsatz()) {
                    for (einzelumsatz in umsatz.einzelumsatzListe) {
                        if (einzelumsatz.datum.isAfter(selectedDate) && (einzelumsatz.kategorie.name == kategorie.name)) {
                            umsatzSumme += -einzelumsatz.betrag
                        }
                    }
                } else {
                    umsatzSumme += -umsatz.betrag
                }
            }
        }
        return umsatzSumme
    }

    fun getAusgaben(month: Month): Double {
        var umsatzSumme = 0.0
        var selectedDate = LocalDate.of(month.year, month.month, 1)

        for (umsatz in umsatzList) {
            if (umsatz.datum.isAfter(selectedDate) && umsatz.betrag < 0) {
                umsatzSumme += -umsatz.betrag
            }
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