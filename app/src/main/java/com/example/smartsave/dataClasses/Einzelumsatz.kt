package com.example.smartsave.dataClasses

import com.example.smartsave.dataClasses.Kategorie
import java.io.Serializable
import java.time.LocalDate
import java.util.Date

data class Einzelumsatz (val verwendungsZweck: String, val betrag: Double, val datum: LocalDate): Serializable {
    var kategorie = Kategorie("Nicht zugeordnet")
    var id = 0
    fun isAssigned(): Boolean {
        //TODO return false if umsatz.kategorie is "Nicht zugeordnet" else return true
        return (kategorie.name != "Nicht zugeordnet")
    }
}