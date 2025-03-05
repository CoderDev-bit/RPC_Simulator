/**************************************************************************
 * File name:
 * main.java
 *
 * Description:
 * This file contains the main entry point for the Rock, Paper, Scissors (RPS)
 * game simulation. The class initializes the graphical user interface (GUI)
 * for the game by creating an instance of the GUI class and invoking the
 * initialization method. It is responsible for launching the game interface
 * where users can interact with the game settings and initiate the RPS simulation.
 *
 * Author:
 * S. Patel
 *
 * Date: Mar/3/2025
 *
 * Concepts:
 * - Use of the main method as the entry point for the application.
 * - Creation and initialization of the GUI class (OOP approach) for user interaction.
 ***************************************************************************/
package jGames.RPS;

public class main {

    /**********************************************************************
     * Method name:
     * main
     *
     * Description:
     * This is the main method which serves as the entry point for the program.
     * It creates an instance of the GUI class and initializes the GUI.
     *
     * Parameters:
     * - args: An array of command-line arguments passed to the program.
     *
     * Parameter Restrictions:
     * - None
     *
     * Return:
     * - None
     *
     * Return Restrictions:
     * - No restrictions
     **********************************************************************/
    public static void main(String[] args) {

        GUI objGUI = new GUI();
        objGUI.initGUI();

    }


}