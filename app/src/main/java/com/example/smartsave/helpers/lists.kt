package com.example.smartsave.helpers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartsave.Kategorie
import com.example.smartsave.Konto
import com.example.smartsave.R
import com.example.smartsave.Umsatz


fun LazyListScope.listItem(text: String, modifier: Modifier = Modifier) = item { ListItem(text, modifier) }

@Composable
fun IconListItem(text: String, modifier: Modifier = Modifier, iconId: Int) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Icon(
            imageVector = ImageVector.vectorResource(id = iconId),
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp),
            contentDescription = "drawable icons",
            tint = Color.Unspecified
        )
        CenteredText(text = "$text€")
    }
    ListDivider()
}

@Composable
fun SparzielListItem(text: String, modifier: Modifier = Modifier, iconId: Int, progress: Int) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Icon(
            imageVector = ImageVector.vectorResource(id = iconId),
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterVertically)
                .padding(start = 20.dp),
            contentDescription = "drawable icons",
            tint = Color.Unspecified
        )
        Text(text = text, modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 34.sp))
        Text(text = "$progress%", modifier = Modifier.padding(vertical = 16.dp), style = TextStyle(fontSize = 34.sp))
    }
    ListDivider()
}

@Composable
fun ListItem(text: String, modifier: Modifier = Modifier) {
    Row (modifier = modifier.fillMaxWidth()){
        CenteredText(text = text)
    }
    ListDivider()
}

@Composable
fun ListDivider() = HorizontalDivider(thickness = 2.dp, color = Color.Black)



//TODO Layout Anpassen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelledDropdownMenu(label: String, options: List<Konto>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    Column {
        CenteredText(text = label)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = selectedOptionText.name,
                onValueChange = { },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption.name) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


//TODO Layout
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelledDatePicker() {
    val dateState = rememberDatePickerState()
    DatePicker(
        state = dateState
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelledDropdownMenuUmsatz(label: String, options: List<Kategorie>, umsatz: Umsatz) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = label, textAlign = TextAlign.Center)
        ExposedDropdownMenuBox(
            expanded = expanded,
            modifier = Modifier.width(150.dp),
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = selectedOptionText.name,
                onValueChange = { },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }

            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption.name) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        }
                    )
                }
            }

        }
       // Text(text = umsatz.value.toString())


    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = umsatz.name)

    }

}