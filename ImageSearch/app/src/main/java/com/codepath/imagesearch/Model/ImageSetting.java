package com.codepath.imagesearch.Model;

import java.io.Serializable;

/**
 * Created by abgandhi on 2/28/15.
 */
public class ImageSetting implements Serializable{
    private String size;
    private String colour;
    private String ImageType;
    private String site;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        if (this.size != null) {
            this.size = "";
            this.size = this.size + size;
        } else {
            this.size = new String(size);
        }
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        if (this.colour != null) {
            this.colour = "";
            this.colour = this.colour + colour;
        } else {
            this.colour = new String(colour);
        }
    }

    public String getImageType() {
        return ImageType;
    }

    public void setImageType(String imageType) {
        if (this.ImageType != null) {
            this.ImageType = "";
            this.ImageType = this.ImageType + imageType;
        } else {
            this.ImageType =  new String(imageType);
        }
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        if (this.site != null) {
            this.site = "";
            this.site = this.site + site;
        } else {
            this.site = new String(site);
        }
    }


}
