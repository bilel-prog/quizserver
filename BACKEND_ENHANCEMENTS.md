# Quiz Beta Backend - Enhanced Question Types

## Overview
This document outlines the backend changes made to support comprehensive question types in the Quiz Beta application. The system now supports 13 different question types across objective and subjective categories.

## Question Types Supported

### Objective Questions (Auto-graded)
1. **Multiple Choice (Single Answer)** - `MULTIPLE_CHOICE_SINGLE`
2. **Multiple Choice (Multiple Answers)** - `MULTIPLE_CHOICE_MULTIPLE`
3. **Multiple Choice (Best Answer)** - `MULTIPLE_CHOICE_BEST`
4. **True/False** - `TRUE_FALSE`
5. **Yes/No** - `YES_NO`
6. **Fill in the Blank** - `FILL_IN_THE_BLANK`
7. **Fill in the Blank (Phrase)** - `FILL_IN_THE_BLANK_PHRASE`
8. **Matching** - `MATCHING`
9. **Matching (One to Many)** - `MATCHING_ONE_TO_MANY`
10. **Sequencing** - `SEQUENCING`
11. **Chronological Ordering** - `ORDERING_CHRONOLOGICAL`

### Subjective Questions (Manual grading required)
12. **Short Answer** - `SHORT_ANSWER`
13. **Essay** - `ESSAY`

## Database Schema Changes

### New Question Table Columns
- `question_type` (VARCHAR): Type of question
- `answer_guidelines` (TEXT): Guidelines for subjective questions
- `max_points` (INTEGER): Maximum points for the question
- `min_word_count` (INTEGER): Minimum word count for essays
- `time_limit` (INTEGER): Time limit per question

### New Tables Created
- `question_options`: Stores flexible question options
- `question_correct_answers`: Stores multiple correct answers
- `question_matching_pairs`: Stores matching pairs (left-right)
- `question_sequence_items`: Stores items for sequencing questions

## API Endpoints

### Admin Endpoints
- `GET /api/admin/question/types` - Get all question types
- `POST /api/admin/test/question` - Create question (enhanced)
- `PUT /api/admin/test/question/{id}` - Update question
- `DELETE /api/admin/test/question/{id}` - Delete question
- `GET /api/admin/question/types/stats` - Get question type statistics

### User Endpoints
- `GET /api/user/question/types` - Get all question types
- `POST /api/user/test/{testId}/question` - Add question to own test (enhanced)

## Key Classes and Components

### Enums
- `QuestionType`: Enum containing all question types with metadata

### Entities
- `Question`: Enhanced with new fields and collections
- `Question.MatchingPair`: Embedded class for matching pairs

### DTOs
- `QuestionDTO`: Enhanced to support all question types
- `QuestionResponse`: Enhanced to handle different answer types

### Services
- `QuestionGradingService`: New service for comprehensive question grading
- `TestServiceImpl`: Updated to handle new question types

## Grading System

### Automatic Grading
The system automatically grades objective questions:
- Multiple choice questions (various types)
- True/False and Yes/No questions
- Fill-in-the-blank questions
- Matching questions
- Sequencing questions

### Manual Grading
Subjective questions require manual grading:
- Short answer questions
- Essay questions
- Basic validation (word count) is performed automatically

### Grading Features
- Partial credit for matching and sequencing questions
- Case-insensitive text matching for fill-in-the-blank
- Phrase detection for fill-in-the-blank phrase questions
- Points-based scoring system

## Backward Compatibility

The system maintains full backward compatibility:
- Existing questions continue to work
- Legacy API endpoints remain functional
- Database migration is handled automatically
- Default question type is `MULTIPLE_CHOICE_SINGLE`

## Testing the Backend

### Sample API Calls

#### Get Question Types
```bash
GET /api/admin/question/types
```

#### Create a Multiple Choice Question
```json
POST /api/admin/test/question
{
  "questionText": "What is the capital of France?",
  "questionType": "MULTIPLE_CHOICE_SINGLE",
  "testId": 1,
  "options": ["London", "Berlin", "Paris", "Madrid"],
  "correctAnswers": ["Paris"],
  "maxPoints": 1
}
```

#### Create a Matching Question
```json
POST /api/admin/test/question
{
  "questionText": "Match the countries with their capitals:",
  "questionType": "MATCHING",
  "testId": 1,
  "matchingPairs": [
    {"left": "France", "right": "Paris"},
    {"left": "Germany", "right": "Berlin"},
    {"left": "Italy", "right": "Rome"}
  ],
  "maxPoints": 3
}
```

#### Create an Essay Question
```json
POST /api/admin/test/question
{
  "questionText": "Discuss the impact of artificial intelligence on society.",
  "questionType": "ESSAY",
  "testId": 1,
  "answerGuidelines": "Should cover technological, social, and ethical aspects",
  "maxPoints": 10,
  "minWordCount": 200
}
```

## Migration Guide

### For Existing Deployments

1. **Backup Database**: Always backup before migration
2. **Run Migration Script**: Execute `database_migration.sql`
3. **Update Application**: Deploy new application version
4. **Verify**: Test with existing and new question types

### Environment Variables
No new environment variables required. The system uses existing database configuration.

## Performance Considerations

- Indexes added for question_type and max_points
- Lazy loading for question collections
- Efficient grading algorithms
- Pagination support maintained

## Security Notes

- All endpoints require proper authentication
- Users can only modify their own tests
- Input validation for all question types
- SQL injection protection maintained

## Future Enhancements

- Question analytics and reporting
- Advanced grading rubrics
- Question templates
- Bulk question import/export
- Question difficulty scoring
- Time-based question recommendations

## Troubleshooting

### Common Issues

1. **Legacy Questions Not Loading**: Ensure migration script was run
2. **New Question Types Not Saving**: Check database schema updates
3. **Grading Errors**: Verify QuestionGradingService is properly injected

### Log Monitoring
Monitor application logs for:
- Question creation errors
- Grading service errors
- Database constraint violations

## Support

For issues related to the enhanced question types system:
1. Check application logs
2. Verify database schema
3. Test API endpoints with Postman
4. Review question JSON structure

---

**Note**: This enhancement maintains full backward compatibility while providing extensive new functionality for creating diverse assessment types.
