/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.builderjunittesting;


public class User{

    
    private String name;
    private int age;
    private String email;
    private String phone;
    
    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.email = builder.email;
        this.phone = builder.phone;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
        
     @Override
    public String toString() {
        return name + " " + age + " " + email + " " + phone;
    }
    
    public static class Builder {

        private String name;
        private int age;
        private String email;
        private String phone;

        public Builder(String name) {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name is required");
            }
            this.name = name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }
        
         public User build() {
            return new User(this);
        }
    }
        
}
