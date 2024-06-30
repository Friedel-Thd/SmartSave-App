package com.example.smartsave

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : SmartSaveActivity() {

    @Preview
    @Composable
    fun PreviewLayout() = GenerateContent()

    override fun Modifier.modify() = this

    @Composable
    override fun BoxScope.GenerateLayout() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            //TODO Bankkonto Darstellung
            ListItem("Kontoansicht", Modifier.clickable {
                val intent = Intent(this@MainActivity, KontoansichtActivity::class.java)
                startActivity(intent)
            })

            ListItem("First item")
        }



        if (drawerState.isOpen || drawerState.isAnimationRunning) ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listItem("Konto anlegen", Modifier.clickable {
                            //TODO KontoAnlegen aufrufen
                            val intent = Intent(this@MainActivity, KontoAnlegenActivity::class.java)
                            startActivity(intent)
                        })
                        listItem("Umsätze verwalten")
                        listItem("Kategorien verwalten")
                    }
                }
            }
        ) { }

        if (drawerState.isClosed) {
            ElevatedButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 40.dp, start = 25.dp)
                    .size(width = 150.dp, height = 80.dp),
            ) {
                Box {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.piggy_bank),
                        modifier = Modifier
                            .size(36.dp),
                        contentDescription = "drawable icons",
                        tint = Color.Unspecified
                    )
                }
            }

            ElevatedButton(
                onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 40.dp, end = 25.dp)
                    .size(width = 150.dp, height = 80.dp)
                    .alpha(if (drawerState.isClosed) 1f else 0f),
                enabled = drawerState.isClosed
            ) {
                Box {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.plus),
                        modifier = Modifier
                            .size(36.dp),
                        contentDescription = "drawable icons",
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}