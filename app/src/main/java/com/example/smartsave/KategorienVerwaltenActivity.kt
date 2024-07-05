package com.example.smartsave

import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import com.example.smartsave.dataClasses.Kategorie
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.ListItem
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity

class KategorienVerwaltenActivity : SmartSaveActivity() {
    private val kategorienListeState = mutableStateOf<List<Kategorie>>(emptyList())
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO getKat (DB)
        kategorienListeState.value = getKat()
        super.onCreate(savedInstanceState)
    }

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun BoxScope.GenerateLayout() {
        val kategorienListe by remember { kategorienListeState }
        var selectedKategorie by remember { mutableStateOf<Kategorie?>(null) }

        var textKategorie by remember { mutableStateOf("") }

        var showDialogAnlegen by remember { mutableStateOf(false) }
        var showDialogLoeschen by remember { mutableStateOf(false) }
        var showDialogAssignedLoeschen by remember { mutableStateOf(false) }

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            for (kategorie in kategorienListe) {
                ListItem(
                    text = kategorie.name,
                    modifier = Modifier.combinedClickable(
                        onClick = {

                        },
                        onLongClick = {
                            selectedKategorie = kategorie
                            if (kategorie.isAssigned()) {
                                showDialogAssignedLoeschen = true
                            }
                            else {
                                showDialogLoeschen = true
                            }
                        }
                    )
                )
            }
        }

        AlignedButton(alignment = Alignment.BottomStart, text = "Zurück") {finish()}
        AlignedButton(alignment = Alignment.BottomEnd, iconId = R.drawable.plus) { showDialogAnlegen = true }

        if (showDialogAnlegen) {
            AlertDialog(
                onDismissRequest = { showDialogAnlegen = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialogAnlegen = false
                        val newKategorie = Kategorie(textKategorie)
                        addKategorieToDatabase(newKategorie)
                        val newkategorienListe = kategorienListe.toMutableList()
                        newkategorienListe.add(newKategorie)
                        kategorienListeState.value = newkategorienListe
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialogAnlegen = false }) {
                        Text("Abbrechen")
                    }
                },
                title = { Text("Neue Kategorie anlegen") },
                text = { LabelledInputField(label = "Name", value = textKategorie) {textKategorie = it} }
            )
        }

        if (showDialogAssignedLoeschen) {
            AlertDialog(
                onDismissRequest = { showDialogAssignedLoeschen = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialogAssignedLoeschen = false
                        //TODO Kategorie inklusive allen zuweisungen löschen
                        if (selectedKategorie != null) {
                            removeKategorieFromDatabase(selectedKategorie!!)
                            val newkategorienListe = kategorienListe.toMutableList()
                            newkategorienListe.remove(selectedKategorie)
                            kategorienListeState.value = newkategorienListe
                            selectedKategorie = null
                        }
                    }) {
                        Text("Bestätigen")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialogAssignedLoeschen = false
                        selectedKategorie = null
                    }) {
                        Text("Abbrechen")
                    }
                },
                title = { Text("Kategorie ist bereits zugeordnet.") },
                text = { Text("Trotzdem löschen?") }
            )
        }

        if (showDialogLoeschen) {
            AlertDialog(
                onDismissRequest = { showDialogLoeschen = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialogLoeschen = false
                        //TODO Kategorie inklusive allen zuweisungen löschen (DB)
                        if (selectedKategorie != null) {
                            removeKategorieFromDatabase(selectedKategorie!!)
                            val newkategorienListe = kategorienListe.toMutableList()
                            newkategorienListe.remove(selectedKategorie)
                            kategorienListeState.value = newkategorienListe
                            selectedKategorie = null
                        }
                    }) {
                        Text("Bestätigen")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialogLoeschen = false
                        selectedKategorie = null
                    }) {
                        Text("Abbrechen")
                    }
                },
                title = { Text("Kategorie Löschen?") }
            )
        }
    }
}

fun addKategorieToDatabase(kategorie: Kategorie) {
    //TODO Add Logic
}

fun removeKategorieFromDatabase(kategorie: Kategorie) {
    //TODO Add Logic
}
