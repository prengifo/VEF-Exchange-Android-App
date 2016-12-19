package melquelolea.vefexchange.data;

import android.content.Context;

import java.io.IOException;

import melquelolea.vefexchange.R;
import melquelolea.vefexchange.models.Bitcoin;
import melquelolea.vefexchange.models.BitcoinVEF;
import melquelolea.vefexchange.models.DolarToday;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Patrick Rengifo on 11/29/16.
 * Definition of the API to get the data from the exchange
 */

class API {

    public interface APIInterface {

        @GET("usdbtc")
        Observable<Bitcoin> bitcoinUSDInformation();

        @GET("vefbtc")
        Observable<BitcoinVEF> bitcoinVEFInformation();

        @GET("vefdtd")
        Observable<DolarToday> dolarTodayInformation();
    }

    private static APIInterface adapter;

    public static APIInterface getClient(final Context context) {
        if (adapter == null) {
            // Log
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            // OkHttpClient
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    // Customize the request
                    Request request = original.newBuilder()
                            .header("X-Device", "Android")
                            .build();

                    // Customize or return the response
                    return chain.proceed(request);
                }
            });


            // add logging as last interceptor
            httpClient.addInterceptor(logging);

            OkHttpClient client = httpClient.build();
            Retrofit restAdapter = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(context.getString(R.string.endpoint))
                    .client(client)
                    .build();

            adapter = restAdapter.create(APIInterface.class);
        }

        return adapter;
    }
}
