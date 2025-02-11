package com.sb3chatgpt.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotRequest {

    private String model;
    private List<Message> messages;
    private int n;
    private double temperature;
    private int max_tokens;
}
