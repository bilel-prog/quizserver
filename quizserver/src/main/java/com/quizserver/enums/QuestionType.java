package com.quizserver.enums;

public enum QuestionType {
    // Objective Questions (Closed-Ended)
    MULTIPLE_CHOICE_SINGLE("multiple_choice_single", "Multiple Choice (Single Answer)", "objective"),
    MULTIPLE_CHOICE_MULTIPLE("multiple_choice_multiple", "Multiple Choice (Multiple Answers)", "objective"),
    MULTIPLE_CHOICE_BEST("multiple_choice_best", "Multiple Choice (Best Answer)", "objective"),
    TRUE_FALSE("true_false", "True/False", "objective"),
    YES_NO("yes_no", "Yes/No", "objective"),
    FILL_IN_THE_BLANK("fill_in_the_blank", "Fill in the Blank", "objective"),
    FILL_IN_THE_BLANK_PHRASE("fill_in_the_blank_phrase", "Fill in the Blank (Phrase)", "objective"),
    MATCHING("matching", "Matching", "objective"),
    MATCHING_ONE_TO_MANY("matching_one_to_many", "Matching (One to Many)", "objective"),
    SEQUENCING("sequencing", "Sequencing", "objective"),
    ORDERING_CHRONOLOGICAL("ordering_chronological", "Chronological Ordering", "objective"),
    
    // Subjective Questions (Open-Ended)
    SHORT_ANSWER("short_answer", "Short Answer", "subjective"),
    ESSAY("essay", "Essay", "subjective");

    private final String value;
    private final String label;
    private final String category;

    QuestionType(String value, String label, String category) {
        this.value = value;
        this.label = label;
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public String getCategory() {
        return category;
    }

    public static QuestionType fromValue(String value) {
        for (QuestionType type : QuestionType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown question type: " + value);
    }

    public boolean isObjective() {
        return "objective".equals(this.category);
    }

    public boolean isSubjective() {
        return "subjective".equals(this.category);
    }
}
