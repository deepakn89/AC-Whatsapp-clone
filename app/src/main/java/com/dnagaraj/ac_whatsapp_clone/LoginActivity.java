package com.dnagaraj.ac_whatsapp_clone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername,etPassword;
    private Button btnSignup,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(R.string.login);

        etUsername=findViewById(R.id.etLoginUsername);
        etPassword=findViewById(R.id.etLoginPassword);

        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnLogin);
                }
                return false;
            }
        });

        btnSignup=findViewById(R.id.btnSignup);
        btnLogin=findViewById(R.id.btnLogin);

        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){
            switchToWhatsAppUsersActivity();
        }
    }

    private void switchToWhatsAppUsersActivity() {
        startActivity(new Intent(LoginActivity.this,WhatsAppUsers.class));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnLogin:
                if (etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                    FancyToast.makeText(this, "None of the fields can be blank", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
                } else {
                    final ParseUser parseUser = new ParseUser();
                    parseUser.setUsername(etUsername.getText().toString());
                    parseUser.setPassword(etPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Logging in ..!");
                    progressDialog.show();

                    ParseUser.logInInBackground(etUsername.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user!=null&&e==null){
                                FancyToast.makeText(LoginActivity.this, ParseUser.getCurrentUser().getUsername() + " logging in..!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                switchToWhatsAppUsersActivity();
                            }else{
                                FancyToast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.btnSignup:
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
                break;
        }

    }

    public void rootLayoutTapped(View view) {
        try{
            InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}