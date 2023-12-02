package com.bishopbaz.ecommercemartproject.models;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.bishopbaz.ecommercemartproject.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private Long id;
    private String name;
    private String email;
    private String password;
    private BigDecimal balance;

    public Users(UserDto signedUpUser) {
        this.name = signedUpUser.getName();
        this.email = signedUpUser.getEmail();
        String password = BCrypt.withDefaults().hashToString(12, signedUpUser.getPassword().toCharArray());
        this.password = password;
    }
}
