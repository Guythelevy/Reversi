# 🎮 Reversi Game in Java 🖥️

Welcome to the Reversi game, a strategic 🎲 two-player board game implemented in Java ☕. This project features 🧑‍🤝‍🧑 human and 🤖 AI players, a customizable 🎨 graphical interface, and various disc types to enhance gameplay.

---

## ⭐ Features

- **Graphical User Interface (🖼️):**
  - Interactive board for making moves 🎯.
  - Configurable settings for AI speed 🐢⚡ and board appearance 🟦⬜.
- **Player Types:**
  - **Human Players 🧑‍🤝‍🧑**: Play against another person 👫.
  - **AI Players 🤖:**
    - **Random AI 🎲**: Chooses moves randomly.
    - **Greedy AI 💡**: Maximizes flipped discs each turn.
- **Special Disc Types:**
  - **Simple Disc (⬤)**: Standard game disc.
  - **Unflippable Disc (⭕):** Cannot be flipped by opponents.
  - **Bomb Disc (💣):** Explodes, flipping adjacent discs 💥.
- **Undo Feature 🔙:**
  - Undo functionality for human vs. human matches 🤝.
- **Dynamic Gameplay 🎮:**
  - Tracks player wins 🏆 across matches.
  - AI strategies selectable via the GUI 🖱️.

---

## 🛠️ Requirements

- Java 8 ☕ or later

---

## 📜 How to Play

1. Clone this repository 📂:
   ```bash
   git clone <repository_url>
Compile the code 🛠️:

bash
Copy code
javac *.java
Run the game ▶️:

bash
Copy code
java Main
Use the GUI 🖼️ to:

Select player types (Human 🧑 or AI 🤖).
Configure game settings like AI speed ⚡ and board appearance 🎨.
📏 Rules
Reversi is played on an 8x8 board 📋 with two players taking turns 🔄. Each player places a disc to capture opponent discs by surrounding them ↔️↕️➕. Captured discs are flipped 🔄 to the player's color 🎨. The game ends when no more legal moves are available ⛔, and the player with the most discs wins 🏆.

Custom Rules for Special Discs:
Unflippable Disc (⭕): Immune to flipping 🚫.
Bomb Disc (💣): Explodes 💥 and flips adjacent discs, triggering chain reactions 🔗 with other Bomb Discs.
🗂️ File Overview
Main.java: Entry point 🚪; initializes game logic 🧠 and GUI 🖼️.
GameLogic.java: Core game rules 📜 and mechanics ⚙️.
PlayableLogic.java: Interface defining game logic methods 📄.
GUI_for_chess_like_games.java: Handles the graphical interface 🖥️.
Player.java: Abstract base class for human 🧑 and AI 🤖 players.
HumanPlayer.java: Represents a human-controlled player 🧑.
AIPlayer.java: Abstract class for AI players 🤖.
GreedyAI.java: AI prioritizing maximum flips 🔄.
RandomAI.java: AI making random moves 🎲.
Disc.java: Interface for disc types 🟠.
SimpleDisc.java: Standard disc ⬤.
UnflippableDisc.java: Disc immune to flipping ⭕.
BombDisc.java: Disc that flips adjacent discs 💣.
Position.java: Represents positions 📍 on the board.
Move.java: Encapsulates a move 🔄, including flipped discs 🔃.
⚙️ Customization
Adding New AI Strategies 🤖:
Extend the AIPlayer class 🏗️.
Implement the makeMove method 🖋️.
Register the new AI in AIPlayer.registerAllAIPlayers 📝.
Adjusting Board Size 📏:
Update the BOARD_SIZE constant in GameLogic.java.
⌨️ Key Bindings
Press B 🅱️: Place a Bomb Disc 💣 (if available).
Press V ✅: Place an Unflippable Disc ⭕ (if available).
🔮 Future Enhancements
Implement additional AI strategies (e.g., Minimax 🧠, Neural Networks 🤖).
Add online multiplayer functionality 🌐.
Enhance GUI with animations ✨ and themes 🎨.
🤝 Contributing
Contributions are welcome! Fork the repository 🍴, make changes 🛠️, and submit a pull request 📬.

📝 License
This project is licensed under the MIT License 📜. See LICENSE for details 📄
