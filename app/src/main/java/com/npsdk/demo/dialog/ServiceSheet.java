package com.npsdk.demo.dialog;


import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.npsdk.demo.databinding.LayoutSheetServiceBinding;
import com.npsdk.demo.util.Utils;

import java.util.List;

public class ServiceSheet extends BottomSheetDialogFragment {

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
//        binding.rcQuality.setHasFixedSize(true);
//        binding.rcQuality.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        QualityAdapter qualityAdapter = new QualityAdapter(getContext(), qualityList, this);
//        binding.rcQuality.setAdapter(qualityAdapter);
    }

}
