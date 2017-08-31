package melquelolea.vefexchange.data;

import android.content.Context;

import com.google.gson.JsonObject;

import melquelolea.vefexchange.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Patrick Rengifo on 11/29/16.
 * Definition of the Api to get the data from the exchange
 */

class CoinBaseApi {

    interface ApiInterface {

        @GET("prices/spot_rate")
        Observable<JsonObject> bitcoinUSD();
    }

    private static ApiInterface adapter;

    static ApiInterface getClient(final Context context) {
        if (adapter == null) {
            // Log
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            // OkHttpClient
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .header("X-Device", "Android")
                        .build();

                // Customize or return the response
                return chain.proceed(request);
            });


            // add logging as last interceptor
            httpClient.addInterceptor(logging);

            OkHttpClient client = httpClient.build();
            Retrofit restAdapter = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(context.getString(R.string.coinbase_endpoint))
                    .client(client)
                    .build();

            adapter = restAdapter.create(ApiInterface.class);
        }

        return adapter;
    }
}