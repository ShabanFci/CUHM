package com.fci.steps.cuhm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mohamed on 6/9/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Notifications> mNotificationList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name, textViewProblem, textViewProblemDescription;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.users_profile);
            user_name = view.findViewById(R.id.users_name);
            textViewProblem = view.findViewById(R.id.problem);
            textViewProblemDescription = view.findViewById(R.id.description_problem);

        }

    }

    public RecyclerViewAdapter(List<Notifications> Notifications) {
        mNotificationList = Notifications;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notifications_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notifications notifications = mNotificationList.get(position);

        String first_name = notifications.getFirst_name().toString();
        String last_name = notifications.getLast_name().toString();
        String problem = notifications.getProblem().toString();
        String description_problem = notifications.getDescription_problem().toString();

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
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

}

