package com.fci.steps.cuhm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mohamed on 6/9/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private List<Notifications> mNotificationList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name, textViewProblem, textViewProblemDescription;
        public ImageView imageView;
        public RelativeLayout parentLayout;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.users_profile);
            user_name = view.findViewById(R.id.users_name);
            textViewProblem = view.findViewById(R.id.problem);
            textViewProblemDescription = view.findViewById(R.id.description_problem);
            parentLayout = view.findViewById(R.id.parent_layout);
        }

    }

    public RecyclerViewAdapter(Context context, List<Notifications> Notifications) {
        this.mNotificationList = Notifications;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notifications_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: called.");

        Notifications notifications = mNotificationList.get(position);

        final String first_name = notifications.getFirst_name().toString();
        final String last_name = notifications.getLast_name().toString();
        final String problem = notifications.getProblem().toString();
        final String description_problem = notifications.getDescription_problem().toString();

        holder.user_name.setText(first_name + " " + last_name);
        holder.textViewProblem.setText(problem);
        holder.textViewProblemDescription.setText(description_problem);

        if (problem.equals("Fire")) {
            holder.imageView.setImageResource(R.drawable.fire);
        } else if (problem.equals("Traffic")) {
            holder.imageView.setImageResource(R.drawable.traffic);
        } else if (problem.equals("Education")) {
            holder.imageView.setImageResource(R.drawable.education);
        } else if (problem.equals("Medical")) {
            holder.imageView.setImageResource(R.drawable.medical);
        } else if (problem.equals("Daily Problems")) {
            holder.imageView.setImageResource(R.drawable.social);
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(mContext, NotificationDetailsActivity.class);
                intent.putExtra("first_name", first_name);
                intent.putExtra("last_name", last_name);
                intent.putExtra("problem", problem);
                intent.putExtra("description_problem", description_problem);

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

}

