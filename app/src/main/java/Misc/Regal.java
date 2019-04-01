package Misc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Regal implements Serializable {
    private String name;
    private int widthGrid;
    private int heigthGrid;
    private int buttonNr;
    private String qr_path;
    private EnumRegalType regalType;
    private List<Item> items;

    public Regal(String name, int width, int height, int buttonNr, String qr_path, EnumRegalType regalType){
        this.name = name;
        this.widthGrid = width;
        this.heigthGrid = height;
        this.buttonNr = buttonNr;
        this.qr_path = qr_path;
        this.regalType = regalType;
        items = new ArrayList<>();
    }

    public Regal(){
        this("Default Regal", 3, 1, -99, "./defaultQR.png", EnumRegalType.HARDWARE);
    }

    public void addItem(Item item){
        try{
            if(item != null){
                items.add(item);
            }
            else{
                throw new Exception("Item is null!");
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidthGrid() {
        return widthGrid;
    }

    public void setWidthGrid(int widthGrid) {
        this.widthGrid = widthGrid;
    }

    public int getHeigthGrid() {
        return heigthGrid;
    }

    public void setHeigthGrid(int heigthGrid) {
        this.heigthGrid = heigthGrid;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getButtonNr() {
        return buttonNr;
    }

    public void setButtonNr(int buttonNr) {
        this.buttonNr = buttonNr;
    }

    public String getQr_path() {
        return qr_path;
    }

    public void setQr_path(String qr_path) {
        this.qr_path = qr_path;
    }

    public EnumRegalType getRegalType() {
        return regalType;
    }

    public void setRegalType(EnumRegalType regalType) {
        this.regalType = regalType;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
