package com.example.smartsave

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.IconListItem
import com.example.smartsave.helpers.ListItem
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.SparzielListItem
import com.example.smartsave.helpers.listItem
import kotlinx.coroutines.launch

class MainActivity : SmartSaveActivity(0.dp, 0.dp, 0.dp, 0.dp) {

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val bankkonto = getBankkonto()
        val kreditkontenListe = getKreditKontenListe()
        val sparzielListe = getSparzielListe()


        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            //TODO Bankkonto Darstellung

            if(bankkonto != null) {
                IconListItem(
                    text = bankkonto.kontostand.toString(),
                    modifier = Modifier.clickable {
                        val intent = Intent(this@MainActivity, KontoansichtActivity::class.java)
                        startActivity(intent)
                    },
                    iconId = R.drawable.arrow_swap
                )
            }

            for (kreditkonto in kreditkontenListe) {
                IconListItem(
                    text = kreditkonto.kontostand.toString(),
                    modifier = Modifier.clickable {
                        val intent = Intent(this@MainActivity, KontoansichtUmsaetzeActivity::class.java)
                        startActivity(intent)
                    },
                    iconId = R.drawable.arrow_forward
                )
            }

            for (sparziel in sparzielListe) {
                val progress = getSparzielprogress(sparziel)

                SparzielListItem(
                    text = sparziel.name,
                    modifier = Modifier.clickable {
                        //TODO Activity #11 Starten
                    },
                    iconId = R.drawable.piggy_bank,
                    progress = progress
                )
            }

        }


        //TODO fix drawer not showing during first open animation
        if (drawerState.isOpen || drawerState.isAnimationRunning) ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ListItem(
                            text = "Konto anlegen",
                            modifier = Modifier.clickable {
                                val intent = Intent(this@MainActivity, KontoAnlegenActivity::class.java)
                                startActivity(intent)
                            }
                        )
                        ListItem(text = "Umsätze verwalten")
                        ListItem(text = "Kategorien verwalten",
                            modifier = Modifier.clickable {
                                val intent = Intent(this@MainActivity, KategorienVerwaltenActivity::class.java)
                                startActivity(intent)
                            })
                    }
                }
            }
        ) { }

        if (drawerState.isClosed && !drawerState.isAnimationRunning) {
                        AlignedButton(
                            alignment = Alignment.BottomStart,
                            modifier = Modifier.padding(bottom = standardPadBottom, start = standardPadH),
                            iconId = R.drawable.piggy_bank
                        ) {
                            //TODO SparzielActivity aufrufen
                            val intent = Intent(this@MainActivity, SparzielActivity::class.java)
                            startActivity(intent)
                        }
                        AlignedButton(
                            alignment = Alignment.BottomEnd,
                            modifier = Modifier.padding(bottom = standardPadBottom, end = standardPadH),
                            iconId = R.drawable.plus
                        ) {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                    }
                }
            }
        }
    }
}

fun getBankkonto(): Konto? {
    //TODO Get Angelegtes Bankkonto
    return Konto("Hauptkonto", 500.0)
    //return null
}

fun getKreditKontenListe(): List<Konto> {
    //TODO Get alle angelegten KreditKartenKonten
    val konto1 = Konto("Kkonto", 500.0)
    val konto2 = Konto("Kkonto", 500.0)
    val konto3 = Konto("Kkonto", 500.0)
    val kontoliste = mutableListOf(konto1, konto2, konto3)
    //val kontoliste: MutableList<Konto> = mutableListOf()

    return kontoliste
}

fun getSparzielListe(): List<Sparziel> {
    //TODO Get alle angelegten KreditKartenKonten
    val sparziel1 = Sparziel("Koka")
    val sparziel2 = Sparziel("Csgo Messer")
    val sparziel3 = Sparziel("Cl500")
    val sparzielliste = mutableListOf(sparziel1, sparziel2, sparziel3)
    //val sparzielliste: MutableList<Konto> = mutableListOf()

    return sparzielliste
}

fun getSparzielprogress(sparziel: Sparziel): Int {
    //TODO Progress des Sparziels berchnen (Alle Umsätze des Bankkontos auf Sparkonto / SparzielGesamt)
    return 50
}