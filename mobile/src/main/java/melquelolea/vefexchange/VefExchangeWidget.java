package melquelolea.vefexchange;

import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import melquelolea.vefexchange.data.UpdateDataService;

/**
 * Implementation of App Widget functionality.
 */
public class VefExchangeWidget extends AppWidgetProvider {

    private static final String TAG = VefExchangeWidget.class.getSimpleName();
    private static final int JOB_ID = 44;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the background service to update the widgets
        context.startService(new Intent(context, UpdateDataService.class));
        JobScheduler jobScheduler =
                (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        long interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        JobInfo job = new JobInfo.Builder(JOB_ID,
                new ComponentName(context.getPackageName(),
                        UpdateDataService.class.getName()))
                .setPersisted(true)
                .setPeriodic(interval)
                .build();

        jobScheduler.schedule(job);

        Log.d(TAG, "setting scheduled job for: " + interval);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

