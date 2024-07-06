package com.example.smartsave

import DbHelper
import android.content.Intent
import android.os.Bundle
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
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.EinzelumsatzListItem
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity

class EinzelumsatzVerwaltenActivity : SmartSaveActivity() {
    private val kontenListeState = mutableStateOf<List<Konto>>(emptyList())
    var db = DbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO get alle konten (außer sparkonten evtl)
        kontenListeState.value = db.getKreditKontenListe()
        super.onCreate(savedInstanceState)
    }
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        //TODO get umsatz per mitegebener Id oder so maybe mäßisch oder direkt mitgeben
        var umsatz = getUmsatz()
        //TODO je nach aufruf einzelumsätze eines umsatzes bzw. einzelumsätze ohne zugewiesenen umsatz glaub ich so mäßisch
        var einzelumsatzListe = umsatz.einzelumsatzListe

        val kontenListe by remember { kontenListeState }


        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            for (einzelumsatz in einzelumsatzListe) {
                EinzelumsatzListItem(einzelumsatz, modifier = Modifier, LocalContext.current, kontenListe)
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