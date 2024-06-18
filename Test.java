package mco1;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello World");
        boolean truer = true;

        while (truer){
            System.out.printf("input int: ");
            int integer = sc.nextInt();
            System.out.printf("Int: %d\n", integer);

            // Clear the buffer
            sc.nextLine();

            System.out.printf("input string: ");
            String string = sc.nextLine();
            System.out.printf("String: %s\n", string);
        }

        sc.close();
    }
}

