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
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.StandardText


class SparzielAnAufActivity: SmartSaveActivity() {
//TODO Werte für vars aus Sparziel holen



    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        var zielkonto = getZielkonto()
        var auszahlkonto = getAuszahlkonto()
        var rate = getRate()
        var zweck = getZweck()

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StandardText("Bitte richten Sie folgenden Dauerauftrag/Terminüberweisung ein:")
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Auszahlkonto")
                StandardText(auszahlkonto.kontonr.toString())

            }
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Zielkonto")
                StandardText(zielkonto.kontonr.toString())
            }
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Betrag")
                StandardText(rate.toString())
            }
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Verwendungszweck")
                StandardText(zweck)
            }
        }

        AlignedButton(alignment = Alignment.BottomStart, text = "Abbrechen") {finish()}
        AlignedButton(alignment = Alignment.BottomEnd, text = "Anlegen/Auflösen") {
        }
    }
}
@Composable
fun getAuszahlkonto() : Konto {
    return Konto(5000,"23","23","23","23","2")
}
@Composable
fun getZielkonto() : Konto {
    return Konto(5000,"23","23","23","23","2")
}
@Composable
fun getRate() : Double{
    return 300.0
}

@Composable
fun getZweck(): String  {
    return "Testzweck"
}