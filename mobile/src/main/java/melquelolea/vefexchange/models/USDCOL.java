
package melquelolea.vefexchange.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class USDCOL {

    @SerializedName("rate")
    @Expose
    public Double rate;
    @SerializedName("ratecash")
    @Expose
    public Double ratecash;
    @SerializedName("ratetrm")
    @Expose
    public Double ratetrm;
    @SerializedName("setfxbuy")
    @Expose
    public Double setfxbuy;
    @SerializedName("setfxsell")
    @Expose
    public Double setfxsell;
    @SerializedName("trmfactor")
    @Expose
    public Double trmfactor;
    @SerializedName("trmfactorcash")
    @Expose
    public Double trmfactorcash;

}
