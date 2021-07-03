/* This is an interview exercise.
 *  Company:    Skymill
 *  Author:     Ondrej Spetko
 *  Date:       03/07/2021
 * */


package com.ospetkodev;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static MapController mapController;
    private static int[][] mapArray;
    private static int[] currPosition;
    private static int[][] directions;
    private static int direction;
    private static int mapHeightTotal;
    private static final boolean debugMode = false;

    // Classes

    private static abstract class DataController {
        abstract String getDataLine();

        int[] userInputToCommands(String userInput, int arrayLength){

            //Filter only numbers and commas
            userInput = userInput.replaceAll("([^0-9,])+", "");

            //Command to array
            String[] userInputArrayString = userInput.split(",");

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

            if (debugMode) {
                System.out.println("Raw input: " + userInput);
                System.out.println("Processed input: " + userInput);
                System.out.println("Processed input (A): " + Arrays.toString(userInputArrayString));
                System.out.println("Commands (I): " + Arrays.toString(userInputArrayInt));
            }

            return userInputArrayInt;
        }

        String processCommands(int[] userCommands) {
            for (int command:userCommands) {
                switch (command) {
                    case 0 -> {
                        if (debugMode)
                            System.out.println("Exit:" + command + " ");
                        return currentPositionToString();
                    }
                    case 1 -> {
                        currPosition[0] += directions[direction][0];
                        currPosition[1] += directions[direction][1];

                        if (debugMode)
                            System.out.print("F:" + command + " ");
                    }
                    case 2 -> {
                        currPosition[0] += directions[direction][0] * -1;
                        currPosition[1] += directions[direction][1] * -1;

                        if (debugMode)
                            System.out.print("B:" + command + " ");
                    }
                    case 3 -> {
                        mapController.rotate('R');

                        if (debugMode)
                            System.out.print("R:" + command + " ");
                    }
                    case 4 -> {
                        mapController.rotate('L');

                        if (debugMode)
                            System.out.print("L:" + command + " ");
                    }
                    default -> {
                        if (debugMode)
                            System.out.print("?:" + command + " ");
                    }
                }

                //Position fell out of map
                if(mapArray[currPosition[0]][currPosition[1]] == 0)
                    return "[-1,-1]";
            }

            return currentPositionToString();

        }

        abstract void close();
    }

    private static abstract class MapController {
        abstract void rotate(char rotDirection);
        abstract boolean initialize(int[] userInputArrayInt);
    }

    private static class ScannerDataController extends DataController{
        private final Scanner scanner;

        public ScannerDataController(){
            scanner = new Scanner(System.in);
        }

        @Override
        public String getDataLine(){
            return scanner.nextLine();
        }

        @Override
        public void close() {
            scanner.close();
        }
    }

    private static class RectangleMapController extends MapController {

        @Override
        public void rotate(char rotDirection){
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

        @Override
        public boolean initialize(int[] userInputArrayInt) {

            //Invalid starting position
            if (userInputArrayInt[2] < 0 || userInputArrayInt[2] > userInputArrayInt[0] - 1 || userInputArrayInt[3] < 0 || userInputArrayInt[3] > userInputArrayInt[1] - 1){
                if (debugMode)
                    System.out.println("Invalid starting position!");
                return false;
            }

            //Create map array (+ edge elements [0] around the map = +2)
            mapArray = new int[userInputArrayInt[0] + 2][userInputArrayInt[1] + 2];
            mapHeightTotal = userInputArrayInt[1] + 2;

            // Fill each row with 1 (1 - active field, 0 - inactive/out[default value]),
            for (int mapColumn = 1; mapColumn < mapArray.length - 1; mapColumn++)
                for (int mapRow = 1; mapRow < mapArray[mapColumn].length - 1; mapRow++)
                    mapArray[mapColumn][mapRow] = 1;

            //Set starting position
            currPosition = new int[]{userInputArrayInt[2]+1, userInputArrayInt[3]+1};

            return true;
        }
    }

    // Main

    public static void main(String[] args) {

        //Initializing
        directions = new int[][]{{0,-1},{1,0},{0,1},{-1,0}};
        direction = 0;
        DataController dataController = new ScannerDataController();
        mapController = new RectangleMapController();

        //Step 1: Create map
        while(true) {
            //Get user input from scanner
            String userInput = dataController.getDataLine();

            //Process user input to array(int)
            int[] userInputArrayInt = dataController.userInputToCommands(userInput, 4);

            //Empty command
            if(userInputArrayInt.length==0) {
                if(debugMode)
                    System.out.println("Invalid initializing command!");
                continue;
            }

            if(!mapController.initialize(userInputArrayInt))
                continue;

            break;
        }

        if(debugMode) {
            System.out.println("Map (1): " + (mapArray.length - 2) + "x" + (mapArray[0].length - 2));
            System.out.println("Starting position (X): " + currentPositionToString());
            mapToString();
        }



        //Step 2: Process move commands
        while(true){
            //Get user input from scanner
            String userInput = dataController.getDataLine();

            //Process user input to array(int)
            int[] userInputArrayInt = dataController.userInputToCommands(userInput, 0);

            //Empty command
            if(userInputArrayInt.length==0) {
                if(debugMode)
                    System.out.println("Invalid movement command!");
                continue;
            }

            //Process commands and output the resulting position
            if(debugMode) {
                System.out.println("Final position:" + dataController.processCommands(userInputArrayInt));
                mapToString();
            }
            else
                System.out.println(dataController.processCommands(userInputArrayInt));

            break;
        }

        dataController.close();
    }

    // Helper functions

    private static String currentPositionToString(){
        return "[" + (currPosition[0] - 1) + ", " + (currPosition[1]-1) + "]";
    }

    private static void mapToString(){
        for (int mapRow = 0; mapRow < mapHeightTotal; mapRow++){
            System.out.println();
            for (int mapColumn = 0; mapColumn < mapArray.length; mapColumn++)
                if (currPosition[0] == mapColumn && currPosition[1] == mapRow)
                    System.out.print("X ");
                else
                    System.out.print(mapArray[mapColumn][mapRow] + " ");

        }
        System.out.println();
    }
}
