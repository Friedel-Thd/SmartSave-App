package com.example.smartsave

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.SparzielAnsichtListItem
import com.example.smartsave.helpers.SparzielEinzahlungListItem


class SparzielAnsichtActivity: SmartSaveActivity() {

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        var textName by remember { mutableStateOf("") }

        //TODO Vllt Sparziel als ID übergeben? und dann aus datenbank info holen
        val bundle = intent.extras
        val sparziel: Sparziel = bundle!!.getSerializable("Sparziel") as Sparziel
        var summe = 0.0

        val einzahlungsListe: List<Umsatz> = sparziel.getEinzahlungsliste()

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            SparzielAnsichtListItem(sparziel.name, "${sparziel.getProgress()}%", Modifier)

            for (einzahlung in einzahlungsListe) {
                SparzielEinzahlungListItem(einzahlung, Modifier)
                summe += einzahlung.value
            }

            SparzielAnsichtListItem("Summe", "$summe€", Modifier)
        }

        AlignedButton(alignment = Alignment.BottomStart, text = "Zurück") {finish()}
        AlignedButton(alignment = Alignment.BottomEnd, text = "Auszahlen") {
            //TODO Sparziel Anlegen/Auflösen (Layout #5)
            val intent = Intent(this@SparzielAnsichtActivity, SparzielAnAufActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}