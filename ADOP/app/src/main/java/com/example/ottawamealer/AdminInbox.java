package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminInbox extends AppCompatActivity implements View.OnClickListener {

    TextView change;
    Button toFire;

    private void getCookData(String uid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();

                        for(DataSnapshot dsp :dataSnapshot.child("listOfComplaints").getChildren()){
                            String data = String.valueOf(dsp.child("cookName").getValue());
                            String data1 = String.valueOf(dsp.child("complaints").getValue());
                            if(data1.equals("")==false){
                                change.append("Name: " +data +" \nThe complaint: " + data1 + "\n\n");
                            }
                        }

                    }

                    else {
                        Toast.makeText(AdminInbox.this, "Failed to take admin info", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AdminInbox.this, "Failed to read", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inbox);

        change = (TextView) findViewById(R.id.listOfComplaints);
        change.setEnabled(false);
        toFire= (Button)findViewById(R.id.dismissOrSuspend);
        toFire.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        getCookData(user.getUid());




    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dismissOrSuspend:
                startActivity(new Intent(this, FireCook.class));
        }
    }
}
