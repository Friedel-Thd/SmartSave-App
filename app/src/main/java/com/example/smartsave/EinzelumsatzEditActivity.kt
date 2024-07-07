package com.example.smartsave

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.smartsave.dataClasses.Einzelumsatz
import com.example.smartsave.dataClasses.Kategorie
import com.example.smartsave.dataClasses.parseDate
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.LabelledDatePickerButton
import com.example.smartsave.helpers.labelledDropdownMenuKategory
import com.example.smartsave.helpers.labelledDropdownMenuKategoryPreset
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import java.time.format.DateTimeFormatter

class EinzelumsatzEditActivity : SmartSaveActivity() {
    var db = DbHelper(this)
    private val kategorienListeState = mutableStateOf<List<Kategorie>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kategorienListeState.value = db.getKategorienListe()

    }
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()


    @Composable
    override fun BoxScope.GenerateLayout(){
        val mode = intent.extras?.getString("mode")
        var einzelumsatz: Einzelumsatz? = if (mode == "editieren") {
            intent.extras?.getSerializable("einzelumsatz") as? Einzelumsatz
        } else { null }

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var selectedDate by remember { mutableStateOf(einzelumsatz?.datum?.format(formatter) ?: "") }
        var textBezeichung by remember { mutableStateOf(einzelumsatz?.verwendungsZweck ?: "") }
        var textBetrag by remember { mutableStateOf(einzelumsatz?.betrag?.toString() ?: "") }
        val kategorienListe by remember { kategorienListeState }
        var isError by remember { mutableStateOf(false) }
        var selectedKategorie by remember { mutableStateOf<Kategorie?>(einzelumsatz?.kategorie) }

        MainColumn (
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
            )
        {
            LabelledInputField(label = "Verwendungszweck*", value = textBezeichung, KeyboardOptions() ) {textBezeichung = it}
            LabelledInputField(label = "Betrag*", value = textBetrag, KeyboardOptions(keyboardType = KeyboardType.Number) ) { textBetrag = it}
            LabelledDatePickerButton(label = "Datum*",
                selectedDate = selectedDate,
                onDateSelected = { date -> selectedDate = date },
                mindate = false,
                maxdate = true
            )

            if (mode == "anlegen") {
                selectedKategorie = labelledDropdownMenuKategory(label = "Kategorie", options = kategorienListe)
            } else if (mode == "editieren" && selectedKategorie != null) {
                selectedKategorie = labelledDropdownMenuKategoryPreset(label = "Kategorie", options = kategorienListe, preset = selectedKategorie!!)
            }

            if(isError) {
                Text(
                    text = "Bitte alle Pflichtfelder ausfüllen!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        AlignedButton(alignment = Alignment.BottomStart, text = "Zurück") {finish()}
        AlignedButton(alignment = Alignment.BottomEnd, text = "Speichern"){
            isError = selectedDate.isEmpty() || textBezeichung.isEmpty() || textBetrag.isEmpty()
            //TODO bei editieren den restbetrag des umsatz holen, um sicherzugehen, dass neuer betrag nicht zu groß/klein ist
            if(!isError){
                if (mode == "anlegen") {
                    val neweinzelumsatz = Einzelumsatz(textBezeichung,textBetrag.toDouble(), parseDate(selectedDate))
                    neweinzelumsatz.kategorie = selectedKategorie!!

                    db.insertEinzelumsatz(neweinzelumsatz)
                } else if (mode == "editieren" && einzelumsatz != null) {
                    einzelumsatz.verwendungsZweck = textBezeichung
                    einzelumsatz.betrag = textBetrag.toDouble()
                    einzelumsatz.datum = parseDate(selectedDate)
                    einzelumsatz.kategorie = selectedKategorie!!

                    db.editEinzelumsatz(einzelumsatz)
                }

                finish()
            }
        }

    }
}