package in.ceeq.define.network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface RandomWordService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("randomword/get.php")
    Call<String> getRandomWord();
}
