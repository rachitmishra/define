package `in`.ceeq.define.data

import `in`.ceeq.define.data.api.DefinitionApi
import `in`.ceeq.define.data.api.RandomWordApi
import `in`.ceeq.define.data.entity.Definition
import io.reactivex.Single
import retrofit2.Retrofit

object DefineRepository {

    private lateinit var mDefinitionApi: DefinitionApi
    private lateinit var mRandomWordApi: RandomWordApi

    fun create(retrofit: Retrofit) =
            this.apply {
                mDefinitionApi = DefinitionApi.create(retrofit)
                mRandomWordApi = RandomWordApi.create(retrofit)
            }

    fun getDefinition(phrase: String, dest: String = "en"): Single<Definition> =
            mDefinitionApi.getDefinition(phrase, dest = dest)

    fun getDefinition(): Single<String> =
            mRandomWordApi.getRandomWord()
}
