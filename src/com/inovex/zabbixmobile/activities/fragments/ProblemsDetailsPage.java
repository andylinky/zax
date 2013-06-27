package com.inovex.zabbixmobile.activities.fragments;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.inovex.zabbixmobile.R;
import com.inovex.zabbixmobile.adapters.EventsDetailsPagerAdapter;
import com.inovex.zabbixmobile.model.Trigger;

/**
 * Represents one page of the event details view pager (see
 * {@link EventsDetailsPagerAdapter} ). Shows the details of a specific event.
 * 
 */
public class ProblemsDetailsPage extends SherlockFragment {

	private Trigger mTrigger;
	private String mTitle = "";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.page_problems_details, null);
		// if (savedInstanceState != null)
		// mEventId = savedInstanceState.getLong(ARG_EVENT_ID, -1);

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// TODO: on orientation change, mEvent is not set ->
		// NullPointerException
		StringBuilder sb = new StringBuilder();
		sb.append("\nTrigger:\n\n");
		sb.append("ID: " + mTrigger.getId() + "\n");
		sb.append("severity: " + mTrigger.getPriority() + "\n");
		sb.append("status: " + mTrigger.getStatus() + "\n");
		sb.append("description: " + mTrigger.getDescription() + "\n");
		sb.append("comments: " + mTrigger.getComments() + "\n");
		sb.append("expression: " + mTrigger.getExpression() + "\n");
		sb.append("URL: " + mTrigger.getUrl() + "\n");
		sb.append("value: " + mTrigger.getValue() + "\n");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(mTrigger.getLastChange());
		DateFormat dateFormatter = SimpleDateFormat.getDateTimeInstance(
				SimpleDateFormat.SHORT, SimpleDateFormat.SHORT,
				Locale.getDefault());
		sb.append("lastchange: "
				+ String.valueOf(dateFormatter.format(cal.getTime())) + "\n");
		TextView text = (TextView) getView().findViewById(R.id.details_text);
		text.setText(sb.toString());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// outState.putLong(ARG_EVENT_ID, mEventId);
		super.onSaveInstanceState(outState);
	}

	public void setTrigger(Trigger trigger) {
		this.mTrigger = trigger;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}

}
