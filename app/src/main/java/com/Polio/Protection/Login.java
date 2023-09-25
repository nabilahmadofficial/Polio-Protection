package com.Polio.Protection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.Polio.Protection.admin.Adminhome;
import com.Polio.Protection.team.Teamhome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText username_text, password_text;
    Button login;
    String email, password;
    private FirebaseAuth mAuth;

    ProgressBar pr;

    ImageView password_image;
    private int passwordNotVisible=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pr = findViewById(R.id.progressBar);
        pr.setVisibility(View.INVISIBLE);

        username_text = findViewById(R.id.login_usename);
        password_text = findViewById(R.id.login_password);
        login = findViewById(R.id.login_btn);
        password_image = findViewById(R.id.password_show_hide);

        password_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passwordNotVisible == 1) {
                    password_text.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_open));
                    passwordNotVisible = 0;
                } else {
                    password_text.setTransformationMethod(new PasswordTransformationMethod());
                    password_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_hide));
                    passwordNotVisible = 1;
                }
                password_text.setSelection(password_text.length());
            }
        });

        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                pr.setVisibility(View.VISIBLE);
                email = username_text.getText().toString().trim();
                password = password_text.getText().toString().trim();

                if (email.isEmpty()) {
                    username_text.setError(getString(R.string.input_error_email));
                    username_text.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    password_text.setError(getString(R.string.input_error_password));
                    password_text.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                            String uid = mAuth.getInstance().getCurrentUser().getUid();

                            ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.child("type").getValue().toString();

                                    if(name.equals("admin")) {
                                        Intent homeintent = new Intent(Login.this , Adminhome.class);
                                        startActivity(homeintent);
                                        pr.setVisibility(View.INVISIBLE);
                                        finish();
                                    }
                                    else if(name.equals("team")) {
                                        Intent homeintent = new Intent(Login.this , Teamhome.class);
                                        startActivity(homeintent);
                                        pr.setVisibility(View.INVISIBLE);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(Login.this, "Something is wrong..", Toast.LENGTH_LONG).show();
                                        pr.setVisibility(View.INVISIBLE);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        } else {
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            pr.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
}
