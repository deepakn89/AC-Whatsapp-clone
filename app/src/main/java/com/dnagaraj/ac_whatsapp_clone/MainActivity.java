package com.dnagaraj.ac_whatsapp_clone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private EditText etEmail,etUsername,etPassword;
    private Button btnSignup,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //setTitle(R.string.signup);

//        Parse.initialize(this).
        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

        etEmail=findViewById(R.id.etEmail);
        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(btnSignup);
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnSignup:
                if (etEmail.getText().toString().equals("") || etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                    FancyToast.makeText(this, "None of the fields can be blank", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
                } else {
                    final ParseUser parseUser = new ParseUser();
                    parseUser.setEmail(etEmail.getText().toString());
                    parseUser.setUsername(etUsername.getText().toString());
                    parseUser.setPassword(etPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing up ..!");
                    progressDialog.show();

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(MainActivity.this, ParseUser.getCurrentUser().getUsername() + " signed up..!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                switchToWhatsAppUsersActivity();
                            } else {
                                FancyToast.makeText(MainActivity.this, e.getCode()+e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.btnLogin:
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
                break;
        }

    }

    private void switchToWhatsAppUsersActivity() {
        startActivity(new Intent(MainActivity.this,WhatsAppUsers.class));
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