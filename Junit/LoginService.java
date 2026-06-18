package com.mycompany.junitclasswork;

import java.util.HashSet;
import java.util.Set;

public class LoginService {

    public boolean userName(String name) {
        if (name == null || name.equals("")) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            int character = name.charAt(i);
            if (!((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z'))) {
                return false;
                
            }
        }
        return true;
    }

    public boolean password(String password) {
        if (password == null || password.equals("")) {
            return false;
        }

        Set<String> resultSet = new HashSet();
        for (int i = 0; i < password.length(); i++) {
            int character = password.charAt(i);
            if (character == ' ') {
                return false;
            } else if ((character >= 'a' && character <= 'z')) {
                resultSet.add("hasLower");
            } else if ((character >= 'A' && character <= 'Z')) {
                resultSet.add("hasUpper");
            } else if ((character >= '0' && character <= '9')) {
                resultSet.add("hasNumeric");
            } else {
                resultSet.add("hasSymbols");
            }
        }
        return resultSet.size() == 4;
    }
    
    public boolean login(String userName, String password)
    {
        return userName(userName) && password(password);
    }

}
