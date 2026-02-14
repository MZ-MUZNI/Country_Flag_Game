package com.example.mobilecoursework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random



class GuessTheFlag : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val countdownTimer = intent.getBooleanExtra("Countdown", false)
            FlagGuess(countdownTimer)
        }
    }
}

@Composable
fun FlagGuess(countdownTimer: Boolean) {

    var img1 by rememberSaveable {
        mutableIntStateOf(0)
    }
    var img2 by rememberSaveable {
        mutableIntStateOf(0)
    }
    var img3 by rememberSaveable {
        mutableIntStateOf(0)
    }
    var flagName by rememberSaveable {
        mutableStateOf("")
    }
    var imgClick by rememberSaveable {
        mutableStateOf(true)
    }
    var answer by rememberSaveable {
        mutableStateOf("")
    }
    val flagChoice by rememberSaveable {
        mutableIntStateOf(0)
    }
    var nextButtonClicked by rememberSaveable {
        mutableStateOf(false)
    }
    var countdown by rememberSaveable {
        mutableStateOf(10)
    }
    var timerRunning by rememberSaveable {
        mutableStateOf(countdownTimer)
    }

//    if(timerRunning){
//        LaunchedEffect(key1 = countdown) {
//            if (countdown > 1) {
//                delay(1000)
//                countdown--
//            } else {
//                showAns = true
//                submitAns = if (countryFlag.flagName == flagSelect) {
//                    correct
//                } else {
//                    wrong
//                }
//                timerRunning= false
//            }
//        }
//    }

    if (timerRunning) {
        LaunchedEffect(key1 = countdown) {
            if (countdown > 1) {
                delay(1000)
                countdown--
            } else {
                if (answer.isEmpty()) {
                    answer = "WRONG!"
                }
                timerRunning = false
            }
        }
    }

    if (imgClick) {
        val newImgs = chooseNewImgs()
        img1 = newImgs[0].flagImage
        img2 = newImgs[1].flagImage
        img3 = newImgs[2].flagImage
        flagName= newImgs[flagChoice].flagName
        imgClick = false
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(text = "---Guess the Flag---", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
            Row {
                Image(painterResource(id = img1),
                    contentDescription = "img1",
                    modifier = Modifier
                        .clickable {
                            if (!nextButtonClicked) {
                                timerRunning = false
                                nextButtonClicked = true
                                if (flagChoice == 0) {
                                    answer = "CORRECT!"
                                } else
                                    answer = "WRONG!"
                            }

                        }
                        .size(120.dp)
                        .padding(8.dp)
                )
                Image(painterResource(id = img2),
                    contentDescription = "img2",
                    modifier = Modifier
                        .clickable {
                            if (!nextButtonClicked) {
                                timerRunning = false
                                nextButtonClicked = true
                                if (flagChoice == 1) {
                                    answer = "CORRECT!"
                                } else
                                    answer = "WRONG!"
                            }
                        }
                        .size(120.dp)
                        .padding(8.dp)
                )
                Image(painterResource(id = img3),
                    contentDescription = "img3",
                    modifier = Modifier
                        .clickable {
                            if (!nextButtonClicked) {
                                timerRunning = false
                                nextButtonClicked = true
                                if (flagChoice == 2) {
                                    answer = "CORRECT!"
                                } else
                                    answer = "WRONG!"
                            }
                        }
                        .size(120.dp)
                        .padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }


        item {
            Text(
                text = flagName, modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Text(
                text = answer, modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = if (answer == "CORRECT!"){
                    Color.Green
                } else {
                    Color.Red
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Button(modifier = Modifier.padding(40.dp),
                onClick = {
                    nextButtonClicked = false
                    imgClick = true
                    answer = ""
                    countdown = 10
                    timerRunning = countdownTimer
                })
            {
                Text("Next")
            }
        }




    }
}

fun chooseNewImgs(): List<Flags> {
    var drawables_list = mutableListOf<Flags>()
    val flags = FlagList.flagNameList
    var index1 = Random.nextInt(flags.size)
    var flag1 = flags[index1]
    var index2 = Random.nextInt(flags.size)
    var flag2 = flags[index2]
    while (flag1 == flag2) {
        index2 = Random.nextInt(flags.size)
        flag2 = flags[index2]
    }
    var index3 = Random.nextInt(flags.size)
    var flag3 = flags[index3]
    while (flag1 == flag3 || flag2 == flag3) {
        index3 = Random.nextInt(flags.size)
        flag3 = flags[index3]
    }
    drawables_list.add(flag1)
    drawables_list.add(flag2)
    drawables_list.add(flag3)
    return drawables_list
}





//@Preview(showBackground = true)
//@Composable
//fun FlagGuessPreview(){
//    FlagGuess()
//}