package com.simple.bean;
/**
 * Created by zhouguizhi on 2017/11/28.
 */
public class Person {
    private String name;
    private String id_card;//身份证
    private int age;

    public Person(String name, String id_card, int age) {
        this.name = name;
        this.id_card = id_card;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", id_card='" + id_card + '\'' +
                ", age=" + age +
                '}';
    }
}
