package com.example.smartsave.helpers

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

abstract class SmartSaveActivity(
    private val padStart: Dp = standardPadH,
    private val padEnd: Dp = standardPadH,
    private val padTop: Dp = standardPadTop,
    private val padBottom: Dp = standardPadBottom,
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GenerateContent()
        }

    }

    @Composable
    protected fun GenerateContent() = Scaffold {paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(199, 216, 230, 255))
                .padding(paddingValues)
                .padding(
                    start = padStart,
                    end = padEnd,
                    top = padTop,
                    bottom = padBottom,
                )
        ) {
            GenerateLayout()
        }
    }

    @Composable
    protected abstract fun BoxScope.GenerateLayout()


    companion object {

        val standardPadH = 20.dp
        val standardPadTop = 25.dp
        val standardPadBottom = 20.dp

    }

}