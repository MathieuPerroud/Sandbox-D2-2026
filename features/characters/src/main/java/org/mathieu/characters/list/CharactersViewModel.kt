package org.mathieu.characters.list

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mathieu.characters.list.CharactersContracts.*
import org.mathieu.domain.characters.Character
import org.mathieu.domain.characters.CharactersRepository

interface CharactersContracts {

    @Immutable
    data class State(
        val characters: List<Character> = emptyList(),
        val error: String? = null
    )


    sealed interface Action

    data class ClickedOnCharacter(val character: Character) : Action


    sealed interface Event
    data class NavigateToCharacterDetails(val character: Character) : Event

}


class CharactersViewModel : ViewModel(), KoinComponent {
    private val charactersRepository: CharactersRepository by inject()

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    val state: StateFlow<State>
        get() = _state


    private val _events = Channel<Event>(Channel.BUFFERED)
    val events: Flow<Event>
        get() = _events.receiveAsFlow()


    init {

        viewModelScope.launch(Dispatchers.IO) {

            charactersRepository.getCharactersFlow().collect { characters ->

                _state.update {
                    State(characters = characters)
                }

            }

        }

    }


    fun handleAction(action: Action) {
        when (action) {
            is ClickedOnCharacter -> viewModelScope.launch(Dispatchers.Main) {
                _events.send(
                    NavigateToCharacterDetails(action.character)
                )
            }
        }
    }

}










