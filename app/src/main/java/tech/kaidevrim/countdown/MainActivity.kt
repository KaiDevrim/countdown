package tech.kaidevrim.countdown

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.kaidevrim.countdown.ui.theme.CountdownTheme
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountdownTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainMenu()
                }
            }
        }
    }
}

var daysDiff: Int = 0

@Composable
fun MainMenu() {
    val formattedDateState = remember { mutableStateOf("") }
    val daysLeft = remember { mutableStateOf("") }
    val incorrectDate = remember {mutableStateOf(false)}
    if (incorrectDate.value) {
        SimpleAlertDialog(incorrectDate)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(20.dp)) {
            ShowDatePicker(formattedDateState)
        }
        if (formattedDateState.value != "") {
            Button(onClick = { calculateDaysLeft(formattedDateState.value, daysLeft, incorrectDate) }) {
                Text(text = "Calculate Days Left")
            }
            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 100.dp)) {
                Text(text = daysLeft.value, fontSize = 80.sp)
            }
            Row {
                Text(text = "Days left")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CountdownTheme {
        MainMenu()
    }
}

@Composable
fun ShowDatePicker(formattedDateState: MutableState<String>) {
    val mContext = LocalContext.current

    val mCalendar = Calendar.getInstance()
    val mYear: Int = mCalendar.get(Calendar.YEAR)
    val mMonth: Int = mCalendar.get(Calendar.MONTH)
    val mDay: Int = mCalendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, fYear: Int, fMonth: Int, fDay: Int ->
            if (fMonth + 1 <= 10) {
                formattedDateState.value = "$fYear-0${fMonth + 1}-$fDay"
            } else {
                formattedDateState.value = "$fYear-${fMonth + 1}-$fDay"
            }
        },
        mYear,
        mMonth,
        mDay
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            datePickerDialog.show()
        }) {
            Text(text = "Choose Date", color = Color.White)
        }

        Text(text = "Date chosen: ${formattedDateState.value}")
    }
}

fun calculateDaysLeft(futureDate: String, daysInput: MutableState<String>, incorrectDate: MutableState<Boolean>) {
    val currentDay = LocalDate.now()
    val parsedFutureDate = LocalDate.parse(futureDate)
    daysDiff = currentDay.until(parsedFutureDate, ChronoUnit.DAYS).toInt()
    if (daysDiff <= 0) {
        incorrectDate.value = true
    }
    else {
        daysInput.value = daysDiff.toString()
    }
}

@Composable
fun SimpleAlertDialog(incorrectDate: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { incorrectDate.value = false},
        confirmButton = {
            TextButton(onClick = {incorrectDate.value = false})
            { Text(text = "OK") }
        },
        title = { Text(text = "Incorrect Date") },
        text = { Text(text = "Please select a date in the FUTURE!") }
    )
}