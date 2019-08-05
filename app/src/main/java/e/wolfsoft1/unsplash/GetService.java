package e.wolfsoft1.unsplash;

import java.util.List;

import model.AllPhotosModel;
import model.RetroPhoto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetService {

    static String CLIENT_ID = "0c5eec0302942e470d12721918a479f9c672212ae4d2f01f5ccaf71d241ff10f";

    @GET("/photos")
    Call<List<AllPhotosModel>>getAllPhotos(@Query("client_id") String key, @Query("page") String page);

    @GET("/search/photos")
    Call<List<RetroPhoto>>getSearchList(@Query("client_id") String key, @Query("page") String page, @Query("query") String query);

}
