package com.mrt.utils;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class passJsonFileAsPayload {

    @Test
    public void createUser(){

        File jsonData =new File("src/test/resources/Schemas/exampleJSON.json");

        given()
                .baseUri("URL")
                .contentType(ContentType.ANY)
                .log().all()
                .body(jsonData).
        when()
                .post("pathParam mrt/test/id").prettyPeek()
        .then()
                .assertThat()
                .statusCode(201)
                .log().all()
                .contentType(ContentType.ANY);
    }

    @Test
    public void createUser2() throws IOException {

        byte[] inp = Files.readAllBytes(Paths.get("src/test/resources/Schemas/exampleJSON.json"));
        String jsonData=new String(inp);
        System.out.println(jsonData);

        given()
                .baseUri("URL")
                .contentType(ContentType.ANY)
                .log().all()
                .body(jsonData).
                when()
                .post("pathParam mrt/test/id").prettyPeek()
                .then()
                .assertThat()
                .statusCode(201)
                .log().all()
                .contentType(ContentType.ANY);
    }
}
