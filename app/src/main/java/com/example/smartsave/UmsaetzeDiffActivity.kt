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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.dataClasses.Umsatz
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.UmsatzDiffDateListItem
import com.example.smartsave.helpers.UmsatzDiffListItem

class UmsaetzeDiffActivity : SmartSaveActivity() {
    var db = DbHelper(this)
    private lateinit var umsatz: Umsatz

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        umsatz = bundle!!.getSerializable("Umsatz") as Umsatz

        super.onCreate(savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
        umsatz = db.getUmsatzByID(umsatz.id)!!


        setContent { GenerateContent() }

    }

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {

        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            UmsatzDiffDateListItem(umsatz)

            for (einzelumsatz in umsatz.einzelumsatzListe) {
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
            val intent = Intent(this@UmsaetzeDiffActivity, EinzelumsatzEditActivity::class.java)
            intent.putExtra("mode", "adden")
            intent.putExtra("umsatz", umsatz)
            startActivity(intent)
        }

        AlignedButton(
            alignment = Alignment.BottomEnd,
            iconId = R.drawable.arrow_swap,
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
        ) {
            val intent = Intent(this@UmsaetzeDiffActivity, EinzelumsatzVerwaltenActivity::class.java)
            startActivity(intent)
        }

        AlignedButton(alignment = Alignment.BottomCenter, text = "Zurück", modifier = Modifier.height(70.dp)) {
            finish()
        }

    }
}