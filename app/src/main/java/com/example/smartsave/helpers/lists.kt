package com.example.smartsave.helpers
import DbHelper
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartsave.dataClasses.Einzelumsatz
import com.example.smartsave.EinzelumsatzEditActivity
import com.example.smartsave.dataClasses.Kategorie
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.R
import com.example.smartsave.UmsaetzeDiffActivity
import com.example.smartsave.dataClasses.Umsatz
import com.example.smartsave.UmsatzAuswahlZuordnungActivity
import java.time.format.DateTimeFormatter
import java.util.Calendar

fun LazyListScope.listItem(text: String, modifier: Modifier = Modifier) = item { ListItem(text, modifier) }

@Composable
fun IconListItem(text: String, modifier: Modifier = Modifier, iconId: Int) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Icon(
            imageVector = ImageVector.vectorResource(id = iconId),
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp),
            contentDescription = "drawable icons",
            tint = Color.Unspecified
        )
        CenteredText(text = "$text€")
    }
    ListDivider()
}

@Composable
fun SparzielListItem(text: String, modifier: Modifier = Modifier, iconId: Int, progress: Int) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Icon(
            imageVector = ImageVector.vectorResource(id = iconId),
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp),
            contentDescription = "drawable icons",
            tint = Color.Unspecified
        )
        Text(text = text, modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 34.sp))
        Text(text = "$progress%", modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 34.sp))
    }
    ListDivider()
}

@Composable
fun SparzielAnsichtListItem(text1: String, text2: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = text1, modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 28.sp))
        Text(text = text2, modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 28.sp))
    }
    ListDivider()
}

@Composable
fun UmsatzDiffDateListItem(umsatz: Umsatz, modifier: Modifier = Modifier) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    Column( modifier = modifier.fillMaxWidth() ) {
        Text(text = umsatz.datum.format(formatter), modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = umsatz.verwendungsZweck, modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
            Text(text = "${umsatz.betrag}€", modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
        }
    }
    ListDivider()
}

@Composable
fun UmsatzDiffListItem(einzelumsatz: Einzelumsatz, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = einzelumsatz.verwendungsZweck, modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 24.sp))
        Text(text = "${einzelumsatz.betrag.toString()}€", modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 24.sp))
    }
    ListDivider()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EinzelumsatzListItem(einzelumsatz: Einzelumsatz, modifier: Modifier = Modifier, context:Context, kontenListe: List<Konto>, onUpdate: () -> Unit) {
    var showDialogAnlegen by remember { mutableStateOf(false) }
    var selectedKonto by remember { mutableStateOf<Konto?>(null) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
            .combinedClickable(
                onClick = {
                    //TODO onHolding #9 (einzelumsatz editieren)
                    showDialogAnlegen = true
                          },
                onLongClick = {
                    //TODO onClick #8 -> zeige zugehörigen umsatz
                    val intent = Intent(context, EinzelumsatzEditActivity::class.java)
                    context.startActivity(intent)
                              },
            )

    ){
        if(einzelumsatz.hasParentUmsatz){
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.tick),
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterVertically)
                    .padding(start = 20.dp),
                contentDescription = "drawable icons",
                tint = Color.Unspecified
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.x_taste),
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterVertically)
                    .padding(start = 20.dp),
                contentDescription = "drawable icons",
                tint = Color.Unspecified
            )
        }


        Text(text = einzelumsatz.verwendungsZweck, modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 24.sp))
        Text(text = "${einzelumsatz.betrag.toString()}€", modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 24.sp))
    }
    ListDivider()
    if (showDialogAnlegen) {
        AlertDialog(
            onDismissRequest = { showDialogAnlegen = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialogAnlegen = false
                    val intent = Intent(context, UmsatzAuswahlZuordnungActivity::class.java)
                    intent.putExtra("Konto", selectedKonto)
                    intent.putExtra("Einzelumsatz", einzelumsatz)
                    context.startActivity(intent)
                    onUpdate()

                }) {
                    //TODO Neue Kategorie anlegen
                    Text("OK")

                }

            },
            dismissButton = {
                TextButton(onClick = { showDialogAnlegen = false }) {
                    Text("Abbrechen")
                }
            },
            title = { Text("Welchem Konto wollen sie diesen Umsatz zuweisen?") },
            text = {
               selectedKonto = labelledDropdownMenu(label = "Konto", options = kontenListe )
            }
        )
    }
}

