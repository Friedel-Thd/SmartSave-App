package com.example.smartsave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
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

class KontoansichtActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GenerateLayout()
        }

    }

    @Preview
    @Composable
    fun PreviewLayout() {
        GenerateLayout()
    }

    @Composable
    fun GenerateLayout() {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(
                        color = Color(199, 216, 230, 255)
                    )
                    .padding(top = 25.dp, end = 20.dp, start = 20.dp)
            ) {
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
                                    color = Color.hsl((index * 40f)%360, 1f, 0.5f),
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
                                        color = Color.hsl((index * 40f)%360, 1f, 0.5f),
                                        size = Size(50f, 50f)
                                    )
                                }
                                Text(text = kategorie.name, style = TextStyle(fontSize = 20.sp))
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
                            Text(text = "Nicht Zugeordnet", style = TextStyle(fontSize = 20.sp))
                        }
                    }

                    Slider(
                        value = 12 - months.toFloat(),
                        onValueChange = { months = 12 - it.roundToInt() },
                        valueRange = 0f..11f,
                        steps = 10
                    )
                    Row (modifier = Modifier.fillMaxWidth()) {
                        Text(text = (currentMonth - 12).toString(), textAlign = TextAlign.Left, modifier = Modifier.fillMaxWidth(1f/3))
                        Text(
                            text = "ab ${currentMonth - months}",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.fillMaxWidth(.5f)
                        )
                        Text(text = "Heute", textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth())
                    }




                    ElevatedButton(
                        onClick = {
                            finish()
                        },
                        modifier = Modifier
                            .padding(bottom = 40.dp, end = 25.dp)
                            .size(width = 150.dp, height = 80.dp),
                    ) {
                        Text(text = "Zurück", style = TextStyle(fontSize = 20.sp))
                    }

                }
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