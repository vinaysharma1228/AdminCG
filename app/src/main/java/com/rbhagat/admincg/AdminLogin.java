package com.rbhagat.admincg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminLogin extends AppCompatActivity {
    String selectedCity;
    EditText adminEmail,adminPassword;
    TextView loginBtn;

    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ImageView pass_img;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);


        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog=new ProgressDialog(this);

        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);

        adminEmail=findViewById(R.id.adminEmailLogin);
        adminPassword=findViewById(R.id.adminPasswordLogin);
        loginBtn=findViewById(R.id.adminloginSignInBtn);
        spinner = findViewById(R.id.spinner);
        pass_img = findViewById(R.id.pass_image);



        final boolean[] isPasswordVisible = {false};

        pass_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible[0] = !isPasswordVisible[0];

                if (isPasswordVisible[0]) {
                    adminPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass_img.setImageResource(R.drawable.open_eye);
                } else {
                    adminPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass_img.setImageResource(R.drawable.close_eye);
                }

                // Move cursor to the end of the text
                adminPassword.setSelection(adminPassword.getText().length());
            }
        });



//
//
     //    ArrayList<String> itemList = new ArrayList<>(Arrays.asList(items));
//
//        // Add a new value
//        itemList.add("New City");
//        itemList.add("Other New City");
//
//        // Convert back to an array
//        items = itemList.toArray(new String[0]);


        ArrayList<String> itemList = new ArrayList<>();

        DatabaseReference ref = database.getReference("cities");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                itemList.clear();
                // dataSnapshot.getChildren() gives you access to the child nodes
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String city = childSnapshot.getValue(String.class);
                    itemList.add(city);

                }

                String[] itemsArray = itemList.toArray(new String[0]);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AdminLogin.this, android.R.layout.simple_spinner_item, itemsArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error reading data: " + error.getCode());
            }
        });



        //selected city!
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = (String) parent.getItemAtPosition(position);
                // Do something with the selected item
                // For example, you can display it in a TextView
                Toast.makeText(AdminLogin.this, selectedCity, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do something when nothing is selected

            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                if(adminEmail.getText().toString().isEmpty() && adminPassword.getText().toString().isEmpty())
                {
                    progressDialog.dismiss();
                    Toast.makeText(AdminLogin.this, "Email and Password is Required.", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    auth.signInWithEmailAndPassword(adminEmail.getText().toString(),adminPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {
                                DatabaseReference reference = database.getReference().child("AdminDetails").child(auth.getUid());

                                adminModel adminModel=new adminModel(adminEmail.getText().toString(),adminPassword.getText().toString(),selectedCity, auth.getUid());
                                reference.setValue(adminModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful())
                                        {
                                            progressDialog.dismiss();
                                            Intent intent=new Intent(AdminLogin.this,Dashboard.class);
                                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(AdminLogin.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });



                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(AdminLogin.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });


                }


            }
        });





    }
}