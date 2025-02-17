package com.restapi.app;

import com.restapi.app.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    WebClient webClient;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        FileAttribute<?>[] attributes = {
                PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-r--r--"))
        };
        Path tempFile = Files.createTempFile(Files.createTempDirectory("temp-dir"), "testData-", ".txt", attributes);

        System.out.println(tempFile.toAbsolutePath());

        webClient.post()
                .uri("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Employee(180, "Elon", "Musk"))
                .retrieve()
                .toBodilessEntity()
                .subscribe(responseEntity -> {
                    if (responseEntity.getStatusCode().is2xxSuccessful()) {

                        int statusCode = responseEntity.getStatusCode().value();
                        String locationURI = responseEntity.getHeaders().getLocation().toString();

                        System.out.println("Status: " + statusCode);
                        System.out.println("Location URI: " + locationURI);
                    }
                });

    /*webClient.post()
        .uri("/create")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new Employee(180, "Lokesh", "Active"))
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError,
            clientResponse -> {
              System.err.println("Client error: " + clientResponse.statusCode().value());
              return Mono.error(new RuntimeException("Client error"));
            })
        .onStatus(HttpStatusCode::is5xxServerError,
            clientResponse -> {
              System.err.println("Server error: " + clientResponse.statusCode().value());
              return Mono.error(new RuntimeException("Server error"));
            })
        .onStatus(HttpStatusCode::isError,
            clientResponse -> {
              System.err.println("Other error: " + clientResponse.statusCode().value());
              return Mono.error(new RuntimeException("Unknown error"));
            })
        .toBodilessEntity()
        .subscribe(responseEntity -> {
          if (responseEntity.getStatusCode().is2xxSuccessful()) {

            int statusCode = responseEntity.getStatusCode().value();
            String locationURI = responseEntity.getHeaders().getLocation().toString();

            System.out.println("Status: " + statusCode);
            System.out.println("Location URI: " + locationURI);
          }
        });

    //Create a Resource and Get Status and Location Header
    Employee newEmployee = new Employee(180, "Lokesh", "Active");
    webClient.post()
        .uri("http://localhost:3000/employees")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newEmployee)
        .retrieve()
        .toBodilessEntity()
        .subscribe(responseEntity -> {
          if (responseEntity.getStatusCode().is2xxSuccessful()) {

            int statusCode = responseEntity.getStatusCode().value();
            String locationURI = responseEntity.getHeaders().getLocation().toString();

            System.out.println("Status: " + statusCode);
            System.out.println("Location URI: " + locationURI);
          }
        });


    //Create a Resource and Get Status, Location Header and Body

    newEmployee = new Employee(190, "Lokesh", "Active");
    webClient.post()
        .uri("http://localhost:3000/employees")
        .body(Mono.just(newEmployee), Employee.class)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, response -> {
          return Mono.just(new ApplicationException("Bad Request"));
        })
        .onStatus(HttpStatusCode::is5xxServerError, response -> {
          return Mono.just(new ApplicationException("Server Error"));
        })
        .toEntity(Employee.class)
        .subscribe(responseEntity -> {
          System.out.println("Status: " + responseEntity.getStatusCode().value());
          System.out.println("Location URI: " + responseEntity.getHeaders().getLocation().toString());
          System.out.println("Created New Employee : " + responseEntity.getBody());
        });

    //Submit Form
    webClient.post()
        .uri("http://localhost:3000/employees")
        .body(BodyInserters.fromFormData("id", "1900")
            .with("name", "test name")
            .with("status", "active"))
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, response -> {
          return Mono.just(new ApplicationException("Bad Request"));
        })
        .onStatus(HttpStatusCode::is5xxServerError, response -> {
          return Mono.just(new ApplicationException("Server Error"));
        })
        .toEntity(Employee.class)
        .subscribe(responseEntity -> {
          System.out.println("Status: " + responseEntity.getStatusCode().value());
          System.out.println("Location URI: " + responseEntity.getHeaders().getLocation().toString());
          System.out.println("Created New Employee : " + responseEntity.getBody());
        });

    //With Multipart
    MultipartBodyBuilder builder = new MultipartBodyBuilder();

    builder.part("file", new FileSystemResource("c:/temp/file-name.txt"));
    builder.part("id", "190001", MediaType.TEXT_PLAIN);
    builder.part("name", "Lokesh", MediaType.TEXT_PLAIN);
    builder.part("status", "active", MediaType.TEXT_PLAIN);

    webClient.post()
        .uri("http://localhost:3000/employees")
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromMultipartData(builder.build()))
        .retrieve()
        .toEntity(Employee.class)
        .doOnError(WriteTimeoutException.class, ex -> {
          System.err.println("WriteTimeout");
        })
        .subscribe(responseEntity -> {
          System.out.println("Status: " + responseEntity.getStatusCode().value());
          System.out.println("Location URI: " + responseEntity.getHeaders().getLocation().toString());
          System.out.println("Created New Employee : " + responseEntity.getBody());
        });*/

    }
}


