package com.example.faceless.fragments;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.faceless.R;
import com.example.faceless.activities.HomeActivity;
import com.example.faceless.activities.LoginActivity;
import com.example.faceless.application.ZApplication;
import com.example.faceless.extras.RequestTags;
import com.example.faceless.gcm.RegistrationIntentService;
import com.example.faceless.objects.ZLoginObject;
import com.example.faceless.preferences.ZPreferences;
import com.google.gson.Gson;

public class LoginFragment2 extends BaseFragment implements RequestTags {

	EditText username, password;
	Button continueButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.signup_fragment_2_layout, container,
				false);

		username = (EditText) v.findViewById(R.id.username);
		password = (EditText) v.findViewById(R.id.password);
		continueButton = (Button) v.findViewById(R.id.continueb);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		continueButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (username.getText().toString().trim().length() == 0) {
					makeToast("Enter username");
				} else if (password.getText().toString().trim().length() == 0) {
					makeToast("Enter password");
				} else {
					makeRequestForLogin(username.getText().toString().trim(),
							password.getText().toString().trim());
				}
			}
		});
	}

	protected void makeRequestForLogin(final String username,
			final String password) {
		String url = "login_request/";
		StringRequest req = new StringRequest(Method.POST,
				ZApplication.getBaseUrl() + url, new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						ZLoginObject obj = new Gson().fromJson(arg0,
								ZLoginObject.class);
						if (!obj.isError()) {
							ZPreferences.setUserId(getActivity(), obj.getId()
									+ "");
							ZPreferences.setIsUserLogin(getActivity(), true);
							ZPreferences.setIsAdmin(getActivity(),
									obj.isIs_admin());

							Intent intent = new Intent(getActivity(),
									RegistrationIntentService.class);
							getActivity().startService(intent);

							if (obj.isPassword_change_required()) {
								makeToast("Password change required");
							}
							Intent i = new Intent(getActivity(),
									HomeActivity.class);
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
									| Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(i);
						} else {
							makeToast("Username or password do not match");
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						makeToast("Error");
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> params = new HashMap<>();
				params.put("team_name",
						((LoginActivity) getActivity()).teamName);
				params.put("username", username);
				params.put("password", password);
				return params;
			}
		};
		ZApplication.getInstance().addToRequestQueue(req, Z_SIGNUP_TAG);
	}

}
