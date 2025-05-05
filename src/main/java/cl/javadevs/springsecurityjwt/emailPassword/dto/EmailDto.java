package com.example.email_send_test.dto;


import lombok.Data;

@Data
public class EmailDto {

    private String mailFrom;
    private String mailTo;
    private String subject;
    private String username;
    private String jwt;

}
