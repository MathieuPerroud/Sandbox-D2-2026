package org.mathieu.characters.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.mathieu.characters.list.CharactersContracts.*
import org.mathieu.domain.characters.Character

/**
 *
 *
 */
@Composable
fun CharactersScreen(navController: NavHostController) {

    val viewModel = viewModel<CharactersViewModel>()
    val state by viewModel.state.collectAsState()


    LaunchedEffect(viewModel) {
        viewModel.events
            .onEach { event ->

                when (event) {
                    is NavigateToCharacterDetails ->
                        navController
                            .navigate("characterDetails/${event.character.id}")
                }

            }.collect()
    }


    Content(
        state = state,
        onAction = { action ->
            viewModel.handleAction(action)
        }
    )
}

@Composable
private fun Content(
    state: State,
    onAction: (Action) -> Unit
) = Column(
    modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray)
) {

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        text = "characters",
        textAlign = TextAlign.Center
    )

    LazyColumn {
        items(state.characters) { character ->

            CharacterCard(
                modifier = Modifier.clickable {
                    onAction(ClickedOnCharacter(character))
                },
                character = character
            )

        }
    }


}

@Composable
private fun CharacterCard(
    modifier: Modifier,
    character: Character
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.White)
        .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
) {


    SubcomposeAsyncImage(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape),
        model = character.pictureUrl,
        contentDescription = null
    )

    Text(
        modifier = Modifier,
        text = character.name
    )

}

@Preview
@Composable
private fun Preview() {
    Content(
        state = State(),
        onAction = {}
    )
}
