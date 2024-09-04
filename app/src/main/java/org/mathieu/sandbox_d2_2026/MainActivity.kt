package org.mathieu.sandbox_d2_2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.mathieu.sandbox_d2_2026.data.CharacterDto
import org.mathieu.sandbox_d2_2026.data.GetCharactersDto
import org.mathieu.sandbox_d2_2026.ui.theme.SandboxD22026Theme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object RetrofitProvider {
    //https://rickandmortyapi.com/documentation/#rest
    private val RMAPI_URL = "https://rickandmortyapi.com/api/"

    private fun provideHttpClient(): OkHttpClient = OkHttpClient()

    private val gson = GsonBuilder()
        .serializeNulls() // Configure Gson to include null values
        .create()

    private fun buildRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(RMAPI_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val retrofit = buildRetrofit(provideHttpClient())
}

object ServiceProvider {
    val charactersService: CharactersService =
        RetrofitProvider.retrofit.create(CharactersService::class.java)
}

interface CharactersService {
    @GET("character")
    suspend fun getCharacters(): GetCharactersDto
}





class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SandboxD22026Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    var firstCharacter by remember {
        mutableStateOf<CharacterDto?>(null)
    }

    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            val charactersDto = ServiceProvider.charactersService.getCharacters()
            firstCharacter = charactersDto.results.firstOrNull()
        }
    }

    Text(
        text = firstCharacter?.toString() ?: "non",
        modifier = modifier
    )
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SandboxD22026Theme {
        Greeting("Android")
    }
}