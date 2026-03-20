import java.util.*;

// Reservation represents a guest’s intent to book a room
class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void displayRequest() {
        System.out.println("Guest: " + guestName + " | Requested: " + roomType);
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
        return requestQueue.poll(); // FIFO dequeue
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
        return inventory.getOrDefault(roomType, 0);
    }

    boolean decrementAvailability(String roomType) {
        int available = getAvailability(roomType);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }
}

// Booking Service – processes requests and allocates rooms
class BookingService {
    private RoomInventory inventory;
    private HashMap<String, Set<String>> allocatedRooms;

    BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashMap<>();
    }

    void processRequest(Reservation reservation) {
        String roomType = reservation.roomType;

        // Check availability
        if (inventory.getAvailability(roomType) > 0) {
            // Generate unique room ID
            String roomId = UUID.randomUUID().toString();

            // Ensure uniqueness using Set
            allocatedRooms.putIfAbsent(roomType, new HashSet<>());
            allocatedRooms.get(roomType).add(roomId);

            // Update inventory immediately
            inventory.decrementAvailability(roomType);

            // Confirm reservation
            System.out.println("Reservation confirmed for " + reservation.guestName +
                    " | Room Type: " + roomType +
                    " | Room ID: " + roomId);
        } else {
            System.out.println("Sorry, " + reservation.guestName +
                    " | " + roomType + " is unavailable.");
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to BookMyStayApp");
        System.out.println("Version: 6.0");
        System.out.println("Application started successfully.");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 0);

        // Initialize queue and booking service
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(inventory);

        // Guests submit requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Diana", "Single Room"));

        // Process requests in FIFO order
        while (bookingQueue.hasRequests()) {
            Reservation next = bookingQueue.getNextRequest();
            bookingService.processRequest(next);
        }
    }
}
