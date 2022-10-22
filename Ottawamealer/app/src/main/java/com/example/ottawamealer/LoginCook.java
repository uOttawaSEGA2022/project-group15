package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginCook extends AppCompatActivity implements View.OnClickListener {
    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progress_Bar;
    private TextView forgotPassword;
    private Button user_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cook);

        user_login = (Button) findViewById(R.id.cookLogin);
        user_login.setOnClickListener(this);
        register = (TextView) findViewById(R.id.regc);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.login_btnc);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.emailc);
        editTextPassword = (EditText) findViewById(R.id.pswrdc);

        progress_Bar = (ProgressBar) findViewById(R.id.progressBar2c);


        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.ForgotPasswordc);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regc:
                startActivity(new Intent(this, RegisterCook.class));
                break;

            case R.id.login_btnc:
                userLogin();
                break;

            case R.id.ForgotPasswordc:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
            case R.id.cookLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progress_Bar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser userCook = FirebaseAuth.getInstance().getCurrentUser();
                    if (userCook.isEmailVerified()) {
                        // redirect to user profile
                        startActivity(new Intent(LoginCook.this, WelcomeCook.class));
                    } else {
                        userCook.sendEmailVerification();
                        Toast.makeText(LoginCook.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginCook.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}