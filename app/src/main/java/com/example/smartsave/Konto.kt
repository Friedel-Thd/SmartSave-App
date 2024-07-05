package com.example.smartsave

import java.io.Serializable

data class Konto(
    val name: String,
    val kontostand: Double,
    val kontotnr: Int,
    val BLZ: Int,
    val BIC: Int,
    val IBAN: String,
    val bemerkung: String,
    val Kontoart: String)
    : Serializable