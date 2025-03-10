package com.prasanth.emicalc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.prasanth.emicalc.model.PeriodicPayment
import com.prasanth.emicalc.ui.Cell
import com.prasanth.emicalc.ui.ERadioGroup
import com.prasanth.emicalc.ui.ETextField
import com.prasanth.emicalc.viewmodel.AppViewModel
import com.prasanth.emicalc.viewmodel.EMIViewModelStoreOwner
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToLong

@Composable
@Preview
fun App() {
    val store = remember { EMIViewModelStoreOwner().viewModelStore }
    val appViewModel: AppViewModel = remember { ViewModelProvider.create(store, AppViewModel)[AppViewModel::class] }
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.primary).height(50.dp)) {
                Text("Emi Calculator", modifier = Modifier.align(Alignment.Center), color = Color.White)
            }
        }) {
            LazyColumn (modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp)) {
                item {
                    Spacer(Modifier.height(5.dp))
                    ETextField(
                        modifier = Modifier.fillMaxWidth(),
                        updatePrinciple = appViewModel::setPrinciple,
                        principleState = appViewModel.principle,
                        keyboardType = KeyboardType.Number,
                        hint = "Principle Amount"
                    )
                }
                item {
                    Spacer(Modifier.height(5.dp))
                    ETextField(
                        modifier = Modifier.fillMaxWidth(),
                        updatePrinciple = appViewModel::setInterest,
                        principleState = appViewModel.interest,
                        hint = "Interest",
                        keyboardType = KeyboardType.Decimal
                    )
                }
                item {
                    Spacer(Modifier.height(5.dp))
                    val isMonth:State<Boolean> = appViewModel.isMonth
                    ERadioGroup(isMonth, appViewModel::toggleIsMonth)
                    ETextField(
                        modifier = Modifier.fillMaxWidth(),
                        updatePrinciple = appViewModel::setYears,
                        principleState = appViewModel.years,
                        hint = "Years",
                        keyboardType = KeyboardType.Decimal
                    )
                }
                item {
                    Spacer(Modifier.height(5.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Monthly Emi: ${appViewModel.emi.value}",
                        fontSize = TextUnit(20f, TextUnitType.Sp)
                    )
                }
                item {
                    Spacer(Modifier.height(5.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Total : ${appViewModel.total.value}",
                        fontSize = TextUnit(20f, TextUnitType.Sp)
                    )
                }
                item {
                    Spacer(Modifier.height(5.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Interest Payed: ${appViewModel.interestPayed.value}",
                        fontSize = TextUnit(20f, TextUnitType.Sp)
                    )
                }
                AmortizationSchedule(amortizationSchedule = appViewModel.amortizationSchedule)

            }
        }
    }
    DisposableEffect(appViewModel,store){
        onDispose {
            store.clear()
        }
    }
}


fun LazyListScope.AmortizationSchedule(amortizationSchedule: State<List<PeriodicPayment>>) {
    if (amortizationSchedule.value.isNotEmpty()) {
        item {
            Spacer(Modifier.height(15.dp))
            Text(
                "Amortization Schedule:", fontWeight = FontWeight.Bold, fontSize = TextUnit(
                    20f,
                    TextUnitType.Sp
                )
            )
            Row(Modifier.border(1.dp, Color.Black).fillMaxWidth().height(IntrinsicSize.Min)) {
                Cell(text = "#", modifier = Modifier.weight(0.3f))
                Cell(text = "Month/Year")
                Cell(text = "Balance")
                Cell(text = "Emi")
                Cell(text = "Interest")
                Cell(text = "Principle")
            }
        }
    }
    items(amortizationSchedule.value.size){ index: Int ->
        val item = amortizationSchedule.value[index]
        Row(Modifier.border(1.dp,Color.Black).fillMaxWidth().height(IntrinsicSize.Min)) {
            Cell(text = "${index+1}", modifier = Modifier.weight(0.3f))
            Cell(text = item.date)
            Cell(text = "${item.balance.roundToLong()}")
            Cell(text = "${item.emi.roundToLong()}")
            Cell(text = "${item.interestPaid.roundToLong()}")
            Cell(text = "${item.principlePaid.roundToLong()}")
        }
    }
    item { Spacer(Modifier.height(5.dp)) }
}



