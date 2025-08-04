-- Database migration script for Quiz Beta - Enhanced Question Types
-- This script adds support for comprehensive question types

-- 1. Add new columns to Question table
ALTER TABLE question 
ADD COLUMN question_type VARCHAR(50) DEFAULT 'MULTIPLE_CHOICE_SINGLE' AFTER question_text,
ADD COLUMN answer_guidelines TEXT AFTER correct_answer,
ADD COLUMN max_points INTEGER DEFAULT 1 AFTER answer_guidelines,
ADD COLUMN min_word_count INTEGER AFTER max_points,
ADD COLUMN time_limit INTEGER AFTER min_word_count;

-- 2. Create table for flexible question options
CREATE TABLE IF NOT EXISTS question_options (
    question_id BIGINT NOT NULL,
    option_text VARCHAR(500) NOT NULL,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    INDEX idx_question_options_question_id (question_id)
);

-- 3. Create table for correct answers
CREATE TABLE IF NOT EXISTS question_correct_answers (
    question_id BIGINT NOT NULL,
    answer_text VARCHAR(500) NOT NULL,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    INDEX idx_question_correct_answers_question_id (question_id)
);

-- 4. Create table for matching pairs
CREATE TABLE IF NOT EXISTS question_matching_pairs (
    question_id BIGINT NOT NULL,
    left_item VARCHAR(255) NOT NULL,
    right_item VARCHAR(255) NOT NULL,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    INDEX idx_question_matching_pairs_question_id (question_id)
);

-- 5. Create table for sequence items
CREATE TABLE IF NOT EXISTS question_sequence_items (
    question_id BIGINT NOT NULL,
    item_text VARCHAR(500) NOT NULL,
    FOREIGN KEY (question_id) REFERENCES question(id) ON DELETE CASCADE,
    INDEX idx_question_sequence_items_question_id (question_id)
);

-- 6. Update existing questions to have the default question type
UPDATE question 
SET question_type = 'MULTIPLE_CHOICE_SINGLE' 
WHERE question_type IS NULL OR question_type = '';

-- 7. Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_question_type ON question(question_type);
CREATE INDEX IF NOT EXISTS idx_question_max_points ON question(max_points);

-- 8. Optional: Add columns to test_result table for enhanced grading (uncomment if needed)
-- ALTER TABLE test_result 
-- ADD COLUMN total_points INTEGER AFTER correct_answers,
-- ADD COLUMN earned_points INTEGER AFTER total_points,
-- ADD COLUMN points_percentage DOUBLE AFTER earned_points,
-- ADD COLUMN requires_manual_grading BOOLEAN DEFAULT FALSE AFTER points_percentage;

-- 9. Create a view for question statistics
CREATE OR REPLACE VIEW question_type_stats AS
SELECT 
    question_type,
    COUNT(*) as question_count,
    AVG(max_points) as avg_points
FROM question 
GROUP BY question_type;

-- 10. Insert sample data for testing (optional)
-- You can uncomment and modify these to create sample questions for testing

/*
-- Sample Multiple Choice Single Answer
INSERT INTO question (question_text, question_type, max_points, test_id) 
VALUES ('What is the capital of France?', 'MULTIPLE_CHOICE_SINGLE', 1, 1);

-- Sample True/False Question
INSERT INTO question (question_text, question_type, max_points, test_id) 
VALUES ('The Earth is flat.', 'TRUE_FALSE', 1, 1);

-- Sample Fill in the Blank
INSERT INTO question (question_text, question_type, max_points, test_id) 
VALUES ('The chemical symbol for water is _____.', 'FILL_IN_THE_BLANK', 1, 1);

-- Sample Essay Question
INSERT INTO question (question_text, question_type, max_points, min_word_count, answer_guidelines, test_id) 
VALUES (
    'Describe the impact of artificial intelligence on modern society.',
    'ESSAY',
    10,
    100,
    'Answer should cover: technological advancement, job market effects, ethical considerations, and future implications.',
    1
);
*/

-- End of migration script
