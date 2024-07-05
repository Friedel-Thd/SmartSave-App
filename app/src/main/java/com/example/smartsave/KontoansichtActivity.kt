package com.example.smartsave

import DbHelper
import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
import com.example.smartsave.dataClasses.Month
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.IconListItem
import com.example.smartsave.helpers.ListItem
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.StandardText
import java.time.LocalDate
import kotlin.math.roundToInt


private const val MAX_MONTHS = 12


class KontoansichtActivity : SmartSaveActivity() {
    var db = DbHelper(this)

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        // Datum
        val currentMonth = LocalDate.now().let { Month(it.year, it.monthValue) }
        // getBudget des monats
        val budget = getBudget(currentMonth)
        // getKategorien
        val kategorienliste = getKategorienliste()
        // gesamtausgaben
        var gesamtausgaben = 0.0

        var months by remember { mutableIntStateOf(1) }

        var bankkonto = db.getBankkonto()


        MainColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {


            if(bankkonto != null) {
                IconListItem(
                    text = bankkonto.kontostand.toString(),
                    modifier = Modifier.clickable {
                        val intent = Intent(this@KontoansichtActivity, KontoansichtUmsaetzeActivity::class.java)
                        startActivity(intent)
                    },
                    iconId = R.drawable.arrow_swap
                )
            }

            ListItem(text = currentMonth.toString())


            //TODO DRAW RECTANGLE FOR CATEGORIES
            // INPUT -> Liste von Kategorien(Name, Betrag) -> Anteil an Ausgaben (Monat) -> Darstellung dann % mäßisch
            // Dynamische Anzahl an Kategorien!
            // Alle Umsätze & Kontostand
            // Kontostand zu begin + alle einkünfte => Budget
            // Ausgaben / Budget => Anteil in Prozent
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
            ) {
                inset(horizontal = 100f, vertical = 100f) {
                    // draw nicht zugewiesenes Rect
                    drawRect(color = Color.White, size = Size(size.width, size.height))

                    var x = 0f
                    for ((index, kategorie) in kategorienliste.withIndex()) {
                        //get anteil, draw rect mit entspr. größe
                        val umsatzKategorie = getUmsatzKategorie(kategorie, currentMonth)
                        gesamtausgaben += umsatzKategorie
                        val percentageSize = calcPercentage(umsatzKategorie, budget)

                        val width = size.width * percentageSize.toFloat()

                        // draw rect irgendwie mit dieser size richtig
                        drawRect(
                            color = colorGen(index),
                            size = Size(width, size.height),
                            topLeft = Offset(x, 0f)
                        )
                        x += width
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
                @Composable
                fun CategoryDisplay(color: Color, text: String) = Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Canvas(modifier = Modifier
                        .padding(end = 30.dp)
                        .size(16.dp)
                    ) {
                        drawRect(
                            color = color,
                            size = size
                        )
                    }
                    StandardText(text = text)
                }

                for ((index, kategorie) in kategorienliste.withIndex()) CategoryDisplay(
                    color = colorGen(index),
                    text = kategorie.name
                )
                CategoryDisplay(color = Color.White, text = "Nicht zugeordnet")
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
            }
        }

        AlignedButton(alignment = Alignment.BottomCenter, text = "Zurück") {finish()}
    }

    private fun calcPercentage(umsatzKategorie: Double, budget: Double): Double {
        //TODO Berechne Anteil des umsatzes einer Kategorie am Budget
        return (umsatzKategorie / budget)
    }

    private fun getUmsatzKategorie(kategorie: Kategorie, month: Month): Double {
        //TODO summa alle umsätze/ausgaben einer kategorie seit bestimmtem monat/jahr
        return 150.0
    }

    private fun getKategorienliste() = buildList {
        //TODO Implement datenbank kram
        repeat(16) {
            add(Kategorie("Kategorie $it"))
        }
    }

    private fun getBudget(month: Month): Double {
        //TODO Implement datenbank kram, hole summe aller ausgaben
        return 3000.0
    }

}


private fun colorGen(index: Int) = Color.hsv((index * 67.5f) % 360, 1f, 1f)