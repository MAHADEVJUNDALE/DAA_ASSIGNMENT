
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

class Item {
    String name;
    double weight;
    double value;
    boolean divisible;
    int priority;

    Item(String name, double weight, double value,
         boolean divisible, int priority) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.divisible = divisible;
        this.priority = priority;
    }

    double valuePerWeight() {
        return value / weight;
    }
}

public class FractionalKnapsack {

    static double fractionalKnapsack(
            ArrayList<Item> items,
            double capacity) {

        // Sort by priority first, then value/weight ratio
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item a, Item b) {
                if (a.priority != b.priority) {
                    return Integer.compare(a.priority, b.priority);
                }

                return Double.compare(
                    b.valuePerWeight(), a.valuePerWeight()
                );
            }
        });

        System.out.println("\nSorted Items:");
        System.out.printf("%-20s %-10s %-10s %-10s %-12s %-12s%n",
                "Item", "Weight", "Value", "Priority",
                "Value/Weight", "Type");

        for (Item item : items) {
            System.out.printf(
                "%-20s %-10.2f %-10.2f %-10d %-12.2f %-12s%n",
                item.name,
                item.weight,
                item.value,
                item.priority,
                item.valuePerWeight(),
                item.divisible ? "Divisible" : "Indivisible"
            );
        }

        double totalValue = 0.0;
        double totalWeight = 0.0;

        System.out.println("\nItems Selected for Transport:");

        for (Item item : items) {
            if (capacity <= 0) {
                break;
            }

            if (item.divisible) {
                // Take the full item or a fraction of it
                double takenWeight = Math.min(item.weight, capacity);
                double takenValue =
                        item.valuePerWeight() * takenWeight;

                totalWeight += takenWeight;
                totalValue += takenValue;
                capacity -= takenWeight;

                System.out.printf(
                    "- %s: %.2f kg, Utility = %.2f, Priority = %d, Type = Divisible%n",
                    item.name, takenWeight, takenValue, item.priority
                );

            } else {
                // Take an indivisible item only if it fits
                if (item.weight <= capacity) {
                    totalWeight += item.weight;
                    totalValue += item.value;
                    capacity -= item.weight;

                    System.out.printf(
                        "- %s: %.2f kg, Utility = %.2f, Priority = %d, Type = Indivisible%n",
                        item.name, item.weight, item.value, item.priority
                    );
                }
            }
        }

        System.out.println("\n========== FINAL REPORT ==========");
        System.out.printf("Total weight carried: %.2f kg%n", totalWeight);
        System.out.printf("Total utility value carried: %.2f units%n",
                totalValue);
        System.out.printf("Remaining capacity: %.2f kg%n", capacity);

        return totalValue;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of relief items: ");
        int n = sc.nextInt();
        sc.nextLine();

        if (n <= 0) {
            System.out.println("Number of items must be positive.");
            sc.close();
            return;
        }

        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("\nItem " + (i + 1));

            System.out.print("Enter item name: ");
            String name = sc.nextLine();

            System.out.print("Enter weight: ");
            double weight = sc.nextDouble();

            System.out.print("Enter utility value: ");
            double value = sc.nextDouble();

            System.out.print("Is divisible? (1 = Yes, 0 = No): ");
            int div = sc.nextInt();

            System.out.print(
                "Enter priority (1 = High, 2 = Medium, 3 = Low): "
            );
            int priority = sc.nextInt();
            sc.nextLine();

            if (weight <= 0 || value < 0 ||
                    (div != 0 && div != 1) ||
                    priority < 1 || priority > 3) {
                System.out.println("Invalid input. Please enter valid values.");
                sc.close();
                return;
            }

            items.add(new Item(
                name, weight, value, div == 1, priority
            ));
        }

        System.out.print("\nEnter knapsack capacity: ");
        double capacity = sc.nextDouble();

        if (capacity < 0) {
            System.out.println("Capacity cannot be negative.");
            sc.close();
            return;
        }

        fractionalKnapsack(items, capacity);

        sc.close();
    }
}
