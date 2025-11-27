package com.example.hourtodec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hourtodec.ui.theme.HourToDecTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HourToDecTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HourToDecConverter(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HourToDecConverter(modifier: Modifier = Modifier) {
    var hoursInput by remember { mutableStateOf("") }
    var decimalInput by remember { mutableStateOf("") }
    var decimalResult by remember { mutableStateOf("") }
    var hoursResult by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Titolo con stile migliorato
        Text(
            text = "Convertitore Ore ⇄ Decimali",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )

        // Conversione da Ore a Decimali
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🕐 Ore → Decimali",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = hoursInput,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("^\\d{0,2}:?\\d{0,2}$"))) {
                            hoursInput = input
                            decimalResult = convertHoursToDecimal(input)
                        }
                    },
                    label = { Text("Inserisci ore (es. 8:30)") },
                    placeholder = { Text("HH:MM") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        if (hoursInput.isNotEmpty()) {
                            IconButton(onClick = {
                                hoursInput = ""
                                decimalResult = ""
                            }) {
                                Icon(Icons.Default.Clear, "Pulisci")
                            }
                        }
                    },
                    singleLine = true
                )

                if (decimalResult.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "✓ $decimalResult ore",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Conversione da Decimali a Ore
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🔢 Decimali → Ore",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                OutlinedTextField(
                    value = decimalInput,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))) {
                            decimalInput = input
                            hoursResult = convertDecimalToHours(input)
                        }
                    },
                    label = { Text("Inserisci decimali (es. 8.5)") },
                    placeholder = { Text("0.00") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    trailingIcon = {
                        if (decimalInput.isNotEmpty()) {
                            IconButton(onClick = {
                                decimalInput = ""
                                hoursResult = ""
                            }) {
                                Icon(Icons.Default.Clear, "Pulisci")
                            }
                        }
                    },
                    singleLine = true
                )

                if (hoursResult.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "✓ $hoursResult",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// Converte ore in formato HH:MM in decimali
fun convertHoursToDecimal(hoursString: String): String {
    if (hoursString.isEmpty()) return ""

    return try {
        val parts = hoursString.split(":")
        if (parts.size == 2) {
            val hours = parts[0].toIntOrNull() ?: 0
            val minutes = parts[1].toIntOrNull() ?: 0

            if (minutes >= 60) return "Minuti non validi"

            val decimal = hours + (minutes / 60.0)
            String.format("%.2f", decimal)
        } else if (parts.size == 1 && hoursString.contains(":").not()) {
            // Solo ore senza minuti
            val hours = parts[0].toIntOrNull() ?: 0
            String.format("%.2f", hours.toDouble())
        } else {
            ""
        }
    } catch (e: Exception) {
        ""
    }
}

// Converte decimali in formato HH:MM
fun convertDecimalToHours(decimalString: String): String {
    if (decimalString.isEmpty()) return ""

    return try {
        val decimal = decimalString.toDoubleOrNull() ?: return ""
        val hours = decimal.toInt()
        val minutes = ((decimal - hours) * 60).roundToInt()

        String.format("%02d:%02d", hours, minutes)
    } catch (e: Exception) {
        ""
    }
}

@Preview(showBackground = true)
@Composable
fun HourToDecConverterPreview() {
    HourToDecTheme {
        HourToDecConverter()
    }
}