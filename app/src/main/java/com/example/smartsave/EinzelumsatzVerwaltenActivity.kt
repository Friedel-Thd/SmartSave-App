package com.example.smartsave

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.EinzelumsatzListItem
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.StandardText
import com.example.smartsave.helpers.UmsatzDiffDateListItem
import com.example.smartsave.helpers.UmsatzDiffListItem

class EinzelumsatzVerwaltenActivity : SmartSaveActivity() {
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        //TODO get umsatz per mitegebener Id oder so maybe mäßisch oder direkt mitgeben
        var umsatz = getUmsatz()
        //TODO je nach aufruf einzelumsätze eines umsatzes bzw. einzelumsätze ohne zugewiesenen umsatz glaub ich so mäßisch
        var einzelumsatzListe = umsatz.getEinzelumsatzListe()

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            for (einzelumsatz in einzelumsatzListe) {
                EinzelumsatzListItem(einzelumsatz)
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
            val intent = Intent(this@EinzelumsatzVerwaltenActivity, SparzielActivity::class.java)
            startActivity(intent)
        }

        AlignedButton(alignment = Alignment.BottomCenter, text = "Abbrechen", modifier = Modifier.height(70.dp)) {
            finish()
        }

    }
}