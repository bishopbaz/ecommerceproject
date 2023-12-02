package com.bishopbaz.ecommercemartproject.serviceImpl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.bishopbaz.ecommercemartproject.dto.LoginRequestDto;
import com.bishopbaz.ecommercemartproject.dto.UserDto;
import com.bishopbaz.ecommercemartproject.models.Users;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserServiceImpl {

    public static List<Users> savedUsers = new ArrayList<>();
    public Function<UserDto, Users> saveUserInformation = (userDto -> {
        Users user = new Users(userDto);
        user.setId(savedUsers.size()==0?1: (long) savedUsers.size()+1);
        savedUsers.add(user);
        return user;
    });

    public Function<LoginRequestDto, Users> findUserByEmail= (loggedInUser)->savedUsers
            .stream()
            .filter(user-> Objects.equals(user.getEmail(), loggedInUser.getEmail()))
            .collect(Collectors.toList()).get(0);

    public Function<LoginRequestDto, Boolean> verifyPassword = (user)->BCrypt.verifyer()
            .verify(user.getPassword().toCharArray(),
                    user.getHashPassword()).verified;

}
