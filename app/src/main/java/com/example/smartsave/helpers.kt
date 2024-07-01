package com.example.smartsave

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val standardTextStyle = TextStyle(fontSize = 20.sp)


fun LazyListScope.listItem(text: String, modifier: Modifier = Modifier) = item {ListItem(text, modifier)}

@Composable
fun ListItem(text: String, modifier: Modifier = Modifier) {
    CenteredText(text = text, modifier = modifier)
    ListDivider()
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
fun ListDivider() = HorizontalDivider(thickness = 2.dp, color = Color.Black)


@Composable
fun LabelledInputField(value: String, label: String, onValueChange: (String) -> Unit) = Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
) {
    Text("$label: ",
        modifier = Modifier.fillMaxWidth(.3f),
        style = standardTextStyle
    )
    TextField(
        value = value,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = onValueChange,
        textStyle = standardTextStyle
    )
}