package melquelolea.vefexchange;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

import melquelolea.vefexchange.data.UpdateDataService;

/**
 * Created by Patrick Rengifo on 19/12/16.
 * Job Service to update the information in widget every fifteen minutes
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScheduledJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        startService(new Intent(this, UpdateDataService.class));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
