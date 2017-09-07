package melquelolea.vefexchange

import android.content.Context

/**
 * Created by Patrick Rengifo on 16/12/16.
 * Helper class to keep important information in preferences share data
 */

object PreferenceHelper {
    private val PREF_NAME = "Preferences"

    /**
     * This method logs the user out by deleting his login information then send him to
     * the start screen.
     */
    fun clearPreferences(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, 0)
        prefs.edit().clear().apply()
    }

    /**
     * This method will save the exchange data for now in preferences
     */
    fun saveData(context: Context, usdbtc: Double?, vefbtc: Double?, vefdtd: Double?) {
        val settings = context.getSharedPreferences(PREF_NAME, 0)
        val editor = settings.edit()
        editor.putFloat("usdbtc", usdbtc!!.toFloat())
        editor.putFloat("vefbtc", vefbtc!!.toFloat())
        editor.putFloat("vefdtd", vefdtd!!.toFloat())
        editor.apply()
    }
}
