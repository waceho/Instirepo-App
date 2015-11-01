package com.instirepo.app.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.instirepo.app.R;
import com.instirepo.app.objects.CommentsListObject;
import com.instirepo.app.objects.CommentsListObject.CommentObject;

public class CommentListAdapter extends BaseAdapter {

	Context context;
	List<CommentsListObject.CommentObject> mData;

	public CommentListAdapter(Context context, List<CommentObject> mData) {
		super();
		this.context = context;
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommentHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.comments_list_item_layout, parent, false);
			holder = new CommentHolder(convertView);
			convertView.setTag(holder);
		} else
			holder = (CommentHolder) convertView.getTag();

		return convertView;
	}

	class CommentHolder {

		public CommentHolder(View v) {

		}
	}

}