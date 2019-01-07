package com.careeranna.careeranna.dummy_data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CourseDummyData {
    private Scanner sc;
    private String dummyData;
    private String pathToDummy = "com/careeranna/careeranna/dummy_data/CourseContentsDummy";
    public CourseDummyData(){
        dummyData = "";
            try {
                sc = new Scanner(new File(pathToDummy));
                StringBuilder sb = new StringBuilder();
                while (sc.hasNextLine()){
                    sb.append(sc.nextLine());
                }
                dummyData = sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

    public String getDummyData(){
        return dummyData;
    }

    public static String dummyString = "{\"content\": [\n" +
            "    \"Introduction\",\n" +
            "    \"$Introduction to Python and Its Features,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/05081401\\/Introduction-copy-2.mp4\",\n" +
            "    \"Getting Started\",\n" +
            "    \"$Setup and Working with Python Interpreter,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/05082225\\/SetupAndStartingInterpreter-copy.mp4\",\n" +
            "    \"Datatypes in Python\",\n" +
            "    \"$Datatypes in Python,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/05083816\\/PythonDatatypes-copy.mp4\",\n" +
            "    \"$Operators and Datatypes in Python,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/05085206\\/FurtherOnDatatypesAndOperators-copy.mp4\",\n" +
            "    \"Control Flow\",\n" +
            "    \"$Control Flow in Python,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/05091131\\/ControlFlow-copy.mp4\",\n" +
            "    \"Advanced Datatypes - List\",\n" +
            "    \"$Lists,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/05092034\\/PythonListSlicing-copy.mp4\",\n" +
            "    \"$Using List as Queue and Stack,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/05092121\\/PythonListComprehensions-copy.mp4\",\n" +
            "    \"$List Comprehensions,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/05092121\\/PythonListComprehensions-copy.mp4\",\n" +
            "    \"$List Slicing,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/05092340\\/PythonListAsQueueAndStack-copy.mp4\",\n" +
            "    \"Advanced Datatypes - Tuples\",\n" +
            "    \"$Tuples,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/07143435\\/PythonTuple-copy-2.mp4\",\n" +
            "    \"Advanced Datatypes - Dictionaries\",\n" +
            "    \"$Dictionaries,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/09161028\\/PythonDictionary-copy.mp4\",\n" +
            "    \"String Handling\",\n" +
            "    \"$String Handling and Processing in Python,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/09161605\\/StringHandling-copy.mp4\",\n" +
            "    \"Executing Programs in Python\",\n" +
            "    \"$Creating and Executing Programs in Python\\t,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/09161951\\/CreatingAndExecutingIndependentPythonFiles-copy.mp4\",\n" +
            "    \"Command Line Arguments in Python\",\n" +
            "    \"$Passing Command Line Arguments to Python,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/09162112\\/CommandLineArguments-copy.mp4\",\n" +
            "    \"Functions in Python\",\n" +
            "    \"$Working with Functions In Python,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/09163101\\/Functions-copy1.mp4\",\n" +
            "    \"$Variable Argument Functions in Python,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/09163101\\/Functions-copy1.mp4\",\n" +
            "    \"$Lambda Functions in Python,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/09163800\\/LambdaFunctions-copy.mp4\",\n" +
            "    \"Variable Scope in Python\",\n" +
            "    \"$Local and Global scopes in Python,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/09165458\\/PythonVariableScoping-copy.mp4\",\n" +
            "    \"Modules and Packages\",\n" +
            "    \"$Working with Modules and Packages,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/09170653\\/ModulesAndPackages-copy.mp4\",\n" +
            "    \"Regular Expressions\",\n" +
            "    \"$Regular Expression Session 1,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/09172407\\/RegularExpression1-copy.mp4\",\n" +
            "    \"$Regular Expression Session 2,urlhttps:\\/\\/careeranna.s3.amazonaws.com\\/online\\/wp-content\\/uploads\\/2018\\/08\\/09172045\\/RegularExpression2-copy.mp4\",\n" +
            "    \"$Regular Expression Session 3,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/80422_RegularExpression3 copy.mp4\",\n" +
            "    \"$Regular Expression Session 4\\t,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/38479_RegularExpression4 copy.mp4\",\n" +
            "    \"Object Oriented Python\",\n" +
            "    \"$Object Orientation in Python\\t,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/82944_ObjectOrientedPython copy.mp4\",\n" +
            "    \"$Example of Object Orientation\\t,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/76855_ObjectOrientedPythonExample copy.mp4\",\n" +
            "    \"$Working with Private Data in OOP\\t,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/35853_ObjectOrientedPythonPrivateAttributes copy.mp4\",\n" +
            "    \"$Inheritance in Python Session 2,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/44655_ObjectOrientedPythonInheritance1 copy.mp4\",\n" +
            "    \"$Diamond Inheritance Problem,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/28108_diamond.mp4\",\n" +
            "    \"$New Style Classes in Python,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/98322_new style.mp4\",\n" +
            "    \"$Delegation,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/34323_delegation.mp4\",\n" +
            "    \"Operator Overloading\",\n" +
            "    \"$Operator Overloading Session 1,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/5302_overloading1.mp4\",\n" +
            "    \"$Operator Overloading Session 2,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/8129_overloading2.mp4\",\n" +
            "    \"Metaclasses in Python\",\n" +
            "    \"$Metaclasses in Python,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/69991_meta class.mp4\",\n" +
            "    \"File Handling\",\n" +
            "    \"$File Reading and Writing in Python,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/59409_FIle Handling.mp4\",\n" +
            "    \"Profiling Python Code\",\n" +
            "    \"$Profiling Python Code,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/2676_Profiling.mp4\",\n" +
            "    \"Parallel Programming in Python\",\n" +
            "    \"$Parallel Programming Session 1,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/42285_ppsession1.mp4\",\n" +
            "    \"$Parallel Programming Session 2,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/63374_ppsession2.mp4\",\n" +
            "    \"$Global Interpreter Lock in Python,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/79819_GIL.mp4\",\n" +
            "    \"$Benchmarking Parallel Programming - Simple Execution,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/16456_simple.mp4\",\n" +
            "    \"$Benchmarking Parallel Programming - Multithreading,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/5686_multithreading.mp4\",\n" +
            "    \"$Benchmarking Parallel Programming - Multiprocessing,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/6018_multiprocessing.mp4\",\n" +
            "    \"Decorator in Python\",\n" +
            "    \"$Function Decorators Session 1,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/68280_FunctionDecoratorsPart1 copy.mp4\",\n" +
            "    \"$Function Decorators Session 2,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/91472_FunctionDecoratorsPart2 copy.mp4\",\n" +
            "    \"$Function Decorators Session 3,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/6513_FunctionDecoratorsPart3 copy.mp4\",\n" +
            "    \"Iterators and Generators\",\n" +
            "    \"$Iterators and Generators Session 1,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/68171_Iterators_And_GeneratorsPart1_Iterator copy.mp4\",\n" +
            "    \"$Iterators and Generators Session 2,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/32865_Iterators_And_GeneratorsPart2_Generators copy.mp4\",\n" +
            "    \"$Generator Expressions,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/48716_GeneratorExpressions copy.mp4\",\n" +
            "    \"Extending Python\",\n" +
            "    \"$Extending Python Session 1,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/22043_ExtendingPython1_Introduction copy.mp4\",\n" +
            "    \"$Extending Python Session 2,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/83478_ExtendingPython2_WritingExtensionPartA copy.mp4\",\n" +
            "    \"$Extending Python Session 3,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/21461_ExtendingPython3_WritingExtensionPartB copy.mp4\",\n" +
            "    \"$Extending Python Session 4,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/57308_ExtendingPython4_WritingExtensionPartC copy.mp4\",\n" +
            "    \"Networking in Python\",\n" +
            "    \"$Networking in Python Session 1,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/68026_NetworkingandSocketsPart1 copy.mp4\",\n" +
            "    \"$Networking in Python Session 2,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/20760_NetworkingandSocketsPart2 copy.mp4\",\n" +
            "    \"Calling REST APIs in Python\",\n" +
            "    \"$Calling REST APIs in Python,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/CallingRestAPIs_From_Python_copy.mp4\",\n" +
            "    \"Working with CSV Files\",\n" +
            "    \"$Writing to CSV Files,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/3897_CSVWrite copy.mp4\",\n" +
            "    \"$Reading From CSV Files,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/35240_CSVRead copy.mp4\",\n" +
            "    \"XML Processing in Python\",\n" +
            "    \"$XML Processing in Python,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/88071_XMLProcessing copy.mp4\",\n" +
            "    \"Debugging in Python\",\n" +
            "    \"$Debugging in Python,urlhttps:\\/\\/s3.amazonaws.com\\/careeranna\\/uploads\\/videos\\/28508_DebuggingPython copy.mp4\"\n" +
            "  ]}";

}
