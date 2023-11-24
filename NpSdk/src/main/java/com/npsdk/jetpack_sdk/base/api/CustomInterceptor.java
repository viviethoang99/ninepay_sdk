package com.npsdk.jetpack_sdk.base.api;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.npsdk.jetpack_sdk.DataOrder;
import com.npsdk.module.NPayLibrary;
import com.npsdk.module.utils.Actions;
import com.npsdk.module.utils.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CustomInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);

        List<Integer> listError = Arrays.asList(Constants.NOT_VERIFY, Constants.NOT_LOGIN);
        if (listError.contains(response.code()) && !DataOrder.Companion.isProgressing()) {
            DataOrder.Companion.setProgressing(true);
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(NPayLibrary.getInstance().activity, "Xác thực thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();

                boolean isInitMerchant = request.url().toString().contains("/func/list");
                boolean isRefreshToken = request.url().toString().contains("/login/refresh_token");
                boolean isValidateOrder = request.url().toString().contains("/validate-order");
                if (isInitMerchant || isRefreshToken || isValidateOrder) {
                    // Đóng màn hình do token merchant sai.
                    NPayLibrary.getInstance().close();
                    return;
                }
                // Api khác danh sách bên trên.
                // Gọi sang webview login
                if (DataOrder.Companion.getMerchantInfo() != null) {
                    // Trường hợp đăng nhập khi chưa đăng nhập user
                    NPayLibrary.getInstance().openSDKWithAction(Actions.LOGIN);
                } else {
                    // Đóng màn hình do token merchant sai.
                    NPayLibrary.getInstance().close();
                }
            });
        }
        return response;
    }
}
