

package com.example.mobilecoursework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilecoursework.FlagList
import kotlinx.coroutines.delay

class AdvanceLevel : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val countdownTimer = intent.getBooleanExtra("Countdown", false)
            Advanced(countdownTimer)
        }
    }
}

@Composable
fun Advanced(countdownTimer: Boolean){
    val context = LocalContext.current
    var flags by rememberSaveable {
        mutableStateOf(FlagList.flagNameList.shuffled().take(3))
    }
    var guesses by rememberSaveable {
        mutableStateOf(listOf("", "", ""))
    }
    var submitted by rememberSaveable {
        mutableStateOf(false)
    }
    var correctGuesses by rememberSaveable {
        mutableStateOf(listOf(false, false, false))
    }
    var allCorrect by rememberSaveable {
        mutableStateOf(false)
    }
    var attempts by rememberSaveable {
        mutableStateOf(0)
    }
    var showCorrectAns by rememberSaveable {
        mutableStateOf(false)
    }
    var score by rememberSaveable {
        mutableStateOf(0)
    }
    var countdown by rememberSaveable {
        mutableStateOf(10)
    }
    var timerRunning by rememberSaveable {
        mutableStateOf(countdownTimer)
    }

    fun submittedGuesses(){
        submitted = true
        attempts++
        correctGuesses = flags.zip(guesses).map { it.first.flagName.equals(it.second, ignoreCase = true) }
        val correctCount = correctGuesses.count { it }
        score = correctCount
        allCorrect = correctGuesses.all { it }
        if (attempts >= 3 && !allCorrect) {
            showCorrectAns = true
        }
    }

    if(timerRunning){
        LaunchedEffect(key1 = countdown) {
            if (countdown > 1) {
                delay(1000)
                countdown--
            } else {
                //attempts++
                submittedGuesses()
                if (attempts < 3){
                    countdown = 10
                }else {
                    showCorrectAns = true
                }

            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Text(text = "---Guess the Flags---", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            if(countdownTimer){
                Spacer(modifier = Modifier.height(6.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Left
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
            ScoreDisplay(score)
        }
        item {
            flags.forEachIndexed { index, flag ->
                FlagImage(
                    resourceId = flag.flagImage,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                if (!submitted) {
                    GuessTextField(
                        value = guesses[index],
                        onValueChange = { guesses = guesses.toMutableList().apply { set(index, it) } }
                    )
                } else {
                    val isCorrect = correctGuesses[index]
                    GuessTextField(
                        value = guesses[index],
                        onValueChange = { guesses = guesses.toMutableList().apply { set(index, it) } },
                        enabled = !isCorrect,
                        focusedContainerColor = if (isCorrect) Color.Green else Color.Red,
                        unfocusedContainerColor = if (isCorrect) Color.Green else Color.Red,
                        disabledContainerColor = if (isCorrect) Color.Green else Color.Red

                    )
                    if (!isCorrect && showCorrectAns) {
                        Text(text = flag.flagName, color = Color.Blue)
                    }
                }
            }
            if (!allCorrect && attempts < 3) {
                Button(
                    onClick = {
                        countdown = 10
                        submittedGuesses()
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Submit")
                }
            } else {
                if (allCorrect) {
                    Text("CORRECT!", color = Color.Green, modifier = Modifier.padding(top = 16.dp))
                } else {
                    Text("WRONG!", color = Color.Red, modifier = Modifier.padding(top = 16.dp))
                }
                Button(
                    onClick = {
                        // Restart the game with new flags
                        flags = FlagList.flagNameList.shuffled().take(3)
                        guesses = listOf("", "", "")
                        submitted = false
                        correctGuesses = listOf(false, false, false)
                        allCorrect = false
                        attempts = 0
                        showCorrectAns = false
                        score = 0
                        countdown = 10
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Next")
                }

            }
        }
    }

}

@Composable
fun ScoreDisplay(score: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text("Score: $score", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun FlagImage(resourceId: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = resourceId),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(150.dp)
    )
}

@Composable
fun GuessTextField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    focusedContainerColor: Color = Color.White,
    unfocusedContainerColor: Color = Color.White,
    disabledContainerColor: Color = Color.White
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(50.dp)
            .width(238.dp),
        enabled = enabled,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            focusedContainerColor = focusedContainerColor,
            unfocusedContainerColor = unfocusedContainerColor,
            disabledContainerColor = disabledContainerColor

        )
    )
}

//@Preview(showBackground = true)
//@Composable
//fun AdvancedPreview(){
//    Advanced()
//}
