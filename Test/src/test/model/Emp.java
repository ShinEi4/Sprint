package test.model;

import ituprom16.framework.annotation.RequestParam;

public class Emp {
    @RequestParam(name = "nom")
    private String nom;
    
    @RequestParam(name = "age")
    private int age;
    
    @RequestParam(name = "salaire")
    private double salaire;
    
    // Constructeur par défaut nécessaire
    public Emp() {}
    
    // Getters et Setters
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public double getSalaire() {
        return salaire;
    }
    
    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }
} 