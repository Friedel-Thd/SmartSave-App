package com.example.smartsave.helpers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp


@Composable
fun BoxScope.AlignedButton(
    alignment: Alignment,
    modifier: Modifier = Modifier,
    iconId: Int,
    onClick: () -> Unit
) = AlignedButton(
    alignment = alignment,
    modifier = modifier,
    label = {
        Box {
            Icon(
                imageVector = ImageVector.vectorResource(id = iconId),
                modifier = Modifier.size(36.dp),
                contentDescription = "drawable icons",
                tint = Color.Unspecified
            )
        }
    },
    onClick = onClick
)

@Composable
fun BoxScope.AlignedButton(
    alignment: Alignment,
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) = AlignedButton(
    alignment = alignment,
    modifier = modifier,
    label = { StandardText(text = text) },
    onClick = onClick
)

@Composable
fun BoxScope.AlignedButton(
    alignment: Alignment,
    modifier: Modifier = Modifier,
    label: @Composable RowScope.() -> Unit,
    onClick: () -> Unit
) = ElevatedButton(
    modifier = Modifier
        .align(alignment)
        .padding(horizontal = 5.dp)
        .then(modifier)
        .size(width = 150.dp, height = 80.dp),
    onClick = onClick,
    content = label
)
