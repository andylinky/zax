package com.inovex.zabbixmobile.adapters;

import java.util.Collection;
import java.util.TreeSet;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Base class for a pager adapter maintained by a service. The base
 * functionality is similar to {@link FragmentPagerAdapter}.
 * 
 * @param <T>
 *            class of the items in this adapter's data set
 */
public abstract class BaseServicePagerAdapter<T> extends PagerAdapter {

	protected TreeSet<T> mObjects;
	private static final String TAG = BaseServicePagerAdapter.class
			.getSimpleName();
	private static final boolean DEBUG = true;
	protected int mCurrentPosition;
	protected FragmentManager mFragmentManager;
	protected FragmentTransaction mCurTransaction = null;
	private Fragment mCurrentPrimaryItem = null;
	
	public BaseServicePagerAdapter() {
		mObjects = new TreeSet<T>();
	}

	public void setFragmentManager(FragmentManager fm) {
		this.mFragmentManager = fm;
	}

	@Override
	public void startUpdate(ViewGroup container) {
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}

		final long itemId = getItemId(position);

		// Do we already have this fragment?
		String name = makeFragmentName(container.getId(), itemId);
		Fragment fragment = mFragmentManager.findFragmentByTag(name);
		if (fragment != null) {
			if (DEBUG)
				Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
			mCurTransaction.attach(fragment);
		} else {
			fragment = getPage(position);
			if (DEBUG)
				Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
			mCurTransaction.add(container.getId(), fragment,
					makeFragmentName(container.getId(), itemId));
		}
		if (fragment != mCurrentPrimaryItem) {
			fragment.setMenuVisibility(false);
			fragment.setUserVisibleHint(false);
		}

		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (mCurTransaction == null) {
			mCurTransaction = mFragmentManager.beginTransaction();
		}
		if (DEBUG)
			Log.v(TAG, "Detaching item #" + getItemId(position) + ": f="
					+ object + " v=" + ((Fragment) object).getView());
		mCurTransaction.detach((Fragment) object);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		Fragment fragment = (Fragment) object;
		if (fragment != mCurrentPrimaryItem) {
			if (mCurrentPrimaryItem != null) {
				mCurrentPrimaryItem.setMenuVisibility(false);
				mCurrentPrimaryItem.setUserVisibleHint(false);
			}
			if (fragment != null) {
				fragment.setMenuVisibility(true);
				fragment.setUserVisibleHint(true);
			}
			mCurrentPrimaryItem = fragment;
		}
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		if (mCurTransaction != null) {
			mCurTransaction.commitAllowingStateLoss();
			mCurTransaction = null;
			mFragmentManager.executePendingTransactions();
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return ((Fragment) object).getView() == view;
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	/**
	 * Return a unique identifier for the item at the given position.
	 * 
	 * <p>
	 * The default implementation returns the given position. Subclasses should
	 * override this method if the positions of items can change.
	 * </p>
	 * 
	 * @param position
	 *            Position within this adapter
	 * @return Unique identifier for the item at position
	 */
	public abstract long getItemId(int position);

	protected static String makeFragmentName(int viewId, long id) {
		return "android:switcher:" + viewId + ":" + id;
	}

	/**
	 * Creates a page (fragment) for a certain position.
	 * 
	 * @param position
	 *            the position within the adapter
	 * @return the created fragment
	 */
	protected abstract Fragment getPage(int position);

	@Override
	public int getCount() {
		return mObjects.size();
	}

	/**
	 * Wrapper for {@link TreeSet#addAll(Collection)}.
	 * 
	 * @param objects
	 */
	public void addAll(Collection<? extends T> objects) {
		this.mObjects.addAll(objects);
	}

	/**
	 * Returns an item from the underlying data set.
	 * 
	 * @param position
	 *            position of the item
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getItem(int position) {
		return (T) mObjects.toArray()[position];
	}

	/**
	 * Returns the current item.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getCurrentItem() {
		return (T) mObjects.toArray()[mCurrentPosition];
	}

	/**
	 * Wrapper for {@link TreeSet#clear()}.
	 */
	public void clear() {
		mObjects.clear();
	}

	public int getCurrentPosition() {
		return mCurrentPosition;
	}

	public void setCurrentPosition(int position) {
		this.mCurrentPosition = position;
	}

}
