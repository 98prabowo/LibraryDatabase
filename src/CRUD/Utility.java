package CRUD;

import java.io.*;
import java.time.Year;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Utility {

    public static void printMenu() {
        System.out.println("LIBRARY DATABASE\n");
        System.out.println("1.\tLook available books");
        System.out.println("2.\tFind a book");
        System.out.println("3.\tAdd books data");
        System.out.println("4.\tChange books data");
        System.out.println("5.\tDelete books data");
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (Exception ex) {
            System.err.println("Can not clear screen");
        }
    }

    public static boolean getYesOrNo(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n" + message + " (y/n)? ");
        String userInput = scanner.next();

        while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n")) {
            System.err.println("Enter yes (y) or no (n)");
            System.out.print("\n" + message + " (y/n)? ");
            userInput = scanner.next();
        }

        return userInput.equalsIgnoreCase("y");
    }

    protected static void printData(String data, int numberData) {
        StringTokenizer stringTokenizer = new StringTokenizer(data, ",");

        // Print No and Published Year
        stringTokenizer.nextToken();
        System.out.printf("| %d  ", numberData);
        System.out.printf("|\t\t%4s\t\t", stringTokenizer.nextToken());

        // Print Author
        String dataTemp = stringTokenizer.nextToken();
        System.out.printf("|  %s", dataTemp);
        printSpace(dataTemp);

        // Print Publisher
        dataTemp = stringTokenizer.nextToken();
        System.out.printf("|  %s", dataTemp);
        printSpace(dataTemp);

        // Print Tittle
        dataTemp = stringTokenizer.nextToken();
        System.out.printf("|  %s", dataTemp);
        printSpace(dataTemp);
        System.out.print("|");

        System.out.println();
    }

    private static void printSpace(String data) {
        int lengthSpace = (26 - data.length());
        for(int i = 0; i < lengthSpace; i++) {
            System.out.print(" ");
        }
    }

    protected static boolean findBookInDatabase(String[] keywords, boolean isDisplay) throws IOException{
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferedReader = new BufferedReader(fileInput);

        String data = bufferedReader.readLine();
        boolean isExist = false;
        int numberData = 1;

        if (isDisplay) {
            System.out.print("---------------------------------------------------------");
            System.out.println("-------------------------------------------------------");
            System.out.println("| No |  Published Year  |  Author\t\t\t\t\t |  Publisher\t\t\t\t  |  Title\t\t\t\t\t   |");
            System.out.print("---------------------------------------------------------");
            System.out.println("-------------------------------------------------------");
        }

        while (data != null) {
            // Check keyword in line
            isExist = true;

            for (String keyword: keywords) {
                isExist = isExist && data.toLowerCase().contains(keyword.toLowerCase());
            }

            if (isExist) {
                if (isDisplay) {
                    printData(data, numberData);
                    numberData++;
                } else {
                    break;
                }
            }

            data = bufferedReader.readLine();
        }

        if (isDisplay) {
            System.out.print("---------------------------------------------------------");
            System.out.println("-------------------------------------------------------");
            System.out.println();
        }

        fileInput.close();
        bufferedReader.close();

        return isExist;
    }

    protected static long getEntryPerYear(String author, String yearPublished) throws IOException{
        FileReader fileInput = new FileReader("database.txt");
        BufferedReader bufferInput = new BufferedReader(fileInput);

        long entry = 0;
        String data = bufferInput.readLine();
        Scanner scanner;
        String primaryKey;

        while (data != null) {
            scanner = new Scanner(data);
            scanner.useDelimiter(",");
            primaryKey = scanner.next();
            scanner = new Scanner(primaryKey);
            scanner.useDelimiter("_");

            author = author.replaceAll("\\s+","");
            if (author.equalsIgnoreCase(scanner.next()) && yearPublished.equalsIgnoreCase(scanner.next())) {
                entry = scanner.nextInt();
            }

            data = bufferInput.readLine();
        }

        return entry;
    }

    protected static String getYear() {
        Scanner scanner = new Scanner(System.in);
        String yearPublished = scanner.nextLine();
        boolean validYear = false;
        while (!validYear) {
            try {
                Year.parse(yearPublished);
                validYear = true;
            } catch (Exception e) {
                System.out.println("Wrong year format (YYYY)");
                System.out.print("Input published year again: ");
                yearPublished = scanner.nextLine();
            }
        }
        return yearPublished;
    }

}
