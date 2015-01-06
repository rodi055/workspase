package com.R.L.A.jobli;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class CreateNewJobActivity extends FragmentActivity {
	static String JobID = "jobid";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_job);
		if (savedInstanceState == null) {
			Long id = (Long) getIntent().getLongExtra(JobID,
					CreateJobFragment.defJobID);
			CreateJobFragment f = (CreateJobFragment) CreateJobFragment
					.newInstance(id);

			getFragmentManager().beginTransaction().add(R.id.frame, f).commit();
		}
	}

}
