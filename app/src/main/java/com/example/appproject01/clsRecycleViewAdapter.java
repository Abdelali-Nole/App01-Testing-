package com.example.appproject01;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;
import java.util.ArrayList;

public class clsRecycleViewAdapter extends RecyclerView.Adapter<clsRecycleViewAdapter.clsCarViewHolder> {

    // Inside clsRecycleViewAdapter class
    public interface OnItemClickListener {
        void onItemClick(int position);


    }
    private ArrayList<clsVehicle> cars;
    private Context context;

    // event when click to certain item at recycle view
    private OnItemClickListener listener;

    public clsRecycleViewAdapter(ArrayList<clsVehicle> cars, OnItemClickListener listener) {
        this.cars=cars;
        this.listener=listener;
    }

    // this method called just number of items at screnn and when scolling recycle same inflate items
    // does not make all items inflatter
    @Override
    public clsCarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v  = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.car_info,null,false);


        clsCarViewHolder carViewHolder = new clsCarViewHolder(v);

        //  return catViewHolder;

        return carViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull clsCarViewHolder holder, int position) {

        clsVehicle car = cars.get(position);

        holder.tv_model.setText(car.getModel());
        holder.tv_color.setText(car.getColor());
        holder.tv_distancePerLitter.setText(String.valueOf(car.getDpl()));
        holder.tv_description.setText(car.getDescription());



        if(car.getImage() != null && !car.getImage().equals("")) {
           // holder.iv_image.setImageURI(Uri.parse(car.getImage()));


            Bitmap imageBitmap = clsImageUtils.convertBase64ToBitmap(car.getImage());
            if (imageBitmap != null) {
                holder.iv_image.setImageBitmap(imageBitmap);
            } else {
                // Optionally set a placeholder image if conversion fails
                holder.iv_image.setImageResource(R.drawable.woman);
            }
            } else {
            // Optionally set a default image when no image is provided
            holder.iv_image.setImageResource(R.drawable.woman);
            }

    }







    @Override
    public int getItemCount() {
        return cars.size();
    }






    // view holder to store inflaters views to use again ( to use car view holder )
    public class clsCarViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_model;
        private TextView tv_color;
        private TextView tv_distancePerLitter;
        private TextView tv_description;
        private ImageView iv_image;


        public clsCarViewHolder(@NonNull View itemView) {

            super(itemView);

            this.tv_model = itemView.findViewById(R.id.carInfo_tv_modelID);
            this.tv_color = itemView.findViewById(R.id.carInfo_tv_colorID);
            this.tv_distancePerLitter = itemView.findViewById(R.id.carInfo_tv_distancePerLitterID);

            this.tv_description=itemView.findViewById(R.id.carInfo_tv_descriptionID);
            this.iv_image=itemView.findViewById(R.id.carInfo_imv_carID);


            // on item clicked on recycle view adapter ( event )
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });


        }
    }





}
