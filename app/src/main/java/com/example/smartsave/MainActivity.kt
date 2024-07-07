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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartsave.dataClasses.Konto
import com.example.smartsave.dataClasses.Sparziel
import com.example.smartsave.helpers.AlignedButton
import com.example.smartsave.helpers.ErrorMsg
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
    private val sparKontoListState = mutableStateOf<List<Konto>>(emptyList())
    var db = DbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        bankkontoState.value = db.getBankkonto()
        kreditkontenListeState.value = db.getKreditKontenListe()
        sparzielListeState.value = db.getSparzielListe()

        super.onCreate(savedInstanceState)
    }
    override fun onResume() {
        bankkontoState.value = db.getBankkonto()
        sparKontoListState.value = db.getSparKontenListe()
        kreditkontenListeState.value = db.getKreditKontenListe()
        sparzielListeState.value = db.getSparzielListe()
        super.onResume()
    }

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
        val sparKontenListe by remember { sparKontoListState}
        var emptyKontenError  by remember { mutableStateOf(false) }

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
                                    val intent = Intent(this@MainActivity, KontoAnlegenActivity::class.java)
                                    intent.putExtra("BankkontoExists", (bankkonto != null))
                                    intent.putExtra("KreditkartenkontoExists", (kreditkontenListe.isNotEmpty()))
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
                        //TODO DELETE
                        ListItem(text = "Testdaten anlegen",
                            modifier = Modifier.clickable {
                                db.insertTestData(db)
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
                            intent.putExtra("Konto", kreditkonto)
                            startActivity(intent)
                        },
                        iconId = R.drawable.arrow_forward
                    )
                }

                for (sparziel in sparzielListe) {
                    SparzielListItem(
                        text = sparziel.name,
                        modifier = Modifier.clickable {
                            val intent = Intent(this@MainActivity, SparzielAnsichtActivity::class.java)
                            intent.putExtra("Sparziel", sparziel)
                            startActivity(intent)
                        },
                        iconId = R.drawable.piggy_bank,
                        progress = sparziel.calculateProgress()
                    )

                }
                if(emptyKontenError) ErrorMsg(msg = "Es müssen zuerst mindestens ein Bankkonto/Kreditkonto und ein Sparkonto angelegt werden, um ein Sparziel zu erstellen")

            }


            if (drawerState.isClosed && !drawerState.isAnimationRunning) {
                AlignedButton(
                    alignment = Alignment.BottomStart,
                    modifier = Modifier.padding(bottom = standardPadBottom, start = standardPadH),
                    iconId = R.drawable.piggy_bank,

                ) {
                    emptyKontenError = sparKontenListe.isEmpty() || (kreditkontenListe.isEmpty() || bankkonto == null)
                    if(!emptyKontenError) {
                        val intent = Intent(this@MainActivity, SparzielActivity::class.java)
                        startActivity(intent)
                    }
                }
                AlignedButton(
                    alignment = Alignment.BottomEnd,
                    modifier = Modifier.padding(bottom = standardPadBottom, end = standardPadH),
                    iconId = R.drawable.plus
                ) {
                    scope.launch {
                        drawerState.apply {
                            emptyKontenError = false
                            if (isClosed) open() else close()
                        }
                    }
                }
            }
        }
    }
}
