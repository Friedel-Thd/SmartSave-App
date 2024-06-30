package com.example.smartsave

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import kotlin.math.roundToInt


private const val COLOR_OFFSET = 67.5f
private val standardTextStyle = TextStyle(fontSize = 20.sp)


class KontoansichtActivity : SmartSaveActivity() {

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        // Datum
        val currentMonth = LocalDate.now().let {Month(it.year, it.monthValue)}
        // getBudget des monats
        val budget = getBudget(currentMonth)
        // getKategorien
        val kategorienliste = getKategorienliste()
        // gesamtausgaben
        var gesamtausgaben = 0.0

        var months by remember { mutableIntStateOf(1) }


        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CenteredText(text = currentMonth.toString())


            //TODO DRAW RECTANGLE FOR CATEGORIES
            // INPUT -> Liste von Kategorien(Name, Betrag) -> Anteil an Ausgaben (Monat) -> Darstellung dann % mäßisch
            // Dynamische Anzahl an Kategorien!
            // Alle Umsätze & Kontostand
            // Kontostand zu begin + alle einkünfte => Budget
            // Ausgaben / Budget => Anteil in Prozent
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)) {
                inset(horizontal = 100f, vertical = 100f) {
                    var x = 0f
                    for ((index, kategorie) in kategorienliste.withIndex()) {
                        //get anteil, draw rect mit entspr. größe
                        val umsatzKategorie = getUmsatzKategorie(kategorie, currentMonth)
                        gesamtausgaben += umsatzKategorie
                        val percentageSize = calcPercentage(umsatzKategorie, budget)

                        val width = size.width*percentageSize.toFloat()

                        // draw rect irgendwie mit dieser size richtig
                        drawRect(
                            color = Color.hsv((index * COLOR_OFFSET) % 360, 1f, 1f),
                            size = Size(width, size.height),
                            topLeft = Offset(x, 0f)
                        )
                        x += width
                    }

                    // draw nicht zugewiesenes Rect
                    drawRect(color = Color.White,
                        size = Size(size.width - x, size.height),
                        topLeft = Offset(x, 0f))
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .weight(0.5f, false),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for ((index, kategorie) in kategorienliste.withIndex()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                    ) {
                        Canvas(modifier = Modifier
                            .padding(end = 40.dp)
                            .fillMaxHeight()
                            .width(30.dp)) {
                            drawRect(
                                color = Color.hsv((index * COLOR_OFFSET) % 360, 1f, 1f),
                                size = Size(50f, 50f)
                            )
                        }
                        Text(text = kategorie.name, style = standardTextStyle)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                ) {
                    Canvas(modifier = Modifier
                        .padding(end = 40.dp)
                        .fillMaxHeight()
                        .width(30.dp)) {
                        drawRect(
                            color = Color.White,
                            size = Size(50f, 50f)
                        )
                    }
                    Text(text = "Nicht Zugeordnet", style = standardTextStyle)
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                val maxMonths = 12
                Slider(
                    value = maxMonths - months.toFloat(),
                    onValueChange = { months = maxMonths - it.roundToInt() },
                    valueRange = 0f..(maxMonths - 1f),
                    steps = maxMonths - 2
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "$maxMonths Monate",
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




            ElevatedButton(
                onClick = {
                    finish()
                },
                modifier = Modifier
                    .padding(bottom = 40.dp, end = 25.dp)
                    .size(width = 150.dp, height = 80.dp),
            ) {
                Text(text = "Zurück", style = standardTextStyle)
            }
        }
    }

    private fun calcPercentage(umsatzKategorie: Double, budget: Double): Double {
        //TODO Berechne Anteil des umsatzes einer Kategorie am Budget
        return (umsatzKategorie / budget)
    }

    private fun getUmsatzKategorie(kategorie: Kategorie, month: Month): Double {
        //TODO summa alle umsätze/ausgaben einer kategorie seit bestimmtem monat/jahr
        return 150.0
    }

    private fun getKategorienliste(): List<Kategorie> {
        //TODO Implement datenbank kram
        val kategorienListe = mutableListOf<Kategorie>()

        kategorienListe.add(Kategorie("Miete"))
        kategorienListe.add(Kategorie("Essen"))
        kategorienListe.add(Kategorie("Auto"))
        kategorienListe.add(Kategorie("Etc"))
        kategorienListe.add(Kategorie("Miete"))
        kategorienListe.add(Kategorie("Etc"))
        kategorienListe.add(Kategorie("Miete"))
        kategorienListe.add(Kategorie("Etc"))
        kategorienListe.add(Kategorie("Miete"))
        kategorienListe.add(Kategorie("Essen"))
        kategorienListe.add(Kategorie("Auto"))

        return kategorienListe
    }

    private fun getBudget(month: Month): Double {
        //TODO Implement datenbank kram, hole kontostand an datum + alle seit dem postiiven umsätze
        return 3000.0
    }
}