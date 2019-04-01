package Misc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Regal implements Serializable {
    private String name;
    private int widthGrid;
    private int heigthGrid;
    private int buttonNr;
    private List<Item> items;

    public Regal(String name, int width, int height, int buttonNr){
        this.name = name;
        this.widthGrid = width;
        this.heigthGrid = height;
        this.buttonNr = buttonNr;
        items = new ArrayList<>();
    }

    public Regal(){
        this("Default Regal", 3, 1, -99);
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

    @Override
    public String toString() {
        return this.name;
    }
}
