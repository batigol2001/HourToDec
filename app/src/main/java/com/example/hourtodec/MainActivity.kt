package com.example.hourtodec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HourToDecTheme {
                TimeConverterScreen()
            }
        }
    }
}

// Colori personalizzati
object AppColors {
    val BackgroundDark = Color(0xFF0F0D1A)
    val BackgroundMid = Color(0xFF1E1B4B)
    val Purple500 = Color(0xFFA855F7)
    val Purple600 = Color(0xFF9333EA)
    val Pink500 = Color(0xFFEC4899)
    val Pink600 = Color(0xFFDB2777)
    val PurpleLight = Color(0xFFE9D5FF)
    val SurfaceLight = Color(0x1AFFFFFF)  // 10% white
    val BorderLight = Color(0x33FFFFFF)   // 20% white
    val Emerald500 = Color(0xFF10B981)
    val EmeraldSurface = Color(0x3310B981) // 20% emerald
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeConverterScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var hoursInput by remember { mutableStateOf("") }
    var decimalInput by remember { mutableStateOf("") }
    var hoursResult by remember { mutableStateOf<String?>(null) }
    var decimalResult by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AppColors.BackgroundDark,
                        AppColors.BackgroundMid,
                        AppColors.BackgroundDark
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Header con icona
            HeaderSection()

            Spacer(modifier = Modifier.height(32.dp))

