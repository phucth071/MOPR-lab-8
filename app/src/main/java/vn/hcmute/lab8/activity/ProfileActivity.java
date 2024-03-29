package vn.hcmute.lab8.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.hcmute.lab8.R;
import vn.hcmute.lab8.SharedPrefManager;
import vn.hcmute.lab8.model.User;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    CircleImageView imgAvatar;
    TextView tv_userid_value, tv_name_value, tv_email_value, tv_gender_value;
    Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            MappingView();
            User user = SharedPrefManager.getInstance(this).getUser();
            tv_userid_value.setText(String.valueOf(user.getId()));
            tv_name_value.setText(user.getFirstName() + " " + user.getLastName());
            tv_email_value.setText(user.getEmail());
            tv_gender_value.setText(user.getGender() == 1 ? "Male" : "Female");
            Glide.with(getApplicationContext()).load(user.getAvatar()).into(imgAvatar);
            btn_logout.setOnClickListener(this);
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

    private void MappingView() {
        imgAvatar = findViewById(R.id.profile_image);
        tv_userid_value = findViewById(R.id.tv_userid_value);
        tv_name_value = findViewById(R.id.tv_username_value);
        tv_email_value = findViewById(R.id.tv_email_value);
        tv_gender_value = findViewById(R.id.tv_gender_value);
        btn_logout = findViewById(R.id.btn_logout);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btn_logout)) {
            SharedPrefManager.getInstance(this).logout();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }
}