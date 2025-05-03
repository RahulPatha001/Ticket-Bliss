package com.example.demo.utils;

import org.mindrot.jbcrypt.BCrypt;

public class UserServiceUtil {

    public static boolean checkPassword(String password, String hashedPassword){

        return BCrypt.checkpw(password, hashedPassword);
    }

    public static String hashPassword(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }
}
