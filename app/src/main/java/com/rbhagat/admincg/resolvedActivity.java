package com.rbhagat.admincg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class resolvedActivity extends AppCompatActivity {

    ImageView baSolved;
    FirebaseDatabase database;
    RecyclerView recyclerViewSolved;
    resolvedAdapter adapter;
    FirebaseAuth auth;
    ArrayList<AddressInformatio> solvedArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolved);

        database=FirebaseDatabase.getInstance();

        baSolved=findViewById(R.id.backArrowSolved);

        auth=FirebaseAuth.getInstance();
        solvedArrayList=new ArrayList<>();

        baSolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(resolvedActivity.this,Dashboard.class);
                startActivity(intent);

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AdminDetails").child(auth.getUid()).child("city");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String adminCity = snapshot.getValue(String.class);

                DatabaseReference reference=database.getReference().child("ResolvedRequests").child(adminCity);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        solvedArrayList.clear();

                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            AddressInformatio users=dataSnapshot.getValue(AddressInformatio.class);
                            solvedArrayList.add(users);

                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(resolvedActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();


                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        recyclerViewSolved=findViewById(R.id.recyclerViewSolved);
        recyclerViewSolved.setLayoutManager(new LinearLayoutManager(this));
        adapter=new resolvedAdapter(resolvedActivity.this,solvedArrayList);
        recyclerViewSolved.setAdapter(adapter);




    }
}