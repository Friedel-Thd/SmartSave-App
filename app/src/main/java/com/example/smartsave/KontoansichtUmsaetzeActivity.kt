package com.example.smartsave

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartsave.dataClasses.Einzelumsatz
import com.example.smartsave.dataClasses.Kategorie
import com.example.smartsave.dataClasses.Month
import com.example.smartsave.dataClasses.Umsatz
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.LabelledDropdownMenuUmsatz
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import java.time.LocalDate
import kotlin.math.roundToInt

private const val MAX_MONTHS = 12


class KontoansichtUmsaetzeActivity : SmartSaveActivity() {

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {

        var months by remember { mutableIntStateOf(1) }
        val currentMonth = LocalDate.now().let { Month(it.year, it.monthValue) }
        var umsatzlist = getUmsaetze()
        var katList = getKat()
        var scrollstate = rememberScrollState()


        MainColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            LabelledInputField(label = "Filter", value = "") {

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
                    for ( kategorie in katList) {
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
                    for ((index, umsatz) in umsatzlist.withIndex()) Row() {
                        //TODO ausgewählten umsatz bzw. umsatzID mitgeben
                        LabelledDropdownMenuUmsatz(label = "Datum1", options = katList, umsatz, LocalContext.current)
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
        AlignedButton(
            alignment = Alignment.BottomEnd,
            iconId = R.drawable.plus,
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
        ) {

        }
        AlignedButton(alignment = Alignment.BottomCenter, text = "Zurück", modifier = Modifier.height(70.dp)) { finish()
        }
    }

}


    fun getUmsaetze(): List<Umsatz> {
        val umsatz1 = Umsatz("umsatz1", 534.55)
        val umsatz2 = Umsatz("umsatz2", 34.55)
        val umsatz3 = Umsatz("umsatz3", 4.55)
        val umsatz4 = Umsatz("umsatz4", 888.55)

        val einzelumsatz1 = Einzelumsatz("Döner", 5.0)
        einzelumsatz1.setKategorie(Kategorie("Essen"))
        umsatz2.setKategorie(Kategorie("Gym"))
        umsatz2.addEinzelumsatz(einzelumsatz1)

        val einzelumsatz2 = Einzelumsatz("Döner", 5.0)
        einzelumsatz2.setKategorie(Kategorie("Essen"))
        umsatz2.addEinzelumsatz(einzelumsatz2)

    val test = umsatz2.hasAssignedEinzelumsatz()

        val umsatzliste = listOf(umsatz1, umsatz2, umsatz3, umsatz4)
        return umsatzliste

    }

    fun getKat(): List<Kategorie> {
        val kat1 = Kategorie("Auto")
        val kat2 = Kategorie("Essen")
        val kat3 = Kategorie("COC")
        val kat5 = Kategorie("Nicht Zugewiesen")
        val kat6 = Kategorie("Gym")
        val kat7 = Kategorie("Gym")
        val kat8 = Kategorie("Gym")
        val kat9 = Kategorie("Gym")
        val kat10 = Kategorie("Gym")


        val katList = listOf(kat5, kat2, kat3, kat1, kat6, kat7, kat8, kat9, kat10)
        return katList
    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogHandler(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
