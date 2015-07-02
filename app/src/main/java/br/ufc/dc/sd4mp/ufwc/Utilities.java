package br.ufc.dc.sd4mp.ufwc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by André on 04/06/2015.
 */
public class Utilities {

    public static final String[] CONTACT_EMAIL = new String[]{"sd4mp.ufwc@gmail.com"};

    public static final String SUBJECT_EMAIL = "";
    public static final String TEXT_EMAIL = "";

    public static void checkNetworkConnection(final Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if ( !(networkInfo != null && networkInfo.isConnected()) ) {

            AlertDialog.Builder alBuilder = new AlertDialog.Builder(context);

            alBuilder.setTitle("Network Alert");
            alBuilder.setMessage("You need to be connected to the internet.");
            alBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    context.startActivity(new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS));
                    ///startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));

                }

            });

            alBuilder.show();

        }

    }

    public static void checkGPSConnection(final Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            AlertDialog.Builder alBuilder = new AlertDialog.Builder(context);

            alBuilder.setTitle("GPS Alert");
            alBuilder.setMessage("Your GPS need to be enabled.");
            alBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                }

            });

            alBuilder.show();

        }

    }

    public static void showProgressDialog(ProgressDialog dialog, String message, Context context) {

        if (dialog == null) {

            dialog = new ProgressDialog(context);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage(message);

        }

        dialog.show();
    }

    public static void hideProgressDialog(ProgressDialog dialog) {

        if (dialog != null)
            dialog.dismiss();

        dialog = null;

    }

}
