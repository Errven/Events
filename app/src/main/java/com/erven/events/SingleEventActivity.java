package com.erven.events;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SingleEventActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Event event;
    private TextView textViewName;
    private TextView textViewPlace;
    private TextView textViewTime;
    private TextView textViewDate;
    private TextView textViewOwner;
    private Button buttonDelete;
    private Button buttonJoin;
    private String user;
    private ListView listViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewPlace = (TextView) findViewById(R.id.textViewPlace);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewOwner = (TextView) findViewById(R.id.textViewOwner);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonJoin = (Button) findViewById(R.id.buttonJoin);
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        buttonDelete.setOnClickListener(this);
        buttonJoin.setOnClickListener(this);

        String event_id = getIntent().getStringExtra("event_id");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser().getEmail();

        databaseReference.child("events/" + event_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                event = dataSnapshot.getValue(Event.class);
                populateView();
                checkIfOwner();
                populateList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonDelete) {
            new AlertDialog.Builder(this).setTitle("Usuwanie").setMessage("Czy na pewno chcesz usunąć to wydarzenie?")
                    .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteEvent();
                        }
                    })
                    .setNegativeButton("NIE", null).show();
        } else if (v == buttonJoin) {
            new AlertDialog.Builder(this).setTitle(event.getName()).setMessage("Czy chcesz dołączyć do tego wydarzenia?")
                    .setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            joinEvent();
                        }
                    })
                    .setNegativeButton("NIE", null).show();

        }
    }

    private void populateView() {
        textViewName.setText(event.getName());
        textViewPlace.setText(event.getPlace());
        textViewTime.setText(event.getTime());
        textViewOwner.setText(event.getOwner());
        textViewDate.setText(event.getDate());
    }

    private void populateList() {
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, event.getUsers());
        listViewUsers.setAdapter(adapter);
    }

    private void checkIfOwner() {
        if (event.getOwner().equals(user)) {
            buttonDelete.setVisibility(View.VISIBLE);
        }
    }

    private void joinEvent() {
        if (event.users.contains(user))
            Toast.makeText(getApplicationContext(), "Już uczestniczysz w tym wydarzeniu!", Toast.LENGTH_SHORT).show();
        else {
            event.users.add(user);
            databaseReference.child("events/" + event.getId() + "/users").setValue(event.users);
        }
    }

    private void deleteEvent() {

    }
}
