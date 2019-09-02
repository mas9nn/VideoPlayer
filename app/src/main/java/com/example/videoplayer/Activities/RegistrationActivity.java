package com.example.videoplayer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.videoplayer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {

    EditText name, email, password, confirmPassword;
    Button register;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        register = findViewById(R.id.register);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().length() != 0 && email.getText().length() != 0 && password.getText().length() != 0 && confirmPassword.getText().length() != 0) {
                    try {
                        requestToRegistr();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(RegistrationActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void requestToRegistr() throws JSONException {

        String requestUrl = "https://video.orzu.org/api/v1.0/?type=register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject j = new JSONObject(response);
                    JSONObject data = j.getJSONObject("data");
                    String status = j.getString("api_status");
                    if (Integer.parseInt(status) == 200) {
                        editor.putBoolean("isLoged", true);
                        editor.putString("userId", data.getString("user_id"));
                        editor.putString("session", data.getString("session_id"));
                        editor.commit();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postMap = new HashMap<>();
                postMap.put("server_key", "e39111734a4e6a21dd442887dd5112c8");
                postMap.put("username", name.getText().toString());
                postMap.put("email", email.getText().toString());
                postMap.put("password", password.getText().toString());
                postMap.put("confirm_password", confirmPassword.getText().toString());
                return postMap;
            }
        };
        Volley.newRequestQueue(RegistrationActivity.this).add(stringRequest);
    }
}

