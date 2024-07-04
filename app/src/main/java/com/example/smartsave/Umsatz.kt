package com.example.smartsave
import java.io.Serializable
 data class Umsatz (val name: String, val value: Double): Serializable {
   private var kategorie = Kategorie("Nicht Zugewiesen")
   private var einzelumsatzListe: List<Einzelumsatz> = listOf()
  fun isAssigned(): Boolean {
   //TODO return false if umsatz.kategorie is "nicht zugewiesen" else return true
   return (kategorie.name != "Nicht Zugewiesen")
  }

  fun setKategorie(kategorie: Kategorie) {
   this.kategorie = kategorie
  }

  fun addEinzelumsatz(einzelumsatz: Einzelumsatz) {
   //TODO EInzelumsatzliste ist immer empty why brotha
    einzelumsatzListe.toMutableList().add(einzelumsatz)
  }

  fun hasAssignedEinzelumsatz(): Boolean {
   //TODO return true if Umsatz has at least 1 einzelumsatz
   var returnValue: Boolean = false
   for (einzelumsatz in einzelumsatzListe) {
    if (einzelumsatz.isAssigned()) {
     returnValue = true
    }
   }
   return returnValue
  }
 }