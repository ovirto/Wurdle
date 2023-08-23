/*
Name: Oswaldo Virto
Date: 02-01-2023
Purpose: This program draws inspiration from the popular word game "Wordle"
Objective is to guess the secret (5-letter) word within 6 tries.

EXIT CODES:
1:  IO Exception
 */
import java.io.*;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        final int MAX_GUESSES = 6;
        final int MAX_LENGTH = 5;
        String[] allWords = new String[21952]; //FIXME: make space for all words in database
        String secretWord;
        boolean replay = true;


        //Game replay loop
        while (replay){

            char[] lettersRemaining = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

            try {
                //fills array with all words in database
                allWords = DataLoad();

            } catch (IOException e) {
                System.err.printf("**  ERROR: IO exception: %s", e);
                System.exit(1);
            }


            String guess;
            secretWord = WordPicker(allWords);
            System.out.println("""
                Welcome to Wurdle!
                A home-made version of the New York Times word game we all know and love.

                Input "help" at any point for the game rules or "exit" to exit the program
                """);

            //Empty game board
            String[] board = {"*****", "*****", "*****", "*****", "*****","*****"};


            int i = 0;


            while(i < MAX_GUESSES){

                guess = sc.next();
                guess = guess.toUpperCase();


                if(guess.equals("HELP")) {
                    //game rules
                    UserHelp();

                } else if(guess.equals("EXIT")) {
                    //exits game
                    UserExit();

                } else if(guess.length() != MAX_LENGTH){
                    //Guesses can only be 5 letters long
                    System.err.println("Please input a 5-letter word.");

                } else if(!(Arrays.asList(allWords).contains(guess.toLowerCase()))){
                    System.err.println("Not in words list. Please try again.");

                } else{
                    //prints remaining letters
                    lettersRemaining = RemainingLetters(lettersRemaining, guess);

                    //prints colored letters
                    board[i] = ColorPrinter(guess, secretWord);

                    //prints game board
                    PrintBoard(board);
                    i++;

                    //checks for win
                    WinCheck(guess, secretWord);

                    //counts guesses
                    GuessCounter(i, secretWord);
                }
            }



            replay = AskReplay(sc);
        }
        sc.close();
    }


    //Loads word database, Random word set as secretWord
    private static String[] DataLoad() throws IOException {
        File f = new File("5 Letter Words.txt");
        String[] words = new String[21952];
        int i = 0;

        Scanner sc = new Scanner(f);

        while (sc.hasNext()){
            words[i] = sc.next();
            i++;
        }

        sc.close();

        return words;
    }


    //Exits game upon user request
    private static void UserExit(){
        System.out.println("Game exited by user.");
        System.exit(0);
    }


    //Prints game rules
    private static void UserHelp(){
        System.out.println("""
                RULES:
                
                Objective:
                Guess the secret word within 6 guesses.
                
                Gameplay:
                White letters do not appear in the secret word.
                Yellow letters are correct, but in the wrong space.
                Green letters are correct and in the correct space.\s""");
    }


    //Updates and prints lettersRemaining
    private static char[] RemainingLetters(char[] lettersRemaining, String guess){

        char currentChar;
        char currentLetter;

        //Iterates through every char in guess
        for(int i = 0; i < guess.length(); i++){
            currentChar = guess.charAt(i);

            //Iterates through every char in lettersRemaining
            for(int j = 0; j < lettersRemaining.length; j++){
                currentLetter = lettersRemaining[j];

                //currentChar and currentLetter match removes j val from lettersRemaining
                if(currentChar == currentLetter){
                    lettersRemaining[j] = ' ';
                }
            }
        }

        System.out.println("\n\nLetters remaining: ");
        for (char c : lettersRemaining) {
            System.out.print(c + " ");
        }

        System.out.println("\n");

        return lettersRemaining;
    }


    //Uses word picker method to pick random word in database
    private static String WordPicker(String[] words){
        String s = words[RandomNumber()];
        s = s.toUpperCase();
        return s;
    }


    //Generates Random Number within words array range
    private static int RandomNumber(){
        int min = 0;
        int max = 21952;
        int range = max-min + 1;
        return (int)(Math.random() * range) + min;
    }


    //Prints String to console in color.
    //Correct letters are green
    //Correct letters in incorrect spaces are yellow
    //Incorrect letters are white
    public static String ColorPrinter(String guess, String secretWord){

        StringBuilder guessColor = new StringBuilder();
        char guessChar;
        int count = 0;
        String s;

        for(int i = 0; i < guess.length(); i++){
            guessChar = guess.charAt(i);
            s = String.valueOf(guessChar);
           guessColor.append(TextColor(guessChar, secretWord, count, s));
           count++;
        }
        return guessColor.toString();
    }


    //Prints game board.
    public static void PrintBoard (String[] board){
        for (String s : board) System.out.println(s);
    }


    //Builds string with colors changed accordingly
    private static String TextColor(char guessChar, String secretWord, int count, String s){

        if(guessChar == secretWord.charAt(count)){
            return GreenText(guessChar);
        } else if(secretWord.contains(s)){
            return YellowText(guessChar);
        }
        return s;
    }


    //Green console text
    private static String GreenText(char c){ return "\033[0;32m" + c + "\033[0m";}


    //Yellow console text
    private static String YellowText(char c) { return "\033[0;33m" + c + "\033[0m";}


    //Grey console text
    private static String GreyText(char c) { return "\033[0;37m" + c + "\033[0m";}


    //If correct, victory message displayed and game ends.
    private static void WinCheck(String secretWord, String s){
        if (s.equals(secretWord)){
            System.out.println("Congratulations, You've won!");
        }
    }


    //Ends game if all guesses have been used.
    private static void GuessCounter(int i, String s){
        if (i == 6){
            System.out.println("Out of guesses. Better luck next time!");
            System.out.printf("Correct answer was: %s\n", s);
        }
    }

    //Game replay
    //Scanner created in main gets passed in as parameter
    private static Boolean AskReplay(Scanner sc) {

        //FIXME: message gets spammed with more than one input
        System.out.println("\nPlay again? (y/n)");

        String response = sc.next();
        if (response.equals("n")) {
            System.out.println("Thanks for playing!");
            //sc.close(); Scanner may not need to be closed here
            return false;
        } else if (!response.equals("y")) {
            System.err.println("Invalid response. 'y' or 'n' responses only.");
            AskReplay(sc);
        }

        return true;
    }
}