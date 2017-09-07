package melquelolea.vefexchange.services

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build

import melquelolea.vefexchange.data.UpdateDataService

/**
 * Created by Patrick Rengifo on 19/12/16.
 * Job Service to update the information in widget every fifteen minutes
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class ScheduledJobService : JobService() {
    override fun onStartJob(params: JobParameters): Boolean {
        startService(Intent(this, UpdateDataService::class.java))
        return false
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return false
    }
}
