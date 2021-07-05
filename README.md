# sm
This is "will it fall-off the map" console application simulation written in Java.

The application set up the map size and initial position by taking initial argument of 4 numbers separated by comma W,H,X,Y where W is width of map,
H is height of the map, X is initial x position of the simulation and Y is initial y position of the simulation.

The origo [0,0] is in the top left corner.

Then application takes movement commands which is arbitrarily long string of numbers ranging between 0-4 separated by comma (e.g. 1,2,3,0,4).
These arguments are evaluated accoring to following protocol: 0 - exit, 1 - forward, 2 - backwards, 3 - rotate right, 4 - rotate left

Finally application will output final position [X,Y] if the object did not fall off the map. Otherwise, the application output [-1,-1] which means the object
fell off the map during the simulation.


The application is using object oriented architecture that allows for further extension of the simulation functionalities using SOLID principals.
The application does not contain any tests.


