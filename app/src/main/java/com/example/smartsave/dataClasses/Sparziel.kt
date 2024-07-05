package com.example.smartsave.dataClasses

import java.io.Serializable
import java.util.Date

data class Sparziel(val name: String, val betrag : Double, val zieldatum : Date, val monatsrate : Double, val zielKonto: Konto, val auszahlungsKonto : Konto) : Serializable {
    private var id = 0
    fun getEinzahlungsliste(): List<Umsatz> {
        //TODO return eine liste mit allen einzahlungen von bankkonto zu sparkonto mit entsprechendem verwendungszweck
        //TODO Attribute Zielkonto, Auszahlkonto und Betrag
        val einzahlung1 = Umsatz("Einzahlung1", 50.0,Date(1999,10,10))
        val einzahlung2 = Umsatz("Einzahlung2", 50.0,Date(1999,10,10))
        val einzahlung3 = Umsatz("Einzahlung3", 50.0,Date(1999,10,10))
        val einzahlung4 = Umsatz("Einzahlung4", 50.0,Date(1999,10,10))
        val einzahlung5 = Umsatz("Einzahlung5", 50.0,Date(1999,10,10))
        val einzahlung6 = Umsatz("Einzahlung6", 50.0,Date(1999,10,10))
        val einzahlung7 = Umsatz("Einzahlung7", 50.0,Date(1999,10,10))
        val einzahlung8 = Umsatz("Einzahlung8", 50.0,Date(1999,10,10))
        val einzahlungsListe: List<Umsatz> = mutableListOf(einzahlung1, einzahlung2, einzahlung3, einzahlung4, einzahlung5, einzahlung6, einzahlung7, einzahlung8)

        return einzahlungsListe
    }

    fun getProgress(): Int {
        //TODO Progress des Sparziels berchnen (Alle Umsätze des Bankkontos auf Sparkonto / SparzielGesamt)
        return 50
    }
    fun setId(id:Int){
        this.id = id
    }
    fun getId(): Int{
        return id
    }
}