package melquelolea.vefexchange.data

import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.google.gson.JsonObject
import melquelolea.vefexchange.PreferenceHelper
import melquelolea.vefexchange.R
import melquelolea.vefexchange.models.DolarToday
import melquelolea.vefexchange.widget.VefExchangeWidget
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

    private var mUsdBtc: Double? = 0.0
    private var mVefBtc: Double? = 0.0
    private var mVefDtd: Double? = 0.0

    override fun onHandleIntent(intent: Intent?) {
        // With Rx observables we call every endpoint to get the data
        val observable = LocalBitcoinsApi.getClient(this).bitcoinVEFInformation()
        observable
                .map<Double>({ this.saveLocalBitcoin(it) })
                .flatMap { CoinBaseApi.getClient(this).bitcoinUSD() }
                .map<Double>({ this.saveUsdBitcoin(it) })
                .flatMap { VefExchangeApi.getClient(this).dolarTodayInformation() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<DolarToday>() {
                    override fun onCompleted() {
                        // handle completed
                    }

                    override fun onError(e: Throwable) {
                        // handle error
                        Log.e(TAG, e.message)
                    }

                    override fun onNext(event: DolarToday) {
                        // handle response
                        mVefDtd = event.usd!!.dolartoday
                        PreferenceHelper.saveData(this@UpdateDataService, mUsdBtc, mVefBtc, mVefDtd)
                        val appWidgetManager = AppWidgetManager.getInstance(this@UpdateDataService)
                        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(this@UpdateDataService,
                                VefExchangeWidget::class.java))
                        // Construct the RemoteViews object
                        val views = RemoteViews(this@UpdateDataService.packageName, R.layout.vef_exchange_widget)
                        // Fix to two decimals
                        val df = DecimalFormat("#.###")
                        df.roundingMode = RoundingMode.CEILING
                        // Show the information
                        views.setTextViewText(R.id.usd_text, "1XBT - $" + mUsdBtc!!.toString())
                        views.setTextViewText(R.id.dolar_today_text, "1$ - Bs" + mVefDtd!!.toString())
                        views.setTextViewText(R.id.localbitcoin_text, "1$ - Bs" + df.format(mVefBtc))

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
        mUsdBtc = response.get("amount").asDouble
        mVefBtc = mVefBtc!! / mUsdBtc!!
        return mVefBtc!!
    }

    /**
     * Save LocalBitcoin data
     * @param response incoming data
     * @return same input
     */
    private fun saveLocalBitcoin(response: JsonObject): Double {
        mVefBtc = response.get("VEF").asJsonObject.get("avg_24h").asDouble
        return mVefBtc!!
    }

    companion object {
        private val TAG = UpdateDataService::class.java.simpleName
    }
}
