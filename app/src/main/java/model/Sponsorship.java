
package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sponsorship implements Serializable {

    @SerializedName("impressions_id")
    @Expose
    private String impressionsId;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("sponsor")
    @Expose
    private Sponsor sponsor;

    public String getImpressionsId() {
        return impressionsId;
    }

    public void setImpressionsId(String impressionsId) {
        this.impressionsId = impressionsId;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

}
