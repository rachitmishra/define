package `in`.ceeq.define.data.api


import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers

interface RandomWordApi {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("randomword/get.php")
    fun getRandomWord(): Single<String>

    companion object {
        fun create(retrofit: Retrofit): RandomWordApi = retrofit.create(RandomWordApi::class.java)
    }
}
