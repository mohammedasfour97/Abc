package com.truevisionsa.ServerConfig.View;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.BaseActivity;
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.R;
import com.truevisionsa.ServerConfig.Contract;
import com.truevisionsa.ServerConfig.Presenter.ServerConfigPresenter;



public class ServerConfigActivity extends BaseActivity implements Contract.ServerConfig.View {

    private EditText server_ip, server_port, server_username, default_schema, web_ip, web_port;
    private EditText server_password ;
    private ImageView pass_vis ,back;
    private CheckBox checkBox;
    private Button save, cancel;
    private String get_server_ip, get_server_port, get_server_username, get_password, get_default_schema, get_web_ip, get_web_port;
    private DatabaseHelper databaseHelper;
    private Config config;
    private Contract.ServerConfig.Presenter presenter;
    private TextView database_config , web_config;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_config);

        initUI();
        setListners();
        setTextViews();

        presenter = new ServerConfigPresenter(this , this);

        databaseHelper = new DatabaseHelper(this);

        if (checkEditData())  setEditData();
    }


    private void initUI() {

        server_ip = findViewById(R.id.server_ip);
        server_password = findViewById(R.id.server_pass);
        server_port = findViewById(R.id.server_port);
        server_username = findViewById(R.id.server_username);
        default_schema = findViewById(R.id.server_default_schema);
        web_ip = findViewById(R.id.web_ip);
        web_port = findViewById(R.id.web_port);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
      //  pass_vis = findViewById(R.id.password_vis);
        checkBox = findViewById(R.id.password_vis);
        web_config = findViewById(R.id.web_conf);
        database_config = findViewById(R.id.database_conf);
        back = findViewById(R.id.back);
    }


    private void setTextViews(){

        Spannable spannable;

        spannable= new SpannableString(getResources().getString(R.string.db_config));
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        database_config.setText(spannable, TextView.BufferType.SPANNABLE);

        spannable= new SpannableString(getResources().getString(R.string.webconfig));
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        web_config.setText(spannable, TextView.BufferType.SPANNABLE);
    }
    private void setListners() {


   /*     pass_vis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pass_vis.getDrawable() == getResources().getDrawable(R.drawable.ic_custom_hide)) {

                    server_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    pass_vis.setImageDrawable(getResources().getDrawable(R.drawable.ic_custom_show));

                }

                else {

                    server_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    pass_vis.setImageDrawable(getResources().getDrawable(R.drawable.ic_custom_hide));
                }
            }
        });

    */

      checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
         public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if (!b)  server_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            else server_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
         }
     });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getEdittextsTexts()) {

                    config = new Config(get_server_ip, get_server_port, get_server_username, get_password, get_default_schema, get_web_ip, get_web_port);

                    if (!checkEditData()) {

                        databaseHelper.insertUser(config);


                    } else {

                        databaseHelper.deleteAndInsertUser(config);

                    }

                    showProgress();
                    presenter.requestCheckCon(config);
                }


            }
        });
    }


    private boolean checkEditData(){

        if (databaseHelper.getUser().size() > 0) {

            return true;

        }
        else return false;
    }


    private boolean getEdittextsTexts() {

        boolean b = true;

        get_default_schema = default_schema.getText().toString();
        get_password = server_password.getText().toString();
        get_server_ip = server_ip.getText().toString();
        get_server_port = server_port.getText().toString();
        get_server_username = server_username.getText().toString();
        get_web_ip = web_ip.getText().toString();
        get_web_port = web_port.getText().toString();

        if (get_server_username.isEmpty()) {

            server_username.setError(getResources().getString(R.string.enter_server_username));

            b = false;

        } else if (get_server_port.isEmpty()) {

            server_port.setError(getResources().getString(R.string.enter_server_port));

            b = false;
        } else if (get_server_ip.isEmpty()) {

            server_ip.setError(getResources().getString(R.string.enter_server_ip));

            b = false;
        } else if (get_password.isEmpty()) {

            server_password.setError(getResources().getString(R.string.enter_server_password));

            b = false;
        } else if (get_default_schema.isEmpty()) {

            default_schema.setError(getResources().getString(R.string.enter_default_schema));

            b = false;
        } else if (get_web_port.isEmpty()) {

            web_port.setError(getResources().getString(R.string.enter_web_port));

            b = false;
        } else if (get_web_ip.isEmpty()) {

            web_ip.setError(getResources().getString(R.string.enter_web_ip));

            b = false;
        }

        return b;

    }


    private void setEditData() {

        Config config = databaseHelper.getUser().get(0);

        server_password.setText(config.getServerPassword());
        server_ip.setText(config.getServerIp());
        server_port.setText(config.getServerPort());
        server_username.setText(config.getServerUserName());
        default_schema.setText(config.getDefaultSchema());
        web_ip.setText(config.getWebIp());
        web_port.setText(config.getWebPort());
    }

    @Override
    public void onFinished() {

        Toast.makeText(ServerConfigActivity.this, getResources().getString(R.string.succ_config_set), Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public void onFailure(int err) {

        Toast.makeText(this, getResources().getString(err), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String err) {

        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {

        showProgressDialog();
    }

    @Override
    public void hideProgress() {

        hideProgressDialog();
    }
}

