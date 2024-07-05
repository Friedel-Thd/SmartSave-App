package com.example.smartsave

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.dataClasses.Sparziel
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.StandardText


class SparzielAnAufActivity: SmartSaveActivity() {

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        val bundle = intent.extras
        val tempSparziel = bundle!!.getSerializable("tempSparziel") as Sparziel
        val mode = bundle.getString("mode")


        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StandardText("Bitte richten Sie folgenden Dauerauftrag/Terminüberweisung ein:")
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Auszahlkonto")
                StandardText(tempSparziel.auszahlungsKonto.kontonr.toString())
            }
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Zielkonto")
                StandardText(tempSparziel.zielKonto.kontonr.toString())
            }
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Betrag")
                StandardText(tempSparziel.monatsrate.toString())
            }
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Verwendungszweck")
                StandardText(tempSparziel.name)
            }
        }

        //TODO Darstellung Drucken/QR-Code und so mäßig

        AlignedButton(alignment = Alignment.BottomStart, text = "Abbrechen") {finish()}
        AlignedButton(alignment = Alignment.BottomEnd, text = "Anlegen/Auflösen") {
            if(mode == "anlegen") {
                //TODO Sparziel in datenbank speichern

            } else if(mode == "auflösen") {
                //TODO Sparziel aus Datenbank entfernen

            } else {
                throw error("Kein gültiger mode!")
            }
        }
    }
}