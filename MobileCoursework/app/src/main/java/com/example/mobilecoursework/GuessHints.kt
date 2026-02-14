package com.example.mobilecoursework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class GuessHints : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val countdownTimer = intent.getBooleanExtra("Countdown", false)
            HintGuess(countdownTimer)
        }
    }
}

@Composable
fun HintGuess(countdownTimer: Boolean) {
    val flagList = FlagList.flagNameList
    var flags = FlagList.flagNameList
    var countryFlagIndex by rememberSaveable {
        mutableIntStateOf(flags.indices.random())
    }
    var countryFlag = flags[countryFlagIndex]

    var flagName by rememberSaveable {
        mutableStateOf(countryFlag.flagName)
    }
    var spaces by rememberSaveable {
        mutableStateOf("_".repeat(flagName.length))
    }
    var userInput by rememberSaveable {
        mutableStateOf("")
    }
    var msg by rememberSaveable {
        mutableStateOf("")
    }
    var msgColor by remember {
        mutableStateOf(Color.Unspecified)
    }
    var attempts by rememberSaveable {
        mutableStateOf(0)
    }
    var correctAns by rememberSaveable {
        mutableStateOf("")
    }
    var nextBtnVisible by rememberSaveable {
        mutableStateOf(false)
    }
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
                attempts++
                if (attempts < 3){
                    countdown = 10
                }else {
                    msg = "WRONG!"
                    msgColor = Color.Red
                    correctAns = "Correct answer: $flagName"
                    nextBtnVisible = true
                    timerRunning = false
                }

            }
        }
    }

    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item {
            Text(text = "---Guess Hints---", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        item {
            if(countdownTimer) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
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
            Image(painter = painterResource(id = countryFlag.flagImage),
                contentDescription = countryFlag.flagName,
                modifier = Modifier.size(260.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            if ( msg.isBlank()){
                Text(text = spaces)
            }else {
                Text(text = msg, color = msgColor)
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = userInput,
                onValueChange = {userInput = it},
                label = { Text(text = "Guess a character")}
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            if (nextBtnVisible){
                Button(onClick = {
                    correctAns = ""
                    countryFlagIndex = flags.indices.random()
                    countryFlag = flags[countryFlagIndex]
                    flagName = countryFlag.flagName
                    spaces = "_".repeat(flagName.length)
                    userInput = ""
                    msg = ""
                    msgColor = Color.Unspecified
                    attempts = 0
                    nextBtnVisible = false
                    countdown = 10
                    timerRunning = countdownTimer
                }) {
                    Text(text = "Next")
                }
            }else{
                Button(onClick = {
                    //countdown = 10
                    val char = userInput.lowercase().firstOrNull()
                    if (char != null && flagName.lowercase().contains(char)) {
                        spaces = spaces.toCharArray().mapIndexed { index, c ->
                            if (flagName.indices.contains(index) && flagName[index].toString().equals(char.toString(), ignoreCase = true))
                                char else c
                        }.joinToString("")
                        if (!spaces.contains("_")) {
                            msg = "CORRECT!"
                            msgColor = Color.Green
                            nextBtnVisible= true
                            timerRunning = false
                        }
                    } else {
                        attempts++
                        if (attempts >= 3) {
                            msg = "WRONG!"
                            msgColor = Color.Red
                            correctAns = "Correct answer: $flagName"
                            nextBtnVisible= true
                            timerRunning = false
                        }
                    }
                }) {
                    Text(text ="Submit")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = correctAns, color = Color.Blue)
        }



    }
}

//@Preview(showBackground = true)
//@Composable
//fun HintGuessPreview(){
//    HintGuess()
//}