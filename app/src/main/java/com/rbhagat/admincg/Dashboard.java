package com.rbhagat.admincg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {

    TextView city,raisedCount,dustbinCount,resolvedCount;
    FirebaseAuth auth;
    LottieAnimationView logOut;
    LinearLayout raised,dustbin,resolved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        city=findViewById(R.id.city);
        auth=FirebaseAuth.getInstance();
        logOut=findViewById(R.id.logOut);
        raised=findViewById(R.id.raised);
        dustbin=findViewById(R.id.dustbinLayout);
        raisedCount=findViewById(R.id.raisedNumber);
        dustbinCount=findViewById(R.id.dustbinNumber);
        resolved=findViewById(R.id.ResolvedLayout);
        resolvedCount=findViewById(R.id.ResolvedNumber);






        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AdminDetails").child(auth.getUid()).child("city");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String adminCity = dataSnapshot.getValue(String.class);
                city.setText(adminCity);

                DatabaseReference countRef = FirebaseDatabase.getInstance().getReference().child("requestCount").child(adminCity).child("count");

                countRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                          if (snapshot.exists())
                          {
                              int count=snapshot.getValue(Integer.class);

                              raisedCount.setText(""+count);

                          }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Dashboard.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });

                DatabaseReference dustbinRef = FirebaseDatabase.getInstance().getReference().child("dustbinCount").child(adminCity).child("count");
                dustbinRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int dustCount=snapshot.getValue(Integer.class);

                        dustbinCount.setText(""+dustCount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                // resolved count Here!!!!!

                DatabaseReference resolvedRef = FirebaseDatabase.getInstance().getReference().child("resolvedCount").child(adminCity).child("count");
                resolvedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        int solvedCount=snapshot.getValue(Integer.class);

                        resolvedCount.setText(""+solvedCount);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getCode());
            }
        });


        // counter


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLogoutDialog();
            }
        });


        raised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,raisedRequestActivity.class);
                startActivity(intent);
            }
        });

        dustbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,dustbinActivity.class);

                startActivity(intent);
            }
        });

        resolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,resolvedActivity.class);
                startActivity(intent);

            }
        });


    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");

        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Yes, perform logout
                // Add your logout code here
                auth.signOut();
                Intent intent=new Intent(Dashboard.this,AdminLogin.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked No, do nothing
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}