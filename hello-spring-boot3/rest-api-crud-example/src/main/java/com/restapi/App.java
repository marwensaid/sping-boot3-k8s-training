package com.restapi;

import com.restapi.dao.model.Item;
import com.restapi.dao.model.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    ItemRepository itemRepository;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        itemRepository.save(new Item(null, "Item 1"));
        itemRepository.save(new Item(null, "Item 2"));
    }
}
