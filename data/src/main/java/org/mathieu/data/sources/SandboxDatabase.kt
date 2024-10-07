package org.mathieu.data.sources

import androidx.room.Database
import androidx.room.RoomDatabase
import org.mathieu.data.characters.CharacterDao
import org.mathieu.data.characters.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1)
abstract class SandboxDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharacterDao
}