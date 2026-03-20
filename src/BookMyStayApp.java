import java.util.*;

// Reservation represents a confirmed booking
class Reservation {
    String guestName;
    String roomType;
    String reservationId;
    String roomId;

    Reservation(String guestName, String roomType, String reservationId, String roomId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.reservationId = reservationId;
        this.roomId = roomId;
    }

    void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Room ID: " + roomId);
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

    void incrementAvailability(String roomType) {
        inventory.put(roomType, getAvailability(roomType) + 1);
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

// Booking History – stores confirmed reservations
class BookingHistory {
    private List<Reservation> history;

    BookingHistory() {
        history = new ArrayList<>();
    }

    void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    boolean removeReservation(String reservationId) {
        return history.removeIf(r -> r.reservationId.equals(reservationId));
    }

    Reservation findReservation(String reservationId) {
        for (Reservation r : history) {
            if (r.reservationId.equals(reservationId)) {
                return r;
            }
        }
        return null;
    }

    void displayHistory() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            r.displayReservation();
        }
    }
}

// Cancellation Service – validates and performs rollback
class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> releasedRoomIds;

    CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        releasedRoomIds = new Stack<>();
    }

    void cancelReservation(String reservationId) {
        Reservation reservation = history.findReservation(reservationId);
        if (reservation == null) {
            System.out.println("Cancellation failed: Reservation ID " + reservationId + " not found.");
            return;
        }

        // Rollback operations
        releasedRoomIds.push(reservation.roomId); // track released room ID
        inventory.incrementAvailability(reservation.roomType); // restore inventory
        history.removeReservation(reservationId); // update history

        System.out.println("Cancellation successful for " + reservation.guestName +
                " | Room Type: " + reservation.roomType +
                " | Released Room ID: " + reservation.roomId);
    }

    void displayRollbackLog() {
        System.out.println("\n--- Rollback Log (Released Room IDs) ---");
        for (String id : releasedRoomIds) {
            System.out.println("Released Room ID: " + id);
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to BookMyStayApp");
        System.out.println("Version: 10.0");
        System.out.println("Application started successfully.");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 1);
        inventory.addRoomType("Double Room", 1);

        // Booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed reservations
        Reservation r1 = new Reservation("Alice", "Single Room", UUID.randomUUID().toString(), UUID.randomUUID().toString());
        Reservation r2 = new Reservation("Bob", "Double Room", UUID.randomUUID().toString(), UUID.randomUUID().toString());

        history.addReservation(r1);
        history.addReservation(r2);

        history.displayHistory();

        // Cancellation service
        CancellationService cancellationService = new CancellationService(inventory, history);

        // Guest initiates cancellation
        cancellationService.cancelReservation(r1.reservationId);

        // Display updated history and rollback log
        history.displayHistory();
        cancellationService.displayRollbackLog();
    }
}
