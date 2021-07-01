package com.ospetkodev;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static Scanner scanner;
    static int[][] mapArray;
    static int[] currPosition;
    static int[][] directions;
    static int direction;

    public static void main(String[] args) {

        //Initializing
        directions = new int[][]{{-1,0},{0,1},{1,0},{0,-1}};
        direction = 0;

        //Step 1: Create map
        while(true) {
            //Get user input from scanner
            String userInput = getUserLineScanner();

            //Process user input to array(int)
            int[] userInputArrayInt = userInputToCommands(userInput, 4);

            //Empty command
            if(userInputArrayInt.length==0) {
                System.out.println("Invalid initializing command!");
                continue;
            }

            if(!initializeMapSquare(userInputArrayInt))
                continue;

            break;
        }

        System.out.println("Map: " + mapArray.length + "x" + mapArray[0].length);
        System.out.println("Starting position: " + currentPositionToString());

        mapToString();

        //Step 2: Process move commands
        while(true){
            //Get user input from scanner
            String userInput = getUserLineScanner();

            //Process user input to array(int)
            int[] userInputArrayInt = userInputToCommands(userInput, 0);

            //Empty command
            if(userInputArrayInt.length==0) {
                System.out.println("Invalid initializing command!");
                continue;
            }

            //Process commands and output the resulting position
            System.out.println("Final position:" + processCommands(userInputArrayInt));

            break;
        }

        scanner.close();
    }

    private static String currentPositionToString(){
        return "[" + (currPosition[0] - 1) + ", " + (currPosition[1]-1) + "]";
    }

    private static void rotate(char rotDirection){
        if(rotDirection == 'L'){
            if(direction == 0)
                direction = 3;
            else
                direction --;
        }
        else if(rotDirection == 'R'){
            if(direction == 3)
                direction = 0;
            else
                direction ++;
        }
    }

    private static String processCommands(int[] userCommands) {
        for (int command:userCommands) {
            switch (command) {
                case 0 -> {
                    System.out.println("Exit:" + command + " ");
                    return currentPositionToString();
                }
                case 1 -> {
                    currPosition[0] += directions[direction][0];
                    currPosition[1] += directions[direction][1];
                    System.out.print("F:" + command + " ");
                }
                case 2 -> {
                    currPosition[0] += directions[direction][0] * -1;
                    currPosition[1] += directions[direction][1] * -1;
                    System.out.print("B:" + command + " ");
                }
                case 3 -> {
                    rotate('R');
                    System.out.print("R:" + command + " ");
                }
                case 4 -> {
                    rotate('L');
                    System.out.print("L:" + command + " ");
                }
                default -> System.out.print("?:" + command + " ");
            }

            //Position fell out of map
            if(mapArray[currPosition[0]][currPosition[1]] == 0)
                return "[-1,-1]";
        }

        return currentPositionToString();

    }

    private static boolean initializeMapSquare(String type, int[] userInputArrayInt) {

        //Create map array
        mapArray = new int[userInputArrayInt[0] + 2][userInputArrayInt[1] + 2];

        // Square shape
        if (type.equals("square")){
            // Fill each row with 1 (1 - active field, 0 - inactive/out),
            for (int mapRow = 1; mapRow < mapArray.length - 1; mapRow++)
                for (int mapColumn = 1; mapColumn < mapArray[mapRow].length - 1; mapColumn++)
                    mapArray[mapRow][mapColumn] = 1;
        }

        //Invalid starting position
        if (userInputArrayInt[2] < 0 || userInputArrayInt[2] > mapArray.length - 3 || userInputArrayInt[3] < 0 || userInputArrayInt[3] > mapArray[userInputArrayInt[2]].length - 3){
            System.out.println("Invalid starting position!");
            return false;
        }

        //Set starting position
        currPosition = new int[]{userInputArrayInt[2]+1, userInputArrayInt[3]+1};

        return true;
    }

    private static void mapToString(){
        for (int[] mapRow:mapArray){
            System.out.println();
            for (int mapPoint:mapRow)
                System.out.print(mapPoint + " ");

        }
        System.out.println();
    }

    private static String getUserLineScanner(){
        if(scanner == null)
            scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static int[] userInputToCommands(String userInput, int arrayLength){

        System.out.println("Raw input: " + userInput);

        //Filter only numbers and commas
        userInput = userInput.replaceAll("([^0-9,])+", "");

        System.out.println("Processed input: " + userInput);

        //Command to array
        String[] userInputArrayString = userInput.split(",");

        System.out.println("Processed input (A): " + Arrays.toString(userInputArrayString));

        //Invalid starting command: invalid number of commands
        if(arrayLength != 0 && userInputArrayString.length != arrayLength)
            return new int[0];

        //Cast commands: String >> Integer, Check empty strings
        int[] userInputArrayInt = new int[userInputArrayString.length];
        for (int i = 0; i<userInputArrayString.length; i++) {
            //Empty data: ""
            if(userInputArrayString[i].isEmpty())
                return new int[0];

            userInputArrayInt[i] = Integer.parseInt(userInputArrayString[i]);
        }

        System.out.println("Commands (I): " + Arrays.toString(userInputArrayInt));

        return userInputArrayInt;
    }
}
