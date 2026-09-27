package com.tejaratbonyan.anbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.runtime.CompositionLocalProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AnbarApp() }
    }
}

@Composable
private fun AnbarApp() {
    var screen by remember { mutableStateOf("home") }
    var showConfirm by remember { mutableStateOf(false) }
    var docType by remember { mutableStateOf("خروج") }
    var receiver by remember { mutableStateOf("") }
    var driver by remember { mutableStateOf("") }
    var vehicle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    MaterialTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            when (screen) {
                "home" -> HomeScreen(
                    onOutbound = { docType = "خروج"; screen = "form" },
                    onInbound = { docType = "ورود"; screen = "form" },
                    onHistory = { screen = "history" }
                )
                "form" -> FormScreen(
                    docType = docType,
                    receiver = receiver,
                    driver = driver,
                    vehicle = vehicle,
                    description = description,
                    onReceiver = { receiver = it },
                    onDriver = { driver = it },
                    onVehicle = { vehicle = it },
                    onDescription = { description = it },
                    onBack = { screen = "home" },
                    onSubmit = { showConfirm = true }
                )
                else -> HistoryScreen(onBack = { screen = "home" }, message = message)
            }

            if (showConfirm) {
                AlertDialog(
                    onDismissRequest = { showConfirm = false },
                    title = { Text("تأیید ثبت") },
                    text = { Text("اطلاعات برگه $docType ثبت شود؟") },
                    confirmButton = {
                        TextButton(onClick = {
                            showConfirm = false
                            message = "برگه ${if (docType == "خروج") "OUT-1405-0001" else "IN-1405-0001"} با موفقیت ثبت شد."
                            screen = "history"
                        }) { Text("ثبت نهایی") }
                    },
                    dismissButton = { TextButton(onClick = { showConfirm = false }) { Text("انصراف") } }
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(onOutbound: () -> Unit, onInbound: () -> Unit, onHistory: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("انبار تجارت بنیان", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text("داشبورد اپراتور", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onOutbound, modifier = Modifier.fillMaxWidth()) { Text("ثبت خروج") }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onInbound, modifier = Modifier.fillMaxWidth()) { Text("ثبت ورود") }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onHistory, modifier = Modifier.fillMaxWidth()) { Text("سوابق من") }
    }
}

@Composable
private fun FormScreen(
    docType: String, receiver: String, driver: String, vehicle: String, description: String,
    onReceiver: (String) -> Unit, onDriver: (String) -> Unit, onVehicle: (String) -> Unit,
    onDescription: (String) -> Unit, onBack: () -> Unit, onSubmit: () -> Unit
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("ثبت $docType", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("حواله ${if (docType == "خروج") "OUT-1405-0001" else "IN-1405-0001"}")
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(receiver, onReceiver, Modifier.fillMaxWidth(), label = { Text("تحویل گیرنده") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(driver, onDriver, Modifier.fillMaxWidth(), label = { Text("راننده") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(vehicle, onVehicle, Modifier.fillMaxWidth(), label = { Text("خودرو / پلاک") })
                Spacer(Modifier.height(12.dp))
                Text("اقلام", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("ردیف")
                    Text("واحد")
                    Text("تعداد")
                    Text("نام کالا")
                }
                Spacer(Modifier.height(8.dp))
                Text("برای نسخه آزمایشی، اقلام از طریق پنل تکمیل می‌شوند.", fontSize = 13.sp)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(description, onDescription, Modifier.fillMaxWidth(), label = { Text("توضیحات") }, minLines = 3)
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("بازگشت") }
            Button(onClick = onSubmit, modifier = Modifier.weight(1f)) { Text("ثبت نهایی") }
        }
    }
}

@Composable
private fun HistoryScreen(onBack: () -> Unit, message: String) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("سوابق من", style = MaterialTheme.typography.headlineSmall)
        if (message.isNotBlank()) Text(message)
        Text("در نسخه آزمایشی، سوابق محلی پس از تکمیل دیتابیس اضافه می‌شود.")
        TextButton(onClick = onBack) { Text("بازگشت") }
    }
}
