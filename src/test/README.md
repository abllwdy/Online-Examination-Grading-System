# JUnit Tests for OEGS-18 Request Review Feature

## Test Coverage

This test suite covers all the main components of the Request Review feature:

1. **ReviewRequestTest** - Tests justification validation
2. **QuestionResultTest** - Tests question status transitions and dispute handling
3. **ExamResultTest** - Tests exam management and final grade calculation
4. **StudentServiceTest** - Tests student review request functionality
5. **InstructorServiceTest** - Tests instructor dispute resolution
6. **ReviewWorkflowTest** - Tests the complete workflow integration
7. **NotificationServiceTest** - Tests notification system
8. **StudentDashboardTest** - Tests student dashboard visibility logic
9. **InstructorDashboardTest** - Tests instructor dashboard notifications

## Running Tests

### Option 1: Using IntelliJ IDEA
1. Right-click on the `test` folder
2. Select "Run 'All Tests'"
3. Or right-click on individual test files to run specific tests

### Option 2: Using Command Line
1. Make sure JUnit JAR files are in your classpath
2. Compile test files:
   ```
   javac -cp "junit-4.13.2.jar;hamcrest-core-1.3.jar;../src" test/*.java
   ```
3. Run TestRunner:
   ```
   java -cp "junit-4.13.2.jar;hamcrest-core-1.3.jar;../src;test" TestRunner
   ```

### Option 3: Using TestRunner
Run the `TestRunner.java` class which will execute all tests and display results.

## Test Requirements

- JUnit 4.x (junit-4.13.2.jar)
- Hamcrest Core (hamcrest-core-1.3.jar) - for JUnit assertions

## Test Coverage Summary

- **ReviewRequest**: Validation of justification (null, empty, whitespace)
- **QuestionResult**: Status transitions, dispute prevention, accept/reject
- **ExamResult**: Question management, final grade calculation
- **StudentService**: Review request with validation
- **InstructorService**: Accept/reject disputes with grade updates
- **ReviewWorkflow**: Complete end-to-end workflow
- **NotificationService**: Pending dispute tracking
- **Dashboards**: Visibility and notification logic

All tests verify the acceptance criteria from the user story OEGS-18.

