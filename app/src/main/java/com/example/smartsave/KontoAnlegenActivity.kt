package com.example.smartsave

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class KontoAnlegenActivity : SmartSaveActivity() {

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        var textKontoNr by remember { mutableStateOf("") }
        var textBLZ by remember { mutableStateOf("") }
        var textBIC by remember { mutableStateOf("") }
        var textIBAN by remember { mutableStateOf("") }
        var textBemerkung by remember { mutableStateOf("") }

        val radioOptions = listOf("Bankkonto", "Sparkonto", "Kreditkartenkonto")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1] ) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LabelledInputField(value = textKontoNr,"Konto Nr.") {textKontoNr = it}
            LabelledInputField(value = textBLZ, "BLZ") { textBLZ = it }
            LabelledInputField(value = textBIC, "BIC") { textBIC = it }
            LabelledInputField(value = textIBAN, "IBAN") { textIBAN = it }
            LabelledInputField(value = textBemerkung, "Bemerkung") { textBemerkung = it }

            Spacer(modifier = Modifier.height(30.dp))

            Column {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    onOptionSelected(text)
                                }
                            )
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) }
                        )
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 8.dp),
                            style = standardTextStyle
                        )
                    }
                }
            }
        }

        ElevatedButton(
            onClick = {
                finish()
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 40.dp, start = 25.dp)
                .size(width = 150.dp, height = 80.dp),
        ) {
            Text(text = "Abbrechen", style = standardTextStyle)
        }

        ElevatedButton(
            onClick = {
                //TODO Kontodaten speichern
                finish()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 40.dp, end = 25.dp)
                .size(width = 150.dp, height = 80.dp),
        ) {
            Text(text = "Speichern", style = standardTextStyle)
        }
    }
}