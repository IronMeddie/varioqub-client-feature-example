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
import androidx.compose.material3.TextField
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
    var textFieldValue by rememberSaveable { mutableStateOf("") }
    var clientFeature by rememberSaveable { mutableStateOf("false") }
    LazyColumn(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "flag is: $flag")
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "client feature is: $clientFeature")
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { flag = Varioqub.getString(Constance.FLAG) }) {// получение флага
                Text(text = "getFlag")
            }
        }
        item {
            Spacer(modifier = Modifier.height(44.dp))
            TextField(value = textFieldValue, onValueChange = { textFieldValue = it } )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    Varioqub.putClientFeature(Constance.IS_TEST, textFieldValue)  // присвоение нового значения для клиентского параметра
                    clientFeature = textFieldValue
                    textFieldValue = ""
            },
                enabled = textFieldValue.isNotBlank()
            ) {
                Text(text = "set new value for client feature")
            }
        }
        item {
            Button(
                onClick = {
                    fetchAndActivate()
                // Значение флага по условию клиентских параметров не поменяется
                // пока не было успешного fetch и activatу
                // тут может возникнуть проблема из-за таймаута. Можно уменьшить его при инициализации методом .withThrottleInterval()
                }
            ) {
                Text(text = "fetch and activate")
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