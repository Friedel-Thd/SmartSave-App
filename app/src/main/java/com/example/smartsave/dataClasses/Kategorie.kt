package com.example.smartsave.dataClasses

import java.io.Serializable

data class Kategorie(val name: String) : Serializable {

    fun isAssigned(): Boolean {
        //TODO check if Kategorie is assigned to a Umsatz
        return true
    }
}