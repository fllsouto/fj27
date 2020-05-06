package model;

import java.io.Serializable;

public class Person implements Serializable {

    private static final long serialVersionUID = -6395990736718308210L;
    static String country = "Brazil";
    private int age;
    private String name;
    private String email;
    transient int height;

    public Person(int age, String name, String email, int height) {
        this.age = age;
        this.name = name;
        this.email = email;
        this.height = height;
    }

    public static String getCountry() {
        return country;
    }

    public static void setCountry(String country) {
        Person.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
