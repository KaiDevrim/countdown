package tech.kaidevrim.countdown

import android.app.TimePickerDialog
import android.os.Bundle
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
import java.time.LocalTime
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

@Composable
fun MainMenu() {
    val timeState = remember { mutableStateOf(IntArray(2)) }
    val timeDiff = remember { mutableStateOf(IntArray(2)) }
    val incorrectTime = remember { mutableStateOf(false) }
    if (incorrectTime.value) {
        SimpleAlertDialog(incorrectTime)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(20.dp)) {
            ShowTimePicker(timeState)
        }
        Button(onClick = { calculateTimeDiff(timeState, timeDiff) }) {
            Text(text = "Calculate Time Left")
        }
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 100.dp)) {
            Text(text = timeDiff.value[0].toString(), fontSize = 80.sp)
            Text(text = "Hours", fontSize = 20.sp)
            Text(text = timeDiff.value[1].toString(), fontSize = 80.sp)
            Text(text = "Minutes", fontSize = 20.sp)
        }
        Row {
            Text(text = "Time left")
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

fun calculateTimeDiff(
    timeState: MutableState<IntArray>,
    timeDiff: MutableState<IntArray>,
) {
    val currentHour = LocalTime.now().hour
    val currentMinute = LocalTime.now().minute

    val hoursLeft = timeState.value[0] - currentHour
    val minutesLeft = timeState.value[1] - currentMinute

    timeDiff.value[0] = hoursLeft
    timeDiff.value[1] = minutesLeft
}

@Composable
fun ShowTimePicker(timeState: MutableState<IntArray>) {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    val timePickerDialog = TimePickerDialog(
        mContext,
        { _, fHour: Int, fMinute: Int ->
            timeState.value = intArrayOf(fHour, fMinute)
        }, mHour, mMinute, true
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            timePickerDialog.show()
        }) {
            Text(text = "Choose Time", color = Color.White)
        }

        Text(text = "Time chosen: ${timeState.value[0]}:${timeState.value[1]}")
    }
}

@Composable
fun SimpleAlertDialog(incorrectTime: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = { incorrectTime.value = false },
        confirmButton = {
            TextButton(onClick = { incorrectTime.value = false })
            { Text(text = "OK") }
        },
        title = { Text(text = "Incorrect Time") },
        text = { Text(text = "Please select a time in the FUTURE!") }
    )
}