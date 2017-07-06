package com.erven.events;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userText;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton addFab;
    private DatabaseReference databaseReference;
    private ValueEventListener postListener;
    private List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        addFab = (FloatingActionButton) findViewById(R.id.addFAB);
        userText = (TextView) findViewById(R.id.userText);


        addFab.setOnClickListener(this);

        databaseReference.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events.clear();
                for (DataSnapshot eventsSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventsSnapshot.getValue(Event.class);
                    events.add(event);
                }
                EventAdapter adapter = new EventAdapter(getApplicationContext(), R.layout.event_list_item, events);
                ListView eventsListView = (ListView) findViewById(R.id.eventsListView);
                eventsListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    class EventAdapter extends ArrayAdapter<Event> {

        private List<Event> eventList;
        private int resource;
        private LayoutInflater inflater;

        public EventAdapter(Context context, int resource, List<Event> objects) {
            super(context, resource, objects);
            eventList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.event_list_item, parent, false);

            TextView textViewName = (TextView) convertView.findViewById(R.id.textView_name);
            TextView textViewPlace = (TextView) convertView.findViewById(R.id.textView_place);
            TextView textViewDate = (TextView) convertView.findViewById(R.id.textView_date);
            TextView textViewOwner = (TextView) convertView.findViewById(R.id.textView_owner);
            TextView textViewTime = (TextView) convertView.findViewById(R.id.textView_time);

            textViewName.setText(events.get(position).getName());
            textViewPlace.setText(events.get(position).getPlace());
            textViewDate.setText(events.get(position).getDate());
            textViewOwner.setText(events.get(position).getOwner());
            textViewTime.setText(events.get(position).getTime());
            return convertView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == addFab) {
            startActivity(new Intent(this, AddEventActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null)
            userText.setText(String.format("Witaj %s", firebaseAuth.getCurrentUser().getEmail()));
    }

    public void getEvents() {
        //eventsList = databaseReference.child("events").getDatabase();
    }


}
