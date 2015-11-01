package com.instirepo.app.fragments;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.instirepo.app.R;
import com.instirepo.app.adapters.CommentListAdapter;
import com.instirepo.app.circularreveal.SupportAnimator;
import com.instirepo.app.circularreveal.ViewAnimationUtils;
import com.instirepo.app.extras.ZAnimatorListener;
import com.instirepo.app.extras.ZCircularAnimatorListener;
import com.instirepo.app.objects.CommentsListObject;
import com.instirepo.app.objects.CommentsListObject.CommentObject;

@SuppressLint("NewApi")
public class CommentsFragment extends BaseFragment implements OnClickListener {

	ListView listView;
	CommentListAdapter adapter;
	FloatingActionButton addCommentFab;
	public LinearLayout addCommentLayout;
	int dimen16, dimen56;
	private int deviceWidth;
	int location[];

	boolean isShowCommentLayoutAnimRunning;

	public static CommentsFragment newInstance(Bundle b) {
		CommentsFragment frg = new CommentsFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.comments_fragment_layout, container,
				false);

		listView = (ListView) v.findViewById(R.id.commentlist);
		addCommentFab = (FloatingActionButton) v
				.findViewById(R.id.adddocmmentfab);
		addCommentLayout = (LinearLayout) v.findViewById(R.id.commentlayoutadd);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dimen16 = getActivity().getResources().getDimensionPixelSize(
				R.dimen.z_margin_large);
		dimen56 = getActivity().getResources().getDimensionPixelSize(
				R.dimen.z_toolbar_height);
		deviceWidth = getActivity().getResources().getDisplayMetrics().widthPixels;

		addCommentFab.setOnClickListener(this);

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount == totalItemCount - 1)
						&& addCommentLayout.getVisibility() == View.GONE
						&& !isShowCommentLayoutAnimRunning) {
					showCommentLayout();
				}
			}
		});

		addData();
	}

	private void addData() {
		List<CommentObject> mData = new ArrayList<>();
		for (int i = 0; i < 30; i++)
			mData.add(new CommentsListObject().new CommentObject());
		adapter = new CommentListAdapter(getActivity(), mData);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.adddocmmentfab) {
			showCommentLayout();
		}
	}

	private void showCommentLayout() {
		isShowCommentLayoutAnimRunning = true;
		addCommentFab.animate().translationY(dimen16).translationX(-dimen16)
				.setInterpolator(new AccelerateInterpolator()).setDuration(150)
				.setListener(new ZAnimatorListener() {

					@Override
					public void onAnimationEnd(Animator animation) {
						location = new int[2];
						addCommentFab.getLocationInWindow(location);
						location[0] = location[0];
						addCommentFab.setVisibility(View.GONE);
						addCommentLayout.setVisibility(View.VISIBLE);

						SupportAnimator animator = ViewAnimationUtils
								.createCircularReveal(addCommentLayout,
										location[0] + dimen56 / 3, dimen56 / 2,
										dimen56 / 2, deviceWidth);
						animator.setInterpolator(new AccelerateDecelerateInterpolator());
						animator.setDuration(600);
						animator.addListener(new ZCircularAnimatorListener() {
							@Override
							public void onAnimationEnd() {
								isShowCommentLayoutAnimRunning = false;
							}
						});
						animator.start();
					}
				}).start();
	}

	void hideCommentLayout() {
		SupportAnimator animator = ViewAnimationUtils.createCircularReveal(
				addCommentLayout, location[0] + dimen56 / 3, dimen56 / 2,
				deviceWidth, dimen56 / 2);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.setDuration(600);
		animator.addListener(new ZCircularAnimatorListener() {

			@Override
			public void onAnimationEnd() {
				addCommentFab.setVisibility(View.VISIBLE);
				addCommentLayout.setVisibility(View.GONE);
				addCommentFab
						.animate()
						.translationX(0)
						.translationY(0)
						.setDuration(80)
						.setInterpolator(new AccelerateDecelerateInterpolator())
						.setListener(new ZAnimatorListener() {
							@Override
							public void onAnimationEnd(Animator animation) {
								addCommentFab.setVisibility(View.VISIBLE);
								addCommentLayout.setVisibility(View.GONE);
							}
						}).start();
			}
		});
		animator.start();
	}

	public boolean shouldGoBackOnBackButtonPress() {
		if (addCommentLayout.getVisibility() == View.VISIBLE) {
			hideCommentLayout();
			return false;
		}
		return true;
	}
}