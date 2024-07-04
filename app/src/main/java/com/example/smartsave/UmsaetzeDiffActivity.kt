package com.example.smartsave

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import com.example.smartsave.helpers.UmsatzDiffDateListItem
import com.example.smartsave.helpers.UmsatzDiffListItem
import com.example.smartsave.helpers.holding

class UmsaetzeDiffActivity : SmartSaveActivity() {
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        //TODO get umsatz per mitegebener Id oder so maybe mäßisch oder direkt mitgeben
        var umsatz = getUmsatz()

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            UmsatzDiffDateListItem(umsatz)

            for (einzelumsatz in umsatz.getEinzelumsatzListe()) {
                UmsatzDiffListItem(einzelumsatz)
            }

        }

        AlignedButton(
            alignment = Alignment.BottomStart,
            iconId = R.drawable.plus,
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
        ) {
            //TODO #9
            val intent = Intent(this@UmsaetzeDiffActivity, SparzielActivity::class.java)
            startActivity(intent)
        }

        AlignedButton(
            alignment = Alignment.BottomEnd,
            iconId = R.drawable.arrow_swap,
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
        ) {
            //TODO #10
            val intent = Intent(this@UmsaetzeDiffActivity, EinzelumsatzVerwaltenActivity::class.java)
            startActivity(intent)
        }

        AlignedButton(alignment = Alignment.BottomCenter, text = "Zurück", modifier = Modifier.height(70.dp)) {
            finish()
        }

    }
}

fun getUmsatz(): Umsatz {
    val umsatz = Umsatz("umsatz", 34.55)

    val einzelumsatz1 = Einzelumsatz("Döner", 5.0)
    einzelumsatz1.setKategorie(Kategorie("Essen"))
    umsatz.setKategorie(Kategorie("Gym"))
    umsatz.addEinzelumsatz(einzelumsatz1)

    val einzelumsatz2 = Einzelumsatz("Döner", 5.0)
    einzelumsatz2.setKategorie(Kategorie("Essen"))
    umsatz.addEinzelumsatz(einzelumsatz2)

    return umsatz
}
