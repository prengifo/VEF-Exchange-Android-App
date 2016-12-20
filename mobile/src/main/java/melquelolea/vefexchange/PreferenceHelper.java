package melquelolea.vefexchange;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Patrick Rengifo on 16/12/16.
 * Helper class to keep important information in preferences share data
 */

public class PreferenceHelper {
    public static String PREF_NAME = "Preferences";

    /**
     * This method logs the user out by deleting his login information then send him to
     * the start screen.
     */
    public static void clearPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PreferenceHelper.PREF_NAME, 0);
        prefs.edit().clear().apply();
    }

    /**
     * This method will save the exchange data for now in preferences
     */
    public static void saveData(Context context, Double usdbtc, Double vefbtc, Double vefdtd) {
        SharedPreferences settings = context.getSharedPreferences(PreferenceHelper.PREF_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("usdbtc", usdbtc.floatValue());
        editor.putFloat("vefbtc", vefbtc.floatValue());
        editor.putFloat("vefdtd", vefdtd.floatValue());
        editor.apply();
    }
}
