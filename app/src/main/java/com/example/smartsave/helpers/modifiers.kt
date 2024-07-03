package com.example.smartsave.helpers

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput


@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.holding(
    onLongPress: () -> Unit
): Modifier = pointerInput(Unit) {
    detectTapGestures(
        onLongPress = {
            onLongPress()
        }
    )
}
