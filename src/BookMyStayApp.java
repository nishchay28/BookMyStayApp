import java.util.HashMap;

// Abstract class representing a generalized Room
abstract class Room {
    String type;
    int beds;
    double price;

    Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: $" + price);
    }
}

// Concrete room classes
class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 1000.0);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 1800.0);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 3500.0);
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

    void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        }
    }

    HashMap<String, Integer> getInventorySnapshot() {
        // Defensive copy to prevent accidental modification
        return new HashMap<>(inventory);
    }
}

// Search service for read-only access
class SearchService {
    void displayAvailableRooms(Room[] rooms, RoomInventory inventory) {
        System.out.println("\n--- Available Rooms ---");
        for (Room room : rooms) {
            int available = inventory.getAvailability(room.type);
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available);
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to BookMyStayApp");
        System.out.println("Version: 4.0");
        System.out.println("Application started successfully.");

        // Initialize rooms
        Room single = new SingleRoom();
        Room doubleR = new DoubleRoom();
        Room suite = new SuiteRoom();
        Room[] rooms = { single, doubleR, suite };

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType(single.type, 5);
        inventory.addRoomType(doubleR.type, 3);
        inventory.addRoomType(suite.type, 0); // Suite unavailable

        // Search service (read-only)
        SearchService search = new SearchService();
        search.displayAvailableRooms(rooms, inventory);
    }
}
