package com.instirepo.app.fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.instirepo.app.R;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.SupportAnimator.AnimatorListener;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.widgets.CircularImageView;
import com.instirepo.app.widgets.ObservableScrollView;
import com.instirepo.app.widgets.ObservableScrollViewListener;

@SuppressLint("NewApi")
public class UserProfileViewedByOtherFragment extends BaseFragment {

	int heightOfUserDetailCard;
	int marginTopForUserDetailCard;
	LinearLayout userProfileDetail, scrollViewLinearLayout;
	ObservableScrollView scrollView;
	CircularImageView circularImageView;

	int userProfileImageHeight;
	FrameLayout touchInterceptFrameLayoutUserProfile;

	float initialY, initialTranslation;
	VelocityTracker mVelocityTracker;
	private float minFlingVelocity;
	float scrollViewCheckTranslationUp;
	private float deviceHeight, deviceWidth;

	View circularRevealView;
	public boolean fragmentDestroyed = false;

	public static UserProfileViewedByOtherFragment newInstance(Bundle b) {
		UserProfileViewedByOtherFragment frg = new UserProfileViewedByOtherFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.user_profile_viewed_by_other_fragment_layout,
				container, false);

		userProfileDetail = (LinearLayout) v.findViewById(R.id.userprofilecard);
		scrollView = (ObservableScrollView) v
				.findViewById(R.id.scrollviewuserrofiel);
		circularImageView = (CircularImageView) v
				.findViewById(R.id.userprofilecircularimage);
		scrollViewLinearLayout = (LinearLayout) v
				.findViewById(R.id.scrollviewlinearlayout);
		touchInterceptFrameLayoutUserProfile = (FrameLayout) v
				.findViewById(R.id.touchinterceptframelayouruserprof);
		circularRevealView = (View) v.findViewById(R.id.revealviewuserprofiel);

		return v;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		deviceHeight = getActivity().getResources().getDisplayMetrics().heightPixels;
		deviceWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
		touchInterceptFrameLayoutUserProfile.setTranslationY(deviceHeight);

		SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
				circularRevealView, (int) (deviceWidth / 2),
				(int) (0.9 * deviceHeight), 0, deviceHeight);
		animator.setInterpolator(new AccelerateInterpolator());
		animator.setDuration(500);
		animator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart() {
			}

			@Override
			public void onAnimationRepeat() {
			}

			@Override
			public void onAnimationEnd() {
				circularRevealView.animate().alpha(0).setDuration(300)
						.setListener(new ZAnimatorListener() {
							@Override
							public void onAnimationEnd(Animator animation) {
								circularRevealView.setVisibility(View.GONE);
							}
						}).start();
			}

			@Override
			public void onAnimationCancel() {
			}
		});
		animator.start();

		ViewConfiguration viewConfiguration = ViewConfiguration
				.get(getActivity());
		minFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity() * 2;

		userProfileImageHeight = getActivity().getResources()
				.getDimensionPixelSize(R.dimen.z_user_profile_image_height);

		userProfileDetail.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						userProfileDetail.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
						heightOfUserDetailCard = userProfileDetail.getHeight();

						performCalculationsAfterViewTreeObserver();
					}
				});
	}

	void performCalculationsAfterViewTreeObserver() {
		marginTopForUserDetailCard = (int) (deviceHeight / 3);

		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) userProfileDetail
				.getLayoutParams();
		params.topMargin = marginTopForUserDetailCard;
		userProfileDetail.setLayoutParams(params);

		params = (FrameLayout.LayoutParams) circularImageView.getLayoutParams();
		params.topMargin = marginTopForUserDetailCard - userProfileImageHeight
				/ 2;
		circularImageView.setLayoutParams(params);

		scrollView.setPadding(0,
				(int) (marginTopForUserDetailCard + heightOfUserDetailCard), 0,
				0);
		scrollViewCheckTranslationUp = deviceHeight
				- (marginTopForUserDetailCard);

		scrollView.setScrollListnerer(new ObservableScrollViewListener() {

			@Override
			public void onScrollStopped() {

			}

			@Override
			public void onScroll(int x, int y, int oldx, int oldy) {
				int location[] = new int[2];
				scrollViewLinearLayout.getLocationInWindow(location);
				Log.w("as", "scr " + scrollView.getScrollY() + " top  "
						+ location[1]);

				// TODO do animation
				userProfileDetail.setTranslationY(-scrollView.getScrollY());
				circularImageView.setTranslationY(-scrollView.getScrollY());
			}
		});

		scrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int index = event.getActionIndex();
				int pointerId = event.getPointerId(index);

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					initialTranslation = touchInterceptFrameLayoutUserProfile
							.getTranslationY();
					initialY = event.getRawY();

					if (mVelocityTracker == null)
						mVelocityTracker = VelocityTracker.obtain();
					else
						mVelocityTracker.clear();
					mVelocityTracker.addMovement(event);

					return true;
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					mVelocityTracker.addMovement(event);

					float dy = initialY - event.getRawY();
					if ((dy < 0 && scrollView.getScrollY() == 0)
							|| touchInterceptFrameLayoutUserProfile
									.getTranslationY() != 0) {
						float trans = initialTranslation - dy;
						if (trans < 0) {
							scrollView.setTranslationY(0);
							return false;
						}
						touchInterceptFrameLayoutUserProfile
								.setTranslationY(trans);
						return true;
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					mVelocityTracker.addMovement(event);
					mVelocityTracker.computeCurrentVelocity(1000);
					float yVelocity = VelocityTrackerCompat.getYVelocity(
							mVelocityTracker, pointerId);

					if (scrollView.getScrollY() == 0) {
						if (Math.abs(yVelocity) < minFlingVelocity) {
							if (touchInterceptFrameLayoutUserProfile
									.getTranslationY() < scrollViewCheckTranslationUp / 2) {
								setScrollViewTranslation0();
							} else {
								dismissScrollViewDown();
							}
						} else if (yVelocity > 0) {
							dismissScrollViewDown();
						}
					}
				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
					mVelocityTracker.recycle();
				}
				return false;
			}
		});

		touchInterceptFrameLayoutUserProfile.animate().translationY(0)
				.setDuration(500)
				.setInterpolator(new AccelerateDecelerateInterpolator())
				.start();
	}

	protected void setScrollViewTranslation0() {
		touchInterceptFrameLayoutUserProfile.animate().translationY(0)
				.setDuration(300)
				.setInterpolator(new AccelerateDecelerateInterpolator())
				.start();
	}

	public void dismissScrollViewDown() {
		touchInterceptFrameLayoutUserProfile.animate()
				.translationY(deviceHeight).setDuration(400)
				.setInterpolator(new AccelerateInterpolator())
				.setListener(new ZAnimatorListener() {
					@Override
					public void onAnimationEnd(Animator animation) {
						fragmentDestroyed = true;
						if (getActivity() != null)
							getActivity().onBackPressed();
					}
				}).start();
	}

	public void dismissScrollViewDownCalledFromActivityBackPressed() {
		circularRevealView.setVisibility(View.VISIBLE);
		circularRevealView.setAlpha(1);
		SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
				circularRevealView, (int) (deviceWidth / 2),
				(int) (0.9 * deviceHeight), deviceHeight, 0);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.setDuration(400);
		animator.start();

		touchInterceptFrameLayoutUserProfile.animate()
				.translationY(deviceHeight).setDuration(400)
				.setInterpolator(new AccelerateInterpolator())
				.setListener(new ZAnimatorListener() {
					@Override
					public void onAnimationEnd(Animator animation) {
						fragmentDestroyed = true;
						if (getActivity() != null)
							getActivity().onBackPressed();
					}
				}).start();
	}
}