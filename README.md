# Color_Swtich_Game

An object oriented program based GUI which implements the color switch game.
<h2> Implementation </h2>

This project has been implemented using javaFX library. Some frequently used JavaFx components are Animation Timer, Shapes, Transitions & Color. The OOP’s involved are **inheritance, polymorphism, encapsulation & abstraction**. 

<h2> Design Patterns </h2>

The project implements several design patterns such as:
- Iterator Pattern : Collision Detection
- Factory Pattern : Dynamic Entity Creation
- Strategy Pattern : Obstacle Transition
- Facade Pattern : Dotted Obstacles in UniquePatte

<h2> Features </h2>

- Menus
  - Main Menu
  - New Game Menu
  - Pause Menu
  - Resurrect Menu
  - Game Over Menu
  - Instructions Menu
- Obstacle Implementation
  - Circle
  - Multi-Circle
  - Plus Obstacle
  - Square
  - Triangle
  - Horizontal Line Obstacle
  - Dotted Obstacle ( 4 shapes )
- Core Functionalities
  - Player Class Setup
  - Jump Ball Mechanism
  - Color Change Mechanism
  - Star Collection Mechanism
  - Position Class Setup
  - Dynamic Collision Mechanism
  - Dynamic Screen Shift Mechanism
  - Dynamic Obstacle Creation
  - Save/Load Game Mechanism

<h2> Problems Faced </h2>

Some problems faced were:
1. Serialization/Deserialization of JavaFx 
Components
2. Object Collision for few shapes ( e.g. Circle )
3. Implementation of Gravity Effect

<h2> Solutions </h2>

1. Serializing primitive values for obstacle position & orientation.
2. Dividing shapes into smaller components 
( such as arc for Circle)
3. Using animation timer for smooth 
animations.

<h2> Bonus Components </h2>

1. **Supersonic Speed** <br>
This feature gives the ball a supersonic boost for a certain amount of time.
2. **Force Field** <br>
This feature activates a shield for 10 seconds to avoid collision from incoming obstacles.
3. **Sound Effects and Background Music** <br>
The addition of sound effects on various actions such as button press,star collection, bonus mode and background music according to the game mode (normal,supersonic and forcefield).

