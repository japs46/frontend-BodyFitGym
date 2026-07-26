package com.japs.frontend.bodyfitgym.models;

import java.time.LocalDate;


public class User {

    private Long id;


    private String document;


    private String name;


    private String lastName;


    private String userName;

    private String password;

    private String status;

    private LocalDate registrationDate;

    public User() {
    }

    public User(Long id, String document, String name, String lastName, String userName, String password, String status, LocalDate registrationDate) {
        this.id = id;
        this.document = document;
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.status = status;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", document='" + document + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
//private List<AfiliadoSimple> afiliados;
    //private List<ProductoSimple> productos;
    //private List<MembresiaSimple> membresias;

}
