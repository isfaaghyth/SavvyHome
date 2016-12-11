package developer.xebia.ismail.savvyhome;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by arrival on 12/6/16.
 */

public interface APIService {
    @GET("/channels/199274/fields/1/last")
    Call<ResponseBody> getLastFeeds();

    @GET("/update")
    Call<ResponseBody> postStatus(@Query("api_key") String api_key,
                                  @Query("field1") String field1);
}
