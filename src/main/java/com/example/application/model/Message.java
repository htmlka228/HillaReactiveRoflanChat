package com.example.application.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Message {
    @NotBlank
    private String text;

    private String userName;
    private String time;
}
