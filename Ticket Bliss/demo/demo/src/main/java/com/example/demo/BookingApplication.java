package com.example.demo;

import com.example.demo.entities.Ticket;
import com.example.demo.entities.Train;
import com.example.demo.entities.User;
import com.example.demo.services.UserBookingService;
import com.example.demo.utils.UserServiceUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class BookingApplication {

	public static void main(String[] args) {

		System.out.println("Welcome to Train Tickets Booking Portal:");
		Scanner scanner = new Scanner(System.in);
		int option = 0;
		UserBookingService userBookingService;
		try{
			userBookingService = new UserBookingService();
		}catch (IOException ex){
			System.out.println(ex);
			System.out.println("Unexpected Error in loading the User");
			return;
		}

		while (option != 7){
			System.out.println("Choose the required service");
			System.out.println("1. Sign up");
			System.out.println("2. Login");
			System.out.println("3. Fetch Bookings");
			System.out.println("4. Search Trains");
			System.out.println("5. Book a seat");
			System.out.println("6. Cancel the booking");
			option = scanner.nextInt();
			Train trainForBooking = new Train();
			switch (option){
				case 1:
					System.out.println("Enter your name:");
					String nameToSignup = scanner.next();
					System.out.println("Enter your password");
					String passwordToSignup = scanner.next();
					User userToSignup = new User(nameToSignup, UUID.randomUUID().toString(), UserServiceUtil.hashPassword(passwordToSignup),
							passwordToSignup, new ArrayList<>());
					userBookingService.signUp(userToSignup);
					break;
				case 2:
					System.out.println("Please Enter Your Name to Login");
					String nametoLogin = scanner.next();
					System.out.println("Please Enter your Password");
					String passwordToLogin = scanner.next();
					User loginUser = new User(nametoLogin, UUID.randomUUID().toString(), UserServiceUtil.hashPassword(passwordToLogin),
							passwordToLogin, new ArrayList<>());
					try{
						userBookingService = new UserBookingService(loginUser);
					} catch (Exception e) {
						System.out.println("Error occured in logging user");
                        throw new RuntimeException(e);
                    }
					break;
				case 3:
					System.out.println("Fetching your Bookings");
					userBookingService.getBookingInfo();
					break;
				case 4:
					System.out.println("Type your source station");
					String source = scanner.next();
					System.out.println("Type your destination station");
					String dest = scanner.next();
					List<Train> trains = userBookingService.getTrains(source, dest);
					int index = 1;
					for (Train t: trains){
						System.out.println(index+" Train id : "+t.getTrainId());
						for (Map.Entry<String, String> entry: t.getStationTimes().entrySet()){
							System.out.println("station "+entry.getKey()+" time: "+entry.getValue());
						}
					}
					System.out.println("Select a train by typing 1,2,3...");
					trainForBooking = trains.get(scanner.nextInt());
					break;
				case 5:
					System.out.println("Select a seat out of these seats");
					List<List<Integer>> seats = userBookingService.fetchSeats(trainForBooking);
					for (List<Integer> row: seats){
						for (Integer val: row){
							System.out.print(val+" ");
						}
						System.out.println();
					}
					System.out.println("Select the seat by typing the row and column");
					System.out.println("Enter the row");
					int row = scanner.nextInt();
					System.out.println("Enter the column");
					int col = scanner.nextInt();
					System.out.println("Booking your seat....");
					Boolean booked = userBookingService.bookTrainSeat(trainForBooking, row, col);
					if(booked.equals(Boolean.TRUE)){
						System.out.println("Booked! Enjoy your journey");
					}else{
						System.out.println("Can't book this seat");
					}
					break;
				default:
					break;

            }
		}
	}

}
