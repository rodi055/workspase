package com.R.L.A.jobli;

import android.app.Application;
import android.content.Context;

public class JobliApplication extends Application {

	private static Context context;

	public void onCreate() {
		super.onCreate();
		JobliApplication.context = getApplicationContext();

	}

	public static Context getAppContext() {
		return JobliApplication.context;

	}
}
