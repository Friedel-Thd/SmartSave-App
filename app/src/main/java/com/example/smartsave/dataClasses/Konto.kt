package com.example.smartsave.dataClasses

import java.io.Serializable

data class Konto(
    val kontotnr: Int,
    val BLZ: Int,
    val BIC: Int,
    val IBAN: String,
    val bemerkung: String,
    val Kontoart: String,
    var umsatzLst: List<Umsatz>
)
    : Serializable