package com.rbhagat.admincg;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class dustbinAdapter extends RecyclerView.Adapter<dustbinAdapter.viewHolder>{

    Context context;
    ArrayList<DustbinModel> dustbinArrayList;
    int dustbinCount = 0;

    public dustbinAdapter(Context userList, ArrayList<DustbinModel> usersArrayList) {
        this.context = userList;
        this.dustbinArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_dustbin,parent,false);
        return new viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        DustbinModel dust = dustbinArrayList.get(position);

        dustbinCount++;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AdminDetails").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("city");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String adminCity = snapshot.getValue(String.class);
                DatabaseReference countRef = FirebaseDatabase.getInstance().getReference().child("dustbinCount").child(adminCity).child("count");
                countRef.setValue(dustbinCount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Drawable drawable;
        Drawable d2;

          String ls="OFF";
          if(dust.lightStatus.equals(ls))
          {
              holder.lightImage.setImageResource(R.drawable.lightoff);


          }
          else {
              holder.lightImage.setImageResource(R.drawable.on);
              d2=holder.lightImage.getDrawable();
              int color = context.getResources().getColor(R.color.orange);
              d2.setColorFilter(color, PorterDuff.Mode.SRC_IN);

          }

          holder.dustId.setText(dust.DustbinID);
          holder.dustLocation.setText(dust.DustbinLocation);
          holder.level.setText(dust.level);

          holder.temp.setText(dust.temp);
          holder.humi.setText(dust.humi);
          holder.lightStatus.setText(dust.lightStatus);


          holder.mainDustbin.setImageResource(R.drawable.trash);
          holder.dustbinLevel.setImageResource(R.drawable.trash);
          drawable=holder.mainDustbin.getDrawable();
          int color = context.getResources().getColor(R.color.green);
          drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);

          holder.tempImg.setImageResource(R.drawable.celsius);
          holder.humiImg.setImageResource(R.drawable.humidity);

          holder.dustMap.setImageResource(R.drawable.map);


//        holder.wasteCity.setText(users.city);
//        holder.wastePincode.setText(users.pincode);
//        holder.approvalBtn.setImageResource(R.drawable.yes);
//        holder.declineBtn.setImageResource(R.drawable.no);
//        holder.mapBtn.setImageResource(R.drawable.map);


        double latitude,longitude;

        latitude=Double.parseDouble(dust.latitude);
        longitude=Double.parseDouble(dust.longitude);





        holder.dustMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,dustbinMap.class);
                intent.putExtra("lat",latitude);
                intent.putExtra("lon",longitude);
                context.startActivity(intent);
            }
        });


//        holder.mapBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context,MapsActivity.class);
//                intent.putExtra("lat",latitude);
//                intent.putExtra("lon",longitude);
//                context.startActivity(intent);
//            }
//        });







    }



    @Override
    public int getItemCount() {
        return dustbinArrayList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {


        ImageView mainDustbin,dustbinLevel,dustbinStatus,tempImg,humiImg,lightImage,dustMap;
        TextView dustId,dustLocation,level,status,temp,humi,lightStatus;



        public viewHolder(@NonNull View itemView) {

            super(itemView);


            mainDustbin=itemView.findViewById(R.id.mainTrash);
            dustbinLevel=itemView.findViewById(R.id.dustbinLevel);

            tempImg=itemView.findViewById(R.id.temperatureImg);
            humiImg=itemView.findViewById(R.id.humidityImg);
            lightImage=itemView.findViewById(R.id.lightImg);
            dustMap=itemView.findViewById(R.id.map);

            dustId=itemView.findViewById(R.id.dustbinId);
            dustLocation=itemView.findViewById(R.id.dustbinLocation);
            level=itemView.findViewById(R.id.level);

            temp=itemView.findViewById(R.id.temperature);
            humi=itemView.findViewById(R.id.humidity);
            lightStatus=itemView.findViewById(R.id.lightStatus);


        }


    }
}
