package melquelolea.vefexchange.data;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.JsonObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import melquelolea.vefexchange.PreferenceHelper;
import melquelolea.vefexchange.R;
import melquelolea.vefexchange.models.DolarToday;
import melquelolea.vefexchange.widget.VefExchangeWidget;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Patrick Rengifo on 16/12/16.
 * This service calls the endpoints to the current currency data
 */

public class UpdateDataService extends IntentService {

    private static final String TAG = UpdateDataService.class.getSimpleName();

    private Double mUsdBtc = 0.0;
    private Double mVefBtc = 0.0;
    private Double mVefDtd = 0.0;

    public UpdateDataService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // With Rx observables we call every endpoint to get the data
        Observable<JsonObject> observable = LocalBitcoinsApi.getClient(this).bitcoinVEFInformation();
        observable
                .map(this::saveLocalBitcoin)
                .flatMap(price -> CoinBaseApi.getClient(this).bitcoinUSD())
                .map(this::saveUsdBitcoin)
                .flatMap(bitcoin -> VefExchangeApi.getClient(this).dolarTodayInformation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DolarToday>() {
                    @Override
                    public void onCompleted() {
                        // handle completed
                    }

                    @Override
                    public void onError(Throwable e) {
                        // handle error
                        Log.e(TAG, e.getMessage());
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            int code = response.code();
//                            APIHelper.onFailure(this, code);
                        }
                    }

                    @Override
                    public void onNext(DolarToday event) {
                        // handle response
                        mVefDtd = event.usd.dolartoday;
                        PreferenceHelper.INSTANCE.saveData(UpdateDataService.this, mUsdBtc, mVefBtc, mVefDtd);
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(UpdateDataService.this);
                        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(UpdateDataService.this,
                                VefExchangeWidget.class));
                        // Construct the RemoteViews object
                        RemoteViews views = new RemoteViews(UpdateDataService.this.getPackageName(), R.layout.vef_exchange_widget);
                        // Fix to two decimals
                        DecimalFormat df = new DecimalFormat("#.###");
                        df.setRoundingMode(RoundingMode.CEILING);
                        // Show the information
                        views.setTextViewText(R.id.usd_text, "1XBT - $" + mUsdBtc.toString());
                        views.setTextViewText(R.id.dolar_today_text, "1$ - Bs" + mVefDtd.toString());
                        views.setTextViewText(R.id.localbitcoin_text, "1$ - Bs" + df.format(mVefBtc));

                        // Instruct the widget manager to update the widget
                        appWidgetManager.updateAppWidget(appWidgetIds, views);
                    }
                });
    }

    /**
     * Save USD - Bitcoin data
     * @param response incoming data
     * @return same input
     */
    private double saveUsdBitcoin(JsonObject response) {
        mUsdBtc = response.get("amount").getAsDouble();
        mVefBtc = mVefBtc / mUsdBtc;
        return mVefBtc;
    }

    /**
     * Save LocalBitcoin data
     * @param response incoming data
     * @return same input
     */
    private double saveLocalBitcoin(JsonObject response) {
        mVefBtc = response.get("VEF").getAsJsonObject().get("avg_24h").getAsDouble();
        return mVefBtc;
    }
}
