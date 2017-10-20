package `in`.ceeq.define.ui.home


import `in`.ceeq.define.BR
import `in`.ceeq.define.R
import `in`.ceeq.define.data.DefineRepository
import `in`.ceeq.define.data.entity.Definition
import `in`.ceeq.define.utils.LogUtils
import `in`.ceeq.define.utils.PreferenceUtils
import android.content.res.Resources
import android.databinding.BaseObservable
import android.databinding.Bindable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class HomeViewModel @Inject constructor(private val resources: Resources,
                                        private val preferenceUtils: PreferenceUtils,
                                        private val defineRepository: DefineRepository) : BaseObservable() {

    @get:Bindable
    var isProgressViewVisible: Boolean = false
        private set(progressViewVisible) {
            field = progressViewVisible
            notifyPropertyChanged(BR.progressViewVisible)
        }

    @get:Bindable
    var isNoResults: Boolean = false
        set(noResults) {
            field = noResults
            notifyPropertyChanged(BR.noResults)
        }

    @get:Bindable
    var forceUpdate: Boolean = false
        set(forceUpdate) {
            field = forceUpdate
            notifyPropertyChanged(BR.forceUpdate)
        }

    @get:Bindable
    var phrase: String = ""
        set(phrase) {
            field = phrase
            notifyPropertyChanged(BR.phrase)
        }

    private val dest: String
        get() {
            val selectedLanguagePosition = preferenceUtils.selectedLanguagePosition
            return resources.getStringArray(R.array.language_array)[selectedLanguagePosition].substring(0, 3).toLowerCase()
        }

    var definition: Definition = Definition()
        @Bindable
        get() = field
        set(value) {
            notifyPropertyChanged(BR.definition)
        }


    private fun loadDefinition(dest: String = "") {
        defineRepository.
                getDefinition(definition.phrase.toLowerCase().trim(), dest)
                .doOnSubscribe { isProgressViewVisible = true }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { isProgressViewVisible = false }
                .subscribe({
                    definition = it
                }, {
                })
    }

    fun loadDefinition() {
        if (phrase.isNotEmpty()) {
            loadDefinition(dest)
            return
        }

        defineRepository.
                getDefinition()
                .flatMap { defineRepository.getDefinition(it) }
                .doOnSubscribe { isProgressViewVisible = true }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { isProgressViewVisible = false }
                .subscribe({
                    definition = it
                }, {
                    LogUtils.log("Failed to get definition!")
                })
    }

    fun loadSuggestedPhrase() {
        loadDefinition(definition.getSuggestedPhrase())
    }

}

