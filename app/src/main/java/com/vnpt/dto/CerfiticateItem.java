package com.vnpt.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class CerfiticateItem implements Parcelable {
    private int idTypeCerfiticate;
    private String pathCerfiticate;

    public int getIdTypeCerfiticate() {
        return idTypeCerfiticate;
    }

    public void setIdTypeCerfiticate(int idTypeCerfiticate) {
        this.idTypeCerfiticate = idTypeCerfiticate;
    }

    public String getPathCerfiticate() {
        return pathCerfiticate;
    }

    public void setPathCerfiticate(String pathCerfiticate) {
        this.pathCerfiticate = pathCerfiticate;
    }

    /**
     * Constructors and Getters/Setters have been removed to make reading easier
     **/
    public CerfiticateItem(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);
        this.pathCerfiticate = data[1];
        this.idTypeCerfiticate = Integer.parseInt(data[0]);
    }
    public CerfiticateItem() {
        this.idTypeCerfiticate = 0;
        this.pathCerfiticate = "";
    }
    public CerfiticateItem(int idTypeCerfiticate,String pathCerfiticate) {
        this.idTypeCerfiticate = idTypeCerfiticate;
        this.pathCerfiticate = pathCerfiticate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.idTypeCerfiticate),
                this.pathCerfiticate});

    }

    private void readFromParcel(Parcel in) {
        pathCerfiticate = in.readString();
        idTypeCerfiticate = in.readInt();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public CerfiticateItem createFromParcel(Parcel in) {
            return new CerfiticateItem(in);
        }

        @Override
        public CerfiticateItem[] newArray(int size) {
            return new CerfiticateItem[size];
        }
    };
}
