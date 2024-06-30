package com.example.smartsave

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GenerateLayout()
        }

    }

    @Preview
    @Composable
    fun PreviewLayout() {
        GenerateLayout()
    }

    @Composable
    fun GenerateLayout() {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        Scaffold() { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(
                        color = Color(199, 216, 230, 255)
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()

                ) {
                    //TODO Bankkonto Darstellung
                    CenteredText(text = "Kontoansicht",
                        modifier = Modifier.clickable {
                            val intent = Intent(this@MainActivity, KontoansichtActivity::class.java)
                            startActivity(intent)
                        })
                    ListDivider()

                    CenteredText(text = "First item")
                    ListDivider()
                }



                if (drawerState.isOpen || drawerState.isAnimationRunning) ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                item {
                                    CenteredText(text = "Konto anlegen",
                                        modifier = Modifier.clickable {
                                            //TODO KontoAnlegen aufrufen
                                            val intent = Intent(this@MainActivity, KontoAnlegenActivity::class.java)
                                            startActivity(intent)
                                        })
                                    ListDivider()
                                }
                                item {
                                    CenteredText(text = "Umsätze verwalten")
                                    ListDivider()
                                }
                                item {
                                    CenteredText(text = "Kategorien verwalten")
                                    ListDivider()
                                }
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
                        Box() {
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
                        Box() {
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
    }
}
@Composable
fun CenteredText(text: String, modifier: Modifier = Modifier) = Text(
    text = text,
    modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .then(modifier),
    textAlign = TextAlign.Center,
    style = TextStyle(
        fontSize = 34.sp
    )
)

@Composable
fun ListDivider() = HorizontalDivider(thickness = 2.dp, color = Color(0, 0, 0))

