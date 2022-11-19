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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private String endDate, cookStatus;
    private ProgressBar progress_Bar;
    private TextView forgotPassword;
    private void checkUserAccessLevel(String username){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        
        reference.child("Cook").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        //Toast.makeText(MainActivity.this, "Successfully read", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        cookStatus = String.valueOf(dataSnapshot.child("cookStatus").getValue());
                        endDate = String.valueOf(dataSnapshot.child("endDate").getValue());
                            if(cookStatus.equals("active")) {
                                startActivity(new Intent(MainActivity.this, WelcomeCook.class));
                            }
                            else if(cookStatus.equals("temporarily suspended")) {
                                Date currentDay = new Date();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

                                if (endDate.compareTo(formatter.format(currentDay)) < 1) {
                                    startActivity(new Intent(MainActivity.this, TemporarilySuspended.class));
                                } else {
                                    startActivity(new Intent(MainActivity.this, WelcomeCook.class));
                                }
                            }
                            else if(cookStatus.equals("indefinitely suspended")){
                                startActivity(new Intent(MainActivity.this, IndefinetlySuspended.class));
                            }
                            else{Toast.makeText(MainActivity.this, "Something went wrong with cook status", Toast.LENGTH_SHORT).show();
                            }
                    } else {
                        reference.child("Client").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        Toast.makeText(MainActivity.this, username, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, Welcome.class));
                                    } else {
                                        reference.child("Admin").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().exists()) {
                                                        Toast.makeText(MainActivity.this, username, Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(MainActivity.this, WelcomeAdmin.class));
                                                    }
                                                    else{
                                                        Toast.makeText(MainActivity.this, "User Doesn't exist", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to read", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        }
                }else {
                    Toast.makeText(MainActivity.this, "Failed to read", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        register = (TextView) findViewById(R.id.reg);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.login_btn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.pswrd);

        progress_Bar = (ProgressBar) findViewById(R.id.progressBar2);


        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.ForgotPassword);
        forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.login_btn:
                userLogin();
                break;

            case R.id.ForgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;

        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }


        progress_Bar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        // redirect to user profile
                        checkUserAccessLevel(user.getUid());

                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify your account!",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();

                }
                progress_Bar.setVisibility(View.GONE);

            }
        });
    }
}