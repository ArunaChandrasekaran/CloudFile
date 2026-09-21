package com.example.demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Ent;

public interface Repo extends JpaRepository<Ent, Integer> 
{

    
} 