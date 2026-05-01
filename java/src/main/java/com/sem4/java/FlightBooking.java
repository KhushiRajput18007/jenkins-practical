package com.sem4.java;

import java.sql.*;
import java.util.Scanner;

public class FlightBooking {

    static final String URL = "jdbc:mysql://localhost:3306/airlinedb";
    static final String USER = "root";
    static final String PASSWORD = "root";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Flight ID: ");
        int flightId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Passenger Name: ");
        String passengerName = sc.nextLine();
        System.out.print("Enter Seats Requested: ");
        int seatsRequested = sc.nextInt();

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            con.setAutoCommit(false);

            // Check available seats and price
            PreparedStatement checkStmt = con.prepareStatement(
                "SELECT available_seats, price_per_seat FROM flights WHERE flight_id = ?");
            checkStmt.setInt(1, flightId);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Booking Failed: Flight not found.");
                con.rollback();
                return;
            }

            int availableSeats = rs.getInt("available_seats");
            double pricePerSeat = rs.getDouble("price_per_seat");

            if (availableSeats < seatsRequested) {
                System.out.println("Booking Failed: Not enough seats available");
                con.rollback();
                return;
            }

         
            PreparedStatement updateStmt = con.prepareStatement(
                "UPDATE flights SET available_seats = available_seats - ? WHERE flight_id = ?");
            updateStmt.setInt(1, seatsRequested);
            updateStmt.setInt(2, flightId);
            updateStmt.executeUpdate();

         
            double totalAmount = seatsRequested * pricePerSeat;
            PreparedStatement insertStmt = con.prepareStatement(
                "INSERT INTO bookings (passenger_name, flight_id, seats_booked, total_amount) VALUES (?, ?, ?, ?)");
            insertStmt.setString(1, passengerName);
            insertStmt.setInt(2, flightId);
            insertStmt.setInt(3, seatsRequested);
            insertStmt.setDouble(4, totalAmount);
            insertStmt.executeUpdate();

            con.commit();
            System.out.println("Booking Successful!");

        } catch (SQLException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}
