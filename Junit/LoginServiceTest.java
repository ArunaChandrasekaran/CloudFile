/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;

import com.mycompany.junitclasswork.LoginService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginServiceTest {

    @Test
    public void validUserNameTest() {
        LoginService j = new LoginService();
        boolean res = j.userName("aruna");

        Assertions.assertTrue(res);
    }

    @Test
    public void invalidUserNameTest() {
        LoginService j = new LoginService();
        boolean res = j.userName("A1234");

        Assertions.assertFalse(res);
    }

    @Test
    public void validPasswordTest() {
        LoginService j = new LoginService();
        boolean res = j.password("Admin@12");

        Assertions.assertTrue(res);
    }

    @Test
    public void invalidPasswordTest() {
        LoginService j = new LoginService();
        boolean res = j.password("A12");

        Assertions.assertFalse(res);
    }

    @Test
    public void validLoginTest() {
        LoginService j = new LoginService();
        boolean res = j.login("aruna", "Admin@12");

        Assertions.assertTrue(res);
    }

    @Test
    public void invalidLoginTest() {
        LoginService j = new LoginService();
        boolean res = j.login(" ", "Admi12");

        Assertions.assertFalse(res);
    }
    
    @Test
    public void nullLoginTest() {
        LoginService j = new LoginService();
        boolean res = j.login(" ", "Admi12");

        Assertions.assertNotNull(res);
    }


}
