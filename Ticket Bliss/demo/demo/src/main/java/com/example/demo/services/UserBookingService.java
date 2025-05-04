package com.example.demo.services;
import com.example.demo.entities.Train;
import com.example.demo.entities.User;
import com.example.demo.utils.UserServiceUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserBookingService {
    private User user;
    private final String USERS_PATH = "C:/java/projects/Ticket Bliss/demo/demo/src/main/java/com/example/demo/localDb/users.json";
    private ObjectMapper objmaper = new ObjectMapper();
    private List<User> userList ;

    // default constructor
    public UserBookingService() throws IOException {
        loadUsers();
    }

    // parameterized constructor

    public UserBookingService(User user) {
        this.user = user;
    }



    public List<User> loadUsers() throws IOException{
        File users = new File(USERS_PATH );
        return userList = objmaper.readValue(users, new TypeReference<List<User>>() { });
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

    public Boolean cancelBooking(String ticketId){

        Scanner s = new Scanner(System.in);
        System.out.println("Enter the ticket id to cancel");
        ticketId = s.next();

        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        String finalTicketId1 = ticketId;  // strings are immutable
        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId1));

        String finalTicketId = ticketId;
        user.getTicketsBooked().removeIf(Ticket -> Ticket.getTicketId().equals(finalTicketId));
        if (removed) {
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return Boolean.TRUE;
        }else{
            System.out.println("No ticket found with ID " + ticketId);
            return Boolean.FALSE;
        }
    }

    public void getBookingInfo(){

        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashPassword());
        }).findFirst();
        userFetched.ifPresent(User::printTickets);
    }

    public List<Train> getTrains(String source, String destination){
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        }catch(IOException ex){
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try{
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return true; // Booking successful
                } else {
                    return false; // Seat is already booked
                }
            } else {
                return false; // Invalid row or seat index
            }
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }
}
