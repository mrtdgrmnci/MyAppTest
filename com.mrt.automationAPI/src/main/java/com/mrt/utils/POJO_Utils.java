package com.mrt.utils;

import com.github.javafaker.Faker;
import com.mrt.pojo.ExamplePojo;

public class POJO_Utils {

    static Faker faker= new Faker();

    public static ExamplePojo getRandomExamplePojo(){

        ExamplePojo examplePojo= new ExamplePojo();
        examplePojo.setMyId(faker.number().numberBetween(1,599));
        examplePojo.setMyName(faker.name().username());
        return examplePojo;
    }



}
