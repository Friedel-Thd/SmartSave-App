package com.example.smartsave.helpers
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
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
import androidx.core.content.ContextCompat.startActivity
import com.example.smartsave.Einzelumsatz
import com.example.smartsave.Kategorie
import com.example.smartsave.Konto
import com.example.smartsave.R
import com.example.smartsave.SparzielActivity
import com.example.smartsave.UmsaetzeDiffActivity
import com.example.smartsave.Umsatz
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
    Column( modifier = modifier.fillMaxWidth() ) {
        Text(text = "Datum", modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = umsatz.name, modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
            Text(text = "${umsatz.value}€", modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
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
        Text(text = einzelumsatz.name, modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 24.sp))
        Text(text = "${einzelumsatz.value.toString()}€", modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 24.sp))
    }
    ListDivider()
}

@Composable
fun EinzelumsatzListItem(einzelumsatz: Einzelumsatz, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
            .holding {
                //TODO onHolding #9 (einzelumsatz editieren)

            }
            .clickable {
                //TODO onClick #8 -> zeige zugehörigen umsatz

            },
    ){
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.plus),
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp),
            contentDescription = "drawable icons",
            tint = Color.Unspecified
        )

        Text(text = einzelumsatz.name, modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 24.sp))
        Text(text = "${einzelumsatz.value.toString()}€", modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 24.sp))
    }
    ListDivider()
}

@Composable
fun SparzielEinzahlungListItem(einzahlung: Umsatz, modifier: Modifier = Modifier) {
    Column( modifier = modifier.fillMaxWidth() ) {
        Text(text = "Datum", modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(text = einzahlung.name, modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
            Text(text = "${einzahlung.value}€", modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 24.sp))
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



//TODO Layout Anpassen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelledDropdownMenu(label: String, options: List<Konto>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "$label: ", modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(fontSize = 20.sp))
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


//TODO Layout
@Composable
fun LabelledDatePickerButton(label: String, selectedDate: String, onDateSelected: (String) -> Unit) {
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

    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$label: ", modifier = Modifier.fillMaxWidth(.6f), style = TextStyle(fontSize = 20.sp))
        Button(onClick = { datePickerDialog.show() }) {
            Text(text = if (selectedDate.isEmpty()) "Datum auswählen" else selectedDate)
        }
    }
    
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelledDropdownMenuUmsatz(
    label: String,
    options: List<Kategorie>,
    umsatz: Umsatz,
    context: Context
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionKategorie by remember { mutableStateOf(options[0]) }
    var openAlertDialog  by remember { mutableStateOf(false) }
    Column{

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            //horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 20.dp, end = 50.dp)
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .padding(end = 50.dp),
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
            Text(text = "${umsatz.value}€")


        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = umsatz.name,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 20.dp)
            )


            ElevatedButton(
                enabled = if(!umsatz.isAssigned()) true else false,
                onClick = {
                //TODO Layout #8 NUR WENN UMSATZ KEINE KATEGORIE ZUGEWIESEN HAT bzw. "NICHT ZUGEWIESEN"
                // Übergabeparameter aktueller Umsatz mäßisch
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
                  Button(onClick = {openAlertDialog = false }) {
                      //TODO Wenn der Umsatz unterumsätze besitzt -> Alle zugewiesenen unterumsätze der ausgewählten Kategorie zuweisen
                      if(umsatz.hasAssignedEinzelumsatz()) {
                          umsatz.setKategorie(selectedOptionKategorie)
                          for (einzelumsatz in umsatz.getEinzelumsatzListe()) {
                              einzelumsatz.setKategorie(selectedOptionKategorie)
                          }
                      } else {
                          //TODO Kategorie für alle Umsätze mit dem gleichen Verwendungszweck übernehmen
                          // Konto -> Umsatzliste -> Alle mit gleichem Verwendungszweck wie ausgewählte überweisung -> setKategorie
                          umsatz.setKategorie(selectedOptionKategorie)
                      }

                    Text("Ja")
                } },
                dismissButton = {
                    TextButton(onClick = { openAlertDialog = false }) {
                        Text("Abbrechen")
                    }
                },
                text = {
                    if(umsatz.hasAssignedEinzelumsatz()) {
                        Text("Die Kategorie aller zugewiesenen Einzelumsätze zu ${selectedOptionKategorie.name} ändern?")
                    } else {
                        Text("Kategorie für alle Umsätze mit diesem Verwendungszweck übernehmen?")
                    }
                }
            )
        }
    }
}
