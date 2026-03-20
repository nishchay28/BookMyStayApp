import java.io.*;
import java.util.*;

// Reservation represents a confirmed booking
class Reservation implements Serializable {
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
class RoomInventory implements Serializable {
    private Map<String, Integer> inventory;

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

    void displayInventory() {
        System.out.println("\n--- Inventory Snapshot ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + " | Available: " + inventory.get(type));
        }
    }
}

// Booking History – stores confirmed reservations
class BookingHistory implements Serializable {
    private List<Reservation> history;

    BookingHistory() {
        history = new ArrayList<>();
    }

    void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    List<Reservation> getAllReservations() {
        return new ArrayList<>(history);
    }

    void displayHistory() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            r.displayReservation();
        }
    }
}

// Persistence Service – handles save and restore
class PersistenceService {
    private static final String FILE_NAME = "system_state.ser";

    static void saveState(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    static Object[] loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("\nSystem state restored successfully.");
            return new Object[]{inventory, history};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous state found. Starting fresh.");
            return new Object[]{new RoomInventory(), new BookingHistory()};
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to BookMyStayApp");
        System.out.println("Version: 12.0");
        System.out.println("Application started successfully.");

        // Restore state if available
        Object[] restored = PersistenceService.loadState();
        RoomInventory inventory = (RoomInventory) restored[0];
        BookingHistory history = (BookingHistory) restored[1];

        // Initialize inventory if empty
        if (inventory.getAvailability("Single Room") == 0 &&
                inventory.getAvailability("Double Room") == 0) {
            inventory.addRoomType("Single Room", 2);
            inventory.addRoomType("Double Room", 1);
        }

        // Simulate new booking
        Reservation r1 = new Reservation("Alice", "Single Room", UUID.randomUUID().toString(), UUID.randomUUID().toString());
        if (inventory.decrementAvailability(r1.roomType)) {
            history.addReservation(r1);
            System.out.println("\nNew booking confirmed:");
            r1.displayReservation();
        }

        // Display current state
        inventory.displayInventory();
        history.displayHistory();

        // Save state before shutdown
        PersistenceService.saveState(inventory, history);
    }
}
