package CRUD;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Operation {

    public static void showData() throws IOException {
        FileReader fileInput;
        BufferedReader bufferInput;
        int numberData = 1;

        try {
            fileInput = new FileReader("database.txt");
            bufferInput = new BufferedReader(fileInput);
        } catch (Exception e) {
            System.out.println("File not found!");
            System.out.println("Please add the data first");
            addNewBook();
            return;
        }

        String data = bufferInput.readLine();

        System.out.print("---------------------------------------------------------");
        System.out.println("-------------------------------------------------------");
        System.out.println("| No |  Published Year  |  Author\t\t\t\t\t |  Publisher\t\t\t\t  |  Title\t\t\t\t\t   |");
        System.out.print("---------------------------------------------------------");
        System.out.println("-------------------------------------------------------");
        while (data != null) {
            Utility.printData(data, numberData);
            data = bufferInput.readLine();
            numberData++;
        }

        System.out.print("---------------------------------------------------------");
        System.out.println("-------------------------------------------------------");
        System.out.println();

        fileInput.close();
        bufferInput.close();
    }

    public static void findBook() throws IOException {

        // Is file exist?
        try {
            FileReader file = new FileReader("database.txt");
            file.close();
        } catch (Exception e) {
            System.err.println("File not found!");
            System.err.println("Please add the data first");
            addNewBook();
            return;
        }

        // Get keyword from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("find: ");
        String keyword = scanner.nextLine();
        String[] keyString = keyword.split("\\s+");

        // Find keyword in database
        Utility.findBookInDatabase(keyString, true);
    }

    public static void addNewBook() throws IOException{

        FileWriter fileOutput = new FileWriter("database.txt",true);
        BufferedWriter bufferOutput = new BufferedWriter(fileOutput);

        Scanner scanner = new Scanner(System.in);
        String author, title, publisher, yearPublished;

        System.out.print("Author name: ");
        author = scanner.nextLine();

        System.out.print("Book title: ");
        title = scanner.nextLine();

        System.out.print("Input publisher: ");
        publisher = scanner.nextLine();

        System.out.print("Year published (YYYY): ");
        yearPublished = Utility.getYear();

        // Check book in database
        String[] keywords = {yearPublished + "," + author + "," + publisher + "," + title};
        boolean isExist = Utility.findBookInDatabase(keywords, false);

        if (!isExist) {
            long entryNumber = Utility.getEntryPerYear(author, yearPublished) + 1;

            String authorWithoutSpace = author.replaceAll("\\s+","");
            String primaryKey = authorWithoutSpace + "_" + yearPublished + "_" + entryNumber;
            System.out.println("\nYou will input this data:");
            System.out.println("-------------------------------------");
            System.out.println("Primary key    : " + primaryKey);
            System.out.println("Year published : " + yearPublished);
            System.out.println("Author name    : " + author);
            System.out.println("Book title     : " + title);
            System.out.println("Publisher      : " + publisher);

            boolean isAdd = getYesOrNo("Do you want to add the data?");

            if (isAdd) {
                bufferOutput.write(primaryKey + "," + yearPublished + "," + author + "," + publisher + "," + title);
                bufferOutput.newLine();
                bufferOutput.flush();
            }


        } else {
            System.out.println("\nThe book is already in database with data as follow:");
            Utility.findBookInDatabase(keywords, true);
        }

        fileOutput.close();
        bufferOutput.close();
    }

    public static void changeData() throws IOException {
        // Get database original
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // Create temporary database
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // Show data
        System.out.println("Books list:");
        showData();

        // Get user input / change data
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter book number that will update: ");
        long updateNum = scanner.nextLong();

        // Show data will updated
        String data = bufferedInput.readLine();
        long entryCounts = 0;

        while (data != null) {
            entryCounts++;

            StringTokenizer stringTokenizer = new StringTokenizer(data, ",");

            if (entryCounts == updateNum) {
                System.out.println("\nData that that will updated:");
                System.out.println("-------------------------------------");
                System.out.println("Reference       :" + stringTokenizer.nextToken());
                System.out.println("Published Year  :" + stringTokenizer.nextToken());
                System.out.println("Author          :" + stringTokenizer.nextToken());
                System.out.println("Publisher       :" + stringTokenizer.nextToken());
                System.out.println("Title           :" + stringTokenizer.nextToken());

                // Get input from user
                String[] fieldData = {"published year", "author", "publisher", "title"};
                String[] tempData = new String[4];

                stringTokenizer = new StringTokenizer(data, ",");
                stringTokenizer.nextToken();

                for (int i = 0; i < fieldData.length; i++) {
                    boolean isUpdate = getYesOrNo("Do you want to change " + fieldData[i] + " data ");

                    String originalData = stringTokenizer.nextToken();
                    if (isUpdate) {
                        // User input
                        if (fieldData[i].equalsIgnoreCase("published year")) {
                            System.out.print("Year published (YYYY): ");
                            tempData[i] = Utility.getYear();
                        } else {
                            scanner = new Scanner(System.in);
                            System.out.print("\nEnter new " + fieldData[i] + ": ");
                            tempData[i] = scanner.nextLine();
                        }
                    } else {
                        tempData[i] = originalData;
                    }
                }

                stringTokenizer = new StringTokenizer(data, ",");
                stringTokenizer.nextToken();
                System.out.println("\nYou will update this data:");
                System.out.println("-------------------------------------");
                System.out.println("Published Year  :" + stringTokenizer.nextToken() + "-->" + tempData[0]);
                System.out.println("Author          :" + stringTokenizer.nextToken() + "-->" + tempData[1]);
                System.out.println("Publisher       :" + stringTokenizer.nextToken() + "-->" + tempData[2]);
                System.out.println("Title           :" + stringTokenizer.nextToken() + "-->" + tempData[3]);

                boolean isUpdate = getYesOrNo("Do you want to update data");

                if (isUpdate) {
                    // Check new data in database
                    boolean isExist = Utility.findBookInDatabase(tempData, false);

                    if (isExist) {
                        System.err.println("Book data is already exist, update process is canceled");
                        bufferedOutput.write(data);
                        bufferedOutput.newLine();
                    } else {
                        // Format new data to database
                        String newYearPublished = tempData[0];
                        String newAuthor = tempData[1];
                        String newPublisher = tempData[2];
                        String newTitle = tempData[3];

                        // Create primary key
                        long entryNumber = Utility.getEntryPerYear(newAuthor, newYearPublished) + 1;

                        String authorWithoutSpace = newAuthor.replaceAll("\\s+","");
                        String primaryKey = authorWithoutSpace + "_" + newYearPublished + "_" + entryNumber;

                        // Write new data to database
                        bufferedOutput.write(primaryKey + "," + newYearPublished + "," + newAuthor + ","
                                + newPublisher + "," + newTitle);
                    }
                } else {
                    // Copy data
                    bufferedOutput.write(data);
                }
            } else {
                // Copy data
                bufferedOutput.write(data);
            }
            bufferedOutput.newLine();

            data = bufferedInput.readLine();
        }

        // Write data to file tempDB
        bufferedOutput.flush();

        fileInput.close();
        fileOutput.close();
        bufferedInput.close();
        bufferedOutput.close();
        System.gc();

        // Delete database
        database.delete();

        // Rename tempDB to database
        tempDB.renameTo(database);
    }

    public static void deleteData() throws IOException {
        // Get database original
        File database = new File("database.txt");
        FileReader fileInput = new FileReader(database);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        // Create temporary database
        File tempDB = new File("tempDB.txt");
        FileWriter fileOutput = new FileWriter(tempDB);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // Show data
        System.out.println("Books list:");
        showData();

        // Get deleted data from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter book number that you want to delete: ");
        long deleteNum = scanner.nextLong();

        // Loop to read every data lines and skip deleted data
        boolean isFound = false;
        long entryCounts = 0;

        String data = bufferedInput.readLine();

        while (data != null) {
            entryCounts++;
            boolean isDelete = false;

            StringTokenizer stringTokenizer = new StringTokenizer(data, ",");

            if (entryCounts == deleteNum) {
                System.out.println("\nYou will delete this data:");
                System.out.println("----------------------------");
                System.out.println("Reference       :" + stringTokenizer.nextToken());
                System.out.println("Published Year  :" + stringTokenizer.nextToken());
                System.out.println("Author          :" + stringTokenizer.nextToken());
                System.out.println("Publisher       :" + stringTokenizer.nextToken());
                System.out.println("Title           :" + stringTokenizer.nextToken());

                isDelete = getYesOrNo("Do you want delete this data");
                isFound = true;
            }

            if (!isFound) {
                System.err.println("Book is not found!");
            }

            if (isDelete) {
                // Skip transfer data from database to tempDB
                System.out.println("\nData is deleted!");
            } else {
                // Transfer data from database to tempDB
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }

            data = bufferedInput.readLine();
        }

        // write buffered data to tempDB
        bufferedOutput.flush();

        // Close and clean database and tempDB from java IO
        fileInput.close();
        fileOutput.close();
        bufferedInput.close();
        bufferedOutput.close();
        System.gc();    // cleanup java IO

        // Delete database
        database.delete();

        // Rename tempDB to database
        tempDB.renameTo(database);

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
}
