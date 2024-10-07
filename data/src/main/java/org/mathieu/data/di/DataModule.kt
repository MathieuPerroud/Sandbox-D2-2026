package org.mathieu.data.di

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import org.mathieu.data.characters.local.CharacterDao
import org.mathieu.data.characters.CharacterRepositoryImpl
import org.mathieu.data.characters.remote.CharactersService
import org.mathieu.data.sources.SandboxDatabase
import org.mathieu.domain.characters.CharactersRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//https://rickandmortyapi.com/documentation/#rest
private const val RMAPI_URL = "https://rickandmortyapi.com/api/"

private fun provideHttpClient(): OkHttpClient = OkHttpClient()

private fun provideDataBase(application: Application): SandboxDatabase =
    Room.databaseBuilder(
        application,
        SandboxDatabase::class.java,
        "sandbox"
    ).
    fallbackToDestructiveMigration().build()

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

    single {
        provideDataBase(get())
    }

    single<CharacterDao> {
        get<SandboxDatabase>().charactersDao()
    }

    single<CharactersService> {
        retrofit.create(CharactersService::class.java)
    }

    single<CharactersRepository> {

        val db: SandboxDatabase = get()

        CharacterRepositoryImpl(
            context = get(),
            charactersService = get(),
            charactersDao = get()
        )
    }

}
