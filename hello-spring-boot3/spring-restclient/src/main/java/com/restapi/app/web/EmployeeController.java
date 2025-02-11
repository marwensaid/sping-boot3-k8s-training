package com.restapi.app.web;

import com.restapi.app.model.Employee;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URI;

@RestController
public class EmployeeController {

  private final WebClient webClient;

  public EmployeeController(WebClient webClient) {
    this.webClient = webClient;
  }


    /** TODO:
     * Creates a new employee by sending a POST request to the /employees endpoint.
     *
     * This method constructs a new Employee object and sends it as the request body
     * using the WebClient. It handles the response by subscribing to the result and
     * processing both success and error scenarios.
     *
     * On success, it retrieves the HTTP status code and the location of the newly created
     * employee from the response headers.
     *
     * On error, it checks if the error is an instance of WebClientResponseException to
     * retrieve the status code and logs the error message. For other types of errors,
     * it logs a generic error message.
     */
  public void createEmployee(){


  }
}
