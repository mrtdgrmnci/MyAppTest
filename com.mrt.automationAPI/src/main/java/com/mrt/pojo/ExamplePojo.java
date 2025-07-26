package com.mrt.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExamplePojo {
    // Add fields here as needed
}
