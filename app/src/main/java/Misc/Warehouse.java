package Misc;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Warehouse implements Serializable {
    private String id;
    private String name;
    private String adminId;
    private String create;
    private String subscribtionEnd;
    private ArrayList<String> users;
    private ArrayList<Regal> regale;
    private ArrayList<Item> items;
    public Warehouse(String name){
        this.name = name;
        users = new ArrayList<>();
        regale = new ArrayList<>();
        items = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if(users == null)
            users = new ArrayList<>();
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

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getSubscribtionEnd() {
        return subscribtionEnd;
    }

    public void setSubscribtionEnd(String subscribtionEnd) {
        this.subscribtionEnd = subscribtionEnd;
    }

    public void setRegale(ArrayList<Regal> regale) {
        this.regale = regale;
    }

    @Override
    public String toString() {
        return name;
    }
}
