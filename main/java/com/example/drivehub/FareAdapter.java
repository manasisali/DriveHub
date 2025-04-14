//package com.example.drivehub;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.List;
//
//public class FareAdapter extends RecyclerView.Adapter<FareAdapter.FareViewHolder> {
//    private List<Fare> fareList;
//
//    public FareAdapter(List<Fare> fareList) {
//        this.fareList = fareList;
//    }
//
//    @NonNull
//    @Override
//    public FareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fare, parent, false);
//        return new FareViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull FareViewHolder holder, int position) {
//        Fare fare = fareList.get(position);
//        holder.tvServiceName.setText(fare.getServiceName());
//        holder.tvCabType.setText(fare.getCabType());
//        holder.tvFareAmount.setText("Fare: ₹" + fare.getFareAmount());
//    }
//
//    @Override
//    public int getItemCount() {
//        return fareList.size();
//    }
//
//    public static class FareViewHolder extends RecyclerView.ViewHolder {
//        TextView tvServiceName, tvCabType, tvFareAmount;
//
//        public FareViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvServiceName = itemView.findViewById(R.id.tvServiceName);
//            tvCabType = itemView.findViewById(R.id.tvCabType);
//            tvFareAmount = itemView.findViewById(R.id.tvFareAmount);
//        }
//    }
//}

package com.example.drivehub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FareAdapter extends RecyclerView.Adapter<FareAdapter.FareViewHolder> {
    private Context context;
    private List<Fare> fareList;

    public FareAdapter(Context context, List<Fare> fareList) {
        this.context = context;
        this.fareList = fareList;
    }

    @NonNull
    @Override
    public FareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fare, parent, false);
        return new FareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FareViewHolder holder, int position) {
        Fare fare = fareList.get(position);
        holder.tvServiceName.setText(fare.getServiceName());
        holder.tvCabType.setText(fare.getCabType());
        holder.tvFareAmount.setText("Fare: ₹" + fare.getFareAmount());

        // Set the correct logo based on the service name
        if (fare.getServiceName().equalsIgnoreCase("Ola")) {
            holder.ivServiceLogo.setImageResource(R.drawable.ola_logo);
        } else if (fare.getServiceName().equalsIgnoreCase("Uber")) {
            holder.ivServiceLogo.setImageResource(R.drawable.uber_logo);
        } else {
            holder.ivServiceLogo.setImageResource(R.drawable.ola_logo); // Default logo
        }
    }

    @Override
    public int getItemCount() {
        return fareList.size();
    }

    public static class FareViewHolder extends RecyclerView.ViewHolder {
        TextView tvServiceName, tvCabType, tvFareAmount;
        ImageView ivServiceLogo;

        public FareViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvCabType = itemView.findViewById(R.id.tvCabType);
            tvFareAmount = itemView.findViewById(R.id.tvFareAmount);
            ivServiceLogo = itemView.findViewById(R.id.ivServiceLogo);
        }
    }
}
