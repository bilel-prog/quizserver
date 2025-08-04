package com.quizserver.service.grading;

import com.quizserver.dto.QuestionResponse;
import com.quizserver.entities.Question;
import com.quizserver.enums.QuestionType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionGradingService {

    public QuestionGradingResult gradeQuestion(Question question, QuestionResponse response) {
        if (question == null || response == null) {
            return new QuestionGradingResult(false, 0, "Invalid question or response");
        }

        QuestionType questionType = question.getQuestionType();
        if (questionType == null) {
            questionType = QuestionType.MULTIPLE_CHOICE_SINGLE; // Default for legacy questions
        }

        switch (questionType) {
            case MULTIPLE_CHOICE_SINGLE:
            case MULTIPLE_CHOICE_BEST:
                return gradeMultipleChoiceSingle(question, response);
            
            case MULTIPLE_CHOICE_MULTIPLE:
                return gradeMultipleChoiceMultiple(question, response);
            
            case TRUE_FALSE:
            case YES_NO:
                return gradeBinaryChoice(question, response);
            
            case FILL_IN_THE_BLANK:
                return gradeFillInTheBlank(question, response);
            
            case FILL_IN_THE_BLANK_PHRASE:
                return gradeFillInTheBlankPhrase(question, response);
            
            case MATCHING:
            case MATCHING_ONE_TO_MANY:
                return gradeMatching(question, response);
            
            case SEQUENCING:
            case ORDERING_CHRONOLOGICAL:
                return gradeSequencing(question, response);
            
            case SHORT_ANSWER:
            case ESSAY:
                return gradeSubjective(question, response);
            
            default:
                return new QuestionGradingResult(false, 0, "Unknown question type");
        }
    }

    private QuestionGradingResult gradeMultipleChoiceSingle(Question question, QuestionResponse response) {
        // Handle legacy format
        if (question.getCorrectAnswer() != null && response.getSelectedOption() != null) {
            boolean isCorrect = question.getCorrectAnswer().trim().equalsIgnoreCase(response.getSelectedOption().trim());
            return new QuestionGradingResult(isCorrect, isCorrect ? question.getMaxPoints() : 0, 
                isCorrect ? "Correct answer" : "Incorrect answer");
        }
        
        // Handle new format
        if (question.getCorrectAnswers() != null && !question.getCorrectAnswers().isEmpty() && 
            response.getSelectedAnswers() != null && !response.getSelectedAnswers().isEmpty()) {
            
            String correctAnswer = question.getCorrectAnswers().get(0);
            String userAnswer = response.getSelectedAnswers().get(0);
            boolean isCorrect = correctAnswer.trim().equalsIgnoreCase(userAnswer.trim());
            
            return new QuestionGradingResult(isCorrect, isCorrect ? question.getMaxPoints() : 0, 
                isCorrect ? "Correct answer" : "Incorrect answer");
        }
        
        return new QuestionGradingResult(false, 0, "No valid answer provided");
    }

    private QuestionGradingResult gradeMultipleChoiceMultiple(Question question, QuestionResponse response) {
        if (question.getCorrectAnswers() == null || response.getSelectedAnswers() == null) {
            return new QuestionGradingResult(false, 0, "Missing correct answers or user responses");
        }

        List<String> correctAnswers = question.getCorrectAnswers().stream()
            .map(String::trim)
            .map(String::toLowerCase)
            .collect(Collectors.toList());
        
        List<String> userAnswers = response.getSelectedAnswers().stream()
            .map(String::trim)
            .map(String::toLowerCase)
            .collect(Collectors.toList());

        boolean isCorrect = correctAnswers.size() == userAnswers.size() && 
                           correctAnswers.containsAll(userAnswers) && 
                           userAnswers.containsAll(correctAnswers);

        return new QuestionGradingResult(isCorrect, isCorrect ? question.getMaxPoints() : 0, 
            isCorrect ? "All correct answers selected" : "Not all correct answers selected");
    }

    private QuestionGradingResult gradeBinaryChoice(Question question, QuestionResponse response) {
        if (question.getCorrectAnswers() == null || question.getCorrectAnswers().isEmpty() || 
            response.getSelectedAnswers() == null || response.getSelectedAnswers().isEmpty()) {
            return new QuestionGradingResult(false, 0, "Missing answer");
        }

        String correctAnswer = question.getCorrectAnswers().get(0).trim().toLowerCase();
        String userAnswer = response.getSelectedAnswers().get(0).trim().toLowerCase();
        
        boolean isCorrect = correctAnswer.equals(userAnswer) || 
                           (correctAnswer.equals("true") && userAnswer.equals("true")) ||
                           (correctAnswer.equals("false") && userAnswer.equals("false")) ||
                           (correctAnswer.equals("yes") && userAnswer.equals("yes")) ||
                           (correctAnswer.equals("no") && userAnswer.equals("no"));

        return new QuestionGradingResult(isCorrect, isCorrect ? question.getMaxPoints() : 0, 
            isCorrect ? "Correct" : "Incorrect");
    }

    private QuestionGradingResult gradeFillInTheBlank(Question question, QuestionResponse response) {
        if (question.getCorrectAnswers() == null || response.getTextAnswer() == null) {
            return new QuestionGradingResult(false, 0, "Missing answer");
        }

        String userAnswer = response.getTextAnswer().trim().toLowerCase();
        
        for (String correctAnswer : question.getCorrectAnswers()) {
            if (correctAnswer.trim().toLowerCase().equals(userAnswer)) {
                return new QuestionGradingResult(true, question.getMaxPoints(), "Correct answer");
            }
        }

        return new QuestionGradingResult(false, 0, "Incorrect answer");
    }

    private QuestionGradingResult gradeFillInTheBlankPhrase(Question question, QuestionResponse response) {
        if (question.getCorrectAnswers() == null || response.getTextAnswer() == null) {
            return new QuestionGradingResult(false, 0, "Missing answer");
        }

        String userAnswer = response.getTextAnswer().trim().toLowerCase();
        int foundPhrases = 0;
        
        for (String phrase : question.getCorrectAnswers()) {
            if (userAnswer.contains(phrase.trim().toLowerCase())) {
                foundPhrases++;
            }
        }

        // Grade based on percentage of key phrases found
        double percentage = (double) foundPhrases / question.getCorrectAnswers().size();
        int points = (int) Math.round(percentage * question.getMaxPoints());
        
        return new QuestionGradingResult(foundPhrases == question.getCorrectAnswers().size(), 
            points, "Found " + foundPhrases + " of " + question.getCorrectAnswers().size() + " key phrases");
    }

    private QuestionGradingResult gradeMatching(Question question, QuestionResponse response) {
        if (question.getMatchingPairs() == null || response.getMatchingAnswers() == null) {
            return new QuestionGradingResult(false, 0, "Missing matching data");
        }

        int correctMatches = 0;
        int totalPairs = question.getMatchingPairs().size();

        for (Question.MatchingPair correctPair : question.getMatchingPairs()) {
            String expectedRight = response.getMatchingAnswers().get(correctPair.getLeft());
            if (expectedRight != null && expectedRight.equals(correctPair.getRight())) {
                correctMatches++;
            }
        }

        double percentage = (double) correctMatches / totalPairs;
        int points = (int) Math.round(percentage * question.getMaxPoints());
        boolean isCorrect = correctMatches == totalPairs;

        return new QuestionGradingResult(isCorrect, points, 
            "Matched " + correctMatches + " of " + totalPairs + " pairs correctly");
    }

    private QuestionGradingResult gradeSequencing(Question question, QuestionResponse response) {
        if (question.getSequenceItems() == null || response.getSequenceAnswers() == null) {
            return new QuestionGradingResult(false, 0, "Missing sequence data");
        }

        List<String> correctOrder = question.getSequenceItems();
        List<String> userOrder = response.getSequenceAnswers();

        if (correctOrder.size() != userOrder.size()) {
            return new QuestionGradingResult(false, 0, "Incorrect number of items");
        }

        int correctPositions = 0;
        for (int i = 0; i < correctOrder.size(); i++) {
            if (correctOrder.get(i).equals(userOrder.get(i))) {
                correctPositions++;
            }
        }

        double percentage = (double) correctPositions / correctOrder.size();
        int points = (int) Math.round(percentage * question.getMaxPoints());
        boolean isCorrect = correctPositions == correctOrder.size();

        return new QuestionGradingResult(isCorrect, points, 
            "Correctly ordered " + correctPositions + " of " + correctOrder.size() + " items");
    }

    private QuestionGradingResult gradeSubjective(Question question, QuestionResponse response) {
        if (response.getTextAnswer() == null || response.getTextAnswer().trim().isEmpty()) {
            return new QuestionGradingResult(false, 0, "No answer provided");
        }

        // For subjective questions, we can only do basic validation
        String answer = response.getTextAnswer().trim();
        
        // Check minimum word count if specified
        if (question.getMinWordCount() != null && question.getMinWordCount() > 0) {
            String[] words = answer.split("\\s+");
            if (words.length < question.getMinWordCount()) {
                return new QuestionGradingResult(false, 0, 
                    "Answer too short. Minimum " + question.getMinWordCount() + " words required.");
            }
        }

        // Subjective questions require manual grading
        return new QuestionGradingResult(true, 0, "Requires manual grading", true);
    }

    public static class QuestionGradingResult {
        private final boolean isCorrect;
        private final int points;
        private final String feedback;
        private final boolean requiresManualGrading;

        public QuestionGradingResult(boolean isCorrect, int points, String feedback) {
            this(isCorrect, points, feedback, false);
        }

        public QuestionGradingResult(boolean isCorrect, int points, String feedback, boolean requiresManualGrading) {
            this.isCorrect = isCorrect;
            this.points = points;
            this.feedback = feedback;
            this.requiresManualGrading = requiresManualGrading;
        }

        public boolean isCorrect() { return isCorrect; }
        public int getPoints() { return points; }
        public String getFeedback() { return feedback; }
        public boolean requiresManualGrading() { return requiresManualGrading; }
    }
}
