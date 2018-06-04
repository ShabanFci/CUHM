package com.example.StepsGroupFCI.cuhm;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    public EditText userFName, userEmail, message;
    public Button reset, send;
    //FireBase
    DatabaseReference mDatabase;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        userFName = (EditText) view.findViewById(R.id.edtUserName);
        userEmail = (EditText) view.findViewById(R.id.edtUserEmail);
        message = (EditText) view.findViewById(R.id.edtMessage);
        reset = (Button) view.findViewById(R.id.reset);
        send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userFName.getText().toString();
                String email = userEmail.getText().toString();
                String msg = message.getText().toString();
                contactUs(name, email, msg);

                if (!isValidEmail(userEmail.getText().toString())) {
                    userEmail.setError("Invalid Email");
                    userEmail.requestFocus();
                } else if (message.getText().toString().isEmpty() || !isValidMessage(message.getText().toString())) {
                    message.setError("Message cannot be null");
                    message.requestFocus();

                } else {
                    Toast.makeText(getContext(), "Message Send", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userFName.setText("");
                userEmail.setText("");
                message.setText("");
            }
        });
        return view;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidMessage(String message) {
        String Message_PATTERN = "^[\\p{L} . '-]$";

        Pattern pattern = Pattern.compile(Message_PATTERN);
        Matcher matcher = pattern.matcher(message);
        return matcher.matches();
    }
    private void contactUs(final String Name, final String email, final String msg) {
        //Firebase Database
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        assert current_user != null;
        String uId = current_user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ContactUs").child(uId).push();
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("name", Name);
        userMap.put("email", email);
        userMap.put("message", msg);
        mDatabase.setValue(userMap);
        Toast.makeText(getActivity(), "Message Is Sent", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
}
