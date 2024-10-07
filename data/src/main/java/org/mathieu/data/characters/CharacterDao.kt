package org.mathieu.data.characters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("Select * From CharacterEntity")
    fun getCharacters(): Flow<List<CharacterEntity>>

    @Query("select * from CharacterEntity where id = :id")
    suspend fun getCharacter(id: Int): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<CharacterEntity>)

}