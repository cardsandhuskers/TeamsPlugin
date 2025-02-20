# TeamsPlugin
Simple Menu for Creating Teams

## Commands:
**/teammenu**
- opens the menu for teams

**/lockteams** and **/unlockteams**
- toggle players' ability to join or leave teams

**/teams**
- Join [teamName] - joins specified team
- Leave - leaves current team
- Add [teamName] [color] - adds team (OP only)
- Remove [teamName] - deletes team (OP only)
- RemovePlayer [playerName] - removes player from their team (OP only)
- AddPlayer [teamName] [playerName] - adds player to team (OP only)
- SetColor [teamName] [color] - recolors the specified team (OP only)
- save - saves current teams w/ players to a file (OP only)
- load - loads the teams from the file (OP only)


### Using this library:
Simply include the below repository and dependency in your pom.xml
```
<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/cardsandhuskers/TeamsPlugin</url>
</repository>
        
<dependency>
  <groupId>io.github.cardsandhuskers</groupId>
  <artifactId>teams</artifactId>
  <version>1.1.1</version>
</dependency>
```

### Dependencies:
- PlayerPoints
- PlaceholderAPI


### Placeholders:
 - %teamsapi_teampoints% - returns the points the requesting player's team has
 - %teamsapi_temppoints% - returns the temp points the requesting player has
 - %teamsapi_totalpoints% - returns the total points (ppAPI points + temp ponts) the requesting player has
 - %teamsapi_tabTeam% - returns the tab string for the player's team (team color and first 4 letters)
 - %teamsapi_team% - returns the name of the requesting player's team
 - %teamsapi_color% - returns the color string for the requesting player's team
 - %teamsapi_position% - returns the requesting player's place on the individual leaderboard
 - %teamsapi_teamline[1-4]% - returns the position of a team relative to the requesting player's team. Positioning is 1st place, 1 above, player team, 1 below. This is adjusted if player team is in 1st, 2nd, or last place

