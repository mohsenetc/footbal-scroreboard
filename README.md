# Live Football World Cup Scoreboard Library

This project is a simple Java library designed to manage and display live scores for football matches during the World Cup. It allows for adding matches, updating scores, finishing matches, and retrieving a summary of ongoing matches.

## Features

- Start new matches with initial scores of 0-0.
- Update scores for ongoing matches.
- Finish matches and remove them from the scoreboard.
- Retrieve a summary of ongoing matches ordered by total score and start time.

## Requirements

- Java 21
- JUnit 5 (for testing)

## Getting Started

### Installation

1. Clone the repository:
   ```bash
     git clone <repository_url>
   ```
2. Navigate to the project directory:
   ```bash
      cd <project_directory>
   ```
3. Build the project:
   ```bash
      mvn clean install
   ```
## Usag
1. Create a Scoreboard Instance:
```java
Scoreboard scoreboard = new Scoreboard();
```
2. Start a New Match:
```java
scoreboard.startMatch("Home Team", "Away Team");
```
3. Update Scores:
```java
scoreboard.updateScore("Home Team", 1, 2); // Home team 1, Away team 2
```
4. Finish a Match:
```java
scoreboard.finishMatch("Home Team");
```

5. Get Matches Summary:
```java
    List<Match> summary = scoreboard.getMatchesSummary();
    for (Match match : summary) {
        System.out.println(match);
    }
```
### Example
Here’s a complete example of using the library:

```java
public class Main {
public static void main(String[] args) {
Scoreboard scoreboard = new Scoreboard();

        // Start matches
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.startMatch("Spain", "Brazil");

        // Update scores
        scoreboard.updateScore("Mexico", 0, 5);
        scoreboard.updateScore("Spain", 10, 2);

        // Get summary
        List<Match> summary = scoreboard.getMatchesSummary();
        for (Match match : summary) {
            System.out.println(match);
        }
    }
}
```
### Running Tests
To run the tests for this library, ensure you have JUnit 5 configured in your project, and then execute:

```bash
mvn test
```
### Assumptions
- Each match is uniquely identified by the home team name.
- The library does not implement a persistence layer; all data is stored in memory.
### Contributing
Contributions are welcome! Please create a pull request or submit issues for improvements.

### Next Steps
 - **Enhance Error Handling:** Consider adding error handling for cases like updating scores for non-existing matches.
 - **Improve Validation:** Validate team names and scores before processing.
 - **Add Additional Features:** Consider adding functionality for features like getting the list of finished matches or getting a specific match by team name.