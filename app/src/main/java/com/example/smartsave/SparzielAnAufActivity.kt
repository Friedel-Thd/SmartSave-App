package com.example.smartsave

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.dataClasses.Sparziel
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.StandardText
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.asImageBitmap


//TODO QRCODE/ÜBERWEISUNGSDATEN DRUCKEN

class SparzielAnAufActivity: SmartSaveActivity() {
    var db = DbHelper(this)

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        val bundle = intent.extras
        val tempSparziel = bundle!!.getSerializable("Sparziel") as Sparziel
        val mode = bundle.getString("mode")
        val betrag = if(mode == "auflösen") { bundle.getDouble("Summe") } else { tempSparziel.monatsrate }


        MainColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            StandardText("Bitte richten Sie folgenden Dauerauftrag/Terminüberweisung ein:")
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Auszahlkonto")
                StandardText(tempSparziel.auszahlungsKonto.kontonr.toString())
            }
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Zielkonto")
                StandardText(tempSparziel.zielKonto.kontonr.toString())
            }
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Betrag")
                StandardText("${betrag}€")
            }
            Row (modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween){
                StandardText("Verwendungszweck")
                StandardText(tempSparziel.name)
            }
            // QR Code anzeigen
            val qrData = """
            Auszahlkonto: ${tempSparziel.auszahlungsKonto.kontonr}
            Zielkonto: ${tempSparziel.zielKonto.kontonr}
            Betrag: ${tempSparziel.monatsrate}
            Verwendungszweck: ${tempSparziel.name}
        """.trimIndent()

            QRCodeComposable(text = qrData, size = 200)
        }

        AlignedButton(alignment = Alignment.BottomStart, text = "Abbrechen") {finish()}
        AlignedButton(
            alignment = Alignment.BottomEnd,
            text = when (mode) {
                "anlegen" -> "Anlegen"
                "auflösen" -> "Auflösen"
                else -> "Error"
            }
        ) {
            when (mode) {
                "anlegen" -> {
                    db.insertSparziel(tempSparziel)
                    finish()
                }
                "auflösen" -> {
                    db.deleteSparziel(tempSparziel.id)
                    finish()
                }
                else -> throw IllegalStateException("Kein gültiger mode!")
            }
        }
    }


    fun generateQRCode(text: String, size: Int): Bitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bmp
    }


    @Composable
    fun QRCodeComposable(text: String, size: Int) {
        val bitmap = generateQRCode(text, size)
        Box(
            modifier = Modifier.fillMaxSize().padding(top= 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier.size(size.dp)
            )
        }
    }


}