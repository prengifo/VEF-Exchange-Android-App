package melquelolea.vefexchange.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Patrick Rengifo on 11/29/16.
 * Model for the Bitcoin information in USD
 */

public class Bitcoin {
    @SerializedName("24h_avg")
    @Expose
    private Double _24hAvg;
    @SerializedName("ask")
    @Expose
    private Double ask;
    @SerializedName("bid")
    @Expose
    private Double bid;
    @SerializedName("last")
    @Expose
    private Double last;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("total_vol")
    @Expose
    private Double totalVol;

    /**
     *
     * @return
     * The _24hAvg
     */
    public Double get24hAvg() {
        return _24hAvg;
    }

    /**
     *
     * @param _24hAvg
     * The 24h_avg
     */
    public void set24hAvg(Double _24hAvg) {
        this._24hAvg = _24hAvg;
    }

    /**
     *
     * @return
     * The ask
     */
    public Double getAsk() {
        return ask;
    }

    /**
     *
     * @param ask
     * The ask
     */
    public void setAsk(Double ask) {
        this.ask = ask;
    }

    /**
     *
     * @return
     * The bid
     */
    public Double getBid() {
        return bid;
    }

    /**
     *
     * @param bid
     * The bid
     */
    public void setBid(Double bid) {
        this.bid = bid;
    }

    /**
     *
     * @return
     * The last
     */
    public Double getLast() {
        return last;
    }

    /**
     *
     * @param last
     * The last
     */
    public void setLast(Double last) {
        this.last = last;
    }

    /**
     *
     * @return
     * The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     * The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     * The totalVol
     */
    public Double getTotalVol() {
        return totalVol;
    }

    /**
     *
     * @param totalVol
     * The total_vol
     */
    public void setTotalVol(Double totalVol) {
        this.totalVol = totalVol;
    }
}
