package com.npsdk.module.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import com.npsdk.jetpack_sdk.repository.model.PublickeyModel;
import com.npsdk.module.NPayLibrary;
import com.npsdk.module.utils.Constants;
import com.npsdk.module.utils.Preference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPublickeyTask extends AsyncTask<Void, Void, Void> {
    private final Context context;

    public GetPublickeyTask(Context ct) {
        context = ct;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            RestfulApi restfulApi = RestfulApi.getInstance(context);
            Call<PublickeyModel> call = restfulApi.GetPublickeyTask();
            call.enqueue(new Callback<PublickeyModel>() {
                @Override
                public void onResponse(@NonNull Call<PublickeyModel> call, @NonNull Response<PublickeyModel> response) {
                    Log.d("Response", "response.code() ==   " + response.code());
                    if (response.code() != 200) {
                        return;
                    }
                    PublickeyModel data = response.body();

                    if (data != null && data.getErrorCode() == 0) {
                        String publicKey = data.getData().getPublicKey();
                        Preference.save(context, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.PUBLIC_KEY, publicKey);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PublickeyModel> callResponse, @NonNull Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
