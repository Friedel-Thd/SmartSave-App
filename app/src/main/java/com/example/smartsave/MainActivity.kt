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
import com.example.smartsave.helpers.ListItem
import com.example.smartsave.helpers.SmartSaveActivity
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

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            //TODO Bankkonto Darstellung
            ListItem(
                text = "Kontoansicht",
                modifier = Modifier.clickable {
                    val intent = Intent(this@MainActivity, KontoansichtActivity::class.java)
                    startActivity(intent)
                }
            )

            ListItem(text = "First item")
        }


        //TODO fix drawer not showing during first open animation
        if (drawerState.isOpen || drawerState.isAnimationRunning) ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        listItem(
                            text = "Konto anlegen",
                            modifier = Modifier.clickable {
                                //TODO KontoAnlegen aufrufen
                                val intent = Intent(this@MainActivity, KontoAnlegenActivity::class.java)
                                startActivity(intent)
                            }
                        )
                        listItem(text = "Umsätze verwalten")
                        listItem(text = "Kategorien verwalten")
                    }
                }
            }
        ) { }

        if (drawerState.isClosed) {
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