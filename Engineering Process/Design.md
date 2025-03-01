GUI DESIGN
https://lucid.app/lucidchart/321d3b8a-5a22-4e96-aa6e-6327fc7195a7/edit?viewport_loc=-11%2C-10%2C1705%2C853%2C0_0&invitationId=inv_eb494aa2-3f3c-472c-a095-0dc5d2b11d80

RPS S/W DIAGRAM:
https://en.wikipedia.org/wiki/Rock_paper_scissors#/media/File:Rock-paper-scissors.svg

ALGORITHM (PROCEDURE):
When algorithm is run...
1. User selects a "Game" to test from dropdown (combobox)
2. User is allowed to proceed
3. User proceeds/quits

If user proceeds...
1. A new test of type "Game" is created
2. User is allowed to configure the test via GUI (default configs are shown)
    [Experimental]
        Duration: Endless/Ending
        Stop when: # of trials reached;(only if ending is selected)
        *Limit of number of trials based on the limits of the hardware
    [Game]
        Player A Strategy
        Player B Strategy
    [Data]
        Real-time
            who won
            player choices
            # of games played
        Report
            win count,
            win rate,
            overall winner,
            winning and losing streaks,
            win rate of a specific choice of a specific player,
            # of games played,
            % of RPC used for each player (this is not theoretical probability because...)
3. User starts test/goes back/quits

If user starts test...
1. RPS Animation IN
2. Update Real-time stats
3. RPS Animation OUT
4. Loop until end condition is true OR User ends test

If user ends test...
1. Show all stats he requested in Test configuration
2. User may retest or quit

If user quits...
1. Exit the program

ALGORITHM (FLOWCHART):

RPC Strategies
    Random (Nash equilibrium)
                Equal chance for all choices independent of any other stats

            Human intuition (Pavlov-based)

                Opening Move{
                    35.4% rock
                    35.0% paper
                    29.6% scissors
                }

                Not likely to repeat same choice more than 2x (third time they will likely choose what counter to the last move they chose)

                1. Win (repeat most likely)
                    W0 = 0.57 ± 0.03
                    W− = 0.20 ± 0.02
                    W+ = 0.23 ± 0.02

                2. Tie (repeat most likely)
                    T0 = 0.56 ± 0.03
                    T− = 0.22 ± 0.02
                    T+ = 0.22 ± 0.02

                2. Losers choose the unpicked choice next round (or go backwards in S/W Diagram)
                    /or choose opponents choice (the choice that beat them)
                    L0 = 0.45 ± 0.03
                    L− = 0.30 ± 0.02
                    L+ = 0.25 ± 0.02

            Anti-Pavlov
                1. Winners choose the opponents choice next round (or go) /choose unpicked choice
                2. Losers choose the unpicked choice next round (or go backwards in S/W Diagram)


            Tiers choose randomly

            *Intuition choices are never absolutely certain (instead more LIKELY). Therefore, I
            likely have to determine the p(w) or r(p(w))
        }
    }


    what statistics they want to see {
        real-time stats {

        }

        Report {

        }

    }
}

PSEUDOCODE:



SOURCES:
https://www.reddit.com/r/dataisbeautiful/comments/xtnsj3/oc_how_to_mathematically_win_at_rock_paper/?rdt=36630
https://arxiv.org/pdf/1404.5199v1
https://www.cmu.edu/dietrich/sds/ddmlab/papers/ZhangMoisanGonzalez2020.pdf

