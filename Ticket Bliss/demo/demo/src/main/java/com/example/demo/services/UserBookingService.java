package com.example.demo.services;
import com.example.demo.entities.User;
import com.example.demo.utils.UserServiceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;
    private static final String USERS_PATH = "C:\\java\\projects\\xyz\\demo\\demo\\src\\main\\java\\com\\example\\demo\\localDb\\users.json";
    private ObjectMapper objmaper = new ObjectMapper();
    private List<User> userList ;

    // parameterized constructor
    public UserBookingService(User user) throws IOException {
        this.user = user;
        File users = new File(USERS_PATH );
        userList = objmaper.readValue(users, new TypeReference<List<User>>() { });
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(curUser -> {
            return curUser.getName().equalsIgnoreCase(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), curUser.getHashPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User newUser){
        try {
            userList.add(newUser);
            addUserToLocalDb();
            return Boolean.TRUE;
        }catch (IOException ex){
            System.out.println("Error signing up the user");
            return Boolean.FALSE;
        }
    }

    public void addUserToLocalDb() throws IOException{
        File userFile = new File(USERS_PATH);
        objmaper.writeValue(userFile,userList);
    }

    public void getBookingInfo(){
        user.printTickets();
    }
}
