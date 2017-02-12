package com.example.telerik.urbanissues.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.models.BaseViewModel;
import com.example.telerik.urbanissues.models.MyUser;
import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.EverliveAppSettings;
import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.query.definition.UserSecretInfo;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import static com.example.telerik.urbanissues.common.Constants.APP_ID;

public class RegisterActivity  extends Activity implements View.OnClickListener {

    EverliveApp myApp;

    private EditText name;
    private EditText email;
    private EditText username;
    private EditText password;
    private Button registerButton;

    private User user = new User();
    private UserSecretInfo secretInfo = new UserSecretInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.name = (EditText) findViewById(R.id.register_name);
        MyTextWatcher textWatcher = new MyTextWatcher();
        this.name.addTextChangedListener(textWatcher);

        this.email = (EditText) findViewById(R.id.register_email);
        this.email.addTextChangedListener(textWatcher);

        this.username = (EditText) findViewById(R.id.register_username);
        this.username.addTextChangedListener(textWatcher);

        this.password = (EditText) findViewById(R.id.register_password);
        this.password.addTextChangedListener(textWatcher);

        this.registerButton = (Button) findViewById(R.id.btn_register);
        this.registerButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register : {
                this.initializeSdk();
                this.onRegisterClick();
                break;
            }
        }
    }

    private void onRegisterClick() {

        System.out.println(username.getText().toString());
        System.out.println(password.getText().toString());
        System.out.println(name.getText().toString());
        System.out.println(email.getText().toString());
/*
        registerUser
                (
                        myapp,
                username.getText().toString(),
                password.getText().toString(),
                name.getText().toString(),
                email.getText().toString());
        */
        final User user = new User();
        user.setUsername(username.getText().toString());
        user.setEmail(email.getText().toString());
        user.setDisplayName(name.getText().toString());
        UserSecretInfo secretInfo = new UserSecretInfo();
        secretInfo.setPassword(password.getText().toString());

        myApp.workWith().
                users(MyUser.class).
                create(user, secretInfo).
                executeAsync(new RequestResultCallbackAction() {
                    @Override
                    public void invoke(RequestResult requestResult) {
                        final String message;
                        final boolean hasErrors;
                        if (requestResult.getSuccess()) {
                            message = "User " + user.getDisplayName() + " created successfully.";
                            hasErrors = false;
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                                    .putBoolean("isUserRegistered", true).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                                    .putString("username", username.getText().toString()).commit();
                            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                                    .putString("password", password.getText().toString()).commit();
                        } else {
                            message = requestResult.getError().getMessage();
                            hasErrors = true;
                        }
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LoginActivity.showAlertMessage(RegisterActivity.this, message, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!hasErrors) {
                                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
    }

    private void updateRegisterButton() {
        if (this.name.getText().length() > 0 &&
                this.username.getText().length() > 0 &&
                this.password.getText().length() > 0) {
            this.registerButton.setEnabled(true);
        } else {
            this.registerButton.setEnabled(false);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateRegisterButton();
        }
    }

    private void initializeSdk() {
        String appId = APP_ID;
        EverliveAppSettings appSettings = new EverliveAppSettings();
        appSettings.setAppId(appId);
        appSettings.setUseHttps(true);

        myApp = new EverliveApp(appSettings);
    }
}
