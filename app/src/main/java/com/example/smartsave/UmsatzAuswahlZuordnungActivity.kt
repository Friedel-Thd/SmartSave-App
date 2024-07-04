package com.example.smartsave

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.EinzelumsatzListItem
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.UmsatzDiffDateListItem
import com.example.smartsave.helpers.UmsatzDiffListItem

class UmsatzAuswahlZuordnungActivity : SmartSaveActivity() {
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout(){

        var umsatzListe = getUmsaetze()
        MainColumn (modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp))
        {
            //TODO Loop über alle Umsätze aus Datenbank so mesisch

            for (umsatz in umsatzListe) {
                UmsatzDiffDateListItem(umsatz, modifier = Modifier.clickable {  })
            }
        }
        AlignedButton(alignment = Alignment.BottomCenter, text = "Abbrechen", modifier = Modifier.height(70.dp)) {
            finish()
        }
    }
}