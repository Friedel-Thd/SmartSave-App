package com.example.smartsave

import java.io.Serializable

data class Kategorie(val name: String) : Serializable {

    fun isAssigned(): Boolean {
        //TODO check if Kategorie is assigned to a Umsatz
        return true
    }
}