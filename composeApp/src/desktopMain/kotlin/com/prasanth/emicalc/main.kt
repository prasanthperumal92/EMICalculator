package com.prasanth.emicalc

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "EMICalculator",
    ) {
        App()
    }
}