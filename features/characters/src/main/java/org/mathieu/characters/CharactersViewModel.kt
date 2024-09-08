package org.mathieu.characters

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.mathieu.domain.CharactersRepository

object CharactersViewModel: KoinComponent {
    val charactersRepository: CharactersRepository by inject()

}
