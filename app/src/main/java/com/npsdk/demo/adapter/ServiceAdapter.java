package com.npsdk.demo.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.npsdk.demo.R;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceHolder> {
    private Context context;
    private List<Pair<String, Integer>> services;
    private IServiceClicked callback;

    public ServiceAdapter(Context context, List<Pair<String, Integer>> services, IServiceClicked callback) {
        this.context = context;
        this.services = services;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_item_service, parent, false);
        return new ServiceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHolder holder, int position) {
        Pair<String, Integer> service = services.get(position);
        holder.imgService.setBackground(ContextCompat.getDrawable(context, service.second));
        holder.tvServiceName.setText(service.first);
        holder.tvServiceName.setOnClickListener(v -> callback.onItemServiceClicked(position));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ServiceHolder extends RecyclerView.ViewHolder {
        ImageView imgService;
        TextView tvServiceName;
        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            imgService = itemView.findViewById(R.id.img_service);
            tvServiceName = itemView.findViewById(R.id.txt_service);
        }
    }

    public interface IServiceClicked {
        void onItemServiceClicked(int position);
    }

}
