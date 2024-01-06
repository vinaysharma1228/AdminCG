package com.rbhagat.admincg;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class raisedRequestActivity extends AppCompatActivity {

    ImageView back_arrow;
    RecyclerView recyclerViewRaised;
    raisedAdapter adapter;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<AddressInformatio> raisedArrayList;
    TextView raise;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raised_request);



        back_arrow=findViewById(R.id.backArrow);
        raise=findViewById(R.id.city);

        database=FirebaseDatabase.getInstance();

        auth=FirebaseAuth.getInstance();
        raisedArrayList=new ArrayList<>();

        String id=auth.getUid();


        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(raisedRequestActivity.this,Dashboard.class);
                startActivity(intent);
            }
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AdminDetails").child(id).child("city");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String adminCity = dataSnapshot.getValue(String.class);

                //

                assert adminCity != null;
                DatabaseReference reference=database.getReference().child("raisedWasteImage").child(adminCity);

                reference.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        raisedArrayList.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            AddressInformatio users=dataSnapshot.getValue(AddressInformatio.class);

                            if(users!=null)
                            {
                                raisedArrayList.add(users);

                            }
                            else {

                                raise.setText(" No Raise Request.");
                            }


                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(raisedRequestActivity.this, "No raised request", Toast.LENGTH_SHORT).show();

                    }
                });



                //

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getCode());
            }
        });




        recyclerViewRaised=findViewById(R.id.recyclerViewRaised);
        recyclerViewRaised.setLayoutManager(new LinearLayoutManager(this));
        adapter=new raisedAdapter(raisedRequestActivity.this,raisedArrayList);
        recyclerViewRaised.setAdapter(adapter);


    }


}