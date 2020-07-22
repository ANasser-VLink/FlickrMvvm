package com.subwilven.basemodel.project_base.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import com.google.android.gms.maps.model.LatLng

import com.subwilven.basemodel.R
import com.subwilven.basemodel.project_base.utils.extentions.attr
import com.subwilven.basemodel.project_base.utils.extentions.showToastLong


public object IntentManager {

    public fun openGoogleMapAtSpecificLocation(context: Context, location: LatLng) {
        val isInstalled = isPackageInstalledAndEnabled("com.google.android.apps.maps",
                context.packageManager)
        if (isInstalled) {
            val uri = Uri.parse("google.navigation:q=" + location.latitude + "," + location.longitude)
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        } else {
            context.showToastLong(com.subwilven.basemodel.project_base.POJO.Message(R.string.google_map_app_not_found))
        }
    }
    fun openAppOnGooglePlay(context: Context) {
        // you can also use BuildConfig.APPLICATION_ID
        val appId = context.packageName
        val rateIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$appId")
        )
        var marketFound = false

        // find all applications able to handle our rateIntent
        val otherApps = context.packageManager
            .queryIntentActivities(rateIntent, 0)
        for (otherApp in otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                == "com.android.vending"
            ) {
                val otherAppActivity = otherApp.activityInfo
                val componentName = ComponentName(
                    otherAppActivity.applicationInfo.packageName,
                    otherAppActivity.name
                )
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.component = componentName
                context.startActivity(rateIntent)
                marketFound = true
                break
            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appId")
            )
            context.startActivity(webIntent)
        }
    }

    private fun isPackageInstalledAndEnabled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getApplicationInfo(packageName, 0).enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }

    private fun launchExternalApp(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }

    fun openTabBrowser(context: Context, url: String) {
        openTabBrowser(context, url, context.resources.getColor(context.attr(android.R.attr.colorPrimary)))
    }

    fun shareApp(activity: Activity,text:String){
        ShareCompat.IntentBuilder.from(activity)
            .setType("text/plain")
            .setChooserTitle("Chooser title")
            .setText(text)
            .startChooser();
    }

    fun openTabBrowser(context: Context, url: String, color: Int) {
        val builder = CustomTabsIntent.Builder()

        builder.setToolbarColor(color)
        //set start and exit animations
        builder.setStartAnimations(context, R.anim.ibase_slide_in_right, R.anim.ibase_slide_out_left)
        builder.setExitAnimations(context, R.anim.ibase_slide_in_left, R.anim.ibase_slide_out_right)

        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    fun openAppSettings(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }


    fun openFacebookPage(context: Context) {
        val isInstalled = isPackageInstalledAndEnabled("com.facebook.katana",
                context.packageManager)

        if (isInstalled) {
            launchExternalApp(context, "fb://page/YOUR_PAGE_ID")
            //ex:fb://page/314086229325675
        } else {
            openTabBrowser(context, "https://www.facebook.com/YOUR_PAGE_URL")
            //ex :https://www.facebook.com/thaipeoplepowerpartypage
        }
    }

    fun openTwitterPage(context: Context) {
        val isInstalled = isPackageInstalledAndEnabled("com.twitter.android",
                context.packageManager)

        if (isInstalled) {
            launchExternalApp(context, "twitter://user?user_id=YOUR_USER_ID")
            //ex :twitter://user?user_id=1049571254137643009
        } else {
            openTabBrowser(context, "https://twitter.com/YOUR_PAGE_URL")
            //ex :https://twitter.com/0xREjn0OHxcFkQg
        }
    }

    fun openInstagramPage(context: Context) {
        val isInstalled = isPackageInstalledAndEnabled("com.instagram.android",
                context.packageManager)

        if (isInstalled) {
            launchExternalApp(context, "http://instagram.com/_u/YOUR_PAGE_URL")
            //ex :http://instagram.com/_u/thaipeoplepowerparty
        } else {
            openTabBrowser(context, "https://www.instagram.com/YOUR_PAGE_URL/")
            //ex :https://www.instagram.com/thaipeoplepowerparty/
        }
    }

    fun openYoutubeChannel(context: Context) {
        val isInstalled = isPackageInstalledAndEnabled("com.google.android.youtube",
                context.packageManager)

        if (isInstalled) {
            launchExternalApp(context, "https://www.youtube.com/channel/YOUR_PAGE_URL")
            //ex :https://www.youtube.com/channel/UC-Mz1RS-96VrxZv_N7mYzEw
        } else {
            openTabBrowser(context, "https://www.youtube.com/channel/YOUR_PAGE_URL")
            //ex :https://www.youtube.com/channel/UC-Mz1RS-96VrxZv_N7mYzEw
        }
    }

    fun openWhatsAppChat(context: Context,number:String){
        val url = "https://api.whatsapp.com/send?phone=$number"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        context.startActivity(i)
    }

    fun dialNumber(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(intent)
    }
}

