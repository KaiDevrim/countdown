package tech.kaidevrim.countdown

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
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
                    SelectDateTimeMenu()
                }
            }
        }
    }
}

var daysDiff: Int = 0

@Composable
fun SelectDateTimeMenu() {
    val formattedDateState = remember { mutableStateOf("") }
    val daysLeft = remember { mutableStateOf("") }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.Center) {
            ShowDatePicker(formattedDateState)
        }
        Row(horizontalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.padding(10.dp))
        }
        Row(horizontalArrangement = Arrangement.Center) {
            ShowTimePicker()
        }
        Spacer(modifier = Modifier.padding(10.dp))

        if (formattedDateState.value != "") {
            Button(onClick = { calculateDaysLeft(formattedDateState.value, daysLeft) }) {
                Text(text = "Calculate Days Left")
            }
            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = "Days left: ${daysLeft.value}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CountdownTheme {
        SelectDateTimeMenu()
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
            if (fMonth+1 <= 10) {
                formattedDateState.value = "$fYear-0${fMonth+1}-$fDay"
            } else {
                formattedDateState.value = "$fYear-${fMonth+1}-$fDay"
            }
        },
        mYear,
        mMonth,
        mDay
    )

    Button(onClick = {
        datePickerDialog.show()
    }) {
        Text(text = "Open Date Picker", color = Color.White)
    }
    // Displaying the mDate value in the Text
    Text(
        text = "Selected Date: ${formattedDateState.value}",
        fontSize = 30.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ShowTimePicker() {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    val formattedTimeState = remember { mutableStateOf("") }

    val mTimePickerDialog = TimePickerDialog(
        mContext,
        { _, fHour: Int, fMinute: Int ->
            formattedTimeState.value = "$fHour:$fMinute"
        }, mHour, mMinute, true
    )
    val timeState = remember { mutableStateOf(IntArray(2)) }
    timeState.value[0] = mHour
    timeState.value[1] = mMinute

    Button(onClick = { mTimePickerDialog.show() }) {
        Text(text = "Open Time Picker", color = Color.White)
    }

    Text(text = "Selected Time: ${formattedTimeState.value}", fontSize = 30.sp)
}

fun calculateDaysLeft(futureDate: String, daysInput: MutableState<String>) {
    val currentDay = LocalDate.now()
    val parsedFutureDate = LocalDate.parse(futureDate)
    println(currentDay)
    println(parsedFutureDate)
    daysDiff = currentDay.until(parsedFutureDate, ChronoUnit.DAYS).toInt()
    println(daysDiff)
    daysInput.value = daysDiff.toString()
}