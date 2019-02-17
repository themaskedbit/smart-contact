package com.themaskedbit.tempcontact;

import java.util.ArrayList;
import java.util.List;

public class Contact {
    private String name, photo;
    private int count, type;
    private ArrayList<String> numbers;

    public Contact() {

    }

    public Contact(String name, ArrayList<String> numbers, String photo, int count, int type) {
        this.name = name;
        this.numbers = numbers;
        this.photo = photo;
        this.count = count;
        this.type = type;
    }

    public Contact(String name, ArrayList<String> numbers, String photo) {
        this.name = name;
        this.numbers = numbers;
        this.photo = photo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }


    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public String getFirstNumber() {
        return numbers.get(0);
    }

    public int getCount(){
        return count;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setNumbers(ArrayList<String> number) {
        this.numbers = number;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
