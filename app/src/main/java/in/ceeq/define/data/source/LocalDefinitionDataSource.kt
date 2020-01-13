package `in`.ceeq.define.data.source

import `in`.ceeq.define.data.DefineDatabase
import `in`.ceeq.define.data.dao.DefinitionDao
import `in`.ceeq.define.data.entity.Definition
import android.content.Context
import androidx.annotation.NonNull

class LocalDefinitionDataSource(private val definitionDao: DefinitionDao) : DefinitionDataSource {

    companion object {

        private var INSTANCE: LocalDefinitionDataSource? = null

        fun getInstance(@NonNull context: Context): LocalDefinitionDataSource {
            if (INSTANCE == null) {
                val database = DefineDatabase.getInstance(context)
                INSTANCE = LocalDefinitionDataSource(database.userDao())
            }

            return INSTANCE as LocalDefinitionDataSource
        }
    }

    override fun getDefinition(phrase: String): Definition {
        return definitionDao.getDefinition(phrase)
    }

    fun insertOrUpdateDefinition(definition: Definition) {
        definitionDao.insertDefinition(definition)
    }

    fun deleteAllUsers() {
        definitionDao.deleteAllDefinitions()
    }
}
