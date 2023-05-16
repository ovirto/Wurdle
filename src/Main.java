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

        final int MAX_GUESSES = 6;
        final int MAX_LENGTH = 5;
        String[] allWords = new String[3321]; //space for all words in database

        //TODO: remove letters as they are guessed, print array after each turn
        char[] lettersRemaining = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        String secretWord;


        try {
            //fills array with all words in database
            allWords = DataLoad();

        } catch (IOException e) {
            System.err.printf("**  ERROR: IO exception: %s", e);
            System.exit(1);
        }

        Scanner sc = new Scanner(System.in);

        String guess;
        secretWord = WordPicker(allWords);
        System.out.println("""
                Welcome to Wurdle!
                A home-made version of the New York Times word game we all know and love.

                Input "help" at any point for the game rules.
                """);

        //Empty game board
        String[] board = {"*****", "*****", "*****", "*****", "*****","*****"};


        int i = 0;


        while(i < MAX_GUESSES){

            if(i > 0){
                ClearScreen();
            }

            guess = sc.next();
            guess = guess.toUpperCase();

            //
            if(guess.equals("HELP")){
                UserHelp();
            } else if(guess.length() != MAX_LENGTH){
                //Guesses can only be 5 letters long
                System.err.println("Please input a 5-letter word.");
            } else if(!(Arrays.asList(allWords).contains(guess.toLowerCase()))){
                System.err.println("Not in words list. Please try again.");
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


    //Loads db. Random word gets set as secretWord
    private static String[] DataLoad() throws IOException {
        File f = new File("5 Letter Words.txt");
        String[] words = new String[3321];
        int i = 0;

        Scanner sc = new Scanner(f);

        while (sc.hasNext()){
            words[i] = sc.next();
            i++;
        }

        sc.close();

        return words;
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


    //FIXME: Prints unused letters
    private static void RemainingLetters(char[] lettersRemaining, String guess){
        //chars in "guess" converted to char array
        char[] guessLetters = guess.toCharArray();

        //sort guessLetters by alphabetical order
        Arrays.sort(guessLetters);

        for(int i = 0; i < lettersRemaining.length; i++){

            if(Arrays.asList(guessLetters).contains(lettersRemaining[i])){
            }
        }
    }


    //FIXME: Clear Console
    //private instead of public??
    public static void ClearScreen(){
        System.out.println("\033[H\033[2J");
        System.out.flush();
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
        int max = 495;
        int range = max-min + 1;
        return (int)(Math.random() * range) + min;
    }


    //Prints String to console in color.
    //Correct letters in correct spaces will be green
    //Correct letters in incorrect spaces will be yellow
    //Incorrect letters remain white
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