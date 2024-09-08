package org.mathieu.sandbox_d2_2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.mathieu.characters.CharactersScreen
import org.mathieu.data.dataModule
import org.mathieu.sandbox_d2_2026.ui.theme.SandboxD22026Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(
                dataModule
            )
        }

        setContent {
            SandboxD22026Theme {
                CharactersScreen()
            }
        }
    }

}
