import java.util.*;

// Custom Exception for invalid bookings
class InvalidBookingException extends Exception {
    InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation represents a confirmed booking
class Reservation {
    String guestName;
    String roomType;
    String reservationId;

    Reservation(String guestName, String roomType, String reservationId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = reservationId;
    }

    void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// Booking Request Queue
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Request added for " + reservation.guestName);
    }

    Reservation getNextRequest() {
        return requestQueue.poll();
    }

    boolean hasRequests() {
        return !requestQueue.isEmpty();
    }
}

// Centralized inventory management
class RoomInventory {
    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
    }

    void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1); // -1 signals invalid type
    }

    boolean decrementAvailability(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        int available = getAvailability(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("No availability for " + roomType);
        }
        inventory.put(roomType, available - 1);
        return true;
    }
}

// Booking Service – validates and processes requests
class BookingService {
    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;

    BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashMap<>();
    }

    void processRequest(Reservation reservation) {
        try {
            String roomType = reservation.roomType;

            // Validate room type and availability
            if (inventory.getAvailability(roomType) == -1) {
                throw new InvalidBookingException("Room type does not exist: " + roomType);
            }

            // Attempt to decrement availability
            inventory.decrementAvailability(roomType);

            // Generate unique room ID
            String roomId = UUID.randomUUID().toString();

            // Ensure uniqueness using Set
            allocatedRooms.putIfAbsent(roomType, new HashSet<>());
            allocatedRooms.get(roomType).add(roomId);

            // Confirm reservation
            System.out.println("Reservation confirmed for " + reservation.guestName +
                    " | Room Type: " + roomType +
                    " | Room ID: " + roomId);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking failed for " + reservation.guestName +
                    " | Reason: " + e.getMessage());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to BookMyStayApp");
        System.out.println("Version: 9.0");
        System.out.println("Application started successfully.");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 0);

        // Initialize queue and booking service
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(inventory);

        // Guests submit requests (including invalid cases)
        bookingQueue.addRequest(new Reservation("Alice", "Single Room", UUID.randomUUID().toString()));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room", UUID.randomUUID().toString()));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room", UUID.randomUUID().toString())); // invalid type

        // Process requests in FIFO order
        while (bookingQueue.hasRequests()) {
            Reservation next = bookingQueue.getNextRequest();
            bookingService.processRequest(next);
        }
    }
}
