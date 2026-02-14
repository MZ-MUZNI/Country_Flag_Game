package com.example.mobilecoursework

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Home()
        }
    }
}

@Composable
fun Home(){
    val context= LocalContext.current
    var switchState by rememberSaveable {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.abcdworldmapbgimg),
            contentDescription = "BgImg",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )

    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        Text(
            text = "-COUNTRY FLAG GAME-",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = Color.Cyan,
        )
    }

    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        item {
            //Text(text = "Country Flag Game", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Absolute.Right
            ){
                Text(text = "Countdown \nSwitch", color = Color.Cyan, fontSize = 16.sp)
                Switch(
                    checked = switchState,
                    onCheckedChange = { switchState = it },
                    modifier = Modifier.padding(bottom = 20.dp)
                )

            }
        }

        item {
            Button(onClick = {
                val iGuessTheCountry= Intent(context, GuessTheCountry::class.java).apply { putExtra("Countdown", switchState) }
                context.startActivity(iGuessTheCountry)
            }, modifier = Modifier.width(258.dp)) {
                Text("Guess the Country", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Button(onClick = {
                val iGuessHints= Intent(context, GuessHints::class.java).apply { putExtra("Countdown", switchState) }
                context.startActivity(iGuessHints)
            }, modifier = Modifier.width(258.dp)) {
                Text("Guess Hints", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Button(onClick = {
                val iGuessTheFlag= Intent(context, GuessTheFlag::class.java).apply { putExtra("Countdown", switchState) }
                context.startActivity(iGuessTheFlag)
            }, modifier = Modifier.width(258.dp)) {
                Text("Guess the Flag", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Button(onClick = {
                val iAdvancedLevel= Intent(context, AdvanceLevel::class.java).apply { putExtra("Countdown", switchState) }
                context.startActivity(iAdvancedLevel)
            }, modifier = Modifier.width(258.dp)) {
                Text("Advanced Level", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }


    }
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){
        Text(
            text = "-COUNTRY FLAG GAME - Designed by Muzi@24-",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            color = Color.Cyan,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    Home()
}
