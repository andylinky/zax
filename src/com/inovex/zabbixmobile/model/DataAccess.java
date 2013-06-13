package com.inovex.zabbixmobile.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class DataAccess {

	private static final String TAG = DataAccess.class.getSimpleName();
	private static DataAccess instance = null;
	private DatabaseHelper mDatabaseHelper;
	private Context mContext;

	/**
	 * Returns an instance of {@link DataAccess}.
	 * 
	 * @param context
	 *            the application context
	 * @return
	 */
	public synchronized static DataAccess getInstance(Context context) {
		if (instance == null)
			instance = new DataAccess(context);
		return instance;
	}

	/**
	 * Creates a new DataAccess object.
	 * 
	 * @param context
	 *            the application context
	 */
	private DataAccess(Context context) {
		mContext = context;
		mDatabaseHelper = OpenHelperManager.getHelper(mContext,
				MockDatabaseHelper.class);
//		databaseHelper.onUpgrade(databaseHelper.getWritableDatabase(), 0, 1);
	}

	/**
	 * Queries all events from the database.
	 * 
	 * @return list of all events
	 * @throws SQLException
	 */
	public List<Event> getAllEvents() throws SQLException {
		List<Event> events = mDatabaseHelper.getDao(Event.class).queryForAll();
		eventCache = events;
		return events;
	}
	
	List<Event> eventCache;

	/**
	 * Returns a list of events for a given trigger severity.
	 * 
	 * @param severity
	 *            the severity
	 * @return list of events with the given severity
	 * @throws SQLException
	 */
	public List<Event> getEventsBySeverity(TriggerSeverities severity) throws SQLException {
		if(severity == TriggerSeverities.ALL)
			return getAllEvents();
		// TODO: replace this with a JOIN
		List<Event> events;
		if(eventCache != null) {
			events = eventCache;
			Log.d(TAG, "Using events from cache.");
		} else
			events = mDatabaseHelper.getDao(Event.class).queryForAll();
		List<Event> eventsBySeverity = new ArrayList<Event>();
		Trigger t;
		for (Event e : events) {
			t = e.getTrigger();
			if (t == null)
				break;
			if (t.getPriority() == severity.getNumber())
				eventsBySeverity.add(e);
		}
		return eventsBySeverity;
	}
	
	public Event getEventById(long id) throws SQLException {
		Dao<Event, Long> dao = mDatabaseHelper.getDao(Event.class);
		return dao.queryForId(id);
	}
}