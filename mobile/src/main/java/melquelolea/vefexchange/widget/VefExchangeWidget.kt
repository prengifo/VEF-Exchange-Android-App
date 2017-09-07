package melquelolea.vefexchange.widget

import android.app.AlarmManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log

import melquelolea.vefexchange.services.UpdateDataService

/**
 * Implementation of App Widget functionality.
 */
class VefExchangeWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        //Start the background service to update the widgets
        context.startService(Intent(context, UpdateDataService::class.java))
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES
        val job = JobInfo.Builder(JOB_ID,
                ComponentName(context.packageName,
                        UpdateDataService::class.java.name))
                .setPersisted(true)
                .setPeriodic(interval)
                .build()

        jobScheduler.schedule(job)

        Log.d(TAG, "setting scheduled job for: " + interval)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        private val TAG = VefExchangeWidget::class.java.simpleName
        private val JOB_ID = 44
    }
}

