package com.example.ottawamealer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class RequestAdapter extends ArrayAdapter<MealRequest> {
    private Activity context;
    List<MealRequest> mealRequests;

    String fullName;



    public RequestAdapter(Activity context, List<MealRequest> mealRequests){
        super(context,R.layout.pending_request_item,mealRequests);
        this.context = context;
        this.mealRequests = mealRequests;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.pending_request_item,null,true);



        //initialize Textviews

        TextView mealName = (TextView) listViewItem.findViewById(R.id.mealName);
        TextView customerName = (TextView) listViewItem.findViewById(R.id.customer);


        //intitialize the buttons
        Button approveButton = (Button) listViewItem.findViewById(R.id.approveButton);
        Button declineButton = (Button) listViewItem.findViewById(R.id.declineButton);


        //get the meal request
        MealRequest mealRequest = mealRequests.get(position);
        //get the meal
        Meal meal = mealRequest.getMeal();
        //return info to respective location
        mealName.setText(meal.getMealName());
        customerName.setText(mealRequest.getCustomer());



        //get current cook name
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference nameRef = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(userID).child("fullName");
        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullName = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealRequest mealRequest = mealRequests.get(position);

                //remove, then update
                mealRequests.remove(position);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests");
                reference.child("Pending").child(fullName).child(mealRequest.getID()).setValue(null);




                mealRequest.acceptRequest();


                //add to firebase
                reference.child("Accepted").child(fullName).child(mealRequest.getID()).setValue(mealRequest);
                notifyDataSetChanged();
            }
        });




        return listViewItem;
    }
}
