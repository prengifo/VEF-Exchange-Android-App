
package melquelolea.vefexchange.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timestamp {

    @SerializedName("dia")
    @Expose
    public String dia;
    @SerializedName("dia_corta")
    @Expose
    public String diaCorta;
    @SerializedName("epoch")
    @Expose
    public String epoch;
    @SerializedName("fecha")
    @Expose
    public String fecha;
    @SerializedName("fecha_corta")
    @Expose
    public String fechaCorta;
    @SerializedName("fecha_corta2")
    @Expose
    public String fechaCorta2;
    @SerializedName("fecha_nice")
    @Expose
    public String fechaNice;

}
