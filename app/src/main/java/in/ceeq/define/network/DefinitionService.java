package in.ceeq.define.network;


import in.ceeq.define.databinding.Definition;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface DefinitionService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("translate")
    Call<Definition> getDefinition(@Query("from") String from, @Query("dest") String dest,
                                   @Query("format") String format, @Query("phrase") String phrase);
}
