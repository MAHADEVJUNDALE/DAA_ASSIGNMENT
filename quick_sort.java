import java.io.*;
import java.util.*;

class Movie {
    String title;
    double rating;
    int releaseYear;
    int popularity;

    Movie(String title, double rating, int releaseYear, int popularity) {
        this.title = title;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.popularity = popularity;
    }
}

public class Main {

    // Compare movies
    static boolean compareMovies(Movie a, Movie b, int choice) {

        if (choice == 1) {
            return a.rating > b.rating;
        } 
        else if (choice == 2) {
            return a.releaseYear > b.releaseYear;
        } 
        else {
            return a.popularity > b.popularity;
        }
    }

    // Partition function
    static int partition(ArrayList<Movie> movies,
                         int low, int high, int choice) {

        Movie pivot = movies.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {

            if (compareMovies(movies.get(j), pivot, choice)) {

                i++;

                Movie temp = movies.get(i);
                movies.set(i, movies.get(j));
                movies.set(j, temp);
            }
        }

        Movie temp = movies.get(i + 1);
        movies.set(i + 1, movies.get(high));
        movies.set(high, temp);

        return i + 1;
    }

    // Quick Sort function
    static void quickSort(ArrayList<Movie> movies,
                          int low, int high, int choice) {

        if (low < high) {

            int pivotIndex = partition(
                    movies, low, high, choice
            );

            quickSort(
                    movies, low, pivotIndex - 1, choice
            );

            quickSort(
                    movies, pivotIndex + 1, high, choice
            );
        }
    }

    // Display first 10 records
    static void displayMovies(ArrayList<Movie> movies) {

        System.out.println(
            "\n-------------------------------------------------------------"
        );

        System.out.println(
            "Title\t\tRating\tYear\tPopularity"
        );

        System.out.println(
            "-------------------------------------------------------------"
        );

        int limit = Math.min(10, movies.size());

        for (int i = 0; i < limit; i++) {

            Movie movie = movies.get(i);

            System.out.println(
                movie.title + "\t" +
                movie.rating + "\t" +
                movie.releaseYear + "\t" +
                movie.popularity
            );
        }
    }

    public static void main(String[] args) {

        ArrayList<Movie> movies = new ArrayList<>();

        // Read CSV records
        try {

            BufferedReader file = new BufferedReader(
                new FileReader("movies_real_titles.csv")
            );

            String line;

            // Skip header
            file.readLine();

            // Read records from CSV
            while ((line = file.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", 4);

                if (parts.length < 4) {
                    continue;
                }

                String title = parts[0];
                double rating = Double.parseDouble(parts[1]);
                int releaseYear = Integer.parseInt(parts[2]);
                int popularity = Integer.parseInt(parts[3]);

                Movie movie = new Movie(
                    title,
                    rating,
                    releaseYear,
                    popularity
                );

                movies.add(movie);
            }

            file.close();

        } catch (IOException e) {

            System.out.println(
                "Error: Could not open movies_real_titles.csv"
            );

            return;

        } catch (NumberFormatException e) {

            System.out.println(
                "Error: Invalid data in CSV file."
            );

            return;
        }

        System.out.println(
            "Total movies loaded: " + movies.size()
        );

        if (movies.isEmpty()) {

            System.out.println(
                "No movie records found."
            );

            return;
        }

        // Select sorting parameter
        Scanner sc = new Scanner(System.in);

        System.out.println(
            "\n========== STREAMFLIX MOVIE SORTING =========="
        );

        System.out.println(
            "\nSelect sorting parameter:"
        );

        System.out.println(
            "1. IMDB Rating"
        );

        System.out.println(
            "2. Release Year"
        );

        System.out.println(
            "3. Popularity"
        );

        System.out.print(
            "\nEnter your choice: "
        );

        int choice = sc.nextInt();

        if (choice < 1 || choice > 3) {

            System.out.println(
                "Invalid choice!"
            );

            sc.close();
            return;
        }

        // Apply Quick Sort
        quickSort(
            movies,
            0,
            movies.size() - 1,
            choice
        );

        System.out.println(
            "\nSorted Movie Data:"
        );

        if (choice == 1) {

            System.out.println(
                "(Highest IMDB Rating First)"
            );

        } else if (choice == 2) {

            System.out.println(
                "(Latest Release Year First)"
            );

        } else {

            System.out.println(
                "(Highest Popularity First)"
            );
        }

        // Display first 10 records
        displayMovies(movies);

        // Create output CSV
        try {

            BufferedWriter output = new BufferedWriter(
                new FileWriter("movies_sorted_output.csv")
            );

            output.write(
                "title,rating,release_year,popularity"
            );

            output.newLine();

            for (Movie movie : movies) {

                output.write(
                    movie.title + "," +
                    movie.rating + "," +
                    movie.releaseYear + "," +
                    movie.popularity
                );

                output.newLine();
            }

            output.close();

        } catch (IOException e) {

            System.out.println(
                "Error: Could not create output file."
            );

            sc.close();
            return;
        }

        System.out.println(
            "\n---------------------------------------------"
        );

        System.out.println(
            "Sorting completed successfully!"
        );

        System.out.println(
            "First 10 sorted records displayed above."
        );

        System.out.println(
            "Complete sorted data saved in: " +
            "movies_sorted_output.csv"
        );

        sc.close();
    }
}