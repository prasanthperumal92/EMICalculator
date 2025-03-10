package com.prasanth.emicalc.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.Cell(modifier: Modifier = Modifier, text:String) {
    Text(text, modifier = modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
    Spacer(Modifier.width(1.dp).fillMaxHeight().background(Color.Black))
}
