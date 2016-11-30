package melquelolea.vefexchange.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Patrick Rengifo on 11/29/16.
 * Model for the Bitcoin information in USD
 */

public class BitcoinVEF {
    @SerializedName("buy")
    @Expose
    public Double buy;
    @SerializedName("high")
    @Expose
    public Double high;
    @SerializedName("last")
    @Expose
    public Double last;
    @SerializedName("low")
    @Expose
    public Double low;
    @SerializedName("pair")
    @Expose
    public String pair;
    @SerializedName("sell")
    @Expose
    public Double sell;
    @SerializedName("vol")
    @Expose
    public Double vol;
    @SerializedName("vol_vef")
    @Expose
    public Double volVef;
}
