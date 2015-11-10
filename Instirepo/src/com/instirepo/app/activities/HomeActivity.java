package com.instirepo.app.activities;

import java.util.ArrayList;
import java.util.List;

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
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.instirepo.app.R;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.AppConstants;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.extras.ZCircularAnimatorListener;
import com.instirepo.app.floatingactionbutton.FloatingActionButton;
import com.instirepo.app.floatingactionbutton.FloatingActionMenu;
import com.instirepo.app.fragments.CommentsFragment;
import com.instirepo.app.fragments.PostsByStudentsFragment;
import com.instirepo.app.fragments.PostsByTeachersFragment;
import com.instirepo.app.fragments.SeenByPeopleFragment;
import com.instirepo.app.fragments.SelectPostCategoryFragment;
import com.instirepo.app.fragments.UserProfileViewedByOtherFragment;
import com.instirepo.app.objects.PostCategoriesListObject;
import com.instirepo.app.objects.PostCategorySinglePostCategory;

public class HomeActivity extends BaseActivity implements
		OnPageChangeListener, AppConstants, OnClickListener {

	ViewPager viewPager;
	TabLayout tabLayout;
	MyPagerAdapter adapter;
	DrawerLayout drawerLayout;
	NavigationView navigationView;
	public static final int TRANSLATION_DURATION = 200;
	boolean isToolbarAnimRunning;
	AppBarLayout appBarLayout;

	FloatingActionMenu floatingActionMenu;
	FloatingActionButton createPostButton;
	View fabBackground;
	int fabRevealMargin;
	int deviceHeight, deviceWidth;
	boolean isFabAnimRunning;
	PostCategoriesListObject postCategoriesData;
	int maxFloatingActionButtonTranslation;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);

		fabRevealMargin = getResources().getDimensionPixelSize(
				R.dimen.z_fab_home_dimension_for_reveal);
		deviceHeight = getResources().getDisplayMetrics().heightPixels;
		deviceWidth = getResources().getDisplayMetrics().widthPixels;
		maxFloatingActionButtonTranslation = getResources()
				.getDimensionPixelSize(
						R.dimen.floating_action_button_height_with_margin_bottom_considered);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		tabLayout = (TabLayout) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.pager_launch);
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
		floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fabmenuy);
		fabBackground = (View) findViewById(R.id.revealviewfab);
		createPostButton = (FloatingActionButton) findViewById(R.id.createpostbutton);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		createPostButton.setOnClickListener(this);

		floatingActionMenu.setOnMenuButtonClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (fabBackground.getVisibility() == View.GONE) {
					showFloatingActionMenu();
				} else {
					hideFloatingActionMenu();
				}
			}
		});

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

	protected void hideFloatingActionMenu() {
		if (!isFabAnimRunning) {
			isFabAnimRunning = true;
			SupportAnimator anim = ViewAnimationUtils.createCircularReveal(
					fabBackground, deviceWidth - fabRevealMargin, deviceHeight
							- 2 * fabRevealMargin, deviceHeight, 0);
			anim.setInterpolator(new AccelerateDecelerateInterpolator());
			anim.setDuration(400);
			anim.addListener(new ZCircularAnimatorListener() {
				@Override
				public void onAnimationEnd() {
					isFabAnimRunning = false;
					fabBackground.setVisibility(View.GONE);
				}
			});
			anim.start();
			floatingActionMenu.toggle(true);
		}
	}

	protected void showFloatingActionMenu() {
		if (!isFabAnimRunning) {
			isFabAnimRunning = true;
			fabBackground.setVisibility(View.VISIBLE);
			SupportAnimator anim = ViewAnimationUtils.createCircularReveal(
					fabBackground, deviceWidth - fabRevealMargin, deviceHeight
							- 2 * fabRevealMargin, 0, deviceHeight);
			anim.setDuration(500);
			anim.addListener(new ZCircularAnimatorListener() {
				@Override
				public void onAnimationEnd() {
					isFabAnimRunning = false;
				}
			});
			anim.start();
			floatingActionMenu.toggle(true);
		}
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

		scrollFABBy(-dy);
	}

	void scrollFABBy(int dy) {
		float requestedTranslation = floatingActionMenu.getTranslationY() + dy;
		if (requestedTranslation > maxFloatingActionButtonTranslation) {
			requestedTranslation = maxFloatingActionButtonTranslation;
			floatingActionMenu.setTranslationY(requestedTranslation);
		} else if (requestedTranslation < 0) {
			requestedTranslation = 0;
			floatingActionMenu.setTranslationY(requestedTranslation);
		} else if (requestedTranslation <= maxFloatingActionButtonTranslation
				&& requestedTranslation >= 0) {
			floatingActionMenu.setTranslationY(requestedTranslation);
		}
	}

	@SuppressLint("NewApi")
	public void scrollToolbarAfterTouchEnds() {
		float currentTranslation = -appBarLayout.getTranslationY();
		if (currentTranslation > toolbarHeight / 2) {
			animateToolbarLayout(-toolbarHeight);
			animateFABLayout(maxFloatingActionButtonTranslation);
		} else {
			animateToolbarLayout(0);
			animateFABLayout(0);
		}
	}

	public void setToolbarTranslation(View firstChild) {
		if (firstChild.getTop() > appBarLayout.getHeight()) {
			animateToolbarLayout(0);
			animateFABLayout(0);
		} else {
			scrollToolbarAfterTouchEnds();
		}
	}

	public void setToolbarTranslationFromPositionOfTopChildAfterTouchEnd(int pos) {
		if (pos > appBarLayout.getHeight()) {
			animateToolbarLayout(0);
			animateFABLayout(0);
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

	void animateFABLayout(int trans) {
		floatingActionMenu.animate().translationY(trans)
				.setDuration(TRANSLATION_DURATION)
				.setInterpolator(new DecelerateInterpolator())
				.setListener(new ZAnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
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
						CommentsFragment.newInstance(new Bundle()),
						Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG)
				.addToBackStack("Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG")
				.commit();
	}

	void switchToSelectPostCategoryFragment() {
		Bundle bundle = new Bundle();

		postCategoriesData = new PostCategoriesListObject();
		List<PostCategorySinglePostCategory> categories = new ArrayList<>();

		PostCategorySinglePostCategory category = new PostCategorySinglePostCategory();
		category.setType(Z_CATEGORY_TYPE_EVENT);
		categories.add(category);
		category.setType(Z_CATEGORY_TYPE_PLACEMENT);
		categories.add(category);
		category.setType(Z_CATEGORY_TYPE_POLLS);
		categories.add(category);
		for (int i = 0; i < 3; i++)
			categories.add(new PostCategorySinglePostCategory());
		postCategoriesData.setCategories(categories);

		bundle.putParcelable("postcategories", postCategoriesData);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						SelectPostCategoryFragment.newInstance(bundle))
				.addToBackStack("").commit();
	}

	public void switchToUserProfileViewedByOtherFragment() {
		Bundle bundle = new Bundle();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.fragmentcontainer,
						UserProfileViewedByOtherFragment.newInstance(bundle),
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG)
				.addToBackStack(
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG)
				.commit();
	}

	@Override
	public void onBackPressed() {
		Fragment fragmentUserProfile = getSupportFragmentManager()
				.findFragmentByTag(
						Z_USER_PROFILE_VIEWED_BY_OTHER_BACKSTACK_ENTRY_TAG);
		Fragment fragmentComments = getSupportFragmentManager()
				.findFragmentByTag(Z_COMMENT_FRAGMENT_BACKSTACK_ENTRY_TAG);

		if (fragmentUserProfile != null
				&& !((UserProfileViewedByOtherFragment) fragmentUserProfile).fragmentDestroyed) {
			((UserProfileViewedByOtherFragment) fragmentUserProfile)
					.dismissScrollViewDownCalledFromActivityBackPressed();
		} else if (fragmentComments != null) {
			CommentsFragment frg = (CommentsFragment) fragmentComments;
			if (frg.shouldGoBackOnBackButtonPress())
				super.onBackPressed();
		} else if (fabBackground.getVisibility() == View.VISIBLE) {
			hideFloatingActionMenu();
		} else
			super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.createpostbutton:
			hideFloatingActionMenu();
			switchToSelectPostCategoryFragment();
			break;

		default:
			break;
		}
	}
}