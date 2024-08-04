package com.npsdk.demo.dialog;


import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.npsdk.demo.adapter.ServiceAdapter;
import com.npsdk.demo.databinding.LayoutSheetServiceBinding;
import com.npsdk.demo.util.Utils;

import java.util.List;

public class ServiceSheet extends BottomSheetDialogFragment implements ServiceAdapter.IServiceClicked {

    private LayoutSheetServiceBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LayoutSheetServiceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Pair<String, Integer>> services = Utils.getServices();
        binding.rcService.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        binding.rcService.setLayoutManager(gridLayoutManager);
        ServiceAdapter serviceAdapter = new ServiceAdapter(getContext(), services, this);
        binding.rcService.setAdapter(serviceAdapter);
    }

    @Override
    public void onItemServiceClicked(int position) {

    }
}