@Composable
fun SparzielEinzahlungListItem(einzahlung: Umsatz, modifier: Modifier = Modifier) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Column( modifier = modifier.fillMaxWidth() ) {
        Text(text = einzahlung.datum.format(formatter), modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = einzahlung.verwendungsZweck, modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
            Text(text = "${-einzahlung.betrag}€", modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
        }
    }
    ListDivider()
}

@Composable
fun ListItem(text: String, modifier: Modifier = Modifier) {
    Row (modifier = modifier.fillMaxWidth()){
        CenteredText(text = text)
    }
    ListDivider()
}

@Composable
fun ListDivider() = HorizontalDivider(thickness = 2.dp, color = Color.Black)

@Composable
fun CategoryDisplay(color: Color, text: String, betrag: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .padding(end = 30.dp)
                .size(16.dp)
        ) {
            drawRect(
                color = color,
                size = size
            )
        }
        StandardText(text = "$text ($betrag€)")


    }
}

//TODO Layout Anpassen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun labelledDropdownMenu(label: String, options: List<Konto>): Konto {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label: ", modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 20.sp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = selectedOptionText.kontonr.toString(),
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
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption.kontonr.toString()) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    return selectedOptionText
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelledDropdownMenuKategory(label: String, options: List<Kategorie>): Kategorie {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$label: ",
                style = TextStyle(fontSize = 20.sp),

            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedOptionText.name,
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
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOption.name) },
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
    return selectedOptionText
}



//TODO Layout
@Composable
fun LabelledDatePickerButton(label: String, selectedDate: String, onDateSelected: (String) -> Unit, mindate: Boolean, maxdate: Boolean) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, monthOfYear, dayOfMonth ->
            onDateSelected("$dayOfMonth/${monthOfYear + 1}/$year")
        }, year, month, day
    )
    if(mindate) {
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
    }
    if(maxdate) {
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$label: ", modifier = Modifier.fillMaxWidth(.6f), style = TextStyle(fontSize = 20.sp))
        Button(onClick = { datePickerDialog.show() }) {
            Text(text = if (selectedDate.isEmpty()) "Datum auswählen" else selectedDate)
        }
    }
    
}



//TODO Für KontoansichtUmsaetzeActivity aber hab es mit extra Composable nicht hinbekommen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelledDropdownMenuUmsatz(
    konto: Konto,
    options: List<Kategorie>,
    umsatz: Umsatz,
    context: Context
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionKategorie by remember { mutableStateOf(umsatz.kategorie) }
    var openAlertDialog  by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val db = DbHelper(context)

    Column{

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
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOption.name) },
                            onClick = {
                                selectedOptionKategorie = selectionOption
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
                // TODO Übergabeparameter aktueller Umsatz mäßisch
                val intent = Intent(context, UmsaetzeDiffActivity::class.java)
                context.startActivity(intent)
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

        if (openAlertDialog){
            AlertDialog(
                onDismissRequest = { openAlertDialog = false },
                confirmButton = {
                  Button(onClick = {
                      openAlertDialog = false

                      //TODO Testen kategorie zuweisung mit verwendungszweck, einzelumsätzen

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

                      //TODO Hier aktualisieren

                  }) {
                    Text("Ja")
                } },
                dismissButton = {
                    TextButton(onClick = {
                        openAlertDialog = false

                        // Kategorie für ausgewählten Umsatz inkl möglicher einzelumsätze setzen
                        umsatz.kategorie = selectedOptionKategorie
                        db.updateKategorieZuweisung(selectedOptionKategorie.id, umsatz.id, false)
                        if(umsatz.hasEinzelumsatz()) {
                            for (einzelumsatz in umsatz.einzelumsatzListe) {
                                einzelumsatz.kategorie = selectedOptionKategorie
                                db.updateKategorieZuweisung(selectedOptionKategorie.id, einzelumsatz.id, true)
                            }
                        }

                        //TODO Hier aktualisieren

                    }) {
                        Text("Nein")
                    }
                },
                text = { Text("Kategorie für alle Umsätze mit diesem Verwendungszweck übernehmen?") }
            )
        }
    }
}
