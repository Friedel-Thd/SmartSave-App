package com.example.smartsave.dataClasses

import android.util.Log
import java.io.Serializable
import java.time.LocalDate
import java.util.Calendar


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

    fun getUmsatzKategorie(month: Month, kategorie: Kategorie): Double {
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

            if (umsatz.datum.isAfter(selectedDate) && (umsatz.kategorie.name == kategorie.name)) {

                if(umsatz.hasAssignedEinzelumsatz()) {
                    for (einzelumsatz in umsatz.einzelumsatzListe) {
                        if (einzelumsatz.datum.isAfter(selectedDate) && (einzelumsatz.kategorie.name == kategorie.name)) {
                            umsatzSumme += einzelumsatz.betrag
                        }
                    }
                } else {
                    umsatzSumme += umsatz.betrag
                }
            }
        }
        return umsatzSumme
    }

    fun getUmsatz(month: Month): Double {
        var umsatzSumme = 0.0
        var selectedDate = LocalDate.of(month.year, month.month, 1)

        for (umsatz in umsatzList) {
            if (umsatz.datum.isAfter(selectedDate)) {
                umsatzSumme += umsatz.betrag
            }
        }
        return umsatzSumme
    }

}