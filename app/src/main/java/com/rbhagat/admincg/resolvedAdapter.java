package com.rbhagat.admincg;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class resolvedAdapter extends RecyclerView.Adapter<resolvedAdapter.viewHolder>{

    Context context;
    ArrayList<AddressInformatio> raisedArrayList;
    int resolvedCount = 0;

    public resolvedAdapter(Context userList, ArrayList<AddressInformatio> usersArrayList) {
        this.context = userList;
        this.raisedArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public  viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_resolved,parent,false);
        return new viewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        AddressInformatio users = raisedArrayList.get(position);

        holder.wasteCity.setText(users.city);
        holder.wastePincode.setText(users.pincode);

        Picasso.get().load(users.imagelink).placeholder(R.drawable.w2_img).into(holder.wasteImg);


        resolvedCount++;

        DatabaseReference countRef = FirebaseDatabase.getInstance().getReference().child("resolvedCount").child(users.city).child("count");
        countRef.setValue(resolvedCount);



    }



    @Override
    public int getItemCount() {

        return raisedArrayList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {

        CircleImageView wasteImg;
        TextView wasteCity,wastePincode;



        public viewHolder(@NonNull View itemView) {

            super(itemView);

            wasteImg=itemView.findViewById(R.id.wasteImg);
            wasteCity=itemView.findViewById(R.id.wasteCity);
            wastePincode=itemView.findViewById(R.id.wastePincode);


        }


    }
}
