package com.mysf.sfwebclient.util;

import java.util.function.*;

public class FunctionAsObjectExample {
    public static void main(String[] args) {
        // Define a function that takes an integer and returns its square
        Function<Integer, Integer> square = x -> x * x;

        // Assign the function to a variable and use it
        int result1 = square.apply(5); // returns 25

        // Pass the function as an argument to another method
        int result2 = operateOnNumber(10, square); // returns 100
    }

    // A method that takes a number and a function as arguments and applies the function to the number
    public static int operateOnNumber(int num, Function<Integer, Integer> operation) {
        return operation.apply(num);
    }
}

