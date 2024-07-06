package com.example.smartsave.dataClasses
import java.io.Serializable
import java.time.LocalDate
import java.util.Date

data class Umsatz (val verwendungsZweck: String, val betrag: Double, val datum : LocalDate): Serializable {
    var kategorie = Kategorie("Nicht zugeordnet")
    var einzelumsatzListe: List<Einzelumsatz> = listOf()
    var id = 0
    fun isAssigned(): Boolean {
        return (kategorie.name != "Nicht zugeordnet")
    }

    fun addEinzelumsatz(einzelumsatz: Einzelumsatz) {
        val mutableList = einzelumsatzListe.toMutableList()
        mutableList.add(einzelumsatz)
        einzelumsatzListe = mutableList
    }

    fun hasAssignedEinzelumsatz(): Boolean {
        var returnValue: Boolean = false
        for (einzelumsatz in einzelumsatzListe) {
            if (einzelumsatz.isAssigned()) {
             returnValue = true
            }
        }
        return returnValue
    }

    fun hasEinzelumsatz(): Boolean {
        return einzelumsatzListe.isNotEmpty()
    }

}