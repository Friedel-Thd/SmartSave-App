package com.example.smartsave.dataClasses

import com.example.smartsave.dataClasses.Kategorie
import java.io.Serializable
import java.util.Date

data class Einzelumsatz (val verwendungsZweck: String, val betrag: Double, val datum: Date): Serializable {
    private var kategorie = Kategorie("Nicht Zugewiesen")
    private var id = 0
    fun isAssigned(): Boolean {
        //TODO return false if umsatz.kategorie is "nicht zugewiesen" else return true
        return (kategorie.name != "Nicht Zugewiesen")
    }
    fun setKategorie(kategorie: Kategorie) {
        this.kategorie = kategorie
    }
    fun setId(id:Int){
        this.id = id
    }
    fun getId(): Int{
        return id
    }
}