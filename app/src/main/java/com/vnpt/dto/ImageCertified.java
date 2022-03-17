package com.vnpt.dto;

import java.io.Serializable;

/**
 * @Description: lop book
 * @author:truonglt2
 * @since:Feb 7, 2014 4:13:08 PM
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class ImageCertified implements Serializable {
    public HoaDon getmHoaDon() {
        return mHoaDon;
    }

    public void setmHoaDon(HoaDon mHoaDon) {
        this.mHoaDon = mHoaDon;
    }

    public String getPathLocal() {
        return pathLocal;
    }

    public void setPathLocal(String pathLocal) {
        this.pathLocal = pathLocal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public void setIdDrawable(int idDrawable) {
        this.idDrawable = idDrawable;
    }

    private HoaDon mHoaDon;
    private String pathLocal;
    private String url;
    private int idDrawable;
}