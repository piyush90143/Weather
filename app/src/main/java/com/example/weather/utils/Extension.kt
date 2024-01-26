package com.example.weather.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.example.weather.R
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.math.floor

////////////////////////////////////////
private var fusedLocationClient: FusedLocationProviderClient? = null

fun showAlertDialog(
    context: Context,
    title: String,
    message: String,
    btn1Title: String = "Leave",
    btn1Click: (() -> Unit)? = null,
    btn2Title: String = "Cancel",
    btn2Click: (() -> Unit)? = null
): AlertDialog {
    val dialog = AlertDialog.Builder(context, R.style.Theme_Weather)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(btn1Title) { dialog, _ ->
            btn1Click?.let { it() }
            dialog.dismiss()
        }
        .setNegativeButton(btn2Title) { dialog, _ ->
            btn2Click?.let { it() }
            dialog.dismiss()
        }
        .show()

    return dialog
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Double.convertKelvinToFarenhit(): Int{
    return (1.8*(this-273) + 32).toInt()
}

fun degToCompass(deg:Int):String {
    var value = floor((deg / 22.5) + 0.5)
    var arr = arrayOf("N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW");
    return arr[(value % 16).toInt()]
}

fun navigateToSettings(context: Context){
    showAlertDialog(context, "Permission was denied", "",
        "Settings",
        {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts(
                "package",
                Build.DISPLAY, null
            )
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    )
}