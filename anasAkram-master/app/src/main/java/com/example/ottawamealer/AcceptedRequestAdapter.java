package com.example.ottawamealer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class AcceptedRequestAdapter extends ArrayAdapter<MealRequest> {
    private Activity context;
    List<MealRequest> mealRequests;
    String fullName;


    public AcceptedRequestAdapter(Activity context, List<MealRequest> mealRequests){
        super(context,R.layout.approved_request_item,mealRequests);
        this.context = context;
        this.mealRequests = mealRequests;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.approved_request_item,null,true);

        //initialize the textviews

        TextView mealName = listViewItem.findViewById(R.id.mealName);
        TextView customer = listViewItem.findViewById(R.id.customerName);

        //get the information
        MealRequest request = mealRequests.get(position);

        mealName.setText(request.getMeal().getMealName());
        customer.setText(request.getCustomer());




        return listViewItem;
    }
}
