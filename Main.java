//Importing required tools
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    // File path of txt file
    private static final String filePath = "C:/Users/msjhstaff/IdeaProjects/FileInput/src/code.txt";
    // Stores variable names and values in the Hashmap, integers and strings only
    private static final HashMap<String, Integer> integers = new HashMap<>();
    private static final HashMap<String, String> strings = new HashMap<>();
    // Constants for the Syntax
    private static final String sInt = "int";
    private static final String sString = "String";
    private static final String sEquals = "equals";
    private static final String sIf = "if";
    private static final String sForLoop = "for";
    private static final String sInput = "input";
    private static final String sPrint = "print";
    private static final String sAdd = "add";
    private static final String sSubtract = "subtract";
    private static final String sMultiply = "multiply";
    private static final String sDivide = "divide";
    private static final String sModulo = "modulo";
    private static final String sEqualsTo = "equalsTo";
    private static final String sGreaterThan = "greaterThan";
    private static final String sLessThan = "lessThan";
    private static final String sGreaterThanEqualsTo = "greaterThanEqualsTo";
    private static final String sLessThanEqualsTo = "lessThanEqualsTo";

    // Main method
    public static void main(String[] args) throws IOException {
        // Reads lines from txt file and tokenize them
        ArrayList<String> lines = readLines(filePath);
        ArrayList<ArrayList<String>> allTokens = new ArrayList<>();
        for (String line : lines) {
            allTokens.add(createTokens(line));
        }
        // Parse and execute all tokens
        parseAndExecute(allTokens);
    }

    // Method that reads all non-empty lines and returns them in an ArrayList
    public static ArrayList<String> readLines(String filePath) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        while (line != null) {
            if (!line.isEmpty()) {
                lines.add(line);
            }
            line = br.readLine();
        }
        br.close();
        return lines;
    }

    // Method that creates tokens from a string
    public static ArrayList<String> createTokens(String str) {
        ArrayList<String> tokens = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(str);
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());
        }
        return tokens;
    }

    // Method that parses and executes all tokens
    public static void parseAndExecute(ArrayList<ArrayList<String>> allTokens) {
        for (ArrayList<String> lineTokens : allTokens) {
            identifyStatementType(lineTokens);
        }
    }

    // Method that identifies the type of statement from a line of tokens
    public static void identifyStatementType(ArrayList<String> tokens) {
        String firstToken = tokens.get(0);
        if (firstToken.equals(sInt) || firstToken.equals(sString)) {
            handleVariableDeclaration(tokens);
        } else if (firstToken.equals(sPrint)) {
            handlePrintStatements(tokens);
        } else if (firstToken.equals(sIf)) {
            handleIfStatements(tokens);
        } else if (firstToken.equals(sForLoop)) {
            handleForLoops(tokens);
        } else if (firstToken.equals(sInput)) {
            handleUserInput(tokens);
        } else if (integers.containsKey(firstToken) || strings.containsKey(firstToken)) {
            handleVariableChanges(tokens);
        } else {
            System.err.println("Invalid beginning of statement: " + firstToken);
        }
    }

    // Method to handle variable declaration
    // integers and strings only
    public static void handleVariableDeclaration(ArrayList<String> tokens) {
        if (tokens.size() == 4 && tokens.get(2).equals(sEquals)) {
            String firstToken = tokens.get(0);
            String varName = tokens.get(1);
            if (integers.containsKey(varName) || strings.containsKey(varName)) {
                System.err.println(varName + " has already been declared.");
            } else {
                try {
                    if (firstToken.equals(sInt)) {
                        integers.put(varName, Integer.parseInt(tokens.get(3)));
                    } else if (firstToken.equals(sString)) {
                        strings.put(varName, tokens.get(3));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid value for variable: " + varName);
                }
            }
        } else {
            System.err.println("Syntax error in variable declaration.");
        }
    }

    // Method to handle changes in preexisting variables
    public static void handleVariableChanges(ArrayList<String> tokens) {
        String varName = tokens.get(0);
        if (tokens.get(1).equals(sEquals)) {
            if (integers.containsKey(varName)) {
                if (tokens.size() == 3) {
                    integers.replace(varName, Integer.parseInt(tokens.get(2)));
                } else {
                    tokens.subList(0, 2).clear();
                    integers.replace(varName, handleMath(tokens));
                }
            } else if (strings.containsKey(varName)) {
                if (tokens.size() == 3) {
                    strings.replace(varName, tokens.get(2));
                } else if (tokens.size() == 5 && tokens.get(3).equals(sAdd)) {
                    strings.replace(varName, strings.get(varName) + tokens.get(4));
                } else {
                    System.err.println("Syntax error in modifying " + varName + ".");
                }
            }
        } else {
            System.err.println("Syntax error in modifying " + varName + ".");
        }
    }

    // Method to handle print statements
    // only integers, strings, comparisons, and math operations can be printed
    public static void handlePrintStatements(ArrayList<String> tokens) {
        if (tokens.size() == 2) {
            String secondToken = tokens.get(1);
            if (strings.containsKey(secondToken)) {
                System.out.println(strings.get(secondToken));
            } else if (integers.containsKey(secondToken)) {
                System.out.println(integers.get(secondToken));
            } else {
                System.out.println(secondToken);
            }
        } else if (tokens.size() == 4) {
            tokens.remove(0);
            if (tokens.get(1).contains("Than") || tokens.get(1).contains("To")) {
                System.out.println(handleComparisons(tokens));
            } else {
                System.out.println(handleMath(tokens));
            }
        } else {
            System.err.println("Syntax error in print statement.");
        }
    }

    // Method to handle if statements
    // only simple statements can be executed
    public static void handleIfStatements(ArrayList<String> tokens) {
        tokens.remove(0);
        if (tokens.size() > 4) {
            if (handleComparisons(new ArrayList<>(tokens.subList(0, 3)))) {
                tokens.subList(0, 3).clear();
                identifyStatementType(tokens);
            }
        } else {
            System.err.println("Syntax error in if statement.");
        }
    }

    // Method to handle for loops
    // only simple statements can be executed
    public static void handleForLoops(ArrayList<String> tokens) {
        if (tokens.size() == 6) {
            int start = Integer.parseInt(tokens.get(1));
            int end = Integer.parseInt(tokens.get(2));
            int change = Integer.parseInt(tokens.get(3));
            tokens.subList(0, 4).clear();
            for (int i = start; i < end; i += change) {
                identifyStatementType(tokens);
            }
        } else {
            System.err.println("Syntax error in for loop statement.");
        }
    }

    // Method to handle user input and store it as a variable
    // integers and strings only
    public static void handleUserInput(ArrayList<String> tokens) {
        Scanner scanner = new Scanner(System.in);
        String secondToken = tokens.get(1);
        if (secondToken.equals(sInt)) {
            int input = scanner.nextInt();
            integers.put(tokens.get(2), input);
            scanner.nextLine();
        } else if (secondToken.equals(sString)) {
            String input = scanner.nextLine();
            strings.put(tokens.get(2), input);
        } else {
            System.err.println("Invalid syntax for user input.");
        }
    }

    // Method to handle comparisons and returns the result
    public static boolean handleComparisons(ArrayList<String> tokens) {
        String a = tokens.get(0);
        String comparison = tokens.get(1);
        String b = tokens.get(2);
        if (integers.containsKey(a) && integers.containsKey(b)) {
            int numA = integers.get(a);
            int numB = integers.get(b);
            switch (comparison) {
                case sEqualsTo -> {
                    return numA == numB;
                }
                case sGreaterThanEqualsTo -> {
                    return numA >= numB;
                }
                case sLessThanEqualsTo -> {
                    return numA <= numB;
                }
                case sGreaterThan -> {
                    return numA > numB;
                }
                case sLessThan -> {
                    return numA < numB;
                }
            }
        }
        if (strings.containsKey(a) && strings.containsKey(b)) {
            String strA = strings.get(a);
            String strB = strings.get(b);
            switch (comparison) {
                case sEqualsTo -> {
                    return strA.equals(strB);
                }
                case sGreaterThan -> {
                    return strA.compareTo(strB) > 0;
                }
                case sLessThan -> {
                    return strA.compareTo(strB) < 0;
                }
            }
        }
        System.err.println("Syntax error in comparisons.");
        return false;
    }

    // Handles basic math operations and returns the result
    public static int handleMath(ArrayList<String> tokens) {
        String a = tokens.get(0);
        String operator = tokens.get(1);
        String b = tokens.get(2);
        int numA;
        int numB;
        if (integers.containsKey(a)) {
            numA = integers.get(a);
        } else {
            numA = Integer.parseInt(a);
        }
        if (integers.containsKey(b)) {
            numB = integers.get(b);
        } else {
            numB = Integer.parseInt(b);
        }
        switch (operator) {
            case sAdd -> {
                return numA + numB;
            }
            case sSubtract -> {
                return numA - numB;
            }
            case sMultiply -> {
                return numA * numB;
            }
            case sDivide -> {
                return numA / numB;
            }
            case sModulo -> {
                return numA % numB;
            }
        }
        System.err.println("Syntax error for math operations.");
        return 0;
    }
}

