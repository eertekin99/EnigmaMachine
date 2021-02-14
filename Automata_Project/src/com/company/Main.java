/**
 * This is an Automata Project. This gets encrypted sentences and with alterations it changes them to decrypted.
 * After that, it checks sentences with CFG if they are included or not with controls their output which is true or false.
 *
 * @Date: 17.01.2021
 * @Author: Efe Ertekin - Şafak Barış
 */

package com.company;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.util.Scanner;


public class Main {

    /**
     * In main, we create all needed files(.txt),
     * apply all alterations to lines that comes from Encrypted.txt. Then, we write to Intermediate.txt.
     * We checks all line inside of Intermediate.txt with CFG. Then, all true lines write to Decrypted.txt.
     * @param args
     */
    public static void main(String[] args) {


        //Recursive check:
        String recursiveCheck = "You and me and he and she would be mad.";
        System.out.println(function_S(recursiveCheck));



        try {
            //Creating new file which is Intermediate.txt.
            File myObj = new File("Intermediate.txt");

            //Checks whether file is created or not.
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            //if error occurs, this will handle that problem.
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        try
        {
            //the file to be opened for reading
            FileInputStream fis=new FileInputStream("Encrypted.txt");
            Scanner sc=new Scanner(fis);    //file to be scanned

            //to write a file, we create FileWriter.
            FileWriter myWriter = new FileWriter("Intermediate.txt");

            //returns true if there is another line to read
            while(sc.hasNextLine())
            {
                //All alteration operations applied to lines.
                String a1 = alteration1(sc.nextLine());
                String a2 = alteration2(a1);
                String a3 = alteration3(a2);
                String a4 = alteration4(a3);
                String a5 = alteration5(a4);
                String res = alteration6(a5);

                //All operated lines write on intermediate.txt
                try {
                    myWriter.write(res+'\n');
                    System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

            }
            sc.close();         //closes the scanner
            myWriter.close();   //closes the write file
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }


        try {

            //Creating new file which is Decrypted.txt.
            File myObj = new File("Decrypted.txt");

            //Checks whether it is created or not.
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            //Handles errors
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        try
        {
            //the file to be opened for reading
            FileInputStream fis2=new FileInputStream("Intermediate.txt");
            Scanner sc1 = new Scanner(fis2);    //file to be scanned

            //Create FileWriter for Decrypted.txt
            FileWriter myWriter2 = new FileWriter("Decrypted.txt");

            //returns true if there is another line to read
            while(sc1.hasNextLine())
            {
                //We hold current line with this.
                String currentLine = sc1.nextLine();

                //this apply CFG to our current line and check if they are true or false (which is include or not to our grammar).
                boolean a = function_S(currentLine);

                //if that line is in our grammar, this will write that line into Decrypted.txt
                if (a){
                    try {
                        myWriter2.write(currentLine+'\n');
                        System.out.println("Successfully wrote to the file.");
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }

                }

            }
            sc1.close();        //closes the scanner
            myWriter2.close();  //closes write file
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }


    //ALL ALTERATIONS ARE HERE:

    /**
     * This alteration changes am-pm and decrease clock time "-1".
     * @param text : Get lines from Encrypted.txt
     * @return String : Make the line decrypted.
     */
    public static String alteration1(String text){

        //It searches a number and consecutively "am" or "pm" to change.
        Pattern pattern = Pattern.compile("[0-9]+ pm", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        boolean matchFound = matcher.find();

        //It searches a number and consecutively "am" or "pm" to change.
        Pattern pattern1 = Pattern.compile("[0-9]+ am", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = pattern1.matcher(text);
        boolean matchFound1 = matcher1.find();

        //With matchFound we change "pm to am"
        if(matchFound) {
            text = matcher.replaceAll(matcher.group().replaceFirst(" pm"," am"));
        }

        //With matchFound1 we change "am to pm"
        if(matchFound1){
            text = matcher1.replaceAll(matcher1.group().replaceFirst(" am"," pm"));
        }



        //In the pattern, we are trying to find clock's digit part.
        Pattern pattern2 = Pattern.compile("[0-9]+");
        Matcher matcher2 = pattern2.matcher(text);

        boolean matchFound2 = matcher2.find();

        //matcher2 which gives us a clock's digit part.
        //We get that as an integer and afterwards we decrease(-1) its number.
        //To have decrypted version of clock.

        if(matchFound2) {
            int a = Integer.parseInt(matcher2.group(0));
            text = matcher2.replaceAll(String.valueOf(a-1));
        }

        return text;
    }



    /**
     * With this alteration, we change (These are -> This is -- Also removes word's plural "s")
     * (This is -> These are -- Also pluralize word.)
     * Same applies to "These were" and "This was" as well.
     * @param text : It gets alteration1's output.
     * @return a String : Operations applied to alteration1's output.
     */
    public static String alteration2(String text){

        // It searches "These are" with plural word.
        Pattern pattern = Pattern.compile("(These) (are) (.*s)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        //Checks whether match is found or not with boolean.
        boolean matchFound = matcher.find();

        // It searches "This is".
        Pattern pattern2 = Pattern.compile("(This) (is) (.*[A-Za-z])", Pattern.CASE_INSENSITIVE); //This is gun.
        Matcher matcher2 = pattern2.matcher(text);
        //Checks whether match is found or not with boolean.
        boolean matchFound2 = matcher2.find();

        // It searches "These were" with plural word.
        Pattern pattern3 = Pattern.compile("(These) (were) (.*s)", Pattern.CASE_INSENSITIVE);
        Matcher matcher3 = pattern3.matcher(text);
        //Checks whether match is found or not with boolean.
        boolean matchFound3 = matcher3.find();

        // It searches "This was".
        Pattern pattern4 = Pattern.compile("(This) (was) (.*[A-Za-z])", Pattern.CASE_INSENSITIVE);
        Matcher matcher4 = pattern4.matcher(text);
        //Checks whether match is found or not with boolean.
        boolean matchFound4 = matcher4.find();


        //it operates changes for the matchFounds.
        if(matchFound) {

            // It changes "These are" to "This is" and also remove plural word's "s".
            text = matcher.replaceAll("This is "+matcher.group(3).replaceFirst(".$",""));
            return text;
        }

        if(matchFound2) {

            // It changes "This is" to "These are" and append "s" to consecutive word.
            text = matcher2.replaceAll("These are " + matcher2.group(3)+"s");
            return text;
        }

        if(matchFound3) {

            // It changes "These were" to "This was" and also remove plural word's "s".
            text = matcher3.replaceAll("This was "+matcher3.group(3).replaceFirst(".$",""));
            return text;
        }

        if(matchFound4) {

            // It changes "This was" to "These were" and append "s" to consecutive word.
            text = matcher4.replaceAll("These were " + matcher4.group(3)+"s");
            return text;
        }

        return text;

    }



    /**
     * In this alteration, we changes True -> true, true -> false.
     * This is a tricky part. Because many thinks that is "True" is a wrong in that sentence grammar wise, they think this is false.
     * But we thought we can trick them like this.
     * @param text : It gets alteration2's output.
     * @return a String : It changes alteration's output.
     */
    public static String alteration3(String text) {

        // try to find "true" inside of the sentence.
        Pattern pattern1 = Pattern.compile("true");
        Matcher matcher1 = pattern1.matcher(text);
        boolean matchFound1 = matcher1.find();

        // try to find "True" inside of the sentence.
        Pattern pattern2 = Pattern.compile("True");
        Matcher matcher2 = pattern2.matcher(text);
        boolean matchFound2 = matcher2.find();

        //if it found something, replace that word in a desired way.
        if(matchFound1){
            text = matcher1.replaceAll(matcher1.group(0).replaceFirst("true","false"));
            return text;
        }

        //if it found something, replace that word in a desired way.
        if (matchFound2){
            text = matcher2.replaceAll(matcher2.group(0).replaceFirst("True","true"));
            return text;
        }

        return text;
    }



    /**
     * With this alteration, it searches possessive words at the beginning of the sentences.
     * Then, change create a loop that changes them.
     * For instance: Your <---> My , His <---> Her
     * @param text : gets alteration3's output.
     * @return a String : Apply operations to alteration3's output.
     */
    public static String alteration4(String text){

        // It searches "your" as a first word.
        Pattern pattern1 = Pattern.compile("^(your) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = pattern1.matcher(text);
        boolean matchFound1 = matcher1.find();

        // It searches "my" as a first word.
        Pattern pattern2 = Pattern.compile("^(my) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = pattern2.matcher(text);
        boolean matchFound2 = matcher2.find();

        // It searches "his" as a first word.
        Pattern pattern3 = Pattern.compile("^(his) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher3 = pattern3.matcher(text);
        boolean matchFound3 = matcher3.find();

        // It searches "her" as a first word.
        Pattern pattern4 = Pattern.compile("^(her) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher4 = pattern4.matcher(text);
        boolean matchFound4 = matcher4.find();

        // It changes words to desired ones.
        if(matchFound1) {
            text = matcher1.replaceAll("my ");
        }
        // It changes words to desired ones.
        if(matchFound2) {
            text = matcher2.replaceAll("your ");
        }
        // It changes words to desired ones.
        if(matchFound3) {
            text = matcher3.replaceAll("her ");
        }
        // It changes words to desired ones.
        if(matchFound4) {
            text = matcher4.replaceAll("his ");
        }

        //Capitalize first word in sentence.
        text = StringUtils.capitalize(text);
        return text;
    }



    /**
     * In this alteration, we changes "-ed" -> "do"; "do" -> "-ing"; "-ing" -> "-ed"
     * @param text : It gets alteration4's output.
     * @return a String : It changes alteration4's output.
     */
    public static String alteration5(String text){

        // We split sentence to find first word.
        String temp = text;
        String[] arr = temp.split(" ", 2);
        String firstWord = arr[0];

        String addAs = "";

        // It stores am/is/are according to first word.
        if(firstWord.equals("I")){
            addAs = "am";
        }
        if(firstWord.equals("You")){
            addAs = "are";
        }
        if(firstWord.equals("We")){
            addAs = "are";
        }
        if(firstWord.equals("He")){
            addAs = "is";
        }
        if(firstWord.equals("She")){
            addAs = "is";
        }
        if(firstWord.equals("It")){
            addAs = "is";
        }

        //It searches "-ed" verb.
        Pattern pattern1 = Pattern.compile("(.*) (.*ed)", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = pattern1.matcher(text);
        boolean matchFound1 = matcher1.find();

        //It searches "do" sentence.
        Pattern pattern2 = Pattern.compile("(.*) (do) (.*) (.*) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = pattern2.matcher(text);
        boolean matchFound2 = matcher2.find();

        //It searches "does" sentence.
        Pattern pattern3 = Pattern.compile("(.*) (does) (.*) (.*) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher3 = pattern3.matcher(text);
        boolean matchFound3 = matcher3.find();

        //It searches "-ing" verb.
        Pattern pattern4 = Pattern.compile("(.*) "+ addAs +" (.*ing) (.*) ", Pattern.CASE_INSENSITIVE);
        Matcher matcher4 = pattern4.matcher(text);
        boolean matchFound4 = matcher4.find();


        //it changes properly, according to what is found.
        if(matchFound1) {
            text = matcher1.replaceAll(matcher1.group(1)+" do "+matcher1.group(2).replaceFirst("(..)$",""));
            return text;
        }

        //it changes properly, according to what is found.
        if(matchFound2) {
            text = matcher2.replaceAll(matcher2.group(1) + " " + addAs + " " + matcher2.group(2).replaceFirst("do", "") + matcher2.group(3) +"ing " + matcher2.group(4) +" ");
            return text;
        }

        //it changes properly, according to what is found.
        if(matchFound3) {
            text = matcher3.replaceAll(matcher3.group(1) + " " + addAs + " " + matcher3.group(2).replaceFirst("does", "") + matcher3.group(3) +"ing " + matcher3.group(4) +" ");
            return text;
        }

        //it changes properly, according to what is found.
        if(matchFound4) {
            text = matcher4.replaceAll(matcher4.group(1) + addAs.replaceFirst(addAs,"") + " " + matcher4.group(2).replaceFirst("(...)$","ed ") + matcher4.group(3) +" ");
            return text;
        }

        text = StringUtils.capitalize(text);
        return text;
    }




    /**
     * It changes like:
     * Were you mad? No.  -> You were mad.
     * Were You mad? Yes. -> You would be mad.
     *
     * @param text : gets alteration5's output.
     * @return a String : changes alteration5's output.
     */
    public static String alteration6(String text){

        //It searches "Yes" at the end of the line.
        Pattern pattern1 = Pattern.compile("(.*) (.*) (.*) Yes.", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = pattern1.matcher(text);
        boolean matchFound1 = matcher1.find();

        //It searches "No" at the end of the line.
        Pattern pattern2 = Pattern.compile("(.*) (.*) (.*) No.", Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = pattern2.matcher(text);
        boolean matchFound2 = matcher2.find();


        // It changes the sentence according to Yes.
        if(matchFound1) {

            text = matcher1.replaceAll(matcher1.group(2)+" would be "+matcher1.group(3).replaceFirst(".$","."));
            text = StringUtils.capitalize(text);
            return text;

        }

        // It changes the sentence according to No.
        if(matchFound2) {

            text = matcher2.replaceAll(matcher2.group(2)+" "+matcher2.group(1).toLowerCase(Locale.ROOT)+" "+matcher2.group(3).replaceFirst(".$","."));
            text = StringUtils.capitalize(text);
            return text;
        }

        // It capitalize sentence's first word.
        text = StringUtils.capitalize(text);
        return text;
    }







    //CONTEXT-FREE GRAMMAR

    /**
     * functions_S is the start of the CFG. We always start with this.
     * We use this for control sentences whether they are in or not.
     * @param text : it gets line from intermediate.txt
     * @return boolean : return true is that line can be created in CFG. Otherwise,
     * it returns false.
     */
    public static boolean function_S(String text){

        //it starts with function_X
        boolean a = function_X(text);

        if (a) {
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * This function is the heart of the CFG. Firstly, it search sentence's first word if it is inside of
     * function_T or function_I. Also it controls recurrence with function_I.
     * @param text : function_S's input
     * @return boolean : return true or false if sentence's specific part approved by function_T or function_I.
     */
    public static boolean function_X(String text){

        // It gets words from T and I.
        String[] arr1 = function_T();
        String[] arr2 = function_I();

        //It splits sentence according to spaces.
        String[] arrSplit = text.split("\\s+");

        //It gets arr1[]'s all elements and changes them if main sentence starts with one of them and apply operations on it.
        for (int i = 0; i < arr1.length; i++) {
            if (text.startsWith(arr1[i])){
                String edited_text =text.replaceFirst(arr1[i]+" ", "");
                boolean a = function_Y(edited_text);
                return a;
            }
        }

        //It gets arr2[]'s all elements and changes them if main sentence starts with one of them and apply operations on it.
        for (int i = 0; i < arr2.length; i++) {

            //This part is recurrence. It checks also "and" to create a recurrence.
            if (text.startsWith(arr2[i]) && arrSplit[1].equals("and")){
                String edited_text =text.replaceFirst(arr2[i]+" and ", "");
                boolean a = function_X(edited_text);
                return a;
            }

            if (text.startsWith(arr2[i])){
                String edited_text =text.replaceFirst(arr2[i]+" ", "");
                boolean a = function_Y(edited_text);
                return a;
            }
        }


        return false;
    }

    /**
     * It controls end of the sentences.
     * @param text : it gets modified sentences that comes from function_X.
     * @return boolean : if it matches the end of the sentence, returns true.
     */
    public static boolean function_Y(String text){

        // It gets words from V, N, and A.
        String[] arr1 = function_V();
        String[] arr2 = function_N();
        String[] arr3 = function_A();

        //It gets arr1[]'s all elements and arr2[]'s all elements; changes them if sentence's desired part matches with them.
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr2.length; j++) {
                if (text.equals(arr1[i]+" "+arr2[j]+".")){
                    return true;
                }
            }
        }

        //It gets arr1[]'s all elements and arr3[]'s all elements; changes them if sentence's desired part matches with them.
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr3.length; j++) {
                if (text.equals(arr1[i]+" "+arr3[j]+".")){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * In this function, we use these words as you can see inside of the function.
     * @return : String[]
     */
    public static String[] function_I(){
        //if text exist in file_I
        String[] arr = new String[]{"I","You","He","She","It","We","They","me","you","he","she","it","we","they"};
        return arr;
    }

    /**
     * In this function, we use these words as you can see inside of the function.
     * @return : String[]
     */
    public static String[] function_T(){
        //if text exist in file_T
        String[] arr = new String[]{"This","These","Your","My","His","Her"};
        return arr;
    }

    /**
     * In this function, we use these words as you can see inside of the function.
     * @return : String[]
     */
    public static String[] function_N(){
        //if text exist in file_N
        String[] arr = new String[]{"true statement","true statements","false statement",
                "false statements","the man","gun","at 8 am"};
        return arr;
    }

    /**
     * In this function, we use these words as you can see inside of the function.
     * @return : String[]
     */
    public static String[] function_A(){
        //if text exist in file_A
        String[] arr = new String[]{"mad","broken"};
        return arr;
    }

    /**
     * In this function, we use these words as you can see inside of the function.
     * @return : String[]
     */
    public static String[] function_V(){
        //if text exist in file_V
        String[] arr = new String[]{"is","will meet",
                "would be","are","was","were"};
        return arr;
    }

}
