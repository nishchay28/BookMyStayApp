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

    void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " | Available: " + inventory.get(roomType));
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to BookMyStayApp");
        System.out.println("Version: 3.0");
        System.out.println("Application started successfully.");

        // Initialize rooms
        Room single = new SingleRoom();
        Room doubleR = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType(single.type, 5);
        inventory.addRoomType(doubleR.type, 3);
        inventory.addRoomType(suite.type, 2);

        // Display room details
        single.displayDetails();
        doubleR.displayDetails();
        suite.displayDetails();

        // Display inventory state
        inventory.displayInventory();
    }
}
