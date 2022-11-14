package com.example.ottawamealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeAdmin extends AppCompatActivity implements View.OnClickListener {

    private Button inbox, cooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);

        inbox = (Button) findViewById(R.id.inbox_admin_button);
        inbox.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.inbox_admin_button:
                startActivity(new Intent(this, AdminInbox.class));
                break;

        }
    }
}