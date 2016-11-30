
package melquelolea.vefexchange.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class COL {

    @SerializedName("compra")
    @Expose
    public Double compra;
    @SerializedName("efectivo")
    @Expose
    public Double efectivo;
    @SerializedName("transfer")
    @Expose
    public Double transfer;
    @SerializedName("venta")
    @Expose
    public Double venta;

}
