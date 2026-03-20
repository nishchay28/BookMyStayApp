import java.util.*;

// Reservation represents a guest’s intent to book a room
class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Centralized inventory management with synchronized access
class RoomInventory {
    private Map<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
    }

    void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    synchronized boolean allocateRoom(String roomType, String guestName) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            System.out.println("Reservation confirmed for " + guestName +
                    " | Room Type: " + roomType +
                    " | Remaining: " + (available - 1));
            return true;
        } else {
            System.out.println("Sorry, " + guestName +
                    " | " + roomType + " is unavailable.");
            return false;
        }
    }
}

// Concurrent Booking Processor
class BookingProcessor implements Runnable {
    private RoomInventory inventory;
    private Reservation reservation;

    BookingProcessor(RoomInventory inventory, Reservation reservation) {
        this.inventory = inventory;
        this.reservation = reservation;
    }

    @Override
    public void run() {
        inventory.allocateRoom(reservation.roomType, reservation.guestName);
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to BookMyStayApp");
        System.out.println("Version: 11.0");
        System.out.println("Application started successfully.");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);

        // Simulate concurrent booking requests
        Thread t1 = new Thread(new BookingProcessor(inventory, new Reservation("Alice", "Single Room")));
        Thread t2 = new Thread(new BookingProcessor(inventory, new Reservation("Bob", "Single Room")));
        Thread t3 = new Thread(new BookingProcessor(inventory, new Reservation("Charlie", "Double Room")));
        Thread t4 = new Thread(new BookingProcessor(inventory, new Reservation("Diana", "Double Room"))); // overbooking attempt

        // Start threads simultaneously
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // Wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nConcurrent booking simulation completed.");
    }
}
