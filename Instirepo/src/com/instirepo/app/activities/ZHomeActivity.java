package com.instirepo.app.activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

import com.instirepo.app.R;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.fragments.CommentsFragment;
import com.instirepo.app.fragments.PostsByStudentsFragment;
import com.instirepo.app.fragments.PostsByTeachersFragment;
import com.instirepo.app.fragments.SeenByPeopleFragment;
import com.instirepo.app.fragments.ZUserProfileViewedByOtherFragment;

public class ZHomeActivity extends BaseActivity implements OnPageChangeListener {

	ViewPager viewPager;
	TabLayout tabLayout;
	MyPagerAdapter adapter;
	DrawerLayout drawerLayout;
	NavigationView navigationView;
	public static final int TRANSLATION_DURATION = 200;
	boolean isToolbarAnimRunning;
	AppBarLayout appBarLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		tabLayout = (TabLayout) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.pager_launch);
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		toolbar.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						try {
							toolbar.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} catch (Exception e) {
							toolbar.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
						toolbarHeight = toolbar.getHeight();
					}
				});

		setDrawerActionBarToggle();
		setDrawerItemClickListener();
		viewPager.setOnPageChangeListener(this);

		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
	}

	private void setDrawerItemClickListener() {
		navigationView
				.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

					@Override
					public boolean onNavigationItemSelected(MenuItem item) {
						item.setChecked(true);
						drawerLayout.closeDrawers();
						switch (item.getItemId()) {
						// case R.id.wishlist_navdrawer:
						// openWishlistActivity();
						// return true;

						default:
							return true;
						}
					}
				});
	}

	private void setDrawerActionBarToggle() {
		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
				this, drawerLayout, toolbar, R.string.z_open_drawer,
				R.string.z_close_drawer) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();
		navigationView.setItemIconTintList(null);
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int pos) {
			Bundle bundle = new Bundle();
			if (pos == 0)
				return PostsByTeachersFragment.newInstance(bundle);
			else
				return PostsByStudentsFragment.newInstance(bundle);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0) {
				return "Posts By Teachers";
			} else
				return "Posts By Students";
		}
	}

	@SuppressLint("NewApi")
	public void scrollToolbarBy(int dy) {
		float requestedTranslation = appBarLayout.getTranslationY() + dy;
		if (requestedTranslation < -toolbarHeight) {
			requestedTranslation = -toolbarHeight;
			appBarLayout.setTranslationY(requestedTranslation);
		} else if (requestedTranslation > 0) {
			requestedTranslation = 0;
			appBarLayout.setTranslationY(requestedTranslation);
		} else if (requestedTranslation >= -toolbarHeight
				&& requestedTranslation <= 0) {
			appBarLayout.setTranslationY(requestedTranslation);
		}
	}

	@SuppressLint("NewApi")
	public void scrollToolbarAfterTouchEnds() {
		float currentTranslation = -appBarLayout.getTranslationY();
		if (currentTranslation > toolbarHeight / 2) {
			animateToolbarLayout(-toolbarHeight);
		} else {
			animateToolbarLayout(0);
		}
	}

	public void setToolbarTranslation(View firstChild) {
		if (firstChild.getTop() > appBarLayout.getHeight()) {
			animateToolbarLayout(0);
		} else {
			scrollToolbarAfterTouchEnds();
		}
	}

	public void setToolbarTranslationFromPositionOfTopChildAfterTouchEnd(int pos) {
		if (pos > appBarLayout.getHeight()) {
			animateToolbarLayout(0);
		} else {
			scrollToolbarAfterTouchEnds();
		}
	}

	void animateToolbarLayout(int trans) {
		appBarLayout.animate().translationY(trans)
				.setDuration(TRANSLATION_DURATION)
				.setInterpolator(new DecelerateInterpolator())
				.setListener(new ZAnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {
						isToolbarAnimRunning = true;
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						isToolbarAnimRunning = false;
					}
				});
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (appBarLayout.getTranslationY() != 0 && !isToolbarAnimRunning) {
			animateToolbarLayout(0);
		}
	}

	@Override
	public void onPageSelected(int arg0) {

	}

	public void switchToSeenByPeopleFragment() {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						SeenByPeopleFragment.newInstance(new Bundle()))
				.addToBackStack("tag").commit();
	}

	public void switchToCommentsFragment() {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						CommentsFragment.newInstance(new Bundle()))
				.addToBackStack("tag").commit();
	}

	public void switchToUserProfileViewedByOtherFragment() {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(
						R.id.fragmentcontainer,
						ZUserProfileViewedByOtherFragment
								.newInstance(new Bundle()))
				.addToBackStack("tag").commit();
	}
}
