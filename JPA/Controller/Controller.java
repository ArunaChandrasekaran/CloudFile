package com.example.demo.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Ent;
import com.example.demo.Service.Ser;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController 
@RequestMapping("/home")
public class Controller {

    @Autowired Ser s;
    @PostMapping("/insert")
    public String postMethodName(@RequestBody Ent entity) {
        
        
         s.insert(entity);

        return "inserted";
    }

    @GetMapping("/view")
    public List<Ent> getMethodName() {
        return s.get();
    }

    @GetMapping("/view/{id}")
    public Optional<Ent> getMethodName(@PathVariable int id) {
        return s.getbyid(id);
    }

    @PutMapping("/update")
    public Ent putMethodName(@RequestBody Ent entity) {

        return s.updatebyid(entity);
    }

    @DeleteMapping("/deletee/{id}")
    public String deleteee(Ent e, @PathVariable int id) {
        s.deletebyid(e, id);
        return "deleted";
    }
    
    
}
