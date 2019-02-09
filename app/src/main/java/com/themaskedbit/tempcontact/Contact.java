package com.themaskedbit.tempcontact;

public class Contact {
    private String name, number, photo;

    public Contact() {

    }

    public Contact(String name, String number, String photo) {
        this.name = name;
        this.number = number;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
