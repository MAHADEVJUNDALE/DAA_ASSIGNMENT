import java.util.*;
 
public class ExamTimetable {
 
    static class Room {
        String name;
        int capacity;
        Room(String name, int capacity) {
            this.name = name;
            this.capacity = capacity;
        }
    }
 
    static int n;                       // number of courses (vertices)
    static List<Set<Integer>> adj;      // adjacency lists of the conflict graph
 
    // Smallest colour not used by any already-coloured neighbour of u
    static int smallestFreeColour(int u, int[] color) {
        Set<Integer> used = new HashSet<>();
        for (int v : adj.get(u)) {
            if (color[v] != -1) used.add(color[v]);
        }
        int c = 0;
        while (used.contains(c)) c++;
        return c;
    }
 
    // Greedy colouring: colour vertices in the given order
    static int[] greedy(List<Integer> order) {
        int[] color = new int[n];
        Arrays.fill(color, -1);
        for (int u : order) {
            color[u] = smallestFreeColour(u, color);
        }
        return color;
    }
 
    // Welsh-Powell: greedy colouring after sorting by degree (descending)
    static int[] welshPowell() {
        List<Integer> order = range(n);
        order.sort((a, b) -> adj.get(b).size() - adj.get(a).size());
        return greedy(order);
    }
 
    // DSATUR: always colour the uncoloured vertex with the highest saturation
    // (number of distinct colours among its neighbours); ties broken by degree
    static int[] dsatur() {
        int[] color = new int[n];
        Arrays.fill(color, -1);
        // satSet[u] = distinct colours already used by neighbours of u (kept up to date)
        List<Set<Integer>> satSet = new ArrayList<>();
        for (int i = 0; i < n; i++) satSet.add(new HashSet<>());
        for (int step = 0; step < n; step++) {
            int best = -1, bestSat = -1, bestDeg = -1;
            for (int u = 0; u < n; u++) {
                if (color[u] != -1) continue;
                int s = satSet.get(u).size();
                int d = adj.get(u).size();
                if (s > bestSat || (s == bestSat && d > bestDeg)) {
                    best = u;
                    bestSat = s;
                    bestDeg = d;
                }
            }
            color[best] = smallestFreeColour(best, color);
            for (int v : adj.get(best)) satSet.get(v).add(color[best]);
        }
        return color;
    }
 
    // Number of colours (time slots) used
    static int countSlots(int[] color) {
        int max = -1;
        for (int c : color) max = Math.max(max, c);
        return max + 1;
    }
 
    static List<Integer> range(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) list.add(i);
        return list;
    }
 
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
 
        System.out.print("Enter number of courses: ");
        n = sc.nextInt();
        String[] courses = new String[n];
        Map<String, Integer> index = new HashMap<>();
        System.out.println("Enter course codes:");
        for (int i = 0; i < n; i++) {
            courses[i] = sc.next();
            index.put(courses[i], i);
        }
 
        adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new HashSet<>());
        int[] studentCount = new int[n];
 
        System.out.print("Enter number of students: ");
        int m = sc.nextInt();
        System.out.println("Enter student enrollments (student_id count course1 course2 ...):");
        for (int s = 0; s < m; s++) {
            sc.next();                       // student id (not needed for colouring)
            int k = sc.nextInt();
            List<Integer> enrolled = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                Integer idx = index.get(sc.next());
                if (idx != null) {
                    enrolled.add(idx);
                    studentCount[idx]++;
                }
            }
            // Every pair of courses taken by this student gets an edge (conflict)
            for (int a = 0; a < enrolled.size(); a++) {
                for (int b = a + 1; b < enrolled.size(); b++) {
                    int u = enrolled.get(a), v = enrolled.get(b);
                    if (u != v) {
                        adj.get(u).add(v);
                        adj.get(v).add(u);
                    }
                }
            }
        }
 
        System.out.print("Enter number of available classrooms: ");
        int r = sc.nextInt();
        Room[] rooms = new Room[r];
        System.out.println("Enter room name and capacity:");
        for (int i = 0; i < r; i++) {
            rooms[i] = new Room(sc.next(), sc.nextInt());
        }
 
        // Run the three colouring algorithms and compare
        int[] gColor = greedy(range(n));
        int[] wColor = welshPowell();
        int[] dColor = dsatur();
        int gS = countSlots(gColor), wS = countSlots(wColor), dS = countSlots(dColor);
 
        System.out.println("\nColouring comparison (number of time slots):");
        System.out.println("Greedy       : " + gS);
        System.out.println("Welsh-Powell : " + wS);
        System.out.println("DSATUR       : " + dS);
 
        // Use the colouring that needs the fewest slots
        int[] color = gColor;
        int slots = gS;
        if (wS < slots) { color = wColor; slots = wS; }
        if (dS < slots) { color = dColor; slots = dS; }
 
        // Room allocation: within each slot, biggest classes first,
        // each takes the smallest free room that is large enough
        String[] assignedRoom = new String[n];
        Arrays.fill(assignedRoom, "No room");
        for (int slot = 0; slot < slots; slot++) {
            List<Integer> slotCourses = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (color[i] == slot) slotCourses.add(i);
            }
            slotCourses.sort((a, b) -> studentCount[b] - studentCount[a]);
 
            boolean[] occupied = new boolean[r];
            for (int c : slotCourses) {
                int best = -1;
                for (int j = 0; j < r; j++) {
                    if (!occupied[j] && rooms[j].capacity >= studentCount[c]) {
                        if (best == -1 || rooms[j].capacity < rooms[best].capacity) {
                            best = j;
                        }
                    }
                }
                if (best != -1) {
                    occupied[best] = true;
                    assignedRoom[c] = rooms[best].name;
                }
            }
        }
 
        System.out.println("\nExam Timetable Schedule:");
        System.out.println("Total Time Slots Required: " + slots);
        System.out.println();
        System.out.printf("%-8s %-10s %-8s %-8s%n", "Course", "Time Slot", "Room", "Students");
        for (int i = 0; i < n; i++) {
            System.out.printf("%-8s %-10s %-8s %-8d%n",
                    courses[i], "Slot " + (color[i] + 1), assignedRoom[i], studentCount[i]);
        }
        sc.close();
    }
}
