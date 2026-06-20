
package gamaexam;


public class Patient
{
    
    private int id;
    private String name;
    private int age;
    private String gender;
    private String disease;

    public Patient(int id, String name, int age, String gender, String disease) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.disease = disease;
    }
    
    

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }
    
    @Override
    public String toString() {
        return "patient id : " + id+
                "\nName     : " + name +
                "\nAge      : " + age +
                "\ndisease   : " + disease +
                "\ngender    : " + gender;    
    }
    
    
}
