package com.example.smartsave

import DbHelper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.dataClasses.Kategorie
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.dataClasses.Month
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.CategoryDisplay
import com.example.smartsave.helpers.IconListItem
import com.example.smartsave.helpers.ListItem
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import java.time.LocalDate
import kotlin.math.roundToInt


private const val MAX_MONTHS = 12


class KontoansichtActivity : SmartSaveActivity() {
    var db = DbHelper(this)
    private lateinit var kategorienListe: List<Kategorie>
    private lateinit var bankkonto: Konto

    override fun onCreate(savedInstanceState: Bundle?) {
        bankkonto = db.getBankkonto()!!
        kategorienListe = db.getKategorienListe()
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        bankkonto = db.getBankkonto()!!
        kategorienListe = db.getKategorienListe()
        super.onResume()
    }

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        val currentMonth = LocalDate.now().let { Month(it.year, it.monthValue) }
        var selectedDate by remember { mutableStateOf(currentMonth - 1) }

        var months by remember { mutableIntStateOf(1) }


        MainColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            IconListItem(
                text = bankkonto.kontostand.toString(),
                modifier = Modifier.clickable {
                    val intent = Intent(this@KontoansichtActivity, KontoansichtUmsaetzeActivity::class.java)
                    intent.putExtra("Konto", bankkonto)
                    startActivity(intent)
                },
                iconId = R.drawable.arrow_swap
            )

            ListItem(text = currentMonth.toString())

            //TODO Es gibt noch das problem, dass wenn mehrere kategorien mit dem selben namen existieren
            // diese umsätze in diesen kategorien doppelt gezählt werden mäßig
            // sollte mit kategorien todos gefixxt werden
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
            ) {
                inset(horizontal = 100f, vertical = 100f) {
                    val gesamtausgaben = bankkonto.getAusgaben(LocalDate.now().let { selectedDate }, kategorienListe)
                    val safeGesamtausgaben = if (gesamtausgaben == 0.0) 1.0 else gesamtausgaben

                    drawRect(color = Color.White, size = Size(size.width, size.height))

                    var x = 0f
                    for ((index, kategorie) in kategorienListe.withIndex()) {
                        if(kategorie.name != "Nicht zugeordnet") {

                            val umsatzKategorie = bankkonto.getAusgabenByKategorie(selectedDate, kategorie)
                            val percentageSize = (umsatzKategorie / safeGesamtausgaben)
                            val width = size.width * percentageSize.toFloat()

                            Log.d("KontoansichtActivity", "Kategorie: ${kategorie.name}, Umsatz: $umsatzKategorie, Percentage Size: $percentageSize, Width: $width")

                            if(umsatzKategorie > 0.0) {
                                drawRect(
                                    color = colorGen(index),
                                    size = Size(width, size.height),
                                    topLeft = Offset(x, 0f)
                                )
                                x += width
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .weight(1f, true),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                var nichtZugeordnetBetrag = 0.0

                for ((index, kategorie) in kategorienListe.withIndex()) {
                    val umsatzKategorie = bankkonto.getAusgabenByKategorie(selectedDate, kategorie)
                    if(kategorie.name != "Nicht zugeordnet") {
                        CategoryDisplay(
                            color = colorGen(index),
                            text = kategorie.name,
                            betrag = umsatzKategorie
                        )
                    } else {
                        nichtZugeordnetBetrag = umsatzKategorie
                    }
                }
                CategoryDisplay(color = Color.White, text = "Nicht zugeordnet", betrag = nichtZugeordnetBetrag)
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = MAX_MONTHS - months.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    onValueChange = {
                        months = MAX_MONTHS - it.roundToInt()
                        selectedDate = currentMonth - months
                        },
                    valueRange = 0f..(MAX_MONTHS - 1f),
                    steps = MAX_MONTHS -2
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "$MAX_MONTHS Monate",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(1f / 3)
                    )
                    Text(
                        text = "ab ${selectedDate}",
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
            }
        }

        AlignedButton(alignment = Alignment.BottomCenter, text = "Zurück") { finish()}
    }

}

private fun colorGen(index: Int) = Color.hsv((index * 67.5f) % 360, 1f, 1f)