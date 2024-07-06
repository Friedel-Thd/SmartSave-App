package com.example.smartsave.dataClasses
import java.io.Serializable
import java.time.LocalDate
import java.util.Date

data class Umsatz (val verwendungsZweck: String, val betrag: Double, val datum : LocalDate): Serializable {
   var kategorie = Kategorie("Nicht zugewiesen")
   var einzelumsatzListe: List<Einzelumsatz> = listOf()
   var id = 0
  fun isAssigned(): Boolean {
   //TODO return false if umsatz.kategorie is "nicht zugewiesen" else return true
   return (kategorie.name != "Nicht zugewiesen")
  }

  fun addEinzelumsatz(einzelumsatz: Einzelumsatz) {
   //TODO EInzelumsatzliste ist immer empty why brotha
   val mutableList = einzelumsatzListe.toMutableList()
   mutableList.add(einzelumsatz)
   einzelumsatzListe = mutableList
  }

  fun hasAssignedEinzelumsatz(): Boolean {
   //TODO return true if Umsatz has at least 1 assigned einzelumsatz
   var returnValue: Boolean = false
   for (einzelumsatz in einzelumsatzListe) {
    if (einzelumsatz.isAssigned()) {
     returnValue = true
    }
   }
   return returnValue
  }
 }