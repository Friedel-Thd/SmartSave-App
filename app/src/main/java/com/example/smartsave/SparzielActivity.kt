package com.example.smartsave

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.CenteredText
import com.example.smartsave.helpers.LabelledDropdownMenu
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.StandardText

class SparzielActivity : SmartSaveActivity() {

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        var textName by remember { mutableStateOf("") }
        var textAuszahlungszeitraum by remember { mutableStateOf("") }
        var textBetrag by remember { mutableStateOf("") }
        var kontolist = getKontolist()

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LabelledInputField(label = "Name", value = textName) {textName = it}
            //TODO Auszahlungszeitraum zu Date Format ändern
            LabelledInputField(label = "Auszahlungszeitraum", value = textAuszahlungszeitraum) { textAuszahlungszeitraum = it }
            LabelledInputField(label = "Betrag", value = textBetrag) { textBetrag = it }

            //TODO Bei leerer Kontoliste auf Layout #4a Weiterleiten
            LabelledDropdownMenu("Auszahlungskonto", kontolist)
            LabelledDropdownMenu("Zielkonto", kontolist)

            CenteredText(text = "Monatliche Rate: ")
            //TODO Monatliche Rate aus Betrag und Auszahlungszeitraum berechnen

        }

        AlignedButton(alignment = Alignment.BottomStart, text = "Abbrechen") {finish()}
        AlignedButton(alignment = Alignment.BottomEnd, text = "Weiter") {
            //TODO Spaziel Anlegen/Auflösen (Layout #5)

        }
    }

    fun getKontolist(): List<Konto> {
        //TODO get liste mit allen angelegten Konten
        var konto1 = Konto(name = "Konto 1")
        var konto2 = Konto(name = "Konto 2")
        var konto3 = Konto(name = "Konto 3")
        var konto4 = Konto(name = "Konto 4")
        var kontoliste = listOf(konto1, konto2, konto3, konto4)

        return kontoliste
    }
}