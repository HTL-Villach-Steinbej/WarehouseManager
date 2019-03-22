package com.example.warehousemanager;


import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String QR_CODE;
    private String EAN_CODE;

    public Item(){

    }
    public Item(String QR_CODE,String EAN_CODE){
        this.EAN_CODE=EAN_CODE;
        this.QR_CODE=QR_CODE;
    }
    public String getQR_CODE(){
        return  QR_CODE;
    }
    public String getEAN_CODE(){
        return  EAN_CODE;
    }

    private Item(Parcel in) {
        EAN_CODE = in.readString();
        QR_CODE = in.readString();

    }
    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(QR_CODE);
        dest.writeString(EAN_CODE);

    }
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];

        }
    };

    @Override
    public String toString() {
        return QR_CODE +" "+ EAN_CODE;
    }
// all get , set method
}

