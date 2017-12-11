package melquelolea.vefexchange.data

import android.content.Context
import com.google.gson.JsonObject
import melquelolea.vefexchange.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import rx.Observable

/**
 * Created by Patrick Rengifo on 11/29/16.
 * Definition of the Api to get the data from the exchange
 */

internal object AirTmApi {

    internal interface ApiInterface {

        @GET("operation/feed")
        fun airtmInformation(): Observable<JsonObject>
    }

    private var adapter: ApiInterface? = null

    fun getClient(context: Context): ApiInterface {
        if (adapter == null) {
            // Log
            val logging = HttpLoggingInterceptor()
            // set your desired log level
            logging.level = HttpLoggingInterceptor.Level.BASIC

            // OkHttpClient
            val httpClient = OkHttpClient.Builder().addInterceptor(logging)

            val client = httpClient.build()
            val restAdapter = Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(context.getString(R.string.airtm_endpoint))
                    .client(client)
                    .build()

            adapter = restAdapter.create(ApiInterface::class.java)
        }

        return adapter!!
    }
}
