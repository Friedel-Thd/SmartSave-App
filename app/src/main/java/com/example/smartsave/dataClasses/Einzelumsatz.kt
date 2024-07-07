package com.example.smartsave.dataClasses

import com.example.smartsave.dataClasses.Kategorie
import java.io.Serializable
import java.time.LocalDate
import java.util.Date

data class Einzelumsatz (var verwendungsZweck: String, var betrag: Double, var datum: LocalDate): Serializable {
    var kategorie = Kategorie("Nicht zugeordnet")
    var id = 0
    var hasParentUmsatz = false
    fun isAssigned(): Boolean {
        return (kategorie.name != "Nicht zugeordnet")
    }

}