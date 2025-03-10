package com.prasanth.emicalc.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment

@Composable
fun ERadioGroup(
    isMonth: State<Boolean>,
    onCheck: ()->Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(isMonth.value, onCheck)
        Text("Months")
        RadioButton(!isMonth.value, onCheck)
        Text("Years")
    }
}