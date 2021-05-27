package com.bsuir.repository.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCredentials {
    private String login;
    private String password;
    private String host;
    private int port;
}
