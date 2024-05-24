package com.appmobiledition.laundryfinder.android.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

val Context.hasLocationPermission
    get() = ContextCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

fun Context.getActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

// TODO: make common
fun Context.sendMail(to: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email" // or "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) { // TODO: Handle case where no email app is available
    } catch (t: Throwable) { // TODO: Handle potential other type of exceptions
    }
}

// TODO: make common
fun Context.dial(phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        startActivity(intent)
    } catch (t: Throwable) { // TODO: Handle potential exceptions
    }
}

fun Context.navigateByGmaps(context: Context, latitude: Double, longitude: Double) {
    try {
        val gmmIntentUri = Uri.parse("google.navigation:q= $latitude, $longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        ContextCompat.startActivity(context, mapIntent, null)
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(context, "j'trouve pas maps sur ton tel frere", Toast.LENGTH_SHORT).show()
    }
}

fun Context.share(
    content: String,
    shareContent: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    shareContent.launch(shareIntent)
}