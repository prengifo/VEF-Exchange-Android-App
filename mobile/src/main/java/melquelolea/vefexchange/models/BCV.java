
package melquelolea.vefexchange.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BCV {

    @SerializedName("fecha")
    @Expose
    public String fecha;
    @SerializedName("fecha_nice")
    @Expose
    public String fechaNice;
    @SerializedName("liquidez")
    @Expose
    public String liquidez;
    @SerializedName("reservas")
    @Expose
    public String reservas;

}
