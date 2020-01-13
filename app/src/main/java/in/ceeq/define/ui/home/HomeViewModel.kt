package `in`.ceeq.define.ui.home


import `in`.ceeq.define.R
import `in`.ceeq.define.data.DefineRepository
import `in`.ceeq.define.data.entity.Definition
import `in`.ceeq.define.utils.PreferenceUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.res.Resources
import androidx.databinding.ObservableField
import android.text.Html
import io.reactivex.Single
import javax.inject.Inject


class HomeViewModel @Inject constructor(private val resources: Resources,
                                        private val preferenceUtils: PreferenceUtils,
                                        private val defineRepository: DefineRepository) : ViewModel() {

    val suffix = arrayOf("ing", "ed", "led", "ling", "tted", "ted", "less", "ly", "wise")

    var isProgressViewVisible = ObservableField<Boolean>(false)

    var hasNoResults = ObservableField<Boolean>(false)

    var forceUpdate = ObservableField<Boolean>(false)

    var phrase = ObservableField<String>("")

    var definition = ObservableField<String>()

    val alternateDefinition = ObservableField<String>()

    private val _suggestedPhrases = MutableLiveData<List<String>>()
    val suggestedPhrases: LiveData<List<String>>
        get() = _suggestedPhrases

    val detectedWords = ObservableField<String>()

    private val dest: String
        get() {
            val selectedLanguagePosition = preferenceUtils.selectedLanguagePosition
            return resources.getStringArray(R.array.language_array)[selectedLanguagePosition]
                    .substring(0, 3)
                    .toLowerCase()
        }

    fun loadDefinition(phrase: String?): Single<Definition> {
        if (phrase == null || phrase.isEmpty()) {
            return defineRepository.getRandomWord()
                    .flatMap { defineRepository.getDefinition(it) }
                    .doOnSubscribe { isProgressViewVisible.set(true) }
                    .doOnSuccess { isProgressViewVisible.set(false) }
                    .doOnError { isProgressViewVisible.set(false) }

        }

        return defineRepository.getDefinition(phrase.toLowerCase().trim(), dest)
                .doOnSubscribe { isProgressViewVisible.set(true) }
                .doOnSuccess { isProgressViewVisible.set(false) }
                .doOnError { isProgressViewVisible.set(false) }
    }

    fun handleApiSuccess(result: Definition) {

        val tucs = result.tucs ?: return

        phrase.set(result.phrase)

        val definitionStr = tucs.dropWhile { it.phrase != null }
                .map { it.phrase?.text }
                .filter { it != null }
                .take(Definition.MAX_DEFINITIONS).joinToString()

        val altDefinitionStr =
                tucs.takeWhile { it.meanings != null }
                        .flatMap { it.meanings!! }
                        .map { it.text }
                        .filter { it != null }
                        .take(Definition.MAX_DEFINITIONS).joinToString()


        when {
            definitionStr.isNotEmpty() -> definition.set(Html.fromHtml(definitionStr).toString())
            definitionStr.isNotEmpty() && altDefinitionStr.isNotEmpty() -> alternateDefinition.set(
                    Html.fromHtml(altDefinitionStr).toString())
            definitionStr.isEmpty() && altDefinitionStr.isNotEmpty() -> definition.set(
                    Html.fromHtml(altDefinitionStr).toString())
            definitionStr.isEmpty() && altDefinitionStr.isEmpty() -> hasNoResults.set(true)
        }

        _suggestedPhrases.value = listOf(
                result.phrase.apply {
                    when {
                        endsWith("ing") -> replace("ing", "")
                        endsWith("ed") -> replace("ed", "e")
                        endsWith("led") -> replace("led", "")
                        endsWith("ling") -> replace("ling", "")
                        endsWith("tted") -> replace("tted", "t")
                        endsWith("ted") -> replace("ted", "")
                        endsWith("less") -> replace("less", "")
                        endsWith("ly") -> replace("wise", "")
                        endsWith("wise") -> replace("less", "")
                        startsWith("pre") -> replace("pre", "")
                        startsWith("extra") -> replace("extra", "")
                        startsWith("over") -> replace("over", "")
                        startsWith("un") -> replace("un", "")
                        startsWith("im") -> replace("im", "")
                        startsWith("anti") -> replace("anti", "")
                        startsWith("auto") -> replace("auto", "")
                        else -> phrase
                    }
                })
    }

    fun onWordsDetected(listOfWords: List<String>) {
        detectedWords.set(listOfWords.joinToString())
    }
}
