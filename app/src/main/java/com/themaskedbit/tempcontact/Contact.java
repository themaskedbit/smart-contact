package com.themaskedbit.tempcontact;

public class Contact {
    private String name, number, photo;
    private int count;

    public Contact() {

    }

    public Contact(String name, String number, String photo, int count) {
        this.name = name;
        this.number = number;
        this.photo = photo;
        this.count = count;
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

    public int getCount(){
        return count;
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

    public void setCount(int count) {
        this.count = count;
    }
}
