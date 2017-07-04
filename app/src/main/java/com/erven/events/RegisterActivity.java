package com.erven.events;

import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonReg;
    private EditText emailInput;
    private EditText passwordInput;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonReg = (Button) findViewById(R.id.buttonReg);
        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);

        buttonReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonReg) {
            register();
        }
    }

    private void register() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            showToast("Email nie może być pusty");
            return;
        }

        if(TextUtils.isEmpty(password)){
            showToast("Hasło nie może być puste");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                           showToast("Rejestracja przebiegła pomyślnie");
                            finish();
                        }
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
}
