package com.npsdk.demo.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.npsdk.demo.R;
import com.npsdk.demo.util.ServiceEnum;
import com.npsdk.demo.util.ServiceModel;

import java.util.List;

public class ServiceAdapter2 extends RecyclerView.Adapter<ServiceAdapter2.Service2Holder> {
    private Context context;
    private List<ServiceModel> services;
    private IServiceClicked callback;

    public ServiceAdapter2(Context context, List<ServiceModel> services, IServiceClicked callback) {
        this.context = context;
        this.services = services;
        this.callback = callback;
    }

    @NonNull
    @Override
    public Service2Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_item_service, parent, false);
        return new Service2Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Service2Holder holder, int position) {
        ServiceModel service = services.get(position);
        holder.imgService.setBackground(ContextCompat.getDrawable(context, service.getId()));
        holder.tvServiceName.setText(service.getTitle());
        holder.parent.setOnClickListener(v -> callback.onItemServiceClicked(service.getServiceType()));

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class Service2Holder extends RecyclerView.ViewHolder {
        private ImageView imgService;
        private TextView tvServiceName;
        private LinearLayout parent;

        public Service2Holder(@NonNull View itemView) {
            super(itemView);
            imgService = itemView.findViewById(R.id.img_service);
            tvServiceName = itemView.findViewById(R.id.txt_service);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    public interface IServiceClicked {
        void onItemServiceClicked(ServiceEnum type);
    }

}
