package com.example.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity 
@Table (name="products")
public class Ent
 {

    @Id 
    private int id;
    private String p_name;
    private int P_cost;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getP_name() {
        return p_name;
    }
    public void setP_name(String p_name) {
        this.p_name = p_name;
    }
    public int getP_cost() {
        return P_cost;
    }
    public void setP_cost(int p_cost) {
        P_cost = p_cost;
    }
    
}
