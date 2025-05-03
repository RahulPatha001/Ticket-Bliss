package com.example.demo;

import com.example.demo.services.UserBookingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
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
			System.out.println("Unexpected Error in loading the User");
		}
	}

}
