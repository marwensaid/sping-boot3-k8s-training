package com.restapi.web.errors;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@XmlRootElement(name = "error")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {

    private String message;
    private List<String> details;
}
