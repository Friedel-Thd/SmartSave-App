package com.example.smartsave.helpers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val standardTextStyle = TextStyle(fontSize = 20.sp)

@Composable
fun LabelledInputField(label: String, value: String, onValueChange: (String) -> Unit) = Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
) {
    StandardText("$label: ", modifier = Modifier.fillMaxWidth(.35f))
    TextField(
        value = value,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = onValueChange,
        textStyle = standardTextStyle
    )
}

@Composable
fun CenteredText(text: String, modifier: Modifier = Modifier) = Text(
    text = text,
    modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .then(modifier),
    textAlign = TextAlign.Center,
    style = TextStyle(fontSize = 34.sp)
)

@Composable
fun StandardText(text: String, modifier: Modifier = Modifier, styleOverride: TextStyle? = null) = Text(
    text = text,
    modifier = modifier,
    style = standardTextStyle.merge(styleOverride),
)