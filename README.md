# Compiler

A simple compiler that reads a text file of code and interprets and executes the code. 
It has integer and string variables, math operations, print statements, if conditions, for loops. 
The compiler interprets each line of code separately and can handle:


- Variables:
    - Integers: `int varName equals value`
    - Strings: `String varName equals value`

- Print statements:
    - Print variable: `print varName`
    - Print strings: `print word`
    - Print statements can also print out Math operations and comparisons
    - Print statements cannot print empty spaces

- If statements:
    - `if varName comparison varName statement`
    - Comparisons: `equalsTo`, `greaterThan`, `lessThan`, `greaterThanEqualsTo`, `lessThanEqualsTo`
    - If statements only execute the next statement provided in the same line
    - All comparisons work on 2 integer values
    - All comparisons except`greaterThanEqualsTo` and `lessThanEqualsTo` comparisons work on 2 string values

- For loops:
    - `for start end change statement`
    - After the for, the 1st number represents the starting point of n, the 2nd number represents the end point of n, and the 3rd number represents the change for each loop for n
    - For loops can only execute the next statement provided in the same line

- User input:
    - Integer input: `input int varName`
    - String input: `input String varName`
    - Users can input values to store as variables

- Math Operations:
    - `varName equals varName operator value`
    - Operators: `add`, `subtract`, `multiply`, `divide`, `modulo`
    - All math operations work for 2 integer values
