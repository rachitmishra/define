package `in`.ceeq.define.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Definition(@SerializedName("result")
                      val result: String = "",
                      @SerializedName("tuc")
                      val tucs: ArrayList<Tuc> = arrayListOf(),
                      @SerializedName("phrase")
                      var phrase: String = "love",
                      @SerializedName("from")
                      val from: String = "en",
                      @SerializedName("dest")
                      val dest: String = "hi") : Parcelable {

    fun getDefinition(): String {
        if (tucs.isEmpty()) {
            return ""
        }

        val definitions = tucs.map { it.phrase.text }.take(MAX_DEFINITIONS)

        if (definitions.isNotEmpty()) {
            return ""
        }

        return definitions.joinToString()
    }

    fun getAlternateDefinition(): String {
        if (tucs.isEmpty()) {
            return ""
        }

        val alternateDefinition = tucs.filter { it.meanings.isNotEmpty() }.flatMap { it.meanings }.map { it.text }[0]

        if (alternateDefinition.isEmpty()) {
            return ""
        }

        return alternateDefinition
    }

    fun getSuggestedPhrase(): String {
        return when {
            phrase.endsWith("ing") -> phrase.replace("ing", "")
            phrase.endsWith("ed") -> phrase.replace("ed", "e")
            phrase.endsWith("led") -> phrase.replace("led", "")
            phrase.endsWith("ling") -> phrase.replace("ling", "")
            phrase.endsWith("tted") -> phrase.replace("tted", "t")
            phrase.endsWith("ted") -> phrase.replace("ted", "")
            else -> phrase
        }
    }

    companion object {
        val MAX_DEFINITIONS = 10
    }
}
