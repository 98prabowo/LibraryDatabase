package com.librata;

import CRUD.Operation;
import CRUD.Utility;

import java.io.IOException;
import java.util.Scanner;

/**
 * Library database: CRUD operation, non-GUI, non-00 version
 * All variables/methods are declared as static
 * in the non-00 version
 * by pad89
 */

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        boolean isContinue = true;

        while (isContinue) {
            Utility.clearScreen();
            Utility.printMenu();

            System.out.print("\n> ");
            String userInput = scanner.next();

            switch (userInput) {
                case "1":
                    System.out.println("\n====================");
                    System.out.println("LIST AVAILABLE BOOKS");
                    System.out.println("====================");
                    Operation.showData();
                    break;
                case "2":
                    System.out.println("\n============");
                    System.out.println("FIND A BOOKS");
                    System.out.println("============");
                    Operation.findBook();
                    break;
                case "3":
                    System.out.println("\n===============");
                    System.out.println("ADD BOOKS DATA");
                    System.out.println("===============");
                    Operation.addNewBook();
                    Operation.showData();
                    break;
                case "4":
                    System.out.println("\n=================");
                    System.out.println("CHANGE BOOKS DATA");
                    System.out.println("=================");
                    Operation.changeData();
                    break;
                case "5":
                    System.out.println("\n=================");
                    System.out.println("DELETE BOOKS DATA");
                    System.out.println("=================");
                    Operation.deleteData();
                    break;
                default:
                    System.err.println("\nYour input is not identified");
                    System.err.println("Please insert menu on the list");
            }

            isContinue = CRUD.Utility.getYesOrNo("Do you want to continue?");
        }

    }
}
