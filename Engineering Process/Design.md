[GUI DESIGN](https://lucid.app/lucidchart/321d3b8a-5a22-4e96-aa6e-6327fc7195a7/edit?viewport_loc=-11%2C-10%2C1705%2C853%2C0_0&invitationId=inv_eb494aa2-3f3c-472c-a095-0dc5d2b11d80)

[RPS S/W DIAGRAM](https://en.wikipedia.org/wiki/Rock_paper_scissors#/media/File:Rock-paper-scissors.svg)

### **ALGORITHM (PROCEDURE):**

#### **1. Initialization**
1. Display main screen (frmMain and pnlMain) with:
    - **Dropdown (JComboBox)** to select a game.
    - **Proceed** and **Quit** buttons.
2. If a game is selected, enable **Proceed**.
3. If **Proceed** is clicked, go to **2. Configuration**.
4. If **Quit** is clicked, exit the program.


#### **2. Configuration**
1. Create a new test object.
2. Display configuration options:
    - **Experimental Settings**:
        - **Radio Buttons**: "Endless" / "Ending".
        - If "Ending" selected, enable **input field** for trial count.
    - **Game Settings**:
        - **Dropdowns** for **Player A Strategy** and **Player B Strategy**.
    - **Data Settings**:
        - **Checkboxes** for real-time statistics (who won, choices, games played).
        - **Checkboxes** for report statistics (win count, streaks, usage rates, etc.).
3. Display **Start Test**, **Back**, and **Quit** buttons.
4. If **Start Test** clicked, validate settings and proceed to **Simulation**.
5. If **Back** clicked, return to **Main Screen**.
6. If **Quit** clicked, exit the program.


#### **3. Simulation Loop**
1. Initialize all counters and stats.
2. While stop condition is **false**:
    1. Determine **Player A** and **Player B** moves based on their selected strategy.
    2. Compare moves to determine **winner, loser, or tie**.
    3. Update statistics:
        - **Win count, win rate, streaks, RPC usage, total games played**.
    4. Update **real-time stats display** (if enabled).
    5. Update **win rate bar animation**.
    6. If stop condition met or user ends test, break loop.
3. When loop ends, proceed to **Report**.


#### **4. Display Report**
1. Show a table with:
    - Two columns (Player A, Player B).
    - Rows for each selected statistic.
2. Display **Retest** and **Quit** buttons.
3. If **Retest** clicked, return to **Configuration**.
4. If **Quit** clicked, exit the program.


#### **5. Exit**
1. If **Quit** is clicked from any screen, terminate the program safely.


ALGORITHM (FLOWCHART):


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

    *Intuition choices are never absolutely certain (instead more LIKELY). Therefore, I
    likely have to determine the p(w) or r(p(w))

Adaptive
    Start with Nash Equilibrium and adjust probabilities based on opponent's frequent moves

PSEUDOCODE:



SOURCES:
https://www.reddit.com/r/dataisbeautiful/comments/xtnsj3/oc_how_to_mathematically_win_at_rock_paper/?rdt=36630
https://arxiv.org/pdf/1404.5199v1
https://www.cmu.edu/dietrich/sds/ddmlab/papers/ZhangMoisanGonzalez2020.pdf

