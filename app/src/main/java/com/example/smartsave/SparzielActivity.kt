package com.example.smartsave

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.smartsave.dataClasses.Sparziel
import com.example.smartsave.dataClasses.parseDate
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.CenteredText
import com.example.smartsave.helpers.ErrorMsg
import com.example.smartsave.helpers.labelledDropdownMenu
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.LabelledDatePickerButton
import com.example.smartsave.helpers.LabelledInputFieldBetrag
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SparzielActivity : SmartSaveActivity() {
    private val kontoListState = mutableStateOf(listOf<Konto>())
    private val sparzielListeState = mutableStateOf<List<Sparziel>>(emptyList())
    private val sparKontoListeState = mutableStateOf<List<Konto>>(emptyList())

    var db = DbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        kontoListState.value = db.getBankUndKreditKontenListe()
        sparzielListeState.value = db.getSparzielListe()
        sparKontoListeState.value = db.getSparKontenListe()

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
        var sparkontolist by remember { sparKontoListeState }
        val sparzielListe by remember { sparzielListeState }
        var ausgangKonto by remember { mutableStateOf<Konto?>(null) }
        var zielKonto by remember { mutableStateOf<Konto?>(null) }
        var monatsRate = 0.0
        var numError  by remember { mutableStateOf(false) }
        var matchingKontoError  by remember { mutableStateOf(false) }


        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LabelledInputField(label = "Name*", value = textName, KeyboardOptions()) {
                textName = it
            }
            LabelledInputFieldBetrag(label = "Betrag*", value = textBetrag, KeyboardOptions(keyboardType = KeyboardType.Number)) { textBetrag = it }
            LabelledDatePickerButton(label = "Auszahlungszeitraum*",
                selectedDate = selectedDate,
                onDateSelected = { date -> selectedDate = date },
                mindate = true,
                maxdate = false
            )

            ausgangKonto = labelledDropdownMenu("Auszahlungskonto*", kontolist)
            zielKonto = labelledDropdownMenu("Zielkonto*", sparkontolist)
            if (kontolist.isEmpty()) {
                val intent = Intent(this@SparzielActivity, SparzielActivity::class.java)
                startActivity(intent)
                finish()
            }

            monatsRate = calcMonatsRate(selectedDate, textBetrag)
            CenteredText(text = "Monatliche Rate: ")
            CenteredText(text = "$monatsRate€")
            if(isError) ErrorMsg(msg = "Bitte alle Pflichtfelder ausfüllen!")
            if(numError) ErrorMsg(msg = "Bitte einen gültigen Betrag angeben!")
            if(matchingKontoError) ErrorMsg(msg = "Auszahl- und Zielkonto müssen unterschiedlich sein!")
            if(sparzielListe.any { it.name == textName }) ErrorMsg(msg = "Sparziel mit diesem Namen existiert bereits!")

        }


        AlignedButton(alignment = Alignment.BottomStart, text = "Abbrechen") {finish()}
        AlignedButton(alignment = Alignment.BottomEnd, text = "Weiter") {

                isError = textBetrag.isEmpty() || textName.isEmpty() || selectedDate.isEmpty() || zielKonto == null || ausgangKonto == null
                if(textBetrag != "-"){
                    if (textBetrag.isNotEmpty()) numError = textBetrag.toDouble()< 0
                } else {
                    numError = true
                }

                if(!(ausgangKonto == null && zielKonto == null) && ausgangKonto == zielKonto) matchingKontoError = true
                if(!isError && !numError && !matchingKontoError && textBetrag != "-"){
                    val intent = Intent(this@SparzielActivity, SparzielAnAufActivity::class.java)
                    val tempSparziel = Sparziel(textName, textBetrag.toDouble(),  parseDate(selectedDate), monatsRate, zielKonto!!, ausgangKonto!!)
                    intent.putExtra("Sparziel", tempSparziel)
                    intent.putExtra("mode", "anlegen")
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

        val result = if (monatsDiff > 0) { betrag / monatsDiff } else { betrag }

        return BigDecimal(result).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

}