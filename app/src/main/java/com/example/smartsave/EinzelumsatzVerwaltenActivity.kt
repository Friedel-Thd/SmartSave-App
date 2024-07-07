package com.example.smartsave

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.dataClasses.Einzelumsatz
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.EinzelumsatzListItem
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity

class EinzelumsatzVerwaltenActivity : SmartSaveActivity() {
    private lateinit var einzelUmsatzListe : List<Einzelumsatz>
    private lateinit var  kontoList : List<Konto>

    var db = DbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kontoList = db.getAllKonten()
        einzelUmsatzListe = db.getEinzelumsatzListe()

    }

    override fun onResume() {
        super.onResume()
        kontoList = db.getAllKonten()
        einzelUmsatzListe = db.getEinzelumsatzListe()
       // Log.d("Resume verwaltung","ON RESUME CALLED")
        setContent{GenerateContent()}

    }
    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        var noKontoError by remember { mutableStateOf(false) }
        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            for (einzelumsatz in einzelUmsatzListe){
                EinzelumsatzListItem(einzelumsatz = einzelumsatz, context = LocalContext.current, kontenListe = kontoList, noKontoError = noKontoError,
                    onUpdate = {
                        einzelUmsatzListe = db.getEinzelumsatzListe()
                    })
            }
        }
        AlignedButton(
            alignment = Alignment.BottomEnd,
            iconId = R.drawable.plus,
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
        ) {
            noKontoError = false
            val intent = Intent(this@EinzelumsatzVerwaltenActivity, EinzelumsatzEditActivity::class.java)
            intent.putExtra("mode", "anlegen")
            startActivity(intent)
        }

        AlignedButton(alignment = Alignment.BottomCenter, text = "Abbrechen", modifier = Modifier.height(70.dp)) {
            noKontoError = false
            finish()
        }

    }


}