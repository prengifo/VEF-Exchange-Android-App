
package melquelolea.vefexchange.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DolarToday {

    @SerializedName("BCV")
    @Expose
    public BCV bcv;
    @SerializedName("COL")
    @Expose
    public COL col;
    @SerializedName("EUR")
    @Expose
    public EUR eur;
    @SerializedName("EURUSD")
    @Expose
    public EURUSD eurusd;
    @SerializedName("GOLD")
    @Expose
    public GOLD gold;
    @SerializedName("MISC")
    @Expose
    public MISC misc;
    @SerializedName("USD")
    @Expose
    public USD usd;
    @SerializedName("USDCOL")
    @Expose
    public USDCOL usdcol;
    @SerializedName("USDVEF")
    @Expose
    public USDVEF usdvef;
    @SerializedName("_labels")
    @Expose
    public Labels labels;
    @SerializedName("_timestamp")
    @Expose
    public Timestamp timestamp;

}
