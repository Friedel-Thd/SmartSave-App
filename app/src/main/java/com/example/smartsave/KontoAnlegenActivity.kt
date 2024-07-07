package com.example.smartsave

import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import com.example.smartsave.helpers.ErrorMsg
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.StandardText

class KontoAnlegenActivity : SmartSaveActivity() {

    private val kontenlisteState = mutableStateOf<List<Konto>>(emptyList())
    var db = DbHelper(this)
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kontenlisteState.value = db.getAllKonten()
    }

    @Composable
    override fun BoxScope.GenerateLayout() {
        var textKontoNr by remember { mutableStateOf("") }
        var textBLZ by remember { mutableStateOf("") }
        var textBIC by remember { mutableStateOf("") }
        var textIBAN by remember { mutableStateOf("") }
        var textBemerkung by remember { mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }
        var kontonrExistsError by remember { mutableStateOf(false) }
        val kontenListe by remember {kontenlisteState}
        val bundle = intent.extras
        val bankkontoExists = bundle!!.getBoolean("BankkontoExists")
        val kreditkartenkontoExists = bundle.getBoolean("KreditkartenkontoExists")
        val radioOptions = listOf("Bankkonto", "Sparkonto", "Kreditkartenkonto")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LabelledInputField(label = "Konto Nr.*", value = textKontoNr, KeyboardOptions(keyboardType = KeyboardType.Number)) { textKontoNr = it }
            LabelledInputField(label = "BLZ*", value = textBLZ, KeyboardOptions(keyboardType = KeyboardType.Number)) { textBLZ = it }
            LabelledInputField(label = "BIC*", value = textBIC, KeyboardOptions()) { textBIC = it }
            LabelledInputField(label = "IBAN*", value = textIBAN, KeyboardOptions()) { textIBAN = it }
            LabelledInputField(label = "Bemerkung", value = textBemerkung, KeyboardOptions()) { textBemerkung = it }

            if (isError) {
                Text(
                    text = "Bitte alle Pflichtfelder ausfüllen!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                radioOptions.forEach { text ->
                    val isOptionEnabled = when (text) {
                        "Bankkonto" -> !bankkontoExists
                        "Kreditkartenkonto" -> !kreditkartenkontoExists
                        else -> true
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    if (isOptionEnabled) {
                                        onOptionSelected(text)
                                    }
                                }
                            )
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            enabled = isOptionEnabled,
                            selected = (text == selectedOption),
                            onClick = {
                                if (isOptionEnabled) {
                                    onOptionSelected(text)
                                }
                            }
                        )
                        StandardText(
                            text = text,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }

                }
                if(textKontoNr.isNotEmpty() && kontonrExistsError) ErrorMsg(msg = "Konto mit dieser Kontonummmer existiert bereits!")
            }
        }

        AlignedButton(alignment = Alignment.BottomStart, text = "Abbrechen") { finish() }
        AlignedButton(alignment = Alignment.BottomEnd, text = "Speichern") {
            isError = (textKontoNr.isEmpty() || textBIC.isEmpty() || textBLZ.isEmpty() || textIBAN.isEmpty())
            for (konto in kontenListe){
                if (konto.kontonr == textKontoNr.toInt()) kontonrExistsError = true
            }
            if (!isError && !kontonrExistsError) {
                val konto = Konto(textKontoNr.toInt(), textBLZ, textBIC, textIBAN, textBemerkung, selectedOption)
                db.insertKonto(konto)
                Log.d("Entry", "Entry so mesisch")
                finish()
            }
        }
    }

}