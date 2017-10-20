package `in`.ceeq.define.data

import `in`.ceeq.define.data.api.DefinitionApi
import `in`.ceeq.define.data.api.RandomWordApi
import `in`.ceeq.define.data.entity.Definition
import io.reactivex.Single
import javax.inject.Inject

class DefineRepository @Inject constructor(private val mDefinitionApi: DefinitionApi,
                                           private val mRandomWordApi: RandomWordApi) {

    fun getDefinition(phrase: String, dest: String = "en"): Single<Definition> =
            mDefinitionApi.getDefinition(phrase, dest = dest)

    fun getDefinition(): Single<String> =
            mRandomWordApi.getRandomWord()
}
