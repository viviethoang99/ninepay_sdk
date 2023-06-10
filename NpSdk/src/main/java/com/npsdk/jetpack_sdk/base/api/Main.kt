package com.npsdk.jetpack_sdk.base.api

import android.os.Build
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.O)
fun main(args: Array<String>) {


    val jsonString = """{"menu": {
    "header": "SVG Viewer",
    "items": [
        {"id": "Open"},
        {"id": "OpenNew", "label": "Open New"},
        null,
        {"id": "ZoomIn", "label": "Zoom In"},
        {"id": "ZoomOut", "label": "Zoom Out"},
        {"id": "OriginalView", "label": "Original View"},
        null,
        {"id": "Quality"},
        {"id": "Pause"},
        {"id": "Mute"},
        null,
        {"id": "Find", "label": "Find..."},
        {"id": "FindAgain", "label": "Find Again"},
        {"id": "Copy"},
        {"id": "CopyAgain", "label": "Copy Again"},
        {"id": "CopySVG", "label": "Copy SVG"},
        {"id": "ViewSVG", "label": "View SVG"},
        {"id": "ViewSource", "label": "View Source"},
        {"id": "SaveAs", "label": "Save As"},
        null,
        {"id": "Help"},
        {"id": "About", "label": "About Adobe CVG Viewer..."}
    ]
}}"""

    val myKey = EncryptServiceHelper.getRandomkeyRaw()
    val e = EncryptServiceHelper.encryptKeyAesBase64(jsonString, myKey)
    println("Ma hoa: $e")

    val xxx = EncryptServiceHelper.decryptAesBase64(e, myKey)
    println("Thuoc giai: $xxx")

    val yyy = EncryptServiceHelper.encryptRandomkey(myKey, DataTest.publicKey)
    println("RKE: $yyy")

}