package com.example.mainproject;

public class User {
    private String name;
    private String age;

    public String getName(){
        return name;
    }
    public String getAge(){
        return age;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setAge(String age){
        this.age = age;
    }
    public String toString(){
        return  "User: " + name + " - " + age;
    }

}
