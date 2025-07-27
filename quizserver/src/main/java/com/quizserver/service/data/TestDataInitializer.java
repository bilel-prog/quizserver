package com.quizserver.service.data;

import com.quizserver.entities.Question;
import com.quizserver.entities.Test;
import com.quizserver.repository.TestRepository;
import com.quizserver.repository.QuestionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestDataInitializer {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostConstruct
    public void initializeTestData() {
        if (testRepository.count() == 0) {
            System.out.println("=== Initializing Quiz Beta Test Data ===");
            
            // Math Tests
            createMathBeginnerTest();
            createMathIntermediateTest();
            
            // Web Development Tests
            createWebDevBeginnerTest();
            createWebDevIntermediateTest();
            
            // IQ Tests
            createIQBeginnerTest();
            createIQIntermediateTest();
            
            // General Knowledge Tests
            createGeneralKnowledgeBeginnerTest();
            createGeneralKnowledgeIntermediateTest();
            
            // Programming Logic Tests
            createProgrammingLogicBeginnerTest();
            createProgrammingLogicIntermediateTest();
            
            System.out.println("=== Test Data Initialization Complete ===");
            System.out.println("Total Tests Created: " + testRepository.count());
            System.out.println("Total Questions Created: " + questionRepository.count());
            System.out.println("=== Data Initializer will now disable itself ===");
            
            // Disable this component by setting a flag or similar mechanism
            // The data is now permanently in the database
        } else {
            System.out.println("=== Test data already exists, skipping initialization ===");
        }
    }

    private void createMathBeginnerTest() {
        Test test = new Test();
        test.setTitle("Mathematics - Beginner Level");
        test.setDescription("Basic arithmetic, simple algebra, and fundamental mathematical concepts for beginners.");
        test.setTime(900L); // 15 minutes
        test.setTimePerQuestion(60L); // 1 minute per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionText("What is 15 + 27?");
        q1.setOptionA("41");
        q1.setOptionB("42");
        q1.setOptionC("43");
        q1.setOptionD("44");
        q1.setCorrectAnswer("B");
        q1.setTest(savedTest);
        questions.add(q1);

        // Question 2
        Question q2 = new Question();
        q2.setQuestionText("What is 8 × 7?");
        q2.setOptionA("54");
        q2.setOptionB("55");
        q2.setOptionC("56");
        q2.setOptionD("57");
        q2.setCorrectAnswer("C");
        q2.setTest(savedTest);
        questions.add(q2);

        // Question 3
        Question q3 = new Question();
        q3.setQuestionText("What is 144 ÷ 12?");
        q3.setOptionA("11");
        q3.setOptionB("12");
        q3.setOptionC("13");
        q3.setOptionD("14");
        q3.setCorrectAnswer("B");
        q3.setTest(savedTest);
        questions.add(q3);

        // Question 4
        Question q4 = new Question();
        q4.setQuestionText("What is the square root of 64?");
        q4.setOptionA("6");
        q4.setOptionB("7");
        q4.setOptionC("8");
        q4.setOptionD("9");
        q4.setCorrectAnswer("C");
        q4.setTest(savedTest);
        questions.add(q4);

        // Question 5
        Question q5 = new Question();
        q5.setQuestionText("If x + 5 = 12, what is x?");
        q5.setOptionA("5");
        q5.setOptionB("6");
        q5.setOptionC("7");
        q5.setOptionD("8");
        q5.setCorrectAnswer("C");
        q5.setTest(savedTest);
        questions.add(q5);

        questionRepository.saveAll(questions);
    }

    private void createMathIntermediateTest() {
        Test test = new Test();
        test.setTitle("Mathematics - Intermediate Level");
        test.setDescription("Algebra, geometry, basic calculus, and intermediate mathematical problem-solving.");
        test.setTime(1800L); // 30 minutes
        test.setTimePerQuestion(120L); // 2 minutes per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionText("Solve for x: 2x² - 8x + 6 = 0");
        q1.setOptionA("x = 1, x = 3");
        q1.setOptionB("x = 2, x = 4");
        q1.setOptionC("x = 1, x = 4");
        q1.setOptionD("x = 2, x = 3");
        q1.setCorrectAnswer("A");
        q1.setTest(savedTest);
        questions.add(q1);

        // Question 2
        Question q2 = new Question();
        q2.setQuestionText("What is the derivative of f(x) = 3x² + 2x - 1?");
        q2.setOptionA("6x + 2");
        q2.setOptionB("6x - 2");
        q2.setOptionC("3x + 2");
        q2.setOptionD("6x + 1");
        q2.setCorrectAnswer("A");
        q2.setTest(savedTest);
        questions.add(q2);

        // Question 3
        Question q3 = new Question();
        q3.setQuestionText("In a right triangle, if one angle is 30°, what is the other acute angle?");
        q3.setOptionA("45°");
        q3.setOptionB("60°");
        q3.setOptionC("70°");
        q3.setOptionD("50°");
        q3.setCorrectAnswer("B");
        q3.setTest(savedTest);
        questions.add(q3);

        // Question 4
        Question q4 = new Question();
        q4.setQuestionText("What is log₂(32)?");
        q4.setOptionA("4");
        q4.setOptionB("5");
        q4.setOptionC("6");
        q4.setOptionD("7");
        q4.setCorrectAnswer("B");
        q4.setTest(savedTest);
        questions.add(q4);

        // Question 5
        Question q5 = new Question();
        q5.setQuestionText("If the radius of a circle is 5, what is its area? (π ≈ 3.14)");
        q5.setOptionA("78.5");
        q5.setOptionB("31.4");
        q5.setOptionC("15.7");
        q5.setOptionD("62.8");
        q5.setCorrectAnswer("A");
        q5.setTest(savedTest);
        questions.add(q5);

        questionRepository.saveAll(questions);
    }

    private void createWebDevBeginnerTest() {
        Test test = new Test();
        test.setTitle("Web Development - Beginner Level");
        test.setDescription("Basic HTML, CSS, and fundamental web development concepts for beginners.");
        test.setTime(1200L); // 20 minutes
        test.setTimePerQuestion(80L); // 1 minute 20 seconds per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionText("Which HTML tag is used to create a hyperlink?");
        q1.setOptionA("<link>");
        q1.setOptionB("<a>");
        q1.setOptionC("<href>");
        q1.setOptionD("<url>");
        q1.setCorrectAnswer("B");
        q1.setTest(savedTest);
        questions.add(q1);

        // Question 2
        Question q2 = new Question();
        q2.setQuestionText("What does CSS stand for?");
        q2.setOptionA("Computer Style Sheets");
        q2.setOptionB("Creative Style Sheets");
        q2.setOptionC("Cascading Style Sheets");
        q2.setOptionD("Colorful Style Sheets");
        q2.setCorrectAnswer("C");
        q2.setTest(savedTest);
        questions.add(q2);

        // Question 3
        Question q3 = new Question();
        q3.setQuestionText("Which CSS property is used to change the text color?");
        q3.setOptionA("font-color");
        q3.setOptionB("text-color");
        q3.setOptionC("color");
        q3.setOptionD("text-style");
        q3.setCorrectAnswer("C");
        q3.setTest(savedTest);
        questions.add(q3);

        // Question 4
        Question q4 = new Question();
        q4.setQuestionText("What does HTML stand for?");
        q4.setOptionA("Hyper Text Markup Language");
        q4.setOptionB("High Tech Modern Language");
        q4.setOptionC("Home Tool Markup Language");
        q4.setOptionD("Hyperlink and Text Markup Language");
        q4.setCorrectAnswer("A");
        q4.setTest(savedTest);
        questions.add(q4);

        // Question 5
        Question q5 = new Question();
        q5.setQuestionText("Which HTML tag is used to define an internal style sheet?");
        q5.setOptionA("<style>");
        q5.setOptionB("<css>");
        q5.setOptionC("<script>");
        q5.setOptionD("<link>");
        q5.setCorrectAnswer("A");
        q5.setTest(savedTest);
        questions.add(q5);

        questionRepository.saveAll(questions);
    }

    private void createWebDevIntermediateTest() {
        Test test = new Test();
        test.setTitle("Web Development - Intermediate Level");
        test.setDescription("JavaScript, responsive design, frameworks, and intermediate web development concepts.");
        test.setTime(2100L); // 35 minutes
        test.setTimePerQuestion(140L); // 2 minutes 20 seconds per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionText("What is the correct way to declare a JavaScript variable?");
        q1.setOptionA("var myVariable;");
        q1.setOptionB("variable myVariable;");
        q1.setOptionC("v myVariable;");
        q1.setOptionD("declare myVariable;");
        q1.setCorrectAnswer("A");
        q1.setTest(savedTest);
        questions.add(q1);

        // Question 2
        Question q2 = new Question();
        q2.setQuestionText("Which CSS framework is known for mobile-first responsive design?");
        q2.setOptionA("jQuery");
        q2.setOptionB("Bootstrap");
        q2.setOptionC("Angular");
        q2.setOptionD("React");
        q2.setCorrectAnswer("B");
        q2.setTest(savedTest);
        questions.add(q2);

        // Question 3
        Question q3 = new Question();
        q3.setQuestionText("What does DOM stand for in web development?");
        q3.setOptionA("Document Object Model");
        q3.setOptionB("Data Object Management");
        q3.setOptionC("Dynamic Object Method");
        q3.setOptionD("Document Oriented Model");
        q3.setCorrectAnswer("A");
        q3.setTest(savedTest);
        questions.add(q3);

        // Question 4
        Question q4 = new Question();
        q4.setQuestionText("Which HTTP method is typically used to update data on a server?");
        q4.setOptionA("GET");
        q4.setOptionB("POST");
        q4.setOptionC("PUT");
        q4.setOptionD("DELETE");
        q4.setCorrectAnswer("C");
        q4.setTest(savedTest);
        questions.add(q4);

        // Question 5
        Question q5 = new Question();
        q5.setQuestionText("What is the purpose of the 'async' attribute in a script tag?");
        q5.setOptionA("To load the script synchronously");
        q5.setOptionB("To load the script asynchronously");
        q5.setOptionC("To cache the script");
        q5.setOptionD("To compress the script");
        q5.setCorrectAnswer("B");
        q5.setTest(savedTest);
        questions.add(q5);

        questionRepository.saveAll(questions);
    }

    private void createIQBeginnerTest() {
        Test test = new Test();
        test.setTitle("IQ Test - Beginner Level");
        test.setDescription("Basic logical reasoning, pattern recognition, and cognitive ability assessment.");
        test.setTime(1500L); // 25 minutes
        test.setTimePerQuestion(100L); // 1 minute 40 seconds per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionText("Complete the sequence: 2, 4, 6, 8, ?");
        q1.setOptionA("9");
        q1.setOptionB("10");
        q1.setOptionC("11");
        q1.setOptionD("12");
        q1.setCorrectAnswer("B");
        q1.setTest(savedTest);
        questions.add(q1);

        // Question 2
        Question q2 = new Question();
        q2.setQuestionText("Which word does not belong: Apple, Orange, Banana, Carrot?");
        q2.setOptionA("Apple");
        q2.setOptionB("Orange");
        q2.setOptionC("Banana");
        q2.setOptionD("Carrot");
        q2.setCorrectAnswer("D");
        q2.setTest(savedTest);
        questions.add(q2);

        // Question 3
        Question q3 = new Question();
        q3.setQuestionText("If all roses are flowers and some flowers are red, which statement is true?");
        q3.setOptionA("All roses are red");
        q3.setOptionB("Some roses might be red");
        q3.setOptionC("No roses are red");
        q3.setOptionD("All flowers are roses");
        q3.setCorrectAnswer("B");
        q3.setTest(savedTest);
        questions.add(q3);

        // Question 4
        Question q4 = new Question();
        q4.setQuestionText("Complete the analogy: Book is to Reading as Fork is to ?");
        q4.setOptionA("Kitchen");
        q4.setOptionB("Eating");
        q4.setOptionC("Spoon");
        q4.setOptionD("Food");
        q4.setCorrectAnswer("B");
        q4.setTest(savedTest);
        questions.add(q4);

        // Question 5
        Question q5 = new Question();
        q5.setQuestionText("What comes next in the pattern: A, C, E, G, ?");
        q5.setOptionA("H");
        q5.setOptionB("I");
        q5.setOptionC("J");
        q5.setOptionD("K");
        q5.setCorrectAnswer("B");
        q5.setTest(savedTest);
        questions.add(q5);

        questionRepository.saveAll(questions);
    }

    private void createIQIntermediateTest() {
        Test test = new Test();
        test.setTitle("IQ Test - Intermediate Level");
        test.setDescription("Advanced logical reasoning, complex patterns, and analytical thinking assessment.");
        test.setTime(2400L); // 40 minutes
        test.setTimePerQuestion(160L); // 2 minutes 40 seconds per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionText("Complete the sequence: 1, 1, 2, 3, 5, 8, ?");
        q1.setOptionA("11");
        q1.setOptionB("12");
        q1.setOptionC("13");
        q1.setOptionD("15");
        q1.setCorrectAnswer("C");
        q1.setTest(savedTest);
        questions.add(q1);

        // Question 2
        Question q2 = new Question();
        q2.setQuestionText("If in a certain code, FLOWER is written as EQLTDS, how is GARDEN written?");
        q2.setOptionA("FAQFCM");
        q2.setOptionB("HBSFFO");
        q2.setOptionC("FBSEFO");
        q2.setOptionD("GBQEFO");
        q2.setCorrectAnswer("C");
        q2.setTest(savedTest);
        questions.add(q2);

        // Question 3
        Question q3 = new Question();
        q3.setQuestionText("A man builds a house with four walls. Each wall has a southern exposure. A bear walks by. What color is the bear?");
        q3.setOptionA("Brown");
        q3.setOptionB("Black");
        q3.setOptionC("White");
        q3.setOptionD("Cannot be determined");
        q3.setCorrectAnswer("C");
        q3.setTest(savedTest);
        questions.add(q3);

        // Question 4
        Question q4 = new Question();
        q4.setQuestionText("Complete the analogy: 16:4 as 64:?");
        q4.setOptionA("6");
        q4.setOptionB("8");
        q4.setOptionC("12");
        q4.setOptionD("16");
        q4.setCorrectAnswer("B");
        q4.setTest(savedTest);
        questions.add(q4);

        // Question 5
        Question q5 = new Question();
        q5.setQuestionText("Which number should replace the question mark: 7, 14, 28, 56, ?");
        q5.setOptionA("84");
        q5.setOptionB("96");
        q5.setOptionC("112");
        q5.setOptionD("128");
        q5.setCorrectAnswer("C");
        q5.setTest(savedTest);
        questions.add(q5);

        questionRepository.saveAll(questions);
    }

    private void createGeneralKnowledgeBeginnerTest() {
        Test test = new Test();
        test.setTitle("General Knowledge - Beginner Level");
        test.setDescription("Basic geography, history, science, and current affairs for beginners.");
        test.setTime(900L); // 15 minutes
        test.setTimePerQuestion(60L); // 1 minute per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionText("What is the capital of France?");
        q1.setOptionA("London");
        q1.setOptionB("Berlin");
        q1.setOptionC("Paris");
        q1.setOptionD("Madrid");
        q1.setCorrectAnswer("C");
        q1.setTest(savedTest);
        questions.add(q1);

        // Question 2
        Question q2 = new Question();
        q2.setQuestionText("How many continents are there?");
        q2.setOptionA("5");
        q2.setOptionB("6");
        q2.setOptionC("7");
        q2.setOptionD("8");
        q2.setCorrectAnswer("C");
        q2.setTest(savedTest);
        questions.add(q2);

        // Question 3
        Question q3 = new Question();
        q3.setQuestionText("What is the largest planet in our solar system?");
        q3.setOptionA("Earth");
        q3.setOptionB("Jupiter");
        q3.setOptionC("Saturn");
        q3.setOptionD("Neptune");
        q3.setCorrectAnswer("B");
        q3.setTest(savedTest);
        questions.add(q3);

        // Question 4
        Question q4 = new Question();
        q4.setQuestionText("Who painted the Mona Lisa?");
        q4.setOptionA("Vincent van Gogh");
        q4.setOptionB("Pablo Picasso");
        q4.setOptionC("Leonardo da Vinci");
        q4.setOptionD("Michelangelo");
        q4.setCorrectAnswer("C");
        q4.setTest(savedTest);
        questions.add(q4);

        // Question 5
        Question q5 = new Question();
        q5.setQuestionText("What is the chemical symbol for gold?");
        q5.setOptionA("Go");
        q5.setOptionB("Au");
        q5.setOptionC("Ag");
        q5.setOptionD("Gd");
        q5.setCorrectAnswer("B");
        q5.setTest(savedTest);
        questions.add(q5);

        questionRepository.saveAll(questions);
    }

    private void createGeneralKnowledgeIntermediateTest() {
        Test test = new Test();
        test.setTitle("General Knowledge - Intermediate Level");
        test.setDescription("Advanced geography, history, science, literature, and world affairs.");
        test.setTime(1800L); // 30 minutes
        test.setTimePerQuestion(120L); // 2 minutes per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionText("Which country has the most time zones?");
        q1.setOptionA("Russia");
        q1.setOptionB("United States");
        q1.setOptionC("China");
        q1.setOptionD("France");
        q1.setCorrectAnswer("D");
        q1.setTest(savedTest);
        questions.add(q1);

        // Question 2
        Question q2 = new Question();
        q2.setQuestionText("Who wrote the novel '1984'?");
        q2.setOptionA("Aldous Huxley");
        q2.setOptionB("George Orwell");
        q2.setOptionC("Ray Bradbury");
        q2.setOptionD("H.G. Wells");
        q2.setCorrectAnswer("B");
        q2.setTest(savedTest);
        questions.add(q2);

        // Question 3
        Question q3 = new Question();
        q3.setQuestionText("What is the smallest bone in the human body?");
        q3.setOptionA("Stapes");
        q3.setOptionB("Fibula");
        q3.setOptionC("Radius");
        q3.setOptionD("Clavicle");
        q3.setCorrectAnswer("A");
        q3.setTest(savedTest);
        questions.add(q3);

        // Question 4
        Question q4 = new Question();
        q4.setQuestionText("In which year did World War II end?");
        q4.setOptionA("1944");
        q4.setOptionB("1945");
        q4.setOptionC("1946");
        q4.setOptionD("1947");
        q4.setCorrectAnswer("B");
        q4.setTest(savedTest);
        questions.add(q4);

        // Question 5
        Question q5 = new Question();
        q5.setQuestionText("What is the deepest ocean trench?");
        q5.setOptionA("Puerto Rico Trench");
        q5.setOptionB("Java Trench");
        q5.setOptionC("Mariana Trench");
        q5.setOptionD("Peru-Chile Trench");
        q5.setCorrectAnswer("C");
        q5.setTest(savedTest);
        questions.add(q5);

        questionRepository.saveAll(questions);
    }

    private void createProgrammingLogicBeginnerTest() {
        Test test = new Test();
        test.setTitle("Programming Logic - Beginner Level");
        test.setDescription("Basic programming concepts, algorithms, and logical thinking for beginners.");
        test.setTime(1200L); // 20 minutes
        test.setTimePerQuestion(80L); // 1 minute 20 seconds per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionText("What is the output of this pseudocode? \nSET x = 5\nSET y = 3\nPRINT x + y");
        q1.setOptionA("5");
        q1.setOptionB("3");
        q1.setOptionC("8");
        q1.setOptionD("53");
        q1.setCorrectAnswer("C");
        q1.setTest(savedTest);
        questions.add(q1);

        // Question 2
        Question q2 = new Question();
        q2.setQuestionText("Which of the following is a loop structure?");
        q2.setOptionA("IF statement");
        q2.setOptionB("FOR loop");
        q2.setOptionC("SWITCH statement");
        q2.setOptionD("RETURN statement");
        q2.setCorrectAnswer("B");
        q2.setTest(savedTest);
        questions.add(q2);

        // Question 3
        Question q3 = new Question();
        q3.setQuestionText("What does an algorithm represent?");
        q3.setOptionA("A programming language");
        q3.setOptionB("A step-by-step solution to a problem");
        q3.setOptionC("A type of variable");
        q3.setOptionD("A computer program");
        q3.setCorrectAnswer("B");
        q3.setTest(savedTest);
        questions.add(q3);

        // Question 4
        Question q4 = new Question();
        q4.setQuestionText("In programming, what is a variable?");
        q4.setOptionA("A constant value");
        q4.setOptionB("A storage location with a name");
        q4.setOptionC("A type of loop");
        q4.setOptionD("A function");
        q4.setCorrectAnswer("B");
        q4.setTest(savedTest);
        questions.add(q4);

        // Question 5
        Question q5 = new Question();
        q5.setQuestionText("What is the purpose of conditional statements?");
        q5.setOptionA("To repeat code");
        q5.setOptionB("To store data");
        q5.setOptionC("To make decisions in code");
        q5.setOptionD("To define functions");
        q5.setCorrectAnswer("C");
        q5.setTest(savedTest);
        questions.add(q5);

        questionRepository.saveAll(questions);
    }

    private void createProgrammingLogicIntermediateTest() {
        Test test = new Test();
        test.setTitle("Programming Logic - Intermediate Level");
        test.setDescription("Advanced algorithms, data structures, and complex programming logic concepts.");
        test.setTime(2100L); // 35 minutes
        test.setTimePerQuestion(140L); // 2 minutes 20 seconds per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionText("What is the time complexity of binary search?");
        q1.setOptionA("O(n)");
        q1.setOptionB("O(log n)");
        q1.setOptionC("O(n²)");
        q1.setOptionD("O(1)");
        q1.setCorrectAnswer("B");
        q1.setTest(savedTest);
        questions.add(q1);

        // Question 2
        Question q2 = new Question();
        q2.setQuestionText("Which data structure follows LIFO (Last In, First Out) principle?");
        q2.setOptionA("Queue");
        q2.setOptionB("Array");
        q2.setOptionC("Stack");
        q2.setOptionD("Linked List");
        q2.setCorrectAnswer("C");
        q2.setTest(savedTest);
        questions.add(q2);

        // Question 3
        Question q3 = new Question();
        q3.setQuestionText("What is recursion in programming?");
        q3.setOptionA("A loop that never ends");
        q3.setOptionB("A function that calls itself");
        q3.setOptionC("A way to store data");
        q3.setOptionD("A sorting algorithm");
        q3.setCorrectAnswer("B");
        q3.setTest(savedTest);
        questions.add(q3);

        // Question 4
        Question q4 = new Question();
        q4.setQuestionText("In object-oriented programming, what is encapsulation?");
        q4.setOptionA("Creating multiple objects");
        q4.setOptionB("Hiding internal implementation details");
        q4.setOptionC("Inheriting from parent classes");
        q4.setOptionD("Overriding methods");
        q4.setCorrectAnswer("B");
        q4.setTest(savedTest);
        questions.add(q4);

        // Question 5
        Question q5 = new Question();
        q5.setQuestionText("What is the purpose of a hash table?");
        q5.setOptionA("To sort data");
        q5.setOptionB("To provide fast data retrieval");
        q5.setOptionC("To compress data");
        q5.setOptionD("To backup data");
        q5.setCorrectAnswer("B");
        q5.setTest(savedTest);
        questions.add(q5);

        questionRepository.saveAll(questions);
    }
}
