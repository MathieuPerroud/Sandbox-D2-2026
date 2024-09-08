package org.mathieu.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import org.mathieu.domain.CharactersRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//https://rickandmortyapi.com/documentation/#rest
private const val RMAPI_URL = "https://rickandmortyapi.com/api/"

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

private val retrofit = buildRetrofit(provideHttpClient())

val dataModule = module {

    single<CharactersService> {
        retrofit.create(CharactersService::class.java)
    }

    single<CharactersRepository> {
        CharacterRepositoryImpl(
            get<CharactersService>()
        )
    }

}
