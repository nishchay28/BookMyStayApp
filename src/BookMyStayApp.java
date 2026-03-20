import java.util.*;

// Reservation represents a guest’s intent to book a room
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

// Add-On Service
class AddOnService {
    String name;
    double cost;

    AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return name + " ($" + cost + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap;

    AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    void displayServices(String reservationId) {
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        if (services.isEmpty()) {
            System.out.println("No add-on services selected for Reservation ID: " + reservationId);
        } else {
            System.out.println("Add-On Services for Reservation ID " + reservationId + ":");
            double totalCost = 0;
            for (AddOnService s : services) {
                System.out.println(" - " + s);
                totalCost += s.cost;
            }
            System.out.println("Total Additional Cost: $" + totalCost);
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("Welcome to BookMyStayApp");
        System.out.println("Version: 7.0");
        System.out.println("Application started successfully.");

        // Simulate confirmed reservations (from UC6)
        Reservation r1 = new Reservation("Alice", "Single Room", UUID.randomUUID().toString());
        Reservation r2 = new Reservation("Bob", "Double Room", UUID.randomUUID().toString());

        r1.displayReservation();
        r2.displayReservation();

        // Add-On Service Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Guests select optional services
        serviceManager.addService(r1.reservationId, new AddOnService("Breakfast", 200.0));
        serviceManager.addService(r1.reservationId, new AddOnService("Airport Pickup", 500.0));
        serviceManager.addService(r2.reservationId, new AddOnService("Spa Access", 800.0));

        // Display services for each reservation
        serviceManager.displayServices(r1.reservationId);
        serviceManager.displayServices(r2.reservationId);
    }
}
