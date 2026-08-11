package Employees.Roles;

public class RoleModel
{
    
    private int id;
    private String name;
    private String description;

    public RoleModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public RoleModel() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
