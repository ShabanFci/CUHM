package com.fci.steps.cuhm;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends Fragment {

    View view;
    FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter;
    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersList = view.findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                        Users.class,
                        R.layout.users_layout,
                        UsersViewHolder.class,
                        mUsersDatabase
                ) {
                    @Override
                    protected void populateViewHolder(UsersViewHolder usersViewHolder, Users users, int position) {
//                        Log.d("ref", String.valueOf(users));
                        String g = firebaseRecyclerAdapter.getRef(position).getKey();

                        usersViewHolder.setDisplayName(users.getFname() +" " +users.getlname());

                        usersViewHolder.setDisplayStatus(users.getJob());
                        usersViewHolder.setDisplayImage(users.getThumb_image(), getContext());

                        final String userId = getRef(position).getKey();

                        usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
                                profileIntent.putExtra("userId", userId);
                                startActivity(profileIntent);
                            }
                        });
                    }
                };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDisplayName(String name) {
            TextView userNameView = mView.findViewById(R.id.users_name);
            userNameView.setText(name);
        }

        public void setDisplayStatus(String job) {
            TextView userStatusView = mView.findViewById(R.id.users_status);
            userStatusView.setText(job);
        }

        public void setDisplayImage(String thumImage, Context context) {
            CircleImageView userImageProfile = mView.findViewById(R.id.users_profile);
            Picasso.with(context).load(thumImage).placeholder(R.mipmap.ic_launcher_round).into(userImageProfile);
        }
    }
}
