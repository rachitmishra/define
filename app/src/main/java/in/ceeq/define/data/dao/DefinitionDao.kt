package `in`.ceeq.define.data.dao

import `in`.ceeq.define.data.entity.Definition
import androidx.room.*

/**
 * @Dao
 *
 * @Insert(onConflict = OnConflictStrategy.REPLACE)
 *
 *  @Insert
 *
 *  @Update
 *
 *  @Delete
 *
 *  @Query("SELECT * FROM definition")
 *
 */
@Dao
interface DefinitionDao {

    @Query("SELECT * FROM definitions WHERE phrase = :phrase")
    fun getDefinition(phrase: String): Definition

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDefinition(definition: Definition)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDefinition(definitions: List<Definition>)

    @Query("DELETE FROM definitions")
    fun deleteAllDefinitions()

    @Transaction
    fun updateDefinitions(definitions: List<Definition>) {
        deleteAllDefinitions()
        insertDefinition(definitions)
    }
}
