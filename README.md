# Old Maid Card Game

This project implements the classic card game Old Maid using object-oriented design principles and multithreading in Java. 

## Features

* Represents the game with separate classes for:
    * **Card:** Stores card suit and value.
    * **Deck:** Manages deck initialization, shuffling, drawing, and adding cards. Uses ConcurrentLinkedQueue for thread safety.
    * **Player:** Represents a player in the game with a hand of cards and turn logic. Each player is a separate thread.
    * **Game:** Manages players, deck, discard pile, and controls game flow (dealing, turns).

* Implements two versions with different synchronization mechanisms:
    * **Version 1 (Synchronized):** Uses synchronized keyword and wait()/notifyAll() methods for thread safety.
    * **Version 2 (Semaphore):** Uses Semaphore for turn control and a thread pool for managing player threads.

* Adheres to clean code principles:
    * Meaningful names
    * Single-responsibility functions
    * Minimal comments (self-explanatory code)
    * Error handling with try-catch blocks
    * Consistent formatting and indentation

## Getting Started

This project requires Java installed. 

1. Clone the repository:
  ```bash
    git clone https://github.com/m0hammad2Z/old-maid-card-game
  ```
2. Navigate to the project directory:
  ```bash
    cd old-maid-card-game
  ```
3. Compile and run the desire version.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE.md) file for details.

