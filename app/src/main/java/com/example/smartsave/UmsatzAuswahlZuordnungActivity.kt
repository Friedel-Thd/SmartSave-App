package com.example.smartsave

import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.dataClasses.Einzelumsatz
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.dataClasses.Umsatz
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.UmsatzDiffDateListItem

class UmsatzAuswahlZuordnungActivity : SmartSaveActivity() {
    val db = DbHelper(this)
    private lateinit var umsatzListe : List<Umsatz>
    private  lateinit var  konto :Konto
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        konto = bundle!!.getSerializable("Konto") as Konto
        konto = db.getKontoByKontonummer(konto.kontonr)!!
        umsatzListe= konto.umsatzList

    }


    @Composable
    override fun BoxScope.GenerateLayout(){
        val bundle = intent.extras
        val einzelumsatz = bundle!!.getSerializable("Einzelumsatz") as Einzelumsatz
        var restBetrag = 0.0

        MainColumn (modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp))
        {

            for (umsatz in umsatzListe) {
                restBetrag = umsatz.betrag
                for(einzelUmsatz in umsatz.einzelumsatzListe){
                    restBetrag -= einzelUmsatz.betrag
                }
                if(umsatz.betrag > 0 && einzelumsatz.betrag >0){
                    if (umsatz.kategorie.name == "Nicht zugeordnet" && einzelumsatz.betrag <= restBetrag){
                        UmsatzDiffDateListItem(umsatz,modifier = Modifier.clickable {

                            if( einzelumsatz.hasParentUmsatz ) {
                                db.updateEinzelumsatzZuweisung(einzelumsatz, umsatz.id)
                            } else {
                                einzelumsatz.hasParentUmsatz = true
                                db.addEinzelumsatzToUmsatz(umsatz.id,einzelumsatz)
                            }
                            finish()

                        })
                    }

                } else if(umsatz.betrag < 0 && einzelumsatz.betrag <0) {
                    if (umsatz.kategorie.name == "Nicht zugeordnet" && einzelumsatz.betrag >= restBetrag){
                        UmsatzDiffDateListItem(umsatz,modifier = Modifier.clickable {

                            if( einzelumsatz.hasParentUmsatz ) {
                                db.updateEinzelumsatzZuweisung(einzelumsatz, umsatz.id)
                            } else {
                                einzelumsatz.hasParentUmsatz = true
                                db.addEinzelumsatzToUmsatz(umsatz.id,einzelumsatz)
                            }
                            finish()

                        })
                    }
                }

            }
        }
        AlignedButton(alignment = Alignment.BottomCenter, text = "Abbrechen", modifier = Modifier.height(70.dp)) {
            finish()
        }


    }
}