            // Tab Selector
            TabSelector(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card principale
            ConverterCard(
                selectedTab = selectedTab,
                hoursInput = hoursInput,
                decimalInput = decimalInput,
                hoursResult = hoursResult,
                decimalResult = decimalResult,
                onHoursInputChange = {
                    hoursInput = it
                    hoursResult = null
                },
                onDecimalInputChange = {
                    decimalInput = it
                    decimalResult = null
                },
                onConvertToDecimal = {
                    hoursResult = convertToDecimal(hoursInput)
                },
                onConvertToHours = {
                    decimalResult = convertToHours(decimalInput)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Reference
            QuickReferenceCard()

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Text(
                text = "Swipe per cambiare modalità",
                color = AppColors.Purple500.copy(alpha = 0.5f),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HeaderSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Icona con gradient
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(AppColors.Purple500, AppColors.Pink500)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🕐",
                fontSize = 32.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Time Converter",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Converti ore e decimali istantaneamente",
            fontSize = 14.sp,
            color = AppColors.PurpleLight.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun TabSelector(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.SurfaceLight)
            .padding(6.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TabButton(
                text = "🕐 Ore → Dec",
                isSelected = selectedTab == 0,
                onClick = { onTabSelected(0) },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "🔢 Dec → Ore",
                isSelected = selectedTab == 1,
                onClick = { onTabSelected(1) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (isSelected) {
                    Modifier.background(
                        Brush.horizontalGradient(
                            colors = listOf(AppColors.Purple500, AppColors.Pink500)
                        )
                    )
                } else {
                    Modifier
                }
            )
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                color = if (isSelected) Color.White else AppColors.PurpleLight.copy(alpha = 0.7f),
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ConverterCard(
    selectedTab: Int,
    hoursInput: String,
    decimalInput: String,
    hoursResult: String?,
    decimalResult: String?,
    onHoursInputChange: (String) -> Unit,
    onDecimalInputChange: (String) -> Unit,
    onConvertToDecimal: () -> Unit,
    onConvertToHours: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AppColors.BorderLight, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.SurfaceLight)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn() + slideInHorizontally { if (targetState > initialState) it else -it } togetherWith
                            fadeOut() + slideOutHorizontally { if (targetState > initialState) -it else it }
                },
                label = "tab_content"
            ) { tab ->
                when (tab) {
                    0 -> HoursToDecimalContent(
                        input = hoursInput,
                        result = hoursResult,
                        onInputChange = onHoursInputChange,
                        onConvert = onConvertToDecimal
                    )
                    1 -> DecimalToHoursContent(
                        input = decimalInput,
                        result = decimalResult,
                        onInputChange = onDecimalInputChange,
                        onConvert = onConvertToHours
                    )
                }
            }
        }
    }
}

@Composable
fun HoursToDecimalContent(
    input: String,
    result: String?,
    onInputChange: (String) -> Unit,
    onConvert: () -> Unit
) {
    Column {
        Text(
            text = "Inserisci orario",
            color = AppColors.PurpleLight.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        StyledTextField(
            value = input,
            onValueChange = onInputChange,
            placeholder = "es. 8:30",
            trailingIcon = "🕐"
        )

        Spacer(modifier = Modifier.height(20.dp))

        GradientButton(
            text = "Converti",
            onClick = onConvert
        )

        ResultBox(result = result)
    }
}

@Composable
fun DecimalToHoursContent(
    input: String,
    result: String?,
    onInputChange: (String) -> Unit,
    onConvert: () -> Unit
) {
    Column {
        Text(
            text = "Inserisci valore decimale",
            color = AppColors.PurpleLight.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        StyledTextField(
            value = input,
            onValueChange = onInputChange,
            placeholder = "es. 8.5",
            trailingIcon = "#",
            keyboardType = KeyboardType.Decimal
        )

        Spacer(modifier = Modifier.height(20.dp))

        GradientButton(
            text = "Converti",
            onClick = onConvert
        )

        ResultBox(result = result)
    }
}

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = AppColors.PurpleLight.copy(alpha = 0.4f)
            )
        },
        trailingIcon = {
            Text(
                text = trailingIcon,
                fontSize = 20.sp,
                color = AppColors.Purple500
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.Purple500,
            unfocusedBorderColor = AppColors.Purple500.copy(alpha = 0.3f),
            focusedContainerColor = AppColors.SurfaceLight,
            unfocusedContainerColor = AppColors.SurfaceLight,
            cursorColor = AppColors.Purple500,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(AppColors.Purple500, AppColors.Pink500)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun ResultBox(result: String?) {
    AnimatedVisibility(
        visible = result != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        result?.let {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(AppColors.EmeraldSurface)
                    .border(1.dp, AppColors.Emerald500.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Risultato",
                        fontSize = 12.sp,
                        color = AppColors.Emerald500
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun QuickReferenceCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AppColors.BorderLight, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.SurfaceLight.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Riferimento rapido",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = AppColors.PurpleLight.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    "0:15" to "0.25",
                    "0:30" to "0.50",
                    "0:45" to "0.75",
                    "1:00" to "1.00"
                ).forEach { (time, dec) ->
                    QuickRefItem(time = time, decimal = dec)
                }
            }
        }
    }
}

@Composable
fun QuickRefItem(time: String, decimal: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AppColors.SurfaceLight)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = time,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        Text(
            text = decimal,
            fontSize = 11.sp,
            color = AppColors.Purple500
        )
    }
}

// Funzioni di conversione
fun convertToDecimal(timeStr: String): String? {
    val regex = Regex("""^(\d+):(\d{1,2})$""")
    val match = regex.find(timeStr.trim()) ?: return null

    val hours = match.groupValues[1].toIntOrNull() ?: return null
    val minutes = match.groupValues[2].toIntOrNull() ?: return null

    if (minutes >= 60) return null

    val decimal = hours + minutes / 60.0
    return String.format("%.2f", decimal)
}

fun convertToHours(decimalStr: String): String? {
    val decimal = decimalStr.trim().replace(",", ".").toDoubleOrNull() ?: return null
    if (decimal < 0) return null

    val hours = decimal.toInt()
    val minutes = ((decimal - hours) * 60).toInt()

    return "$hours:${minutes.toString().padStart(2, '0')}"
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TimeConverterPreview() {
    HourToDecTheme {
        TimeConverterScreen()
    }
}
