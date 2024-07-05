package com.example.smartsave.dataClasses
import java.io.Serializable
import java.util.Date

data class Umsatz (val verwendungsZweck: String, val betrag: Double, val datum : Date): Serializable {
   private var kategorie = Kategorie("Nicht Zugewiesen")
   private var einzelumsatzListe: List<Einzelumsatz> = listOf()
    private var id = 0
  fun isAssigned(): Boolean {
   //TODO return false if umsatz.kategorie is "nicht zugewiesen" else return true
   return (kategorie.name != "Nicht Zugewiesen")
  }

  fun setKategorie(kategorie: Kategorie) {
   this.kategorie = kategorie
  }

  fun getEinzelumsatzListe(): List<Einzelumsatz> {
   return einzelumsatzListe
  }

  fun addEinzelumsatz(einzelumsatz: Einzelumsatz) {
   //TODO EInzelumsatzliste ist immer empty why brotha
   val mutableList = einzelumsatzListe.toMutableList()
   mutableList.add(einzelumsatz)
   einzelumsatzListe = mutableList
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
    fun setId(id:Int){
        this.id = id
    }
    fun getId(): Int{
        return id
    }
 }