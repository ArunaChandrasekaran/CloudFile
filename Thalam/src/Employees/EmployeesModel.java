package Employees;

/**
 *
 * @author aruna
 */
public class EmployeesModel {

    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private int roleId;
    private String role;

    public EmployeesModel() {
    }

    public EmployeesModel(String name, String phone, String email, String address, int roleId, String role) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.roleId = roleId;
        this.role = role;
    }

    public Integer getId() {
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
