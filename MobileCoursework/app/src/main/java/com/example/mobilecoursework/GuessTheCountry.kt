package com.example.mobilecoursework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


class GuessTheCountry : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val countdownTimer = intent.getBooleanExtra("Countdown", false)
            CountryGuess(countdownTimer)
        }
    }
}

@Composable
fun CountryGuess(countdownTimer: Boolean){
    var flagSelect by rememberSaveable {
        mutableStateOf("")
    }
    var flags = FlagList.flagNameList
    var countryFlagIndex by rememberSaveable {
        mutableIntStateOf(flags.indices.random())
    }
    var countryFlag = flags[countryFlagIndex]

    var showAns by rememberSaveable {
        mutableStateOf(false)
    }
    var submitAns by rememberSaveable {
        mutableStateOf("")
    }
    val correct = "CORRECT!"
    val wrong = "WRONG!"
    var countdown by rememberSaveable {
        mutableStateOf(10)
    }
    var timerRunning by rememberSaveable {
        mutableStateOf(countdownTimer)
    }

    if(timerRunning){
        LaunchedEffect(key1 = countdown) {
            if (countdown > 1) {
                delay(1000)
                countdown--
            } else {
                showAns = true
                submitAns = if (countryFlag.flagName == flagSelect) {
                    correct
                } else {
                    wrong
                }
                timerRunning= false
            }
        }
    }



    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item { Text(text = "---Guess the Country---", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            if(countdownTimer){
            Spacer(modifier = Modifier.height(6.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Right
            ){
                Text(
                    text = "Countdown : ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = countdown.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            }
        }

        item {
            Image(
                painter = painterResource(id = countryFlag.flagImage),
                contentDescription = countryFlag.flagName,
                modifier = Modifier.size(260.dp)
            )
        }
        item {
            FlagList.flagNameList.forEach {
                    flags ->
                Text(text = flags.flagName, modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        if (!showAns) {
                            flagSelect = flags.flagName
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            if (!showAns) {
                Button(onClick = {
                    submitAns = if (countryFlag.flagName == flagSelect) {
                        correct
                    } else {
                        wrong
                    }
                    showAns = true
                    timerRunning = false // Stop the timer when answer is submitted
                }) {
                    Text("Submit")
                }
            } else{
                Text(text = submitAns,
                    color = if (submitAns==correct)
                        Color.Green
                    else
                        Color.Red
                )
                if (submitAns==wrong){
                    Text(text = "Correct Answer : ${countryFlag.flagName}",
                        color = Color.Blue
                        )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    countdown = 10
                    timerRunning = countdownTimer // Reset the timer
                    countryFlagIndex = flags.indices.random()
                    countryFlag = flags[countryFlagIndex]
                    flagSelect = ""
                    submitAns = ""
                    showAns = false
                }) {
                    Text("Next")
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun CountryGuessPreview(){
//    CountryGuess()
//}
