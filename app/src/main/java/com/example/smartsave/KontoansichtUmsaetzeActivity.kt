package com.example.smartsave

import DbHelper
import android.content.Intent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartsave.dataClasses.Kategorie
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.dataClasses.Month
import com.example.smartsave.dataClasses.Umsatz
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.ListDivider
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

private const val MAX_MONTHS = 12


class KontoansichtUmsaetzeActivity : SmartSaveActivity() {
    var db = DbHelper(this)
    private val kategorienListeState = mutableStateOf<List<Kategorie>>(emptyList())
    private lateinit var konto: Konto

    override fun onCreate(savedInstanceState: Bundle?) {
        kategorienListeState.value = db.getKategorienListe()

        val bundle = intent.extras
        konto = bundle!!.getSerializable("Konto") as Konto

        super.onCreate(savedInstanceState)
    }

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun BoxScope.GenerateLayout() {
        val kategorienListe by remember { kategorienListeState }
        var months by remember { mutableIntStateOf(12) }
        val currentMonth = LocalDate.now().let { Month(it.year, it.monthValue) }
        val scrollstate = rememberScrollState()

        var openAlertDialog  by remember { mutableStateOf(false) }
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        var selectedKategorie by remember { mutableStateOf<Kategorie?>(null) }
        var selectedUmsatz by remember { mutableStateOf<Umsatz?>(null) }

        var kategorieFilter by remember { mutableStateOf<Kategorie?>(null) }
        var nameFilter by remember { mutableStateOf<String>("") }
        var dateFilter by remember { mutableStateOf<LocalDate?>(null) }

        MainColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            LabelledInputField(label = "Filter", value = nameFilter, KeyboardOptions()) { nameFilter = it }

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
                    onValueChange = {
                        months = MAX_MONTHS - it.roundToInt()
                        if (months == 12) {
                            dateFilter = null
                        } else {
                            dateFilter = LocalDate.of((currentMonth - months).year, (currentMonth - months).month, 1)
                        }
                        },
                    valueRange = 0f..(MAX_MONTHS - 1f),
                    steps = MAX_MONTHS - 2
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Alle Umsätze",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth(1f / 3)
                    )
                    Text(
                        text = if(months == 12) { "Alle Umsätze" } else {"ab ${currentMonth - months}"},
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .horizontalScroll(scrollstate),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Button(
                        onClick = {
                            kategorieFilter = null
                        }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.x_taste),
                            modifier = Modifier.size(18.dp),
                            contentDescription = "drawable icons",
                            tint = Color.Unspecified
                        )
                    }

                    for ( kategorie in kategorienListe) {
                        Button(modifier = Modifier.padding(horizontal = 5.dp),
                            onClick = {
                                kategorieFilter = kategorie
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
                    for (umsatz in konto.umsatzList)
                        if ((kategorieFilter == null || umsatz.kategorie == kategorieFilter) &&
                            (nameFilter.isEmpty() || umsatz.verwendungsZweck.contains(nameFilter, ignoreCase = true)) &&
                            (dateFilter == null || umsatz.datum.isAfter(dateFilter))) {

                            Row() {
                                var expanded by remember { mutableStateOf(false) }
                                var selectedOptionKategorie = umsatz.kategorie

                                Column {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = umsatz.datum.format(formatter),
                                            textAlign = TextAlign.Center
                                        )
                                        ExposedDropdownMenuBox(
                                            expanded = expanded,
                                            modifier = Modifier
                                                .width(175.dp)
                                                .height(50.dp),
                                            onExpandedChange = { expanded = !expanded }
                                        ) {
                                            TextField(
                                                readOnly = true,
                                                value = selectedOptionKategorie.name,
                                                onValueChange = { },
                                                trailingIcon = {
                                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                                        expanded = expanded
                                                    )
                                                },
                                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                                modifier = Modifier.menuAnchor()
                                            )
                                            ExposedDropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = {
                                                    expanded = false
                                                }

                                            ) {
                                                kategorienListe.forEach { selectionOption ->
                                                    DropdownMenuItem(
                                                        text = { Text(text = selectionOption.name) },
                                                        onClick = {
                                                            selectedUmsatz = umsatz
                                                            selectedKategorie = selectionOption
                                                            selectedOptionKategorie =
                                                                selectionOption
                                                            expanded = false
                                                            openAlertDialog = true
                                                        }
                                                    )

                                                }
                                            }

                                        }
                                        Text(text = "${umsatz.betrag}€")


                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = umsatz.verwendungsZweck,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(start = 20.dp)
                                        )


                                        ElevatedButton(
                                            enabled = !umsatz.isAssigned(),
                                            onClick = {
                                                val intent = Intent(this@KontoansichtUmsaetzeActivity, UmsaetzeDiffActivity::class.java)
                                                intent.putExtra("Umsatz", umsatz)
                                                startActivity(intent)
                                            }) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(id = R.drawable.plus),
                                                modifier = Modifier.size(18.dp),
                                                contentDescription = "drawable icons",
                                                tint = Color.Unspecified
                                            )
                                        }
                                    }

                                    ListDivider()

                            }
                        }
                    }
                }
            }
        }

        if (openAlertDialog){
            AlertDialog(
                onDismissRequest = { openAlertDialog = false },
                confirmButton = {
                    Button(onClick = {
                        openAlertDialog = false
                        val umsatz = selectedUmsatz!!
                        val selectedOptionKategorie = selectedKategorie!!

                        //Kategorie für alle Umsätze mit gleichem Verwendungszweck inkl möglicher einzzelumsätze setzen
                        for (um in konto.umsatzList) {
                            if (um.verwendungsZweck == umsatz.verwendungsZweck) {
                                um.kategorie = selectedOptionKategorie
                                db.updateKategorieZuweisung(selectedOptionKategorie.id, um.id, false)
                                if (um.hasEinzelumsatz()) {
                                    for (eum in um.einzelumsatzListe) {
                                        eum.kategorie = selectedOptionKategorie
                                        db.updateKategorieZuweisung(selectedOptionKategorie.id, eum.id, true)
                                    }
                                }
                            }
                        }

                        reloadKonto()

                    }) {
                        Text("Ja")
                    } },
                dismissButton = {
                    TextButton(onClick = {
                        openAlertDialog = false

                        var umsatz = selectedUmsatz!!
                        var selectedOptionKategorie = selectedKategorie!!

                        // Kategorie für ausgewählten Umsatz inkl möglicher einzelumsätze setzen
                        umsatz.kategorie = selectedOptionKategorie
                        db.updateKategorieZuweisung(selectedOptionKategorie.id, umsatz.id, false)
                        if(umsatz.hasEinzelumsatz()) {
                            for (einzelumsatz in umsatz.einzelumsatzListe) {
                                einzelumsatz.kategorie = selectedOptionKategorie
                                db.updateKategorieZuweisung(selectedOptionKategorie.id, einzelumsatz.id, true)
                            }
                        }

                        reloadKonto()

                    }) {
                        Text("Nein")
                    }
                },
                text = { Text("Kategorie für alle Umsätze mit diesem Verwendungszweck übernehmen?") }
            )
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
        AlignedButton(alignment = Alignment.BottomCenter, text = "Zurück", modifier = Modifier.height(70.dp)) {
            finish()
        }
    }

    private fun reloadKonto() {
        konto = db.getKontoByKontonummer(konto.kontonr)!!
    }

}

