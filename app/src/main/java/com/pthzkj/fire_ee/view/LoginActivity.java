package com.pthzkj.fire_ee.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.*;
import com.pthzkj.fire_ee.R;
import com.pthzkj.fire_ee.deploy.Common;
import com.pthzkj.fire_ee.util.MapUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final String TAG = "LoginActivity";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    // UI references.
    private AutoCompleteTextView mAccountView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private Context context;

    //net
    private static final String appName = "HZKJ";

    //login for result
    private int r = 0;

    //通知显示内容
    private PendingIntent pd;
    //通知管理器
    private NotificationManager nm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_login);

        //获取当前版本号
        //System.out.println(ActivityUtil.getAppVersionName(mContext) + "<<<<<<<<<<<<<<<<<");
        //验证设备是否激活
        SharedPreferences preferences = this.getSharedPreferences("getValidate", MODE_PRIVATE);
        boolean result = preferences.getBoolean("validate", true);
        //如果没有激活，则弹出对话输入框，让其输入激活码
        if (!result) {
//            getValidate();
        }

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, LoginActivity.class);
        pd = PendingIntent.getActivity(LoginActivity.this, 0, intent, 0);
        //getStatusBar();
        //检查更新
//        VersionUtil versionUtil = new VersionUtil(this, getString(R.string.app_name), getPackageName(), nm, pd);
//        Thread versonThread = new Thread(versionUtil.versonRunnable);
//        versonThread.start();
        //	UpdateVersionUtil updateVersionUtil = new UpdateVersionUtil(mContext);
        //updateVersionUtil.checkUpdateInfo();

        init();
    }

    private void init(){
        // Set up the login form.
        mAccountView = (AutoCompleteTextView) findViewById(R.id.account);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {

        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;

        View focusView = null;

        // Check for a valid password, if the user entered one.
        //判断密码是否为空
        if (TextUtils.isEmpty(password) ) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        //判断账号是否为空
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            result(account, password);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mAccountView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public void result(String account, String password){
        RequestParams param = new RequestParams();
        param.put("accountLogin", account);
        param.put("accountPass", password);
        param.put("appName", appName);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(Common.URL + Common.FIRST_INTERFACE + "getLogin", param,new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                JSONObject jsonObject = null;
                boolean result = false;
                showProgress(false);
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        result = result(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private boolean result(JSONObject jsonObject) throws JSONException {
        String role;
        boolean result = false;
        role = jsonObject.get("role").toString();
        if (!role.equals("yes")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("登录失败，帐号或者密码有误，请重新输入！");
            builder.setPositiveButton("ok", null);
            builder.show();
            result = false;
        } else {
            Map map = MapUtil.getMapForJson(jsonObject.get("account")
                    .toString());
            Log.d(TAG, "result: " + jsonObject.get("zlck_power")
                    .toString());
            Log.d(TAG, "result: " + jsonObject.get("fzxt_power")
                    .toString());
            // SharedPreferences 保存数据的实现代码
            SharedPreferences sharedPreferences = context
                    .getSharedPreferences("account", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // 如果不能找到Editor接口。尝试使用 SharedPreferences.Editor
            editor.putString("op_id", map.get("opId").toString());//使用者ID
            editor.putString("op_name", map.get("opName").toString());//使用者名称
            editor.putString("op_role", map.get("accountRole").toString());//使用者权限
            editor.putString("account_level", map.get("accountLevel").toString());//使用者级别
            editor.putString("account_login", map.get("accountLogin").toString());//使用者账号
            editor.putString("account_dept", map.get("accountDept").toString());//使用者部门
            editor.putString("account_code", map.get("accountCode").toString());//使用者所属队伍
            editor.putString("zlck_power", jsonObject.get("zlck_power").toString());
            editor.putString("fzxt_power", jsonObject.get("fzxt_power").toString());
            // 我将用户信息保存到其中，你也可以保存登录状态
            editor.commit();
            result = true;
        }
        return result;
    }

//    /****
//     * 激活
//     */
//    public void getValidate(){
//        LayoutInflater factory = LayoutInflater.from(LoginActivity.this);
//        //得到自定义对话框
//        final View DialogView = factory.inflate(R.layout.validate_dialog, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("");
//        builder.setView(DialogView);
//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                EditText validate_text = (EditText) DialogView.findViewById(R.id.validate_text);
//                Log.d(TAG, "onClick: " + validate_text.getText().toString());
//                validateCode(validate_text.getText().toString());
//            }
//        });
//        builder.setCancelable(false);
//        builder.show();
//
//    }
//
//    /**
//     * 向服务器传递激活信息
//     */
//    public void validateCode(String code){
//        RequestParams params = new RequestParams();
//        params.put("imei", ActivityUtil.getImei(context));
//        params.put("activation", code);
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.post(URL + "/json/MCommon?action=getActivation", params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    JSONObject jsonObject = new JSONObject(new String(responseBody));
//                    boolean result = jsonObject
//                            .getBoolean("result");
//                    if (result) {
//
//                        SharedPreferences preferences = context.getSharedPreferences("getValidate", context.MODE_PRIVATE);
//                        // 点击体验
//                        SharedPreferences.Editor editor = preferences.edit();
//                        // 存入数据
//                        editor.putBoolean("validate", true);
//                        // 提交修改
//                        editor.commit();
//                        Toast.makeText(context, "激活成功！", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (jsonObject.get("message").toString().equals("NULL")) {
//                            Toast.makeText(context, "激活失败，请重新尝试或者请联系管理员！", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(context, "激活失败，失败原因：" + jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
//                        }
//                        getValidate();
//                        return;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                getValidate();
//                Toast.makeText(context, "连接失败，请联系管理员！", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}

