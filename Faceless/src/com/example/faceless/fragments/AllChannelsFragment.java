package com.example.faceless.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.faceless.R;
import com.example.faceless.activities.ChatActivity;
import com.example.faceless.application.ZApplication;
import com.example.faceless.extras.RequestTags;
import com.example.faceless.gcm.RegistrationIntentService;
import com.example.faceless.objects.ZAllChannelsObject;
import com.example.faceless.preferences.ZPreferences;
import com.google.gson.Gson;

public class AllChannelsFragment extends BaseFragment implements RequestTags {

	ListView listView;
	ZAllChannelsObject obj;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.all_channels_fragment, container,
				false);

		listView = (ListView) v.findViewById(R.id.list);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		StringRequest req = new StringRequest(Method.POST,
				ZApplication.getBaseUrl() + "all_channels/",
				new Listener<String>() {

					@Override
					public void onResponse(String res) {
						obj = new Gson()
								.fromJson(res, ZAllChannelsObject.class);

						ArrayList<String> names = new ArrayList<>();
						for (int i = 0; i < obj.getChannels().size(); i++) {
							names.add(obj.getChannels().get(i).getName());
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<>(
								getActivity(),
								android.R.layout.simple_list_item_1, names);
						listView.setAdapter(adapter);

						listView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View view, int pos, long arg3) {
								Intent i = new Intent(getActivity(),
										ChatActivity.class);
								i.putExtra("channel_id",
										obj.getChannels().get(pos).getId());
								getActivity().startActivity(i);
							}
						});

						Intent intent = new Intent(getActivity(),
								RegistrationIntentService.class);
						getActivity().startService(intent);
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						makeToast("Error " + arg0.getLocalizedMessage());
						System.out.print(arg0.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_profile_id",
						ZPreferences.getUserId(getActivity()));
				return params;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, Z_ALL_CHANNELS);
	}
}
