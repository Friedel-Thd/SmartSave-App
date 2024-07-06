package com.example.smartsave

import DbHelper
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.dataClasses.Einzelumsatz
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.dataClasses.Umsatz
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.EinzelumsatzListItem
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.UmsatzDiffListItem

class EinzelumsatzVerwaltenActivity : SmartSaveActivity() {
    private val einzelUmsatzListeState = mutableStateOf<List<Einzelumsatz>>(emptyList())
    private val kontoListState = mutableStateOf<List<Konto>>(emptyList())
    var db = DbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO get alle konten (außer sparkonten evtl)
        einzelUmsatzListeState.value = db.getEinzelumsatzListe()
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        einzelUmsatzListeState.value = db.getEinzelumsatzListe()
        kontoListState.value = db.getAllKonten()
        super.onResume()
    }
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        //TODO get umsatz per mitegebener Id oder so maybe mäßisch oder direkt mitgeben
        //TODO je nach aufruf einzelumsätze eines umsatzes bzw. einzelumsätze ohne zugewiesenen umsatz glaub ich so mäßisch


        val einzelumsatzListe by remember { einzelUmsatzListeState }
        val kontoListe by remember { kontoListState }

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            for (einzelumsatz in einzelumsatzListe){
                EinzelumsatzListItem(einzelumsatz = einzelumsatz, context = LocalContext.current, kontenListe = kontoListe)
            }


        }

        AlignedButton(
            alignment = Alignment.BottomEnd,
            iconId = R.drawable.plus,
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
        ) {
            //TODO #9
            val intent = Intent(this@EinzelumsatzVerwaltenActivity, EinzelumsatzEditActivity::class.java)
            startActivity(intent)
        }

        AlignedButton(alignment = Alignment.BottomCenter, text = "Abbrechen", modifier = Modifier.height(70.dp)) {
            finish()
        }

    }

}