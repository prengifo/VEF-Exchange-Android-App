package melquelolea.vefexchange;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

/**
 * Created by Patrick Rengifo on 20/12/16.
 * Initializes the font-awesome icons for the rest of the app
 */

public class VefExchange extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new FontAwesomeModule());
    }
}
