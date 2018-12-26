package com.hany.el_bazaar.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Hany on 12/1/2018.
 */
@IgnoreExtraProperties
public class Bazaar {

    public String bazaarName, organizerName, bazaarPlace,bazaarDesc,bazaarId;
    public String vendorNumbers;
    public List<String> images;

    public Bazaar(){

    }
    public Bazaar(String bazaarName, String organizerName, String bazaarPlace,String bazaarDesc, String vendorNumbers,List<String> images) {
        this.bazaarName = bazaarName;
        this.organizerName = organizerName;
        this.bazaarPlace = bazaarPlace;
        this.bazaarDesc = bazaarDesc;
        this.vendorNumbers = vendorNumbers;
        this.images = images;
    }

    public String getBazaarId() {
        return bazaarId;
    }

    public void setBazaarId(String bazaarId) {
        this.bazaarId = bazaarId;
    }

    public void setBazaarName(String bazaarName) {
        this.bazaarName = bazaarName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public void setBazaarPlace(String bazaarPlace) {
        this.bazaarPlace = bazaarPlace;
    }

    public void setVendorNumbers(String vendorNumbers) {
        this.vendorNumbers = vendorNumbers;
    }

    public String getBazaarDesc() {
        return bazaarDesc;
    }

    public void setBazaarDesc(String bazaarDesc) {
        this.bazaarDesc = bazaarDesc;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getBazaarName() {
        return bazaarName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public String getBazaarPlace() {
        return bazaarPlace;
    }

    public String getVendorNumbers() {
        return vendorNumbers;
    }
}
