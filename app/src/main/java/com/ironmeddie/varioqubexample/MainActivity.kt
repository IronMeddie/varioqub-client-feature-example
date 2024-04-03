package com.ironmeddie.varioqubexample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ironmeddie.varioqubexample.ui.theme.VarioqubExampleTheme
import com.yandex.varioqub.config.FetchError
import com.yandex.varioqub.config.OnFetchCompleteListener
import com.yandex.varioqub.config.Varioqub

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchAndActivate()
        setContent {
            VarioqubExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var flag by rememberSaveable { mutableStateOf("not loaded yet") }
    LazyColumn(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "flag is: $flag")
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { flag = Varioqub.getString(Constance.FLAG, "not loaded yet") }) {// получение флага
                Text(text = "getFlag")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VarioqubExampleTheme {
        MainScreen()
    }
}

private fun fetchAndActivate() {
    Varioqub.fetchConfig(object : OnFetchCompleteListener {
        override fun onSuccess() {
            Log.i("VARIOQUB", "FETCH SUCCESS")
            Varioqub.activateConfig()
        }
        override fun onError(message: String, error: FetchError) {
            Log.i("VARIOQUB", "FETCH ERROR: $message")
        }
    })

}