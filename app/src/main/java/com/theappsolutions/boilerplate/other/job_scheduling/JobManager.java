package com.theappsolutions.boilerplate.other.job_scheduling;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class JobManager {

    private static final String JOB_TAG = "check-tasks-job-tag";
    private static final int SECONDS_IN_MIN = 60;
    private static final int INTERVAL_LENGTH = 5 * SECONDS_IN_MIN;
    private static final int WINDOW_LENGTH = SECONDS_IN_MIN;

    private FirebaseJobDispatcher dispatcher;

    @Inject
    public JobManager(Context context) {
        Timber.d("Create Job Manager");
        // Create a new dispatcher using the Google Play driver.
        dispatcher =
            new FirebaseJobDispatcher(new GooglePlayDriver(context));
    }

    public void scheduleJob() {
        Job myJob = dispatcher.newJobBuilder()
            // the JobService that will be called
            .setService(CheckTasksJobService.class)
            // uniquely identifies the job
            .setTag(JOB_TAG)
            .setRecurring(true)
            // don't persist past a device reboot
            .setLifetime(Lifetime.FOREVER)
            .setTrigger(Trigger.executionWindow(
                INTERVAL_LENGTH, INTERVAL_LENGTH + WINDOW_LENGTH))
            // don't overwrite an existing job with the same tag
            .setReplaceCurrent(true)
            // retry with exponential backoff
            .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
            // constraints that need to be satisfied for the job to run
            .setConstraints(
                Constraint.ON_ANY_NETWORK
            )
            .build();

        dispatcher.mustSchedule(myJob);
        Timber.d("Job scheduled in JobManager");
    }

    public void cancelAllJobs() {
        dispatcher.cancelAll();
        Timber.d("Job canceled in JobManager");
    }

    public void processJob(boolean isEnable) {
        if (isEnable) {
            scheduleJob();
        } else {
            cancelAllJobs();
        }
    }
}
