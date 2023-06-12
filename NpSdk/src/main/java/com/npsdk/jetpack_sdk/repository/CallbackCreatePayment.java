package com.npsdk.jetpack_sdk.repository;

import androidx.annotation.Nullable;
import com.npsdk.jetpack_sdk.repository.model.ListBankModel;

public interface CallbackCreatePayment {
    void onSuccess(@Nullable String paymentId, String message);
}
