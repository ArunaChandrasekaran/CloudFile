package Onboarding;

/**
 *
 * @author aruna
 */
public class OnboardingModel {

    private int id;
    private String companyName;
    private String companyAddress;
    private String username;
    private String password;
    private String forgotPwdPhrase;
    private Integer employeeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getForgotPwdPhrase() {
        return forgotPwdPhrase;
    }

    public void setForgotPwdPhrase(String forgotPwdPhrase) {
        this.forgotPwdPhrase = forgotPwdPhrase;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
