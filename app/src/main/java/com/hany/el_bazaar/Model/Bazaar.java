package com.hany.el_bazaar.Model;

/**
 * Created by Hany on 12/1/2018.
 */

public class Bazaar {

    private String bazaarName,organizerName,bazaarPlace;
    private int vendorNumbers;
    public Bazaar(String bazaarName,String organizerName,String bazaarPlace,int vendorNumbers){
        this.bazaarName = bazaarName;
        this.organizerName = organizerName;
        this.bazaarPlace = bazaarPlace;
        this.vendorNumbers = vendorNumbers;
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

    public int getVendorNumbers() {
        return vendorNumbers;
    }
}
