package com.example.smartsave.dataClasses

import java.io.Serializable

data class Konto(
    val kontonr: Int,
    val blz: String,
    val bic: String,
    val iban: String,
    val bemerkung: String,
    val kontoart: String
): Serializable{
    var umsatzLst: List<Umsatz> = listOf()
    var kontostand = 0.0
    }