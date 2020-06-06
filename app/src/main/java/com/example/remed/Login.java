package com.example.remed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText emailView;
    private EditText passwordView;
    private Button loginButton;
    private Button registerButton;

    boolean login = true;
    static String listName ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        loginButton = (Button) findViewById(R.id.login_login);
        emailView = (EditText) findViewById(R.id.login_email);
        passwordView = findViewById(R.id.login_password);
        registerButton = findViewById(R.id.login_register);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }

    public void registerNewUser(View v) {
        login = false;
        Intent intent = new Intent(this, Register.class);
        finish();
        startActivity(intent);
    }

    public void signInUser(View v) {
        String email = emailView.getText().toString();
        listName = email;
        String password = passwordView.getText().toString();

        if (email.equals("") || password.equals(""))
            return;

        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAGG", "signInWithEmail:success");
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            //User user = new User(firebaseUser, displayName);

                            Intent intent = new Intent(Login.this, AfterLoginPage.class);
                            //intent.putExtra(user_name_from_login, displayName);
                            finish();
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAGG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public String listName(){

        return listName;

    }

    public boolean isLogin(){

        return login;

    }
}
