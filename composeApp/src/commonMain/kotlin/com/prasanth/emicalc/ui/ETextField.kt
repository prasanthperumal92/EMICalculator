package com.prasanth.emicalc.ui

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun ETextField(
    modifier: Modifier = Modifier,
    principleState: State<String>,
    updatePrinciple: (value: String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    hint:String = ""
) {
    val principle:String by remember { principleState }
    val textFieldValue = TextFieldValue(text = principle, selection = TextRange(principle.length))
    OutlinedTextField(
        modifier = modifier,
        value = textFieldValue,
        onValueChange = { updatePrinciple(it.text)},
        placeholder = { Text(hint) },
        singleLine = true,
        keyboardOptions = KeyboardOptions (
            imeAction = ImeAction.Done,
            keyboardType = keyboardType,
        ),
        keyboardActions = KeyboardActions(onDone = {})
    )
}