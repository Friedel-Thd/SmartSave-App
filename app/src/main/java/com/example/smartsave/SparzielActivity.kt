package com.example.smartsave

import DbHelper
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SparzielActivity : SmartSaveActivity() {
    private val kontoListState = mutableStateOf(listOf<Konto>())
    var db = DbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        kontoListState.value = db.getAllKonten()

        super.onCreate(savedInstanceState)
    }

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        var textName by remember { mutableStateOf("") }
        var selectedDate by remember { mutableStateOf("") }
        var textBetrag by remember { mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }
        var kontolist by remember { kontoListState }
        var monatsRate = 0.0

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LabelledInputField(label = "Name*", value = textName, KeyboardOptions()) { textName = it }

            //TODO Input auf zahlen beschränken
            LabelledInputField(label = "Betrag*", value = textBetrag, KeyboardOptions(keyboardType = KeyboardType.Number)) { textBetrag = it }

            //TODO Von String zu einem Date Format ändern?
            LabelledDatePickerButton(label = "Auszahlungszeitraum*",
                selectedDate = selectedDate,
                onDateSelected = {
                    date -> selectedDate = date
                    isError = date.isEmpty()

                                 },
                true

            )

            //TODO Bei leerer Kontoliste auf Layout #4a Weiterleiten ( Popup alter mäßisch aber erst bei click auf die menues)
            LabelledDropdownMenu("Auszahlungskonto*", kontolist)
            LabelledDropdownMenu("Zielkonto*", kontolist)
            if (kontolist.isEmpty()) {
                val intent = Intent(this@SparzielActivity, SparzielActivity::class.java)
                startActivity(intent)
                finish()
            }

            //TODO Monatliche Rate aus Betrag und Auszahlungszeitraum berechnen
            monatsRate = calcMonatsRate(selectedDate, textBetrag)
            CenteredText(text = "Monatliche Rate: ")
            CenteredText(text = "$monatsRate€")
            if (isError){
                Text(
                    text = "Bitte alle Pflichtfelder ausfüllen!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }

        //TODO Beschränkungen hinzugügen: Auszahlungskonto != Zielkonto und Zielkonto muss Kontotyp Sparkonto sein
        AlignedButton(alignment = Alignment.BottomStart, text = "Abbrechen") {finish()}
        AlignedButton(alignment = Alignment.BottomEnd, text = "Weiter") {
            isError = textBetrag.isEmpty() || textName.isEmpty() || selectedDate.isEmpty()
            if(!isError){
                //TODO Spaziel Anlegen/Auflösen (Layout #5)
                //TODO Sparziel Objekt mitgeben (noch nicht in datenbank schreiben mäßisch)
                val intent = Intent(this@SparzielActivity, SparzielAnAufActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

    }

    private fun calcMonatsRate(selectedDate: String, textBetrag: String): Double {
        if (selectedDate.isEmpty() || textBetrag.isEmpty()) return 0.0

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = dateFormat.parse(selectedDate)
        val today = Calendar.getInstance().time

        val monatsDiff = ((date.time - today.time) / (1000L * 60 * 60 * 24 * 30)).toInt()

        val betrag = textBetrag.toDoubleOrNull() ?: return 0.0

        val result = if (monatsDiff > 0) {
            betrag / monatsDiff
        } else {
            betrag
        }

        return BigDecimal(result).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

}