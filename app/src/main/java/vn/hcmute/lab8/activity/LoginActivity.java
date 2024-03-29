package vn.hcmute.lab8.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import vn.hcmute.lab8.R;
import vn.hcmute.lab8.SharedPrefManager;
import vn.hcmute.lab8.VolleySingle;
import vn.hcmute.lab8.constants;
import vn.hcmute.lab8.model.User;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
//    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

//        progressBar = findViewById(R.id.progressBar);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    private void userLogin() {
        final String email = edtEmail.getText().toString();
        final String password = edtPassword.getText().toString();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();

        Log.d("URL", "LOGIN_URL: " + constants.URL_LOGIN);

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Please enter your email");
            edtEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Please enter your password");
            edtPassword.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPONSE", "Response: " + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject userJson = obj.getJSONObject("data");
                            if (obj.getString("status").equals("OK")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                User user = new User();
                                user.setId(userJson.getLong("id"));
                                user.setEmail(userJson.getString("email"));
                                user.setFirstName(userJson.getString("firstName"));
                                user.setLastName(userJson.getString("lastName"));
                                user.setGender(userJson.getInt("gender"));
                                user.setAvatar(userJson.getString("avatar"));
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                Log.d("RESPONSE", "User JSON: " + user);
                                finish();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        VolleySingle.getInstance(this).addToRequestQueue(stringRequest);
    }
}