package com.instirepo.app.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instirepo.app.R;
import com.instirepo.app.widgets.RoundedImageView;

public class CreatePostFragment1OtherCategory extends BaseFragment implements
		OnClickListener {

	LinearLayout uploadPicLayout;
	TextView uploadPicText;
	public static int SELECT_FILE = 50;
	RoundedImageView roundedImageView;

	public static CreatePostFragment1OtherCategory newInstance(Bundle b) {
		CreatePostFragment1OtherCategory frg = new CreatePostFragment1OtherCategory();
		frg.setArguments(b);
		return frg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.create_post_fragment_1_other_category, container,
				false);

		uploadPicLayout = (LinearLayout) v.findViewById(R.id.uploadpiclayout);
		uploadPicText = (TextView) v.findViewById(R.id.uploadpictext);
		roundedImageView = (RoundedImageView) v
				.findViewById(R.id.rounderimagecreapost);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		uploadPicLayout.setOnClickListener(this);
	}

	public void setImageForPost(Bitmap bitmap) {
		roundedImageView.setVisibility(View.VISIBLE);
		roundedImageView.setImageBitmap(bitmap);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.uploadpiclayout:
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(
					Intent.createChooser(intent, "Select Image"), SELECT_FILE);
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == CreatePostFragment1OtherCategory.SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				String[] projection = { MediaColumns.DATA };
				Cursor cursor = getActivity().managedQuery(selectedImageUri,
						projection, null, null, null);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaColumns.DATA);
				cursor.moveToFirst();
				String selectedImagePath = cursor.getString(column_index);
				Bitmap bm;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(selectedImagePath, options);
				final int REQUIRED_SIZE = 400;
				int scale = 1;
				while (options.outWidth / scale / 2 >= REQUIRED_SIZE
						&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
					scale *= 2;
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeFile(selectedImagePath, options);
				setImageForPost(bm);
			}
		}
	}
}