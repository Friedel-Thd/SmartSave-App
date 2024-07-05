package com.example.smartsave.dataClasses

import java.io.Serializable
import java.util.Date

data class Sparziel(val name: String, val betrag : Double, val zieldatum : Date, val monatsrate : Double, val zielKonto: Konto, val auszahlungsKonto : Konto) : Serializable {
    var id = 0
    fun getEinzahlungsliste(): List<Umsatz> {
        //TODO entfernen todo wenn der aal hier getestet ist
        var einzahlungsListe: List<Umsatz> = mutableListOf()
        for (einzahlung in zielKonto.umsatzLst) {
            if (einzahlung.verwendungsZweck == this.name) {
                val mutableList = einzahlungsListe.toMutableList()
                mutableList.add(einzahlung)
                einzahlungsListe = mutableList
            }
        }
        return einzahlungsListe
    }

    fun calculateProgress(): Int {
        var summe = 0.0
        for (einzahlung in zielKonto.umsatzLst) {
            if (einzahlung.verwendungsZweck == this.name) {
                summe += einzahlung.betrag
            }
        }
        return (summe/this.betrag).toInt()
    }

}