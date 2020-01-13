package `in`.ceeq.define.data.convertor

import androidx.room.TypeConverter


object ListConverter {

    @TypeConverter
    fun toString(list: List<String>): String? {
        return if (list.isNotEmpty()) list.joinToString() else ""
    }

    @TypeConverter
    fun toList(string: String): List<String> {
        return if (string.isNotEmpty()) string.split(',') else emptyList()
    }
}
