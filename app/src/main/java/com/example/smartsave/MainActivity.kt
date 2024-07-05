package com.example.smartsave

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.IconListItem
import com.example.smartsave.helpers.ListItem
import com.example.smartsave.helpers.MainColumn
import com.example.smartsave.helpers.SmartSaveActivity
import com.example.smartsave.helpers.SparzielListItem
import kotlinx.coroutines.launch

class MainActivity : SmartSaveActivity(0.dp, 0.dp, 0.dp, 0.dp) {
    private val bankkontoState = mutableStateOf<Konto?>(null)
    private val kreditkontenListeState = mutableStateOf<List<Konto>>(emptyList())
    private val sparzielListeState = mutableStateOf<List<Sparziel>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        bankkontoState.value = getBankkonto()
        kreditkontenListeState.value = getKreditKontenListe()
        sparzielListeState.value = getSparzielListe()

        super.onCreate(savedInstanceState)
    }

    //TODO Wahrscheinlich aktualisieren der States in onResume()

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    @Composable
    override fun BoxScope.GenerateLayout() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        val bankkonto by remember { bankkontoState }
        val kreditkontenListe by remember { kreditkontenListeState }
        val sparzielListe by remember { sparzielListeState }

        LaunchedEffect(Unit) {
            drawerState.close()
        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ListItem(
                            text = "Konto anlegen",
                            modifier = Modifier.clickable {
                                scope.launch {
                                    //TODO (Bankkonto != null) mitgeben -> Wenn bankkonto existiert darf kein neues erstellt werden
                                    val intent = Intent(this@MainActivity, KontoAnlegenActivity::class.java)
                                    drawerState.close()
                                    startActivity(intent)
                                }
                            }
                        )
                        ListItem(
                            text = "Umsätze verwalten",
                            modifier = Modifier.clickable {
                                scope.launch {
                                    val intent = Intent(
                                        this@MainActivity,
                                        EinzelumsatzVerwaltenActivity::class.java
                                    )
                                    drawerState.close()
                                    startActivity(intent)
                                }
                            }
                        )
                        ListItem(text = "Kategorien verwalten",
                            modifier = Modifier.clickable {
                                scope.launch {
                                    val intent = Intent(
                                        this@MainActivity,
                                        KategorienVerwaltenActivity::class.java
                                    )
                                    drawerState.close()
                                    startActivity(intent)
                                }
                            })
                    }
                }
            }
        ) {
            MainColumn(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                if (bankkonto != null) {
                    IconListItem(
                        text = bankkonto!!.kontostand.toString(),
                        modifier = Modifier.clickable {
                            //TODO KontoID mitgeben
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
                            //TODO KontoID mitgeben
                            val intent = Intent(this@MainActivity, KontoansichtUmsaetzeActivity::class.java)
                            startActivity(intent)
                        },
                        iconId = R.drawable.arrow_forward
                    )
                }

                for (sparziel in sparzielListe) {
                    val progress = sparziel.getProgress()
                    //TODO SparzielListItem darstellung (Sparziel oder Sparziel mitgeben und % ausrechnen)
                    SparzielListItem(
                        text = sparziel.name,
                        modifier = Modifier.clickable {
                            //TODO SparzielID mitgeben
                            val intent = Intent(this@MainActivity, SparzielAnsichtActivity::class.java)
                            intent.putExtra("Sparziel", sparziel);
                            startActivity(intent)
                        },
                        iconId = R.drawable.piggy_bank,
                        progress = progress
                    )
                }
            }

            if (drawerState.isClosed && !drawerState.isAnimationRunning) {
                AlignedButton(
                    alignment = Alignment.BottomStart,
                    modifier = Modifier.padding(bottom = standardPadBottom, start = standardPadH),
                    iconId = R.drawable.piggy_bank
                ) {
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
}

fun getBankkonto(): Konto? {
    //TODO Get Angelegtes Bankkonto
  //  return Konto("Hauptkonto", 500.0)
    return null
}

fun getKreditKontenListe(): List<Konto> {
    //TODO Get alle angelegten KreditKartenKonten
   // val konto1 = Konto("Konto1", 500.0)
   // val konto2 = Konto("Konto2", 500.0)
   // val konto3 = Konto("Konto3", 500.0)
    //val kontoliste = mutableListOf(konto1, konto2, konto3)
    val kontoliste: MutableList<Konto> = mutableListOf()

    return kontoliste
}

fun getSparzielListe(): List<Sparziel> {
    //TODO Get alle angelegten KreditKartenKonten
    val sparziel1 = Sparziel("Auto")
    val sparziel2 = Sparziel("PC")
    val sparziel3 = Sparziel("Urlaub")
    val sparzielliste = mutableListOf(sparziel1, sparziel2, sparziel3, sparziel1, sparziel2, sparziel3, sparziel1, sparziel2, sparziel3,sparziel1, sparziel2, sparziel3)
    //val sparzielliste: MutableList<Konto> = mutableListOf()

    return sparzielliste
}