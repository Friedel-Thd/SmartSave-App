package com.example.smartsave

import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.smartsave.helpers.ErrorMsg
import com.example.smartsave.helpers.LabelledInputField
import com.example.smartsave.helpers.ListItem
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity

class KategorienVerwaltenActivity : SmartSaveActivity() {
    private val kategorienListeState = mutableStateOf<List<Kategorie>>(emptyList())
   private var db = DbHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        kategorienListeState.value = db.getKategorienListe()
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
        var nameExistsError  by remember { mutableStateOf(false) }
        var emptyError  by remember { mutableStateOf(false) }

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            for (kategorie in kategorienListe) {
                if (kategorie.name != "Nicht zugeordnet") {
                    ListItem(
                        text = kategorie.name,
                        modifier = Modifier.combinedClickable(
                            onClick = {

                            },
                            onLongClick = {
                                selectedKategorie = kategorie
                                if (db.kategorieisAssigned(kategorie)) {
                                    showDialogAssignedLoeschen = true
                                } else {
                                    showDialogLoeschen = true
                                }
                            }
                        )
                    )
                }
            }

        }

        AlignedButton(alignment = Alignment.BottomStart, text = "Zurück") {finish()}
        AlignedButton(alignment = Alignment.BottomEnd, iconId = R.drawable.plus) { showDialogAnlegen = true
        textKategorie = ""}

        if (showDialogAnlegen) {
            AlertDialog(
                onDismissRequest = {
                    emptyError = false
                    nameExistsError = false
                    showDialogAnlegen = false },
                confirmButton = {
                    TextButton(onClick = {
                        if(textKategorie.isEmpty()){
                            emptyError = true
                            nameExistsError = false
                        } else if (kategorienListe.any{it.name == textKategorie}){
                            nameExistsError = true
                            emptyError = false
                        }
                        else {
                            showDialogAnlegen = false
                            val newKategorie = Kategorie(textKategorie)
                            db.insertKategorie(newKategorie)
                            val newkategorienListe = kategorienListe.toMutableList()
                            newkategorienListe.add(newKategorie)
                            kategorienListeState.value = newkategorienListe
                            emptyError = false
                            nameExistsError = false
                        }
                    })
                    {
                        Text("OK")

                    }
                    if(nameExistsError) ErrorMsg(msg = "Kategorie mit diesem Namen existiert bereits!")
                    if(emptyError) ErrorMsg(msg = "Bitte Textfeld ausfüllen!")
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialogAnlegen = false
                        emptyError = false
                        nameExistsError = false}) {
                        Text("Abbrechen")
                    }
                },
                title = { Text("Neue Kategorie anlegen") },
                text = { LabelledInputField(label = "Name", value = textKategorie, KeyboardOptions()) {textKategorie = it} }
            )
        }

        if (showDialogAssignedLoeschen) {
            AlertDialog(
                onDismissRequest = { showDialogAssignedLoeschen = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialogAssignedLoeschen = false
                        if (selectedKategorie != null) {
                            db.removeKategorie(selectedKategorie!!)
                            db.removeKategorieAssigns(selectedKategorie!!)
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
                title = { Text("Kategorie ${selectedKategorie?.name} ist bereits zugeordnet.") },
                text = { Text("Trotzdem löschen?") }
            )
        }

        if (showDialogLoeschen) {
            AlertDialog(
                onDismissRequest = { showDialogLoeschen = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialogLoeschen = false
                        if (selectedKategorie != null) {
                            db.removeKategorie(selectedKategorie!!)
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
                title = { Text("Kategorie löschen?") }
            )
        }
    }
}




