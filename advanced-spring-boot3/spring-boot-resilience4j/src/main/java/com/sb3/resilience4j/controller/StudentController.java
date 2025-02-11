package com.sb3.resilience4j.controller;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    // TODO : This method handles GET requests to retrieve student details by ID.
    // It uses the RateLimiter pattern to limit the number of requests.
    // If the rate limit is exceeded, the `rateLimitingFallback` method is called.
    //
    // @param id the ID of the student
    // @return ResponseEntity containing the student details or a fallback response

    public ResponseEntity getStudentById(@PathVariable int id) {

        return null;
    }

    /**
     * TODO :
     * This method handles GET requests to retrieve course details by ID.
     * It uses the Bulkhead pattern to limit the number of concurrent calls.
     * If the Bulkhead limit is reached, the `bulkheadFallback` method is called.
     *
     * @param id the ID of the course
     * @return ResponseEntity containing the course details or a fallback response
     */

    public ResponseEntity getCourse(@PathVariable int id) {
        return null;
    }

    public ResponseEntity bulkheadFallback(int id,
                                           io.github.resilience4j.bulkhead.BulkheadFullException ex) {
        System.out.println("Bulkhead applied no further calls are accepted");

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many request - No further calls are accepted");
    }


    public ResponseEntity rateLimitingFallback(int id, RequestNotPermitted ex) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Retry-After", "60s"); // retry after one second

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .headers(responseHeaders) // send retry header
                .body("Too Many Requests - Retry After 1 Minute");
    }
}
