package com.example.smartsave

import DbHelper
import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.dataClasses.Einzelumsatz
import com.example.smartsave.dataClasses.Kategorie
import com.example.smartsave.dataClasses.parseDate
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.LabelledDatePickerButton
import com.example.smartsave.helpers.LabelledDropdownMenuKategory
import com.example.smartsave.helpers.LabelledDropdownMenuUmsatz
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

class EinzelumsatzEditActivity : SmartSaveActivity() {
    var db = DbHelper(this)
    private val kategorienListeState = mutableStateOf<List<Kategorie>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        kategorienListeState.value = db.getKategorienListe()

        super.onCreate(savedInstanceState)
    }
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()


    @Composable
    override fun BoxScope.GenerateLayout(){
        var selectedDate by remember { mutableStateOf("") }
        var textBezeichung by remember { mutableStateOf("") }
        var textBetrag by remember { mutableStateOf("") }
        val kategorienListe by remember { kategorienListeState }
        var isError by remember { mutableStateOf(false) }

        MainColumn (
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
            )
        {
            LabelledInputField(label = "Bezeichung*", value =textBezeichung, KeyboardOptions() ) {textBezeichung = it}
            LabelledInputField(label = "Betrag*", value =textBetrag, KeyboardOptions() ) { textBetrag = it}
            LabelledDatePickerButton(label = "Datum*",
                selectedDate = selectedDate,
                onDateSelected = { date -> selectedDate = date },
                false
            )
            LabelledDropdownMenuKategory(label = "Kategorie", options = kategorienListe)

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

            if(!isError){
                //TODO Datenbank ding für einzelumsatz anlegen mesisch

                db.insertEinzelumsatz(Einzelumsatz(textBezeichung,textBetrag.toDouble(), parseDate(selectedDate)))

                finish()
            }
        }

    }
}