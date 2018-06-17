package `in`.ceeq.define.data

import `in`.ceeq.define.data.entity.Definition
import `in`.ceeq.define.data.source.DefinitionApi
import `in`.ceeq.define.data.source.DefinitionDataSource
import `in`.ceeq.define.data.source.RandomWordApi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DefineRepository @Inject constructor(private val definitionApi: DefinitionApi,
                                           private val randomWordApi: RandomWordApi,
                                           private val datasource: DefinitionDataSource) {

    fun getDefinition(phrase: String, dest: String = "en"): Single<Definition> =
            definitionApi.getDefinition(phrase, dest = dest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getRandomWord(): Single<String> =
            randomWordApi.getRandomWord()
                    .map { it.word }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}
