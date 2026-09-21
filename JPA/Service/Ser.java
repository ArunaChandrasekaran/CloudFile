package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Ent;
import com.example.demo.Repo.Repo;

@Service 
public class Ser {

    final Repo r;

    Ser(Repo r) {
        this.r = r;
    }
    public String insert(Ent e)
    {
        r.save(e);
        return "inserted";
    }

    public List<Ent> get()
    {

        return r.findAll();
    }


    public Optional<Ent> getbyid(int id)
    {
        return r.findById(id);
    }

    public Ent updatebyid(Ent b)
    {

        return r.save(b);
        
    }


    public String deletebyid(Ent b,int id)
    {

        r.deleteById(id);
        return "deleted";
    }
    
}
