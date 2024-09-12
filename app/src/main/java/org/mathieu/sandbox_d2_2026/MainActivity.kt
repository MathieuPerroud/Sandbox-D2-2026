package org.mathieu.sandbox_d2_2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.mathieu.characters.details.CharacterDetailsScreen
import org.mathieu.characters.list.CharactersScreen
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
            Content()
        }
    }

}

@Composable
fun Content() {
    SandboxD22026Theme {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "charactersList"
        ) {

            composable("charactersList") {

                CharactersScreen(
                    navController = navController
                )

            }


            composable(
                "characterDetails/{characterId}",
                arguments = listOf(navArgument("characterId") { type = NavType.IntType })
            ) { navBackstackEntry ->

                val characterId = navBackstackEntry.arguments?.getInt("characterId")

                if (characterId == null) {
                    navController.popBackStack()
                } else {
                    CharacterDetailsScreen(
                        navController = navController,
                        id = characterId
                    )

                }


            }

        }
    }
    }