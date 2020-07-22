package com.subwilven.basemodel.project_base.utils.third_party.map

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager

import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete

public object PlaceAutoComplete {

    fun getPlaceAutoCompleteIntent(context: Activity): Intent? {

        var locale: String?
        //get the local country by the network service provider
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        locale = tm.networkCountryIso

        if (locale == null)
        // if it still null get it with the device local
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = context.resources.configuration.locales.get(0).country
            } else {
                locale = context.resources.configuration.locale.country
            }

        var intent: Intent? = null
        try {
            val typeFilter = AutocompleteFilter.Builder()
                    .setCountry(locale)
                    .build()

            intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(context)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

        return intent

    }

    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //
    //        if (resultCode == RESULT_OK) {
    //            Place place = PlaceAutocomplete.getPlace(this, data);
    //        }
    //    }
}
