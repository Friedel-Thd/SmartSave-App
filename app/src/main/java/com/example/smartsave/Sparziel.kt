package com.example.smartsave

import java.io.Serializable

data class Sparziel(val name: String) : Serializable {

    fun getEinzahlungsliste(): List<Umsatz> {
        //TODO return eine liste mit allen einzahlungen von bankkonto zu sparkonto mit entsprechendem verwendungszweck
        //TODO Attribute Zielkonto, Auszahlkonto und Betrag
        val einzahlung1 = Umsatz("Einzahlung1", 50.0)
        val einzahlung2 = Umsatz("Einzahlung2", 50.0)
        val einzahlung3 = Umsatz("Einzahlung3", 50.0)
        val einzahlung4 = Umsatz("Einzahlung4", 50.0)
        val einzahlungsListe: List<Umsatz> = mutableListOf(einzahlung1, einzahlung2, einzahlung3, einzahlung4)

        return einzahlungsListe
    }

    fun getProgress(): Int {
        //TODO Progress des Sparziels berchnen (Alle Umsätze des Bankkontos auf Sparkonto / SparzielGesamt)
        return 50
    }
}