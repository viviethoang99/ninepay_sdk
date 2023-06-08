package com.npsdk.jetpack_sdk.base.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast


fun showDialogConfirm(context: Context) {
//    val dialog = Dialog(context)
//    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//    dialog.setCancelable(true)
//    dialog.setCanceledOnTouchOutside(true)
//    dialog.setContentView(R.layout.custom_dialog_layout)
//    dialog.show()
    AlertDialog.Builder(context)
        .setTitle("Bạn có chắc chắn muốn quay lại?")
        .setMessage("Giao dịch thanh toán của bạn sẽ bị hủy nếu bạn thực hiện quay lại.")
        .setPositiveButton("Quay lại",
            DialogInterface.OnClickListener { dialog, whichButton ->
                (context as Activity).finish()
            })
        .setNegativeButton("Không", null)
        .show()
}