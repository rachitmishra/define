package `in`.ceeq.define.data.source


import `in`.ceeq.define.data.entity.Definition
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DefinitionApi {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("translate")
    fun getDefinition(@Query("phrase") phrase: String,
                      @Query("from") from: String = "en",
                      @Query("dest") dest: String = "hi",
                      @Query("format") format: String = "json"):
            Single<Definition>
}
