package Materials.Vendors;

public class VendorsModel 
{
    
    private int id;
    private String name;
    private String phone;
    private String email;
    private String altPhone;
    private String address;

    public VendorsModel(String name, String phone, String email, String altPhone, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.altPhone = altPhone;
        this.address = address;
    }

    public VendorsModel() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAltPhone() {
        return altPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAltPhone(String altPhone) {
        this.altPhone = altPhone;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
    
}
