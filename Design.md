# Low Level Design
This document talks about the design of game by describing its flow for game loop. The game mimics "Hill Climb Racing", a popular mobile
game, for desktop. The MVP for these focuses on 1-D terrain generation & car movement using arrow keys. The stuff related to "game over"
condition & scoring will be dealt later as part of features.

## Tech Stack
Modular JavaFX is being used for the game, which will be compiled with GraalVM for a standalone installable binary package which works cross-platform.

## Game Classes
Following are the different game objects existing in the game:

#### Vector
```java
class Vector {
    double x;
    double y;
}
```

#### TerrainVector
```java
class TerrainVector {
    Vector world;
    Vector screen;
}
```

#### CarBody
```java
class CarBody {
    // coordinates for position
    private Vector position; 

    // fixed coordinates for drawing
    private final Vector screen = CAR_BODY_SCREEN_POS; 

    // coordinates for velocity
    private Vector velocity;

    // orientation for body (radians)
    private double orientation;

    // update position
    private void updatePosition();

    // update velocity
    private void updateVelocity();
}
```

#### CarWheel
```java
class CarWheel {
    // fixed offset to car body (right or left wheel)
    private final Vector offset = CAR_WHEEL_OFFSET;

    // fixed radius
    private final double radius = CAR_WHEEL_RADIUS;

    // orientation in radians
    private double orientation;

    // get center of wheel (position is of car body world/screen)
    public Vector getCenter(double position);

    // get penetration factor (position is of car body)
    public double penetration(double position);

    // rotate the wheel of car (velocity of car body)
    public void rotate(double velocityX)
}
```

#### Terrain
```java
class Terrain {
    // list of points for polyline
    List<TerrainVector> worldPoints;

    // update the terrain, by pruning at the left & extending at the right
    public updateTerrain();

    // get height of terrain (interpolation)
    public double getHeight(double x);

    // get slope factor (carPos is x, y for car body)
    public double getSlope(Vector carPos);

    // translate terrain (all screen points changed)
    public void translate(Vector diff);
}
```

#### GameLoop
```java
public GameLoop extends AnimationTimer {
    // implement the entire loop here
    public void handle(long now)
}
```

#### Renderer
```java
public Renderer {
    // graphics context for drawing
    private final GraphicsContext gc;

    // sprite for car body
    private final Image carBodyImage;

    // sprite for car wheel
    private final Image carWheelImage;

    // clear the screen
    public void clearScreen();

    // draw the car body
    public void drawCarBody(CarBody body);

    // draw the car wheel
    public void drawCarWheel(CarWheel wheel);

    // draw the terrain
    public void drawTerrain(Terrain terrain);
}
```

#### InputHandler
```java
class InputHandler {
    private Boolean leftKeyPressed;
    private Boolean rightKeyPressed;

    // update key state on press
    public void onKeyPressed(KeyEvent event);

    // update key state on release
    public void onKeyReleased(KeyEevnt event);

    // is left key pressed
    public Boolean isLeftKeyPressed();

    // is right key pressed
    public Boolean isRightKeyPressed();
}
```

#### GameApp
Main javafx class, acts as entry point for the game.

## Directory Structure
```
src/
 └─ main/
     ├─ java/
     │   ├─ module-info.java
     │   └─ com.yourgame.hillclimbracing/
     │        ├─ app/
     │        │    ├─ GameApp.java          // main Application, scene switching, loads UI+canvas
     │        │    └─ GameLoop.java         // AnimationTimer, physics + scrolling + render
     │        │
     │        ├─ input/
     │        │    └─ InputHandler.java     // key press state tracking
     │        │
     │        ├─ render/
     │        │    └─ Renderer.java         // draws sky, ground fill, terrain, car, wheels
     │        │
     │        ├─ model/
     │        │    ├─ Vector.java           // simple vector
     │        │    ├─ TerrainVector.java    // world + screen pair
     │        │    ├─ CarBody.java          // world physics + orientation
     │        │    ├─ CarWheel.java         // wheel offset + rotation + penetration helpers
     │        │    └─ Terrain.java          // terrain points, extend, prune, interpolate, translate
     │        │
     │        └─ ui/
     │             ├─ GameController.java // controller for in-game UI (exit, pause, labels)
     │             └─ MenuController.java   // controller for intro menu UI
     │
     └─ resources/
         ├─ com/yourgame/hillclimbracing
         │    ├─ Game.fxml                  // UI over canvas
         │    └─ Menu.fxml                  // intro screen
         │
         └─ images/
              ├─ car_body.png
              ├─ wheel.png
              └─ other assets...
```

## Game Loop Flow
The game loop will function at 60 frames/sec to allow for smooth display, each frame following will happen :

1. Positon, Velocity & Acceleration updates to CarBody in both X and Y (world coordinates).
2. Check if the end world point of terrain polyline is "threshold" away, if yes, extend terrain to right and cut from left.
3. Calculate penetration factor for the two wheels, and move CarBody up in Y if it's a positive value (use world coordinates).
4. Based on the terrain Y at two wheel locations, calculate the slope and if > 5 degrees, rotate CarBody 75% appropriately otherwise 100%.
5. check the diff between previous frame positions and new ones after all changes in X and Y (calculate the displacement in world coords).
6. Redraw the CarBody with new orientation at the same point (fixed screen coordinates but desired angle).
7. Redraw wheels relative to CarBody, and rotate the orientation of wheels on their center based on direction of X velocity (get the screen coordinates based on body orientation).
8. Translate the terrain by the negative of diff of X and Y calculated earlier. (update terrain's screen coordinates)
