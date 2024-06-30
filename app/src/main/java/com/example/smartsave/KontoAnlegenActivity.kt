package com.example.smartsave

import android.content.Intent
import android.graphics.fonts.FontStyle
import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch

class KontoAnlegenActivity : ComponentActivity() {
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
        var textKontoNr by remember { mutableStateOf("") }
        var textBLZ by remember { mutableStateOf("") }
        var textBIC by remember { mutableStateOf("") }
        var textIBAN by remember { mutableStateOf("") }
        var textBemerkung by remember { mutableStateOf("") }

        val radioOptions = listOf("Bankkonto", "Sparkonto", "Kreditkartenkonto")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1] ) }

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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BankInputField(value = textKontoNr,"Konto Nr.") {textKontoNr = it}
                    BankInputField(value = textBLZ, "BLZ") { textBLZ = it }
                    BankInputField(value = textBIC, "BIC") { textBIC = it }
                    BankInputField(value = textIBAN, "IBAN") { textIBAN = it }
                    BankInputField(value = textBemerkung, "Bemerkung") { textBemerkung = it }

                    Spacer(modifier = Modifier.height(30.dp))

                    Column {
                        radioOptions.forEach { text ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (text == selectedOption),
                                        onClick = {
                                            onOptionSelected(text)
                                        }
                                    )
                                    .padding(horizontal = 30.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (text == selectedOption),
                                    onClick = { onOptionSelected(text) }
                                )
                                Text(
                                    text = text,
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = TextStyle(fontSize = 20.sp)
                                )
                            }
                        }
                    }
                }

                ElevatedButton(
                    onClick = {
                        finish()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 40.dp, start = 25.dp)
                        .size(width = 150.dp, height = 80.dp),
                ) {
                    Text(text = "Abbrechen", style = TextStyle(fontSize = 20.sp))
                }

                ElevatedButton(
                    onClick = {
                        //TODO Kontodaten speichern

                        finish()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 40.dp, end = 25.dp)
                        .size(width = 150.dp, height = 80.dp),
                ) {
                    Text(text = "Speichern", style = TextStyle(fontSize = 20.sp))
                }

            }
        }
    }
}

@Composable
fun BankInputField(value: String, label: String, onValueChange: (String) -> Unit) = Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    Text("$label: ", modifier = Modifier
        .fillMaxWidth(.3f),
        style = TextStyle(fontSize = 20.sp))
    TextField(value = value, singleLine = true, modifier = Modifier.fillMaxWidth(), onValueChange = onValueChange, textStyle = TextStyle(fontSize = 20.sp))
}

