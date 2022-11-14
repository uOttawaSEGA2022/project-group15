package com.example.ottawamealer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.zip.Inflater;

public class ComplaintList extends ArrayAdapter<Complaint> {
    private Activity context;
    List<Complaint> complaints;

    public ComplaintList(Activity context, List<Complaint> complaints){
        super(context,R.layout.complaint_list,complaints);
        this.context = context;
        this.complaints = complaints;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.complaint_list,null,true);

        TextView returnCook = (TextView) listViewItem.findViewById(R.id.returnCook);
        TextView returnComplaint = (TextView) listViewItem.findViewById(R.id.returnComplaint);

        Complaint complaint = complaints.get(position);
        returnCook.setText(complaint.getCookName());
        returnComplaint.setText(complaint.getComplaint());




        return listViewItem;
    }
}
