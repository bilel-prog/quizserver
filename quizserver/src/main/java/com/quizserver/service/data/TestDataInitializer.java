package com.quizserver.service.data;

import com.quizserver.entities.Question;
import com.quizserver.entities.Test;
import com.quizserver.repository.TestRepository;
import com.quizserver.repository.QuestionRepository;
import com.quizserver.enums.QuestionType;
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
    public void initData() {
        if (testRepository.count() == 0) {
            System.out.println("=== Initializing Test Data ===");
            
            createMathBeginnerTest();
            createMathIntermediateTest();
            createWebDevBeginnerTest();
            createWebDevIntermediateTest();
            createAdvancedProgrammingTest();

            System.out.println("Total Tests Created: " + testRepository.count());
            System.out.println("Total Questions Created: " + questionRepository.count());
            System.out.println("=== Data Initializer completed ===");
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
        q1.setQuestionType(QuestionType.MULTIPLE_CHOICE_SINGLE);
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
        q2.setQuestionType(QuestionType.MULTIPLE_CHOICE_SINGLE);
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
        q3.setQuestionType(QuestionType.MULTIPLE_CHOICE_SINGLE);
        q3.setQuestionText("What is 144 ÷ 12?");
        q3.setOptionA("11");
        q3.setOptionB("12");
        q3.setOptionC("13");
        q3.setOptionD("14");
        q3.setCorrectAnswer("B");
        q3.setTest(savedTest);
        questions.add(q3);

        questionRepository.saveAll(questions);
    }

    private void createMathIntermediateTest() {
        Test test = new Test();
        test.setTitle("Mathematics - Intermediate Level");
        test.setDescription("Intermediate algebra, geometry, and mathematical problem solving.");
        test.setTime(1200L); // 20 minutes
        test.setTimePerQuestion(120L); // 2 minutes per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionType(QuestionType.MULTIPLE_CHOICE_SINGLE);
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
        q2.setQuestionType(QuestionType.MULTIPLE_CHOICE_SINGLE);
        q2.setQuestionText("What is the derivative of f(x) = 3x² + 2x - 1?");
        q2.setOptionA("6x + 2");
        q2.setOptionB("6x - 2");
        q2.setOptionC("3x + 2");
        q2.setOptionD("6x");
        q2.setCorrectAnswer("A");
        q2.setTest(savedTest);
        questions.add(q2);

        questionRepository.saveAll(questions);
    }

    private void createWebDevBeginnerTest() {
        Test test = new Test();
        test.setTitle("Web Development - Beginner Level");
        test.setDescription("Basic HTML, CSS, and JavaScript concepts for web development beginners.");
        test.setTime(900L); // 15 minutes
        test.setTimePerQuestion(90L); // 1.5 minutes per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionType(QuestionType.MULTIPLE_CHOICE_SINGLE);
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
        q2.setQuestionType(QuestionType.MULTIPLE_CHOICE_SINGLE);
        q2.setQuestionText("What does CSS stand for?");
        q2.setOptionA("Computer Style Sheets");
        q2.setOptionB("Creative Style Sheets");
        q2.setOptionC("Cascading Style Sheets");
        q2.setOptionD("Colorful Style Sheets");
        q2.setCorrectAnswer("C");
        q2.setTest(savedTest);
        questions.add(q2);

        questionRepository.saveAll(questions);
    }

    private void createWebDevIntermediateTest() {
        Test test = new Test();
        test.setTitle("Web Development - Intermediate Level");
        test.setDescription("Advanced JavaScript, DOM manipulation, and modern web development concepts.");
        test.setTime(1500L); // 25 minutes
        test.setTimePerQuestion(150L); // 2.5 minutes per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionType(QuestionType.MULTIPLE_CHOICE_SINGLE);
        q1.setQuestionText("What is the difference between '==' and '===' in JavaScript?");
        q1.setOptionA("No difference");
        q1.setOptionB("'==' checks type and value, '===' checks only value");
        q1.setOptionC("'==' checks only value, '===' checks type and value");
        q1.setOptionD("Both are deprecated");
        q1.setCorrectAnswer("C");
        q1.setTest(savedTest);
        questions.add(q1);

        questionRepository.saveAll(questions);
    }

    private void createAdvancedProgrammingTest() {
        Test test = new Test();
        test.setTitle("Advanced Programming Concepts");
        test.setDescription("Data structures, algorithms, and advanced programming paradigms.");
        test.setTime(1800L); // 30 minutes
        test.setTimePerQuestion(180L); // 3 minutes per question

        Test savedTest = testRepository.save(test);

        List<Question> questions = new ArrayList<>();

        // Question 1
        Question q1 = new Question();
        q1.setQuestionType(QuestionType.MULTIPLE_CHOICE_SINGLE);
        q1.setQuestionText("What is the time complexity of binary search?");
        q1.setOptionA("O(n)");
        q1.setOptionB("O(log n)");
        q1.setOptionC("O(n²)");
        q1.setOptionD("O(1)");
        q1.setCorrectAnswer("B");
        q1.setTest(savedTest);
        questions.add(q1);

        questionRepository.saveAll(questions);
    }
}
