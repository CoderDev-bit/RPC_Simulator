[GUI DESIGN](https://lucid.app/lucidchart/321d3b8a-5a22-4e96-aa6e-6327fc7195a7/edit?viewport_loc=-11%2C-10%2C1705%2C853%2C0_0&invitationId=inv_eb494aa2-3f3c-472c-a095-0dc5d2b11d80)

[RPS S/W DIAGRAM](https://en.wikipedia.org/wiki/Rock_paper_scissors#/media/File:Rock-paper-scissors.svg)

### **ALGORITHM (PROCEDURE):**

#### **1. Initialization**
1. Display main screen (frmMain displays pnlMain) with:
    - **Dropdown (JComboBox)** to select a game. (RPS only)
    - **Proceed** and **Quit** buttons.
2. If a game is selected, enable **Proceed**.
3. If **Proceed** is clicked, go to **2. Configuration**.
4. If **Quit** is clicked, exit the program.


#### **2. Configuration**
1. Create a new object type RPS which extends Test.
2. Display configuration options in pnlConfig:
    - **Experimental**:
        - **Radio Buttons**: "Endless" / "Ending".
        - If "Ending" selected, enable **text field** for trial count.
    - **Game**:
        - **2 Dropdowns** for **Player A Strategy** and **Player B Strategy**.
    - **Data**:
        - **Checkboxes** for real-time stats (who won, choices, games played).
        - **Checkboxes** for report stats (win count, streaks, usage rates, etc.).
3. Display **Start Test**, **Back**, and **Quit** buttons.
4. If **Start Test** clicked, validate settings and proceed to **3. Simulation**.
5. If **Back** clicked, return to **1. Initialization**.
6. If **Quit** clicked, exit the program.


#### **3. Simulation Loop**
1. Initialize all counters and stats.
2. While stop condition is **false**:
    1. Determine **Player A** and **Player B** moves based on their selected strategy.
    2. Compare moves to determine the **winner, loser, or tiers**.
    3. Update enabled real time and report statistics:
    4. Update **win rate bar animation (JProgressbar) (only supports int values so round double win rate to int)**.
    5. If stop condition met or user ends test, break loop.
3. When loop ends, proceed to **4. Report**.
4. There should be text field and a button to set speed of trials (# of delay between trials from 1 ms to max 1 sec)


#### **4. Display Report**
1. Display pnlReport and show a table with:
    - Constant two columns (Player A, Player B).
    - Variable rows for each selected report statistic.
2. Display **Retest** and **Quit** buttons.
3. If **Retest** clicked, return to **Configuration**.
4. If **Quit** clicked, exit the program.


#### **5. Exit**
1. If **Quit** is clicked from any screen, terminate the program safely.

RPC STRATEGIES:

Perfect Random (Nash equilibrium)
    Equal chance for all choices independent of any other stats (1/3)

Human Intuition (Pavlov-based)
    Opening Move
        35.4% rock
        35.0% paper
        29.6% scissors

    Not likely to repeat same choice more than 2x (third time they will likely choose what counter to the last move they chose)

    1. Win (likely to repeat choice)
    2. Tie (likely to choose random choice)
    3. Lose (likely to choose counter to opponent's choice/choose unpicked choice/shift)

Against Intuition (Anti-Pavlov)
    1. Win (choose counter to counter of your choice/choose opponent's choice/shift)
    2. Tie (choose random choice)
    3. Lose (choose counter to opponents choice/unpicked choice/shift)

Adaptive
    Start with Nash Equilibrium and adjust probabilities based on opponent's moves. Initially, all of opponent's choices
    should have a count of 1 (therefore, 1/3 equal probability for all choices). Each subsequent trial,
    update counters and calculate the most likely move from the opponent. Then, choose the counter to that move.


PSEUDOCODE:
_class main_
declare and initialize GUI object
objGUI.initializeGUI()

_class GUI_
import java swing
global/fields 
    frmMain,
    pnlMain, pnlConfig, pnlSimulate, pnlReport
    btnA, btnB, btnExit
    comboBoxA, comboBoxB
    radiobtnEndless, radiobtnEnding
    2D array of checkboxes
    tblReport
    lblTitle, lblDuration, lblTrials, lblA, lblB, lblReal, lblReport

initializeGUI()
    frmMain add pnlMain
    pnlMain add lblTitle("GameTest™"), btnA("Proceed", disabled), comboBoxA("RPS") and btnExit("Exit")
    comboBoxA addActionListener for selectionOfItem -> enable btnA
    btnA addActionListener for click -> proceed()
    btnExit addActionListener for click -> System.exit(0)

//Event handlers
proceed()
    hide pnlMain
    pnlConfig add lblTitle("Config"), btnA("Start"), btnB("Go Back"), all checkboxes in array
    btnA addActionListener for click -> start()
    btnB addActionListener for click -> goBack()
    btnExit addActionListener for click -> System.exit(0)

    

goBack()

start()

pause()

end()

restart()
    

_class test_

_class RPS_


SOURCES:
https://www.reddit.com/r/dataisbeautiful/comments/xtnsj3/oc_how_to_mathematically_win_at_rock_paper/?rdt=36630
https://arxiv.org/pdf/1404.5199v1
https://www.cmu.edu/dietrich/sds/ddmlab/papers/ZhangMoisanGonzalez2020.pdf
https://medium.com/ground-state-curiosity/how-to-probably-win-at-rock-paper-scissors-172aa3e768a8
