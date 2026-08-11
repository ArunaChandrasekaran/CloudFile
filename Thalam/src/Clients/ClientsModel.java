package Clients;

/**
 *
 * @author aruna
 */
public class ClientsModel {

    private int id;
    private String name;
    private String phone;
    private String altPhone;
    private String email;
    private String address;

    public ClientsModel() {
    }

    public ClientsModel(String name, String phone, String altPhone, String email, String address) {
        this.name = name;
        this.phone = phone;
        this.altPhone = altPhone;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAltPhone() {
        return altPhone;
    }

    public void setAltPhone(String altPhone) {
        this.altPhone = altPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
