package Misc;

public class WarehouseUser {
    private String name;
    private String email;

    public WarehouseUser(String name, String email){
        this.name = name;
        this.email = email;
    }
    public WarehouseUser(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "WarehouseUser{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
