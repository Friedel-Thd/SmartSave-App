package com.example.smartsave

import DbHelper
import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.dataClasses.Einzelumsatz
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.dataClasses.Sparziel
import com.example.smartsave.dataClasses.Umsatz
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.UmsatzDiffDateListItem

class UmsatzAuswahlZuordnungActivity : SmartSaveActivity() {
    val db = DbHelper(this)
    private val umsatzListeState = mutableStateOf<List<Umsatz>>(emptyList())
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
    }

    @Composable
    override fun BoxScope.GenerateLayout(){
    // TODO Machene halt nech
        val bundle = intent.extras
        val konto = bundle!!.getSerializable("Konto") as Konto
        val einzelumsatz = bundle!!.getSerializable("Einzelumsatz") as Einzelumsatz
        val umsatzListe by remember { umsatzListeState }
        umsatzListeState.value = konto.umsatzList

        MainColumn (modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp))
        {
            //TODO Loop über alle Umsätze aus Datenbank so mesisch

            for (umsatz in umsatzListe) {
                if (umsatz.kategorie.name == "Nicht zugeordnet" && einzelumsatz.betrag >= umsatz.betrag){
                    UmsatzDiffDateListItem(umsatz, modifier = Modifier.clickable {
                        //TODO wenn man auf umsatz klickt wird der einzelumsatz da in die liste inserted
                        umsatz.einzelumsatzListe
                        db.addEinzelumsatzToUmsatz(umsatz.id,einzelumsatz)

                    })
                }

            }
        }
        AlignedButton(alignment = Alignment.BottomCenter, text = "Abbrechen", modifier = Modifier.height(70.dp)) {
            finish()
        }


    }
}