package com.instirepo.app.fragments;

import java.util.ArrayList;
import java.util.List;

import com.instirepo.app.R;
import com.instirepo.app.adapters.CommentListAdapter;
import com.instirepo.app.objects.CommentsListObject;
import com.instirepo.app.objects.CommentsListObject.CommentObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CommentsFragment extends BaseFragment {

	ListView listView;
	CommentListAdapter adapter;

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

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		addData();
	}

	private void addData() {
		List<CommentObject> mData = new ArrayList<>();
		for (int i = 0; i < 30; i++)
			mData.add(new CommentsListObject().new CommentObject());
		adapter = new CommentListAdapter(getActivity(), mData);
		listView.setAdapter(adapter);
	}
}
