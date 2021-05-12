package com.truevisionsa.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.truevisionsa.BaseActivity;
import com.truevisionsa.BuildConfig;
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.Fragments.AddDeviceFragment;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.R;
import com.truevisionsa.Utils.TinyDB;
import com.truevisionsa.UserPriviliges.UserPriviligesActivity;
import com.truevisionsa.ServerConfig.View.ServerConfigActivity;


public class LoginActivity extends BaseActivity implements Contract.View {

    private ProgressBar progressBar ;
    private EditText username ;
    private ShowHidePasswordEditText password;
    private Button btn_login ;
    private String get_username , get_pass ;
    private LoginPresenter loginPresenter;
    private DatabaseHelper databaseHelper ;
    private Config config ;
    private String user_id;
    private TinyDB tinyDB;
    private ImageView setting , phone;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        setListners();

        databaseHelper = new DatabaseHelper(this);

        tinyDB = new TinyDB(this);

        loginPresenter = new LoginPresenter(this , this);

        if (tinyDB.getString("id").equals("")){
            String android_id = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            tinyDB.putString("id" , android_id);

        }


    }


    private void initUI(){

        progressBar = findViewById(R.id.progress);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        phone = findViewById(R.id.phone);
        setting = findViewById(R.id.setting);
    }


    private void setListners(){

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getEdittextsTexts()){

                    showProgress();
                    loginPresenter.requestCheckUser(get_username , get_pass , tinyDB.getString("id") , config);
                }

            }
        });



        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                check_device();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this , ServerConfigActivity.class));
            }
        });
    }


    private boolean getEdittextsTexts(){

        boolean b = true ;

        get_username = username.getText().toString();
        get_pass = password.getText().toString();

        if (get_username.isEmpty()){

            username.setError(getResources().getString(R.string.error_usrname));

            b = false ;

        }

        else if (get_pass.isEmpty()){

            password.setError(getResources().getString(R.string.error_pass));

            b = false ;
        }

        else if (databaseHelper.getUser().size() == 0){

            Toast.makeText(this, getResources().getString(R.string.set_config), Toast.LENGTH_SHORT).show();

            b = false ;
        }

        else {
            config = databaseHelper.getUser().get(0);
        }

        return b ;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.add_device :


                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.config_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCheckFinished(String title) {

        user_id = title;

        loginPresenter.requestPublicIp();

    }


    public void check_device(){

        showProgress();

        loginPresenter.requestCheckDevice(tinyDB.getString("id") ,databaseHelper.getUser().get(0));
    }

    @Override
    public void ongetPublicIpFinished(String title) {

        android.net.wifi.WifiManager wm = (android.net.wifi.WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = android.text.format.Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        Config config = databaseHelper.getUser().get(0) ;

        loginPresenter.requestLogin(user_id , String.valueOf(BuildConfig.VERSION_NAME) , tinyDB.getString("id") ,
                "false" , title , ip , "unknown" , config.getServerIp() , config.getServerIp() , config);
    }

    @Override
    public void showDialog() {

        FragmentManager fm = getSupportFragmentManager();

        AddDeviceFragment addDeviceFragment = AddDeviceFragment.newInstance();
        addDeviceFragment.show(fm, "fragment_new_activity");
    }


    public void addDevice(String user_name, String pass, String device_name) {

        showProgress();
        loginPresenter.requestAddDevice(user_name , pass, tinyDB.getString("id") , device_name , databaseHelper.getUser().get(0));
    }

    @Override
    public void onAddFinished() {

        Toast.makeText(this, getResources().getString(R.string.succ_add_device), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFinished(String title) {

        tinyDB.putString("user_id" , user_id);

        tinyDB.putString("con_id" , title);

        startActivity(new Intent(LoginActivity.this , UserPriviligesActivity.class));

        showSnackBar(getResources().getString(R.string.hello) + get_username);

        finish();


    }

    @Override
    public void onFailure(int error) {

        Toast.makeText(this, getResources().getString(error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String error) {

        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {

        progressBar.setVisibility(View.GONE);
    }
}
