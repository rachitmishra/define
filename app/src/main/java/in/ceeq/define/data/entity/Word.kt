package `in`.ceeq.define.data.entity

import com.google.gson.annotations.SerializedName

data class Word(@SerializedName("id")
                val id: Int,
                @SerializedName("word")
                val word: String)
