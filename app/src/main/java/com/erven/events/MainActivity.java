package com.erven.events;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private Button buttonLogin;
    private EditText emailInput;
    private EditText passwordInput;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);

        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);

        if (firebaseAuth.getCurrentUser() != null) userLogged();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {
            login();
        } else if (view == buttonRegister) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }


    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showToast("Email nie może być pusty");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("Hasło nie może być puste");
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showToast("Pomyślnie zalogowano");
                        userLogged();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(e.getLocalizedMessage());
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void userLogged() {
        finish();
        startActivity(new Intent(this, HomeActivity.class));
    }
}
