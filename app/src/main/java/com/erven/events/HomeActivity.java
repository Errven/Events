package com.erven.events;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userText;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton addFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();

        addFab = (FloatingActionButton) findViewById(R.id.addFAB);
        userText = (TextView) findViewById(R.id.userText);
        addFab.setOnClickListener(this);
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
        if(view == addFab) {
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
            userText.setText(String.format("Witaj %s",firebaseAuth.getCurrentUser().getEmail()));
    }
}
