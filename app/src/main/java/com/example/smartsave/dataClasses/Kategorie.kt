package com.example.smartsave.dataClasses

import java.io.Serializable

data class Kategorie(val name: String) : Serializable {
    private var id = 0
    fun isAssigned(): Boolean {
        //TODO check if Kategorie is assigned to a Umsatz
        return true
    }
    fun setID(id:Int){
        this.id = id
    }
    fun getID(): Int{
        return id
    }
}