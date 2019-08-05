package network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllPhotosNetwork {

    public static Retrofit retrofit;
    public static final String BASE_URL = "https://api.unsplash.com/";

    public static Retrofit getRetrofitResponse() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
