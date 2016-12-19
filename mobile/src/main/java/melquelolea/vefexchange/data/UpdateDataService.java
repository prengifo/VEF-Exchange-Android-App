package melquelolea.vefexchange.data;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.renderscript.Double2;
import android.util.Log;
import android.widget.RemoteViews;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import melquelolea.vefexchange.PreferenceHelper;
import melquelolea.vefexchange.R;
import melquelolea.vefexchange.VefExchangeWidget;
import melquelolea.vefexchange.models.Bitcoin;
import melquelolea.vefexchange.models.BitcoinVEF;
import melquelolea.vefexchange.models.DolarToday;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Patrick Rengifo on 16/12/16.
 */

public class UpdateDataService extends IntentService {

    private static final String TAG = UpdateDataService.class.getSimpleName();

    private Double usdbtc = 0.0;
    private Double vefbtc = 0.0;
    private Double vefdtd = 0.0;

    public UpdateDataService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Hacer llamada al servidor
        Observable<Bitcoin> observable = API.getClient(this).bitcoinUSDInformation();
        observable
                .map(this::saveBitcoin)
                .flatMap(bitcoin -> API.getClient(this).bitcoinVEFInformation())
                .map(this::saveSurBitcoin)
                .flatMap(bitcoin -> API.getClient(this).dolarTodayInformation())
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
                        vefdtd = event.usd.dolartoday;
                        PreferenceHelper.saveData(UpdateDataService.this, usdbtc, vefbtc, vefdtd);
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(UpdateDataService.this);
                        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(UpdateDataService.this,
                                VefExchangeWidget.class));
                        // Construct the RemoteViews object
                        RemoteViews views = new RemoteViews(UpdateDataService.this.getPackageName(), R.layout.vef_exchange_widget);
                        DecimalFormat df = new DecimalFormat("#.###");
                        df.setRoundingMode(RoundingMode.CEILING);
                        views.setTextViewText(R.id.dolar_today_text, "$"+vefdtd.toString());
                        views.setTextViewText(R.id.sur_bitcoin_text, "$"+df.format(vefbtc));

                        // Instruct the widget manager to update the widget
                        appWidgetManager.updateAppWidget(appWidgetIds, views);
                    }
                });
    }

    private BitcoinVEF saveSurBitcoin(BitcoinVEF bitcoinVEF) {
        vefbtc = (bitcoinVEF.buy + bitcoinVEF.sell) / 2;
        vefbtc = vefbtc/usdbtc;
        return bitcoinVEF;
    }

    private Bitcoin saveBitcoin(Bitcoin bitcoin) {
        usdbtc = bitcoin.getLast();
        return bitcoin;
    }
}
