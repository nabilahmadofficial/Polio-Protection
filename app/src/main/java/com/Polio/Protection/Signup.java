package com.Polio.Protection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.Polio.Protection.admin.Teams.AdminTeamsLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText user_name, user_password;
    private RadioGroup radioGroup;
    Button signup;

    String email, type, id_teams="123456";
    String password_teams;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        user_name = findViewById(R.id.signup_usename);
        user_password = findViewById(R.id.singup_password);
        radioGroup = findViewById(R.id.user_type);
        signup = findViewById(R.id.signup_btn);

        mAuth = FirebaseAuth.getInstance();

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = group.findViewById(checkedId);
                    }
                });

        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId == -1) {
                    Toast.makeText(Signup.this, "Please selete user type ", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);

                    email = user_name.getText().toString().trim();
                    password_teams = user_password.getText().toString().trim();
                    type = radioButton.getText().toString().trim();

                    if (email.isEmpty()) {
                        user_name.setError(getString(R.string.input_error_email));
                        user_name.requestFocus();
                        return;
                    }
                    if (password_teams.isEmpty()) {
                        user_password.setError(getString(R.string.input_error_password));
                        user_password.requestFocus();
                        return;
                    }
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password_teams)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        AdminTeamsLogin adminTeamsLogin = new AdminTeamsLogin(

                                                id_teams,email, type
                                        );
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(adminTeamsLogin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                } else {
                                                    //display a failure message
                                                    Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}

