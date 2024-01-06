package com.rbhagat.admincg;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class raisedAdapter extends RecyclerView.Adapter<raisedAdapter.viewHolder>{

    Context context;
    ArrayList<AddressInformatio> raisedArrayList;
    int requestCount = 0;

    public raisedAdapter(Context userList, ArrayList<AddressInformatio> usersArrayList) {
        this.context = userList;
        this.raisedArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_raised_request,parent,false);
        return new viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        AddressInformatio users = raisedArrayList.get(position);

        holder.wasteCity.setText(users.city);
        holder.wastePincode.setText(users.pincode);
        holder.approvalBtn.setImageResource(R.drawable.yes);
        holder.declineBtn.setImageResource(R.drawable.no);
        holder.mapBtn.setImageResource(R.drawable.map);
        Picasso.get().load(users.imagelink).placeholder(R.drawable.w2_img).into(holder.wasteImg);
        String descrip,w_image;
        double latitude,longitude;
        String uploaderId;

        //

        requestCount++; // Increment the execution count

        // Save this count to your database
        // Assuming you have a DatabaseReference pointing to your database
        DatabaseReference countRef = FirebaseDatabase.getInstance().getReference().child("requestCount").child(users.city).child("count");
        countRef.setValue(requestCount);

        uploaderId=users.id;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uploaderId).child("uName");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.getValue(String.class);
                holder.uploadBy.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        // Inside your onBindViewHolder method, add this click listener for the approval button
        holder.approvalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to approve this?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Yes, perform your approval action here

                        // For example, you can start a new activity or update the database
                        // ...

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ResolvedRequests").child(users.city).child(users.id);

                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {

                                    DatabaseReference rewardRef = FirebaseDatabase.getInstance().getReference().child("users").child(users.id).child("rewardPoint");

                                    rewardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Integer currentPoints = snapshot.getValue(Integer.class);

                                            if (currentPoints == null) {
                                                currentPoints = 0;
                                            }

                                            currentPoints += 5;

                                            rewardRef.setValue(currentPoints).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        String rewPoint="Updated";
                                                    }

                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });




                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("raisedWasteImage").child(users.city).child(users.id);
                                    ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                DatabaseReference countRef2 = FirebaseDatabase.getInstance().getReference().child("requestCount").child(users.city).child("count");

                                                countRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        Integer currentCount = snapshot.getValue(Integer.class);
                                                        if (currentCount==null)
                                                        {
                                                            currentCount=0;
                                                        }

                                                        currentCount=currentCount-1;

                                                        countRef2.setValue(currentCount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful())
                                                                {
                                                                    String up="updatedd...";

                                                                }

                                                            }
                                                        });








                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });




                                            }
                                            else {
                                                Exception exception = task.getException();

                                            }

                                        }
                                    });



                                }
                                else {
                                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });



                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, do nothing or perform any required action
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });





        descrip=users.description;
        w_image=users.imagelink;

        latitude=users.latitude;
        longitude=users.longitude;

         holder.wasteImg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(context,imagePreview.class);
                 intent.putExtra("descrip",descrip);
                 intent.putExtra("w_image",w_image);
                 context.startActivity(intent);
             }
         });

         holder.mapBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(context,MapsActivity.class);
                 intent.putExtra("lat",latitude);
                 intent.putExtra("lon",longitude);
                 context.startActivity(intent);
             }
         });

        holder.declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to decline?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Code to execute when "Yes" is clicked
                        // This is where you put your code after the decline button was clicked

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("raisedWasteImage").child(users.city).child(users.id);
                        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    DatabaseReference countRef2 = FirebaseDatabase.getInstance().getReference().child("requestCount").child(users.city).child("count");

                                    countRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Integer currentCount = snapshot.getValue(Integer.class);
                                            if (currentCount==null)
                                            {
                                                currentCount=0;
                                            }

                                            currentCount=currentCount-1;

                                            countRef2.setValue(currentCount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful())
                                                    {
                                                        String up="updatedd...";

                                                    }

                                                }
                                            });








                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });




                                }
                                else {
                                    Exception exception = task.getException();

                                }

                            }
                        });


                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // Dismiss the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });








    }



    @Override
    public int getItemCount() {

        return raisedArrayList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {

        CircleImageView wasteImg;
        ImageView approvalBtn,declineBtn,mapBtn;
        TextView wasteCity,wastePincode,uploadBy;



        public viewHolder(@NonNull View itemView) {

            super(itemView);

            wasteImg=itemView.findViewById(R.id.wasteImg);
            wasteCity=itemView.findViewById(R.id.wasteCity);
            wastePincode=itemView.findViewById(R.id.wastePincode);
            approvalBtn=itemView.findViewById(R.id.approvalBtn);
            declineBtn=itemView.findViewById(R.id.declineBtn);
            mapBtn=itemView.findViewById(R.id.mapBtn);
            uploadBy=itemView.findViewById(R.id.uploadedBy);


        }


    }
}
