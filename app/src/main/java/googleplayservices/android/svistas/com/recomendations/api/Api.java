package googleplayservices.android.svistas.com.recomendations.api;


import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by svistas on 14/12/16.
 */

public interface Api {

    @GET("/listings/active")
    void activeListings(@Query("includes") String includes,
                        Callback<ActiveListings> callback);
}
