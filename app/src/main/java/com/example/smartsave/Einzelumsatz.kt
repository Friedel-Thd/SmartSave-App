package com.example.smartsave

import java.io.Serializable

data class Einzelumsatz (val name: String, val value: Double): Serializable {
    private var kategorie = Kategorie("Nicht Zugewiesen")
    fun isAssigned(): Boolean {
        //TODO return false if umsatz.kategorie is "nicht zugewiesen" else return true
        return (kategorie.name != "Nicht Zugewiesen")
    }
    fun setKategorie(kategorie: Kategorie) {
        this.kategorie = kategorie
    }
}