package `in`.ceeq.define.data

import `in`.ceeq.define.data.convertor.ListConverter
import `in`.ceeq.define.data.dao.DefinitionDao
import `in`.ceeq.define.data.entity.Definition
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.content.Context


@Database(entities = [Definition::class], version = 2)
@TypeConverters(ListConverter::class)
abstract class DefineDatabase : RoomDatabase() {

    abstract fun userDao(): DefinitionDao

    companion object {

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Since we didn't alter the table, there's nothing else to do here.
            }
        }

        private var INSTANCE: DefineDatabase? = null

        fun getInstance(context: Context): DefineDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        DefineDatabase::class.java,
                        "Define.db")
                        .addMigrations(MIGRATION_1_2)
                        .addCallback(object : Callback() {

                        })
                        .build()
            }
            return INSTANCE as DefineDatabase
        }
    }
}
