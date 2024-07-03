package com.example.smartsave

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.ListItem
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.holding

class KategorienVerwaltenActivity : SmartSaveActivity() {
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        val kategorienListe = getKat().toMutableList()

        var textKategorie by remember { mutableStateOf("") }

        var showDialogAnlegen by remember { mutableStateOf(false) }
        var showDialogLoeschen by remember { mutableStateOf(false) }



        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            for (kategorie in kategorienListe) {
                ListItem(
                    text = kategorie.name,
                    modifier = Modifier.holding {
                        if (kategorie.isAssigned()) {
                            showDialogLoeschen = true
                        }
                        else {
                        //TODO Entferne Kategorie aus Kategorienliste in Datenbank, wenn Eintrag lange gehalten wird

                        }
                    }
                )
            }

        }

        AlignedButton(alignment = Alignment.BottomStart, text = "Zurück") {finish()}

        AlignedButton(alignment = Alignment.BottomEnd, iconId = R.drawable.plus) {
            //TODO #12a
            showDialogAnlegen = true
        }

        if (showDialogAnlegen) {
            AlertDialog(
                onDismissRequest = { showDialogAnlegen = false },
                confirmButton = {
                    TextButton(onClick = { showDialogAnlegen = false }) {
                        //TODO Neue Kategorie anlegen
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialogAnlegen = false }) {
                        Text("Abbrechen")
                    }
                },
                title = { Text("Neue Kategorie anlegen") },
                text = {
                    LabelledInputField(label = "Name", value = textKategorie) {textKategorie = it}
                }
            )
        }

        if (showDialogLoeschen) {
            AlertDialog(
                onDismissRequest = { showDialogLoeschen = false },
                confirmButton = {
                    TextButton(onClick = { showDialogLoeschen = false }) {
                        //TODO Kategorie inklusive allen zuweisungen löschen
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialogLoeschen = false }) {
                        Text("Abbrechen")
                    }
                },
                title = { Text("Kategorie ist bereits zugeordnet.") },
                text = { Text("Trotzdem löschen?") }
            )
        }

    }
}
