package com.R.L.A.jobli;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/***
 * 
 * @author Sofia Gendelman
 * 
 * @precondition this is a static object. however, to be properly initialized
 *               the object must have app's context. since this object is static
 *               it will be initialized before the context can be reached. to
 *               correct this make sure to call initializeUserManager() before
 *               using the object.
 * 
 *               make sure initializeUserManager() is called from
 *               SocialCampusApp.onCreate();
 * 
 */

public enum UserManager {

	INSTANCE();

	public static final String anonymousUID = "";
	private static User currentUser = createAnonymous();
	private static Set<String> pinnedIds = new HashSet<String>();

	private static User createAnonymous() {
		return new User(-1L, anonymousUID, "", "Anonymous");
	}

	private boolean isSavedInPrefs(Context appContext) {
		SharedPreferences prefs = appContext.getSharedPreferences(
				"", Context.MODE_PRIVATE);
		return !prefs.getString(".LoggedIn",
				UserManager.anonymousUID).equals(UserManager.anonymousUID);
	}

	public void initializeUserManager() {
		if (isSavedInPrefs(JobliApplication.getAppContext())) {

			// TODO: when you have a local DB, save in prefs only the Long id
			// and get
			// the object from DB instead of creating new instance

			// when local DB done syncing, rebuild this object in case user was
			// revoked or the
			// name or picture was edited.

			SharedPreferences prefs = JobliApplication.getAppContext()
					.getSharedPreferences("",
							Context.MODE_PRIVATE);
			Long lId = prefs.getLong(
					".LoggedInLongId", -1L);
			String id = prefs.getString(".LoggedIn",
					anonymousUID);
			String name = prefs.getString(
					".LoggedInName", "");
			String image = prefs.getString(
					".LoggedInImage", "");

			currentUser = new User(lId, id, image, name);

			pinnedIds = prefs.getStringSet(
					".PinnedHotSpotIDs",
					new HashSet<String>());
		}
	}

	public String getMyID() {
		return currentUser.getStringId();
	}

	public Long getMyLongID() {
		return currentUser.getId();
	}

	public User getMyData() {
		return currentUser;
	}

	public boolean isLoggedIn() {
		return !currentUser.getStringId().equals(anonymousUID);
	}

	// TODO- works fine only if DB was synced or if we have a local DB
	// better way is to ask the server DB if this one exists.
	// if there is no connection to the server relay on info in local DB.
//	public boolean isRegistered(String uid) throws DBNotSynced {
//		if (!LocalDBManager.INSTANCE.UserDB.isSyncDone()) {
//			throw new DBNotSynced();
//		}
//		return LocalDBManager.INSTANCE.UserDB.getItemById(uid) != null;
//	}

	public void loginUser(final User u, final UiOnDone uiOnDone,
			UiOnError uiOnError) {
//		boolean registrated;
//
//		u.setStringId("1234567891");
//
//		try {
//			registrated = UserManager.INSTANCE.isRegistered(u.getStringId());
//		} catch (DBNotSynced e) {
//			e.printStackTrace();
//			Toast.makeText(SociaCampusApplication.getAppContext(),
//					"slow connection. please try again", Toast.LENGTH_SHORT)
//					.show();
//			return;
//		}
//
//		if (registrated) {
//			User nu = LocalDBManager.INSTANCE.UserDB.getItemById(u
//					.getStringId());
//			setLoggedIn(nu);
//			uiOnDone.execute();
//		} else {
//			ServerRequestManager.INSTANCE.addUser(u, new UiOnDone() {
//
//				@Override
//				public void execute() {
//					User nu = LocalDBManager.INSTANCE.UserDB.getItemById(u
//							.getStringId());
//					setLoggedIn(nu);
//					uiOnDone.execute();
//				}
//			}, uiOnError);
//		}

	}

	private static void setLoggedIn(User u) {
		Context appContext = JobliApplication.getAppContext();
		SharedPreferences prefs = appContext.getSharedPreferences(
				"", Context.MODE_PRIVATE);
		prefs.edit().putLong(".LoggedInLongId",
				u.getId());
		prefs.edit()
				.putString(".LoggedIn",
						u.getStringId()).commit();
		prefs.edit()
				.putString(".LoggedInImage",
						u.getmImage()).commit();
		prefs.edit()
				.putString(".LoggedInName",
						u.getmName()).commit();

		currentUser = u;
	}

	public void logout() {

		Context appContext = JobliApplication.getAppContext();
		currentUser = createAnonymous();

		SharedPreferences prefs = appContext.getSharedPreferences(
				"", Context.MODE_PRIVATE);
		prefs.edit().remove(".LoggedInLongId")
				.commit();
		prefs.edit().remove(".LoggedIn").commit();
		prefs.edit().remove(".LoggedInImage")
				.commit();
		prefs.edit().remove(".LoggedInName")
				.commit();
	}

	public void pinHotSpot(Long hId) {

		Context appContext = JobliApplication.getAppContext();
		SharedPreferences prefs = appContext.getSharedPreferences(
				"", Context.MODE_PRIVATE);

		String strid = String.valueOf(hId);
		pinnedIds.add(strid);
		prefs.edit().putStringSet(
				"PinnedJobsIDs", pinnedIds);
	}

	public void unPinHotSpot(Long hId) {
		Context appContext = JobliApplication.getAppContext();
		SharedPreferences prefs = appContext.getSharedPreferences(
				"", Context.MODE_PRIVATE);

		String strid = String.valueOf(hId);
		pinnedIds.remove(strid);
		prefs.edit().putStringSet(
				"PinnedJobsIDs", pinnedIds);
	}

	public List<Long> getPinned() {
		List<Long> res = new ArrayList<Long>();
		for (String s : pinnedIds) {
			res.add(Long.getLong(s));
		}
		return res;
	}

	public boolean isPinned(Long hid) {

		for (Long h : getPinned()) {
			if (h == hid) {
				return true;
			}
		}
		return false;
	}
}
