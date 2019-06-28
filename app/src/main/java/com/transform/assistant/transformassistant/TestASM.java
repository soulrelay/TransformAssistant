package com.transform.assistant.transformassistant;

public class TestASM {
    private static void printTwo() {
        System.out.println("CALL printOne");
        printOne();
        System.out.println("RETURN printOne");
        System.out.println("CALL printOne");
        printOne();
        System.out.println("RETURN printOne");
    }

    private static void printOne() {
    }
}
