package melquelolea.vefexchange.data

import android.content.Context
import melquelolea.vefexchange.R
import melquelolea.vefexchange.models.DolarToday
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

internal object VefExchangeApi {

    internal interface ApiInterface {

        @GET("vefdtd")
        fun dolarTodayInformation(): Observable<DolarToday>
    }

    private var adapter: ApiInterface? = null

    fun getClient(context: Context): ApiInterface {
        if (adapter == null) {
            // Log
            val logging = HttpLoggingInterceptor()
            // set your desired log level
            logging.level = HttpLoggingInterceptor.Level.BASIC

            // OkHttpClient
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val original = chain.request()

                // Customize the request
                val request = original.newBuilder()
                        .header("X-Device", "Android")
                        .build()

                // Customize or return the response
                chain.proceed(request)
            }


            // add logging as last interceptor
            httpClient.addInterceptor(logging)

            val client = httpClient.build()
            val restAdapter = Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(context.getString(R.string.vefexchange_endpoint))
                    .client(client)
                    .build()

            adapter = restAdapter.create(ApiInterface::class.java)
        }

        return adapter!!
    }
}
