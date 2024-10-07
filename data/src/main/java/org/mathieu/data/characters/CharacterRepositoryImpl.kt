package org.mathieu.data.characters

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mathieu.data.characters.local.CharacterDao
import org.mathieu.data.characters.local.CharacterEntity
import org.mathieu.data.characters.local.toModel
import org.mathieu.data.characters.remote.CharacterDto
import org.mathieu.data.characters.remote.CharactersService
import org.mathieu.data.characters.remote.toEntity
import org.mathieu.domain.characters.CannotProvideUpdatedCharacters
import org.mathieu.domain.characters.Character
import org.mathieu.domain.characters.CharactersAndVersion
import org.mathieu.domain.characters.CharactersNotFound
import org.mathieu.domain.characters.CharactersRepository
import org.mathieu.domain.characters.Version
import java.time.Instant
import java.util.Date

private const val CHARACTER_PREFS = "character_repository_preferences"
private val lastFetchDateKey = longPreferencesKey("last_date_fetch")
private val nextPage = intPreferencesKey("next_characters_page_to_load")

private val Context.dataStore by preferencesDataStore(
    name = CHARACTER_PREFS
)

class CharacterRepositoryImpl(
    private val context: Context,
    private val charactersService: CharactersService,
    private val charactersDao: CharacterDao
) : CharactersRepository {



    override suspend fun getCharactersFlow(): Flow<List<Character>> =
        charactersDao
            .getCharacters()
            .mapElement(transform = CharacterEntity::toModel)
            .also { if (it.first().isEmpty()) fetchNext() }



    /**
     * Fetches the next batch of characters and saves them to local storage.
     *
     * This function works as follows:
     * 1. Reads the next page number from the data store.
     * 2. If there's a valid next page (i.e., page is not -1), it fetches characters from the API for that page.
     * 3. Extracts the next page number from the API response and updates the data store with it.
     * 4. Transforms the fetched character data into their corresponding entity.
     * 5. Saves the transformed realm objects to the local database.
     *
     * Note: If the `next` attribute from the API response is null or missing, the page number is set to -1, indicating there's no more data to fetch.
     */
    private suspend fun fetchNext() {

        val page = context.dataStore.data.map { prefs -> prefs[nextPage] }.first()

        //-1 just means that we have load every pages (next was null).
        // Otherwise it will return the page or null.
        if (page != -1) {

            fetchCharacters(page)

            val response = charactersService.getCharacters(page)

            // page number or -1 if last page reached.
            val nextPageToLoad = response.info.next?.split("?page=")?.last()?.toInt() ?: -1

            context.dataStore.edit { prefs -> prefs[nextPage] = nextPageToLoad }

            val objects = response.results.map(transform = CharacterDto::toEntity)

            charactersDao.insertAll(objects)

            context.dataStore.edit { prefs ->
                prefs[lastFetchDateKey] = Instant.now().epochSecond
            }

        }

    }


    override suspend fun loadMore() = fetchNext()

    /**
     * Retrieves the character with the specified ID.
     *
     * The function follows these steps:
     * 1. Tries to fetch the character from the local storage.
     * 2. If not found locally, it fetches the character from the API.
     * 3. Upon successful API retrieval, it saves the character to local storage.
     * 4. If the character is still not found, it throws an exception.
     *
     * @param id The unique identifier of the character to retrieve.
     * @return The [Character] object representing the character details.
     * @throws Exception If the character cannot be found both locally and via the API.
     */
    override suspend fun getCharacterById(id: Int): Character =
        charactersDao.getCharacter(id)?.toModel()
            ?: charactersService.getCharacter(id = id)?.let { dto ->
                val entity = dto.toEntity()

                charactersDao.insert(entity)

                entity.toModel()
            }
            ?: throw Exception("Character not found.")



    private suspend fun fetchCharacters(page: Int? = null): List<CharacterEntity> {

        val pager = charactersService.getCharacters(page)

        val entities = pager.results.map(CharacterDto::toEntity)

        charactersDao.insertAll(entities)

        context.dataStore.edit { prefs ->
            prefs[lastFetchDateKey] = Instant.now().epochSecond
        }

        return entities
    }



    override suspend fun getVersionedCharactersFlow(): Flow<CharactersAndVersion> = flow {

        val charactersEntities = charactersDao.getCharacters().first()

        val lastDate = context.dataStore.data.map { prefs -> prefs[lastFetchDateKey] }.first()

        if (lastDate != null && charactersEntities.isNotEmpty())
            emit(
                CharactersAndVersion(
                    list = charactersEntities.map { it.toModel() },
                    version = Version.Old(
                        lastDateUpdate = Date.from(
                            Instant.ofEpochSecond(lastDate)
                        )
                    )
                )
            )


        try {

            val newCharacters = fetchCharacters()

            emit(
                CharactersAndVersion(
                    list = newCharacters.map(CharacterEntity::toModel),
                    version = Version.Latest
                )
            )

        } catch (ex: Exception) {

             if (charactersEntities.isEmpty())
                 throw CharactersNotFound(ex)

            throw CannotProvideUpdatedCharacters(ex)

        }

    }

}

inline fun <T, R> Flow<List<T>>.mapElement(crossinline transform: suspend (value: T) -> R): Flow<List<R>> =
    this.map { list ->
        list.map { element -> transform(element) }
    }