package com.example.smartsave

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.smartsave.helpers.LabelledDatePickerButton
import com.example.smartsave.helpers.LabelledDropdownMenuKategory
import com.example.smartsave.helpers.LabelledDropdownMenuUmsatz
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity

class EinzelumsatzEditActivity : SmartSaveActivity() {
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout(){
        var selectedDate by remember { mutableStateOf("") }
        var katList = getKat()

        MainColumn (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
            )
        {


            LabelledInputField(label = "Bezeichung", value ="", KeyboardOptions() ) {

            }
            LabelledInputField(label = "Betrag", value ="", KeyboardOptions() ) {

            }
            LabelledDatePickerButton(label = "Datum",
                selectedDate = selectedDate,
                onDateSelected = { date -> selectedDate = date },
                false
            )
            LabelledDropdownMenuKategory(label = "Kategorie", options = katList)


        }
        AlignedButton(alignment = Alignment.BottomStart, text = "Zurück") {finish()}
        AlignedButton(alignment = Alignment.BottomEnd, text = "Auszahlen"){}

    }
}