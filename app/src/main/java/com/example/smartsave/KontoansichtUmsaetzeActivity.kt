package com.example.smartsave

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.CenteredText
import com.example.smartsave.helpers.LabelledDropdownMenu
import com.example.smartsave.helpers.LabelledDropdownMenuUmsatz
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.StandardText
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

        MainColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LabelledInputField(label = "Filter", value = "") {

            }
            Column(modifier = Modifier.fillMaxWidth()) {
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
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Button(onClick = { /*TODO*/ }) {
                        Text(
                            text = "TBA",
                            style = TextStyle(fontSize = 15.sp)
                        )
                    }
                    Button(onClick = { /*TODO*/ }) {
                        Text(
                            text = "TBA",
                            style = TextStyle(fontSize = 15.sp)
                        )

                    }
                    Button(onClick = { /*TODO*/ }) {
                        Text(
                            text = "TBA",
                            style = TextStyle(fontSize = 15.sp)
                        )

                    }
                    Button(onClick = { /*TODO*/ }) {
                        Text(
                            text = "TBA",
                            style = TextStyle(fontSize = 15.sp)
                        )

                    }

                }
                Column {
                    for ((index, umsatz) in umsatzlist.withIndex()) Row(){
                        LabelledDropdownMenuUmsatz(label = "Datum1", options = katList, umsatz)
                    }

                }

            }
        }
    }
}
fun getUmsaetze() : List<Umsatz>{
    val umsatz1 = Umsatz("umsatz1", 534.55)
    val umsatz2 = Umsatz("umsatz2", 34.55)
    val umsatz3 = Umsatz("umsatz3", 4.55)
    val umsatz4 = Umsatz("umsatz4", 888.55)

    val umsatzliste = listOf(umsatz1,umsatz2,umsatz3,umsatz4)
    return umsatzliste

}
fun getKat() : List<Kategorie>{
    val kat1 = Kategorie("Auto")
    val kat2 = Kategorie("Essen")
    val kat3 = Kategorie("COC")
    val kat4 = Kategorie("Gym")


    val katList = mutableListOf(kat4,kat2,kat3,kat1)

    return katList
}

