package com.prasanth.emicalc.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ERadioGroup(
    isMonth: State<Boolean>,
    onCheck: ()->Unit
) {
    Row(modifier = Modifier.padding(start = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("Duration: ")
        RadioButton(isMonth.value, onCheck)
        Text("Months")
        RadioButton(!isMonth.value, onCheck)
        Text("Years")
    }
}