package com.example.smartsave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

abstract class SmartSaveActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GenerateContent()
        }

    }

    protected open fun Modifier.modify() = this.padding(top = 25.dp, end = 20.dp, start = 20.dp)

    @Composable
    protected fun GenerateContent() = Scaffold {paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(199, 216, 230, 255))
                .padding(paddingValues)
                .modify()
        ) {
            GenerateLayout()
        }
    }

    @Composable
    protected abstract fun BoxScope.GenerateLayout()

}