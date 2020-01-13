package `in`.ceeq.define.data.source


import `in`.ceeq.define.BuildConfig
import `in`.ceeq.define.data.entity.Word
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers

interface RandomWordApi {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("words.json/wordOfTheDay?api_key=${BuildConfig.API_KEY}")
    fun getRandomWord(): Single<Word>

    companion object {
        fun create(retrofit: Retrofit): RandomWordApi = retrofit.create(RandomWordApi::class.java)
    }
}
