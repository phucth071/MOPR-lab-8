package vn.hcmute.lab8;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import vn.hcmute.lab8.activity.LoginActivity;
import vn.hcmute.lab8.model.User;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_ID = "keyid";
    private static final String KEY_FIRSTNAME = "keyfirstname";
    private static final String KEY_LASTNAME = "keylastname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_AVATAR = "keyavatar";
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    public SharedPrefManager(Context context) {
        mCtx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences.Editor editor = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                .edit();

        editor.putLong(KEY_ID, user.getId() == null ? -1 : user.getId());
        editor.putString(KEY_FIRSTNAME, user.getFirstName());
        editor.putString(KEY_LASTNAME, user.getLastName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_AVATAR, user.getAvatar());
        editor.putInt(KEY_GENDER, user.getGender());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getLong(KEY_ID, -1),
                sharedPreferences.getString(KEY_FIRSTNAME, null),
                sharedPreferences.getString(KEY_LASTNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_AVATAR, null),
                sharedPreferences.getInt(KEY_GENDER, -1));
    }

    public void logout() {
        SharedPreferences.Editor editor = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
