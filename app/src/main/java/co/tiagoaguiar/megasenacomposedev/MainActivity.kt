package co.tiagoaguiar.megasenacomposedev

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.tiagoaguiar.megasenacomposedev.ui.theme.MegaSenaTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MegaSenaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {

    val context = LocalContext.current
    val preferences = context.getSharedPreferences("megasena", Context.MODE_PRIVATE)
    val result = remember {
        mutableStateOf(preferences.getString(PREFS_KEY,"") ?: "")
    }
    val textFieldValue = remember {
        mutableStateOf("")
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Boa sorte!",
                modifier = Modifier.padding(bottom = 20.dp),
                style = TextStyle(
                    color = Color(0xFF50C878),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = textFieldValue.value,
                    label = {
                        Text("Digite um número entre 6 e 15")
                    },
                    onValueChange = {
                        textFieldValue.value = validateinput(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

                Text(
                    result.value,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Button(
                onClick = {
                    val inputIsValid = validateTextField(textFieldValue.value)
                    if (inputIsValid) {
                        result.value = generateRandomNumbers(textFieldValue.value.toInt())
                        saveNumberSequence(preferences, result.value)
                    } else {
                        Toast.makeText(
                            context,
                            "Digite um número entre 6 e 15!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text("Gerar Números")
            }
        }
    }
}

fun validateinput(input: String): String {
    return input.filter {
        it in "0123456789"
    }
}

fun validateTextField(textFieldValue: String): Boolean {
    if (textFieldValue.isEmpty()) return false
    return textFieldValue.toInt() in 6..15
}

fun generateRandomNumbers(qtde: Int): String {
    val numbers = mutableSetOf<Int>()
    while (numbers.size <= qtde) {
        val n = Random.nextInt(1, 61)
        numbers.add(n)
    }
    return numbers.joinToString(" - ")
}

fun saveNumberSequence(prefs: SharedPreferences, numberSequence: String) {
    prefs.edit().apply {
        putString(PREFS_KEY, numberSequence)
        apply()
    }
}

const val PREFS_KEY = "key_mega"

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MegaSenaTheme {
        MainApp()
    }
}