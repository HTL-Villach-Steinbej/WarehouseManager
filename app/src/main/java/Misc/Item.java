package Misc;


import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String QR_CODE;
    private String EAN_CODE;
    private String brand;
    private String name;
    private String category;

    public Item(){

    }
    public Item(String QR_CODE,String EAN_CODE){
        this.EAN_CODE=EAN_CODE;
        this.QR_CODE=QR_CODE;
    }


    private Item(Parcel in) {
        QR_CODE = in.readString();
        EAN_CODE = in.readString();

        brand = in.readString();
        name = in.readString();
        category = in.readString();

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(QR_CODE);
        dest.writeString(EAN_CODE);
        dest.writeString(brand);
        dest.writeString(name);
        dest.writeString(category);
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];

        }
    };

    public String getQR_CODE(){
        return  QR_CODE;
    }

    public String getEAN_CODE(){
        return  EAN_CODE;
    }

    public void setQR_CODE(String QR_CODE) {
        this.QR_CODE = QR_CODE;
    }

    public void setEAN_CODE(String EAN_CODE) {
        this.EAN_CODE = EAN_CODE;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }
    public String getEANCODE() {
        return this.EAN_CODE;
    }
    public String getQRCODE() {
        return this.QR_CODE;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return QR_CODE + " " + brand + " " + name + " " + category;
    }
}

