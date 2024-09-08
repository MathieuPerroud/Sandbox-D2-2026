package org.mathieu.characters

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mathieu.domain.Character

@Composable
fun CharactersScreen(modifier: Modifier = Modifier) {

    Content()

}

@Composable
private fun Content(modifier: Modifier = Modifier) {

    var firstCharacter by remember {
        mutableStateOf<Character?>(null)
    }

    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            val characters = CharactersViewModel.charactersRepository.getAllCharacters()
            firstCharacter = characters.firstOrNull()
        }
    }

    Text(
        text = firstCharacter?.toString() ?: "non",
        modifier = modifier
    )
}

@Preview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    Content()
}
