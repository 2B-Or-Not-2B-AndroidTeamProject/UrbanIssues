package com.example.telerik.urbanissues.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telerik.urbanissues.R;
import com.example.telerik.urbanissues.models.BaseViewModel;
import com.example.telerik.urbanissues.models.MyUser;
import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.model.system.User;
import com.telerik.everlive.sdk.core.query.definition.UserSecretInfo;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import static com.example.telerik.urbanissues.activities.MainActivity.urbanIssuesApp;


public class RegisterActivity  extends Activity implements View.OnClickListener {

    private Boolean exit = false;

    private EditText name;
    private EditText email;
    private EditText username;
    private EditText password;
    private Button registerButton;

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

        findViewById(R.id.register_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register : {
                this.onRegisterClick();
                break;
            }
            case R.id.register_login : {
                Intent intent_login = new Intent(this, LoginActivity.class);
                intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
                finish(); // destroy current activity..
                startActivity(intent_login);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    private void onRegisterClick() {

        System.out.println(username.getText().toString());
        System.out.println(password.getText().toString());
        System.out.println(name.getText().toString());
        System.out.println(email.getText().toString());

        final User user = new User();
        user.setUsername(username.getText().toString());
        user.setEmail(email.getText().toString());
        user.setDisplayName(name.getText().toString());
        UserSecretInfo secretInfo = new UserSecretInfo();
        secretInfo.setPassword(password.getText().toString());

        urbanIssuesApp.workWith().
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
                                            Intent intent_login = new Intent(RegisterActivity.this, LoginActivity.class);
                                            intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
                                            finish(); // destroy current activity..
                                            startActivity(intent_login);
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
}
