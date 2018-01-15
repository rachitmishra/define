package `in`.ceeq.define.data.entity

import android.databinding.BaseObservable
import android.databinding.Bindable
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
                      val dest: String = "hi") : Parcelable, BaseObservable() {


    val definition: String
        @Bindable
        get() {
            if (tucs.isEmpty()) {
                return ""
            }

            @Suppress("SENSELESS_COMPARISON")
            val definitions = tucs
                    .dropWhile { it.phrase == null }
                    .map { it.phrase?.text }
                    .filter { it != null }
                    .take(MAX_DEFINITIONS)

            if (definitions.isEmpty()) {
                return ""
            }

            return definitions.joinToString()
        }

    val alternateDefinition: String
        @Bindable
        get() {
            if (tucs.isEmpty()) {
                return ""
            }

            @Suppress("SENSELESS_COMPARISON")
            val alternateDefinition = tucs
                    .dropWhile { it.meanings == null }
                    .flatMap { it.meanings }
                    .map { it.text }
                    .filter { it != null }
                    .take(MAX_DEFINITIONS)

            if (alternateDefinition.isEmpty()) {
                return ""
            }

            return alternateDefinition.joinToString()
        }

    val suggestedPhrase: String
        @Bindable
        get() {
            return when {
                phrase.endsWith("ing") -> phrase.replace("ing", "")
                phrase.endsWith("ed") -> phrase.replace("ed", "e")
                phrase.endsWith("led") -> phrase.replace("led", "")
                phrase.endsWith("ling") -> phrase.replace("ling", "")
                phrase.endsWith("tted") -> phrase.replace("tted", "t")
                phrase.endsWith("ted") -> phrase.replace("ted", "")
                phrase.endsWith("less") -> phrase.replace("less", "")
                phrase.endsWith("ly") -> phrase.replace("wise", "")
                phrase.endsWith("wise") -> phrase.replace("less", "")
                phrase.startsWith("pre") -> phrase.replace("pre", "")
                phrase.startsWith("extra") -> phrase.replace("extra", "")
                phrase.startsWith("over") -> phrase.replace("over", "")
                phrase.startsWith("un") -> phrase.replace("un", "")
                phrase.startsWith("im") -> phrase.replace("im", "")
                phrase.startsWith("anti") -> phrase.replace("anti", "")
                phrase.startsWith("auto") -> phrase.replace("auto", "")
                else -> phrase
            }
        }

    companion object {
        val MAX_DEFINITIONS = 10
    }

    val suffix = arrayOf("ing", "ed", "led", "ling", "tted", "ted", "less", "ly", "wise")
}
