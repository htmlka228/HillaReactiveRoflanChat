package com.example.application.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageDTO {
    @NotBlank
    private String text;

    private String userName;
    private String time;
}
