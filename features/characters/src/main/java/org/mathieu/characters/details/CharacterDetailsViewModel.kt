package org.mathieu.characters.details

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mathieu.characters.details.CharacterDetailsContracts.*
import org.mathieu.domain.CharactersRepository

interface CharacterDetailsContracts {

    @Immutable
    data class State(
        val isLoading: Boolean = true,
        val avatarUrl: String = "",
        val name: String = "",
        val error: String? = null
    )

}

class CharacterDetailsViewModel : ViewModel(), KoinComponent {

    private val charactersRepository: CharactersRepository by inject()

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    val state: StateFlow<State>
        get() = _state

    fun init(characterId: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            val character = charactersRepository.getCharacterById(id = characterId)

            _state.update {
                it.copy(
                    avatarUrl = character.pictureUrl,
                    name = character.name,
                    error = null,
                    isLoading = false
                )
            }

        }

    }

}
