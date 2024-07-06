package com.example.smartsave

import DbHelper
import android.os.Bundle
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartsave.dataClasses.Kategorie
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.dataClasses.Month
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.LabelledDropdownMenuUmsatz
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import java.time.LocalDate
import kotlin.math.roundToInt

private const val MAX_MONTHS = 12


class KontoansichtUmsaetzeActivity : SmartSaveActivity() {
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
    override fun BoxScope.GenerateLayout() {
        val bundle = intent.extras
        val konto: Konto = bundle!!.getSerializable("Konto") as Konto
        val kategorienListe by remember { kategorienListeState }

        var months by remember { mutableIntStateOf(1) }
        val currentMonth = LocalDate.now().let { Month(it.year, it.monthValue) }
        var scrollstate = rememberScrollState()


        MainColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            //TODO Filtern mit filterinput und umsatz.verwendungszweck
            //TODO Filtern mit kategoriefilter und umsatz.kategorie
            //TODO Filtern mit zeit (es kracht)
            LabelledInputField(label = "Filter", value = "", KeyboardOptions()) {

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Slider(
                    value = MAX_MONTHS - months.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    onValueChange = { months = MAX_MONTHS - it.roundToInt() },
                    valueRange = 0f..(MAX_MONTHS - 1f),
                    steps = MAX_MONTHS - 2
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "$MAX_MONTHS Monate",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(1f / 3)
                    )
                    Text(
                        text = "ab ${currentMonth - months}",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.fillMaxWidth(.5f)
                    )
                    Text(
                        text = "1 Monat",
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                //TODO die richtigen buttons reinmachen
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .horizontalScroll(scrollstate),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    for ( kategorie in kategorienListe) {
                        Button(modifier = Modifier.padding(horizontal = 5.dp),
                            onClick = {
                            //TODO set kategory filter für umsätze

                        }) {
                            Text(
                                text = kategorie.name,
                                style = TextStyle(fontSize = 15.sp)
                            )
                        }
                    }

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .fillMaxHeight()
                        .weight(1f, true),
                    verticalArrangement = Arrangement.spacedBy(12.dp)

                ) {
                    for ((index, umsatz) in konto.umsatzList.withIndex()) Row() {
                        //TODO ausgewählten umsatz bzw. umsatzID mitgeben
                        LabelledDropdownMenuUmsatz(kategorienListe, umsatz, LocalContext.current)
                    }
                }
            }
        }


        AlignedButton(
            alignment = Alignment.BottomStart,
            iconId = R.drawable.arrow_forward,
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
        ) {

        }

        //TODO Import funktion imitieren mäßig, testdaten (umsätze) in konto einfügen
        AlignedButton(
            alignment = Alignment.BottomEnd,
            iconId = R.drawable.plus,
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
        ) {

        }
        AlignedButton(alignment = Alignment.BottomCenter, text = "Zurück", modifier = Modifier.height(70.dp)) {
            finish()
        }
    }

}

