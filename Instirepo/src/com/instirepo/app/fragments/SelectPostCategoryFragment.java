package com.instirepo.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.instirepo.app.R;
import com.instirepo.app.adapters.SelectPostCategoryGridAdapter;
import com.instirepo.app.objects.PostCategoriesListObject;

public class SelectPostCategoryFragment extends BaseFragment {

	GridView gridView;
	PostCategoriesListObject mData;
	SelectPostCategoryGridAdapter adapter;

	public static SelectPostCategoryFragment newInstance(Bundle b) {
		SelectPostCategoryFragment frg = new SelectPostCategoryFragment();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.select_post_category_fragment_layout,
						container, false);

		gridView = (GridView) v.findViewById(R.id.selectcategorygrd);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mData = getArguments().getParcelable("postcategories");

		adapter = new SelectPostCategoryGridAdapter(mData.getCategories(),
				getActivity());
		gridView.setAdapter(adapter);
	}

}
