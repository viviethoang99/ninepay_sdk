package com.npsdk.demo.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.npsdk.demo.R;

import java.util.List;

public class ServiceAdapter extends ArrayAdapter<Pair<String, Integer>> {
    private Context context;
    private List<Pair<String, Integer>> services;
    private IServiceClicked callback;

    public ServiceAdapter(Context context, List<Pair<String, Integer>> services, IServiceClicked callback) {
        super(context, 0, services);
        this.context = context;
        this.services = services;
        this.callback = callback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item_service, parent, false);
        }
        Pair<String, Integer> service = services.get(position);
        ImageView imgService = listitemView.findViewById(R.id.img_service);
        TextView tvServiceName = listitemView.findViewById(R.id.txt_service);
        imgService.setBackground(ContextCompat.getDrawable(context, service.second));
        tvServiceName.setText(service.first);
        tvServiceName.setOnClickListener(v -> callback.onItemServiceClicked(position));
        return listitemView;
    }

    public interface IServiceClicked {
        void onItemServiceClicked(int position);
    }

}
