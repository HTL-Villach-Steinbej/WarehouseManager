package Misc;

import java.io.Serializable;
import java.util.ArrayList;

public class Warehouse implements Serializable {
    private String name;
    private String adminId;
    private ArrayList<String> users;
    private ArrayList<Regal> regale;
    private ArrayList<Item> items;
    public Warehouse(String name){
        this.name = name;
        users = new ArrayList<>();
        regale = new ArrayList<>();
        items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Regal> getRegale() {
        return regale;
    }

    public void setRegale(ArrayList<Regal> regale) {
        this.regale = regale;
    }

    @Override
    public String toString() {
        return name;
    }
}
