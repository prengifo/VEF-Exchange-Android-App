package melquelolea.vefexchange.services

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import com.google.gson.Gson
import com.google.gson.JsonObject
import melquelolea.vefexchange.PreferenceHelper
import melquelolea.vefexchange.R
import melquelolea.vefexchange.data.AirTmApi
import melquelolea.vefexchange.data.CoinBaseApi
import melquelolea.vefexchange.data.DolarTodayApi
import melquelolea.vefexchange.data.LocalBitcoinsApi
import melquelolea.vefexchange.models.DolarToday
import melquelolea.vefexchange.widget.VefExchangeWidget
import okhttp3.ResponseBody
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.math.RoundingMode
import java.text.DecimalFormat



/**
 * Created by Patrick Rengifo on 16/12/16.
 * This service calls the endpoints to the current currency data
 */

class UpdateDataService : IntentService(TAG) {

    private var usdBtc: Double = 0.0
    private var vefBtc: Double = 0.0
    private var vefDtd: Double = 0.0
    private var vefAirtm: Double = 0.0
    private lateinit var appWidgetManager: AppWidgetManager
    private lateinit var views: RemoteViews
    private lateinit var appWidgetIds: IntArray
    private val decimalFormat: DecimalFormat
        get() {
            val df = DecimalFormat("#.###")
            df.roundingMode = RoundingMode.CEILING
            return df
        }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= 26) {
            val channelId = "com.prengifo.vefexhange"
            val channel = NotificationChannel(channelId,
                    "Update service",
                    NotificationManager.IMPORTANCE_LOW)

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this, channelId)
                    .setContentTitle("VEF Exchange Update Service").build()

            startForeground(1, notification)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        startForeground(0, null)
        // Construct the RemoteViews object
        views = RemoteViews(this@UpdateDataService.packageName, R.layout.vef_exchange_widget)
        val pendingIntent = PendingIntent.getService(this@UpdateDataService, 0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT)
        views.setOnClickPendingIntent(R.id.refresh, pendingIntent)

        appWidgetManager = AppWidgetManager.getInstance(this@UpdateDataService)
        appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this@UpdateDataService,
                VefExchangeWidget::class.java))
        appWidgetManager.updateAppWidget(appWidgetIds, views)

        // With Rx observables we call every endpoint to get the data
        val observable = LocalBitcoinsApi.getClient(this).bitcoinVEFInformation()
        observable
                .map<Double>({ this.saveLocalBitcoin(it) })
                .flatMap { CoinBaseApi.getClient(this).bitcoinUSD() }
                .map<Double>({ this.saveUsdBitcoin(it) })
                .flatMap { AirTmApi.getClient(this).airtmInformation() }
                .map { this.saveAirtmInformation(it) }
                .flatMap { DolarTodayApi.getClient(this).dolarTodayInformation() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<ResponseBody>() {
                    override fun onCompleted() {
                        // handle completed
                    }

                    override fun onError(e: Throwable) {
                        // handle error
                        Log.e(TAG, e.message)
                    }

                    override fun onNext(event: ResponseBody) {
                        // handle response
                        val response = event.string()
                        val json = response.split("= ")
                        val dolarToday: DolarToday =
                                Gson().fromJson<DolarToday>(json[1],
                                        DolarToday::class.java)
                        vefDtd = dolarToday.usd.dolartoday
                        PreferenceHelper.saveData(this@UpdateDataService, usdBtc, vefBtc, vefDtd)

                        // Fix to two decimals
                        decimalFormat.roundingMode = RoundingMode.CEILING
                        // Show the information
                        views.setTextViewText(R.id.usd_text, "1XBT - $" + usdBtc.toString())
                        views.setTextViewText(R.id.dolar_today_text, "1$ - Bs" + vefDtd.toString())
                        views.setTextViewText(R.id.localbitcoin_text, "1$ - Bs" + decimalFormat.format(vefBtc))
                        views.setTextViewText(R.id.airtm_text, "1$ - Bs" + decimalFormat.format(vefAirtm))

                        // Instruct the widget manager to update the widget
                        appWidgetManager.updateAppWidget(appWidgetIds, views)
                    }
                })
    }

    /**
     * Save USD - Bitcoin data
     * @param response incoming data
     * @return same input
     */
    private fun saveUsdBitcoin(response: JsonObject): Double {
        usdBtc = response.get("amount").asDouble
        vefBtc /= usdBtc
        views.setTextViewText(R.id.usd_text, "1XBT - $" + usdBtc.toString())
        views.setTextViewText(R.id.localbitcoin_text, "1$ - Bs" + decimalFormat.format(vefBtc))
        appWidgetManager.updateAppWidget(appWidgetIds, views)
        return vefBtc
    }

    /**
     * Save LocalBitcoin data
     * @param response incoming data
     * @return same input
     */
    private fun saveLocalBitcoin(response: JsonObject): Double {
        vefBtc = response.get("VEF").asJsonObject.get("avg_24h").asDouble
        return vefBtc
    }

    private fun saveAirtmInformation(response: JsonObject): Double {
        val results = response.get("results").asJsonArray
        val result = results.first {
            val result = it.asJsonObject
            result.get("type").asString == "WITHDRAWAL" &&
                    result.get("localCurrency").asJsonObject.get("ISOcode").asString == "VEF"
        }
        vefAirtm = result.asJsonObject.get("clientNetExchangeRateBrToLocalCurrencyApplied").asDouble
        return vefAirtm
    }

    companion object {
        private val TAG = UpdateDataService::class.java.simpleName
    }
}
