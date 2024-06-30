package com.example.smartsave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate
import java.util.Date
import java.util.Random

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
        Scaffold() { innerPadding ->
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
                val date = LocalDate.of(2024, 1, 1)
                // getBudget des monats
                val budget = getBudget(date)
                // getKategorien
                val kategorienliste = getKategorienliste()
                // gesamtausgaben
                var gesamtausgaben = 0.0

                var sliderPosition by remember { mutableFloatStateOf(0f) }


                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CenteredText(text = "Monat Jahr")


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
                                val umsatzKategorie = getUmsatzKategorie(kategorie, date)
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
                            val percentageSize = calcPercentage(budget - gesamtausgaben, budget)
                            drawRect(color = Color.White,
                                size = Size(size.width*percentageSize.toFloat(), size.height),
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
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it }
                    )
                    Text(text = sliderPosition.toString())




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

    private fun getUmsatzKategorie(kategorie: Kategorie, date: LocalDate?): Double {
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

    private fun getBudget(localDate: LocalDate): Double {
        //TODO Implement datenbank kram, hole kontostand an datum + alle seit dem postiiven umsätze
        return 3000.0
    }
}