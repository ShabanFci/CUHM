package com.fci.steps.cuhm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mohamed on 6/6/2018.
 */

public class ListViewAdapter extends ArrayAdapter<Notifications> {

    //the hero list that will be displayed
    private List<Notifications> notification_list;

    //the context object
    private Context mCtx;

    //here we are getting the herolist and context
    //so while creating the object of this adapter class we need to give herolist and context
    public ListViewAdapter(List<Notifications> notification_list, Context mCtx) {
        super(mCtx, R.layout.notifications_list, notification_list);
        this.notification_list = notification_list;
        this.mCtx = mCtx;
    }

    //this method will return the list item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //getting the layoutInflater
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        //creating a view with our xml layout
        View listViewItem = inflater.inflate(R.layout.notifications_list, null, true);

        //getting text views
        ImageView imageView = listViewItem.findViewById(R.id.users_profile);
        TextView user_name = listViewItem.findViewById(R.id.users_name);
        TextView textViewProblem = listViewItem.findViewById(R.id.problem);
        TextView textViewProblemDescription = listViewItem.findViewById(R.id.description_problem);


        //Getting the hero for the specified position
        Notifications notifications = notification_list.get(position);

        String problem = notifications.getProblem().toString();
        String description_problem = notifications.getDescription_problem().toString();

        //setting hero values to textViews
        user_name.setText(notifications.getFirst_name() + " " + notifications.getLast_name());
        textViewProblem.setText(problem);
        textViewProblemDescription.setText(description_problem);

        if (problem.equals("Fire")) {
            imageView.setImageResource(R.drawable.fire);
        }
        if (problem.equals("Traffic")) {
            imageView.setImageResource(R.drawable.traffic);
        }
        if (problem.equals("Education")) {
            imageView.setImageResource(R.drawable.education);
        }
        if (problem.equals("Medical")) {
            imageView.setImageResource(R.drawable.medical);
        }
        if (problem.equals("Daily Problems")) {
            imageView.setImageResource(R.drawable.social);
        }

        //returning the listItem
        return listViewItem;
    }
}