package melquelolea.vefexchange.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DolarToday {

    @SerializedName("USD")
    @Expose
    var usd: USD? = null

}
