package com.example.smartsave

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.CenteredText
import com.example.smartsave.helpers.LabelledDropdownMenu
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.LabelledDatePickerButton

class SparzielActivity : SmartSaveActivity() {

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        var textName by remember { mutableStateOf("") }
        var selectedDate by remember { mutableStateOf("") }
        var textBetrag by remember { mutableStateOf("") }
        var kontolist = getKontolist()
        var monatsRate = 0.0

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LabelledInputField(label = "Name", value = textName) {textName = it}

            //TODO Input auf zahlen beschränken
            LabelledInputField(label = "Betrag", value = textBetrag) { textBetrag = it }

            //TODO Von String zu einem Date Format ändern?
            LabelledDatePickerButton(label = "Auszahlungszeitraum",
                selectedDate = selectedDate,
                onDateSelected = { date -> selectedDate = date },
                true

            )

            //TODO Bei leerer Kontoliste auf Layout #4a Weiterleiten
            LabelledDropdownMenu("Auszahlungskonto", kontolist)
            LabelledDropdownMenu("Zielkonto", kontolist)

            //TODO Monatliche Rate aus Betrag und Auszahlungszeitraum berechnen
            monatsRate = calcMonatsRate(selectedDate, textBetrag)
            CenteredText(text = "Monatliche Rate: ")
            CenteredText(text = "$monatsRate€")

        }

        AlignedButton(alignment = Alignment.BottomStart, text = "Abbrechen") {finish()}
        AlignedButton(alignment = Alignment.BottomEnd, text = "Weiter") {
            //TODO Spaziel Anlegen/Auflösen (Layout #5)
            //TODO Sparziel Objekt mitgeben
            val intent = Intent(this@SparzielActivity, SparzielAnAufActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun getKontolist(): List<Konto> {
        //TODO get liste mit allen angelegten Konten
        var konto1 = Konto( 500, 23,23,"23","23","23")
     //   var konto2 = Konto(name = "Konto 2", 500.0)
      //  var konto3 = Konto(name = "Konto 3", 500.0)
       // var konto4 = Konto(name = "Konto 4", 500.0)
        var kontoliste = listOf(konto1)

        return kontoliste
    }

    fun calcMonatsRate(selectedDate: String, textBetrag: String): Double {
        //TODO Berechne Monatsrate
        return 10.0
    }
}