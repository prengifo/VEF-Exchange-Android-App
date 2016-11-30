
package melquelolea.vefexchange.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class USD {

    @SerializedName("cencoex")
    @Expose
    public Double cencoex;
    @SerializedName("dolartoday")
    @Expose
    public Double dolartoday;
    @SerializedName("efectivo")
    @Expose
    public Double efectivo;
    @SerializedName("efectivo_cucuta")
    @Expose
    public Double efectivoCucuta;
    @SerializedName("efectivo_real")
    @Expose
    public Double efectivoReal;
    @SerializedName("promedio")
    @Expose
    public Double promedio;
    @SerializedName("promedio_real")
    @Expose
    public Double promedioReal;
    @SerializedName("sicad1")
    @Expose
    public Double sicad1;
    @SerializedName("sicad2")
    @Expose
    public Double sicad2;
    @SerializedName("transfer_cucuta")
    @Expose
    public Double transferCucuta;
    @SerializedName("transferencia")
    @Expose
    public Double transferencia;

}
