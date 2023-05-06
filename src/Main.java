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

public class Main {
    public static void main(String[] args){

        final int MAX_GUESSES = 6;
        final int MAX_LENGTH = 5;
        String secretWord = "";
        try {
            secretWord = DataLoad();
        } catch (IOException e) {
            System.err.printf("**  ERROR: IO exception: %s", e);
            System.exit(1);
        }

        Scanner sc = new Scanner(System.in);

        String guess;
        System.out.printf("Welcome to Wurdle. You get %d guesses. Go!\n", MAX_GUESSES);

        //Board at the start of the game TODO: make method for this
        String[] board = {"*****", "*****", "*****", "*****", "*****","*****"};

        int i = 0;

        while(i < MAX_GUESSES){
            guess = sc.next();
            guess = guess.toUpperCase();

            //Guesses can ONLY be 5 letters long
            if(guess.length() != MAX_LENGTH){
                System.err.println("Please input a 5-letter word.");
            } else{
                board[i] = ColorPrinter(guess, secretWord);
                PrintBoard(board);
                i++;

                WinCheck(guess, secretWord);
                GuessCheck(i, secretWord);
            }
        }
        sc.close();
    }


    //Loads db/random word set as secretWord
    private static String DataLoad() throws IOException {
        File f = new File("5 Letter Words.txt");
        String[] words = new String[496];
        int i = 0;
        Scanner sc = new Scanner(f);
        while (sc.hasNext()){
            words[i] = sc.next();
            i++;
        }
        sc.close();

        return WordPicker(words);
    }


    //Uses word picker method to pick random word in database
    private static String WordPicker(String[] words){
        String s = words[RandomNumber()];
        s = s.toUpperCase();
        //TODO: Remove if no longer needed: System.out.println(s);
        return s;
    }


    //Generates Random Number within words array range
    private static int RandomNumber(){
        int min = 0;
        int max = 495;
        int range = max-min + 1;
        return (int)(Math.random() * range) + min;
    }


    //Prints String to console in color.
    //"Correct" letters will be green if they are in the word, in the right space.
    //Letters will be yellow if they are contained within the word but in the wrong space.
    public static String ColorPrinter(String guess, String secretWord){

        StringBuilder guessColor = new StringBuilder();
        char guessChar;
        int count = 0;
        String s;

        for(int i = 0; i < guess.length(); i++){
            guessChar = guess.charAt(i);
            s = "" + guessChar;
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
    private static String GreenText(char c){
        return "\033[0;32m" + c + "\033[0m";
    }


    //Yellow console text
    private static String YellowText(char c) {
        return "\033[0;33m" + c + "\033[0m";
    }


    //If correct, victory message displayed and game ends.
    private static void WinCheck(String secretWord, String s){
        if (s.equals(secretWord)){
            System.out.println("Congratulations, You've won!");
            System.exit(0);
        }
    }


    //Ends game if all guesses have been used.
    private static void GuessCheck(int i, String s){
        if (i == 6){
            System.out.println("Out of guesses. Better luck next time!");
            System.out.printf("Correct answer was: %s\n", s);
            System.exit(0);
        }
    }
}