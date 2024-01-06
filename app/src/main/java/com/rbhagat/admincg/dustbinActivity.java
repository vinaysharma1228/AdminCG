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

public class dustbinActivity extends AppCompatActivity {

    ImageView back_arrow;
    RecyclerView recyclerViewDustbin;
    dustbinAdapter adapter;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<DustbinModel> dustbinArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dustbin);

        back_arrow=findViewById(R.id.backArrowD);

        database=FirebaseDatabase.getInstance();

        auth=FirebaseAuth.getInstance();
        dustbinArrayList=new ArrayList<>();

        String id="UMRTsC6q85QjdzL6Gjr8io93b4J2";


        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(dustbinActivity.this,Dashboard.class);
                startActivity(intent);
            }
        });

        //

        DatabaseReference reference=database.getReference().child("DustbinLevel").child(id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dustbinArrayList.clear();

                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    DustbinModel dustbinModel=dataSnapshot.getValue(DustbinModel.class);
                    dustbinArrayList.add(dustbinModel);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(dustbinActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();


            }
        });






        //

        recyclerViewDustbin=findViewById(R.id.recyclerViewDustbin);
        recyclerViewDustbin.setLayoutManager(new LinearLayoutManager(this));
        adapter=new dustbinAdapter(dustbinActivity.this,dustbinArrayList);
        recyclerViewDustbin.setAdapter(adapter);
    }
}