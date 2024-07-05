package com.example.smartsave.dataClasses

import java.io.Serializable

data class Konto(
    val kontotnr: Int,
    val BLZ: Int,
    val BIC: Int,
    val IBAN: String,
    val bemerkung: String,
    val Kontoart: String,

)
    : Serializable{
    var umsatzLst: List<Umsatz> = listOf()
    var kontostand = 0.0
    fun getKnr(): Int{
        return kontotnr
    }
    fun setKontostand(kontostand: Double){
        this.kontostand = kontostand
    }
    fun getKontostand(): Double{
        return kontostand
    }
    }