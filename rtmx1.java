🧬 Class Design / Inheritance & Polymorphism
Abstract Class
java
Copy
Edit
public abstract class SoccerTeam {
    protected String teamNumber;
    protected String teamName;

    public SoccerTeam(String teamNumber, String teamName) {
        setTeamNumber(teamNumber); this.teamName = teamName;
    }

    public abstract int getRating();  // Polymorphism
}
Subclasses
java
Copy
Edit
public class AmSoccerTeam extends SoccerTeam {
    private int goalsScored, goalsConceded;
    public int getRating() {
        return (int)(((4 * goalsScored - 3 * goalsConceded) * 200.0) / (goalsScored + goalsConceded));
    }
}
public class ProSoccerTeam extends SoccerTeam {
    private int gamesWon, gamesTied, gamesLost;
    public int getRating() {
        return (int)(((3 * gamesWon + gamesTied - 2 * gamesLost) * 200.0) / (gamesWon + gamesTied + gamesLost));
    }
}
🤝 Interfaces & Implementation
Interface
java
Copy
Edit
public interface IRanked {
    int getRating(); // enforced in SoccerTeam subclasses
}
Implementation
java
Copy
Edit
// Implemented by abstract class and overridden by subclasses
public abstract class SoccerTeam implements IRanked { ... }
📊 Sorting & Comparable
Comparable Implementation
java
Copy
Edit
public abstract class SoccerTeam implements Comparable<SoccerTeam> {
    public int compareTo(SoccerTeam other) {
        String thisKey = teamNumber.charAt(0) + String.format("%04d", this.getRating());
        String otherKey = other.teamNumber.charAt(0) + String.format("%04d", other.getRating());
        return thisKey.compareTo(otherKey);
    }
}
Sorting Teams
java
Copy
Edit
Arrays.sort(teamsArray);  // Uses compareTo
⚠️ Custom Exception / Exception Handling
Custom Exception
java
Copy
Edit
public class InvalidTeamNumberException extends RuntimeException {
    public InvalidTeamNumberException(String msg) {
        super("InvalidTeamNumberException: " + msg);
    }
}
Exception Conditions in setTeamNumber()
java
Copy
Edit
public void setTeamNumber(String num) {
    if (num.length() != 4 && !num.matches("[AP]\\d{3}"))
        throw new InvalidTeamNumberException("Team number " + num + " must have: Length of 4. Only digits in positions 2 to 4.");
    if (num.length() != 4)
        throw new InvalidTeamNumberException("Team number " + num + " must have: Length of 4.");
    if (!num.matches("[AP]\\d{3}"))
        throw new InvalidTeamNumberException("Team number " + num + " must have: Only digits in positions 2 to 4.");
    this.teamNumber = num;
}
Usage
java
Copy
Edit
try {
    // Create teams
} catch (InvalidTeamNumberException e) {
    System.out.println(e.getMessage());
}
💾 Application Logic & File I/O + Arrays
Main Application
java
Copy
Edit
SoccerTeam[] teams = new SoccerTeam[25];
Scanner sc = new Scanner(new File("soccerdata.txt"));
int count = 0;
while (sc.hasNextLine()) {
    String[] parts = sc.nextLine().split("#");
    try {
        if (parts[0].startsWith("A"))
            teams[count++] = new AmSoccerTeam(...);
        else if (parts[0].startsWith("P"))
            teams[count++] = new ProSoccerTeam(...);
        else
            System.out.println("Incorrect code: " + parts[0]);
    } catch (InvalidTeamNumberException e) {
        System.out.println(e.getMessage());
    }
}
💽 Serialization / Deserialization
Serialization
java
Copy
Edit
ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("teams.ser"));
for (SoccerTeam t : teams) if (t != null) out.writeObject(t);
out.close();
Deserialization
java
Copy
Edit
ObjectInputStream in = new ObjectInputStream(new FileInputStream("teams.ser"));
while (true) {
    try {
        SoccerTeam team = (SoccerTeam) in.readObject();
        System.out.println(team.teamNumber + " " + team.teamName);
    } catch (EOFException e) {
        System.out.println("End of file reached.");
        break;
    }
}
in.close();




//Desterilized Items:
import java.io.*;

public class DeserializeTeams
{
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        SoccerTeam[] teams = new SoccerTeam[10];
        int teamCount = 0;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("teams.ser"))) 
        {
            System.out.println("List of teams:");
            while (true)
            {
                try 
                {
                    SoccerTeam team = (SoccerTeam) in.readObject();
                    teams[teamCount++] = team;
                    
                    // Use instanceof to check team type and downcast
                    if (team instanceof ProSoccerTeam) 
                    {
                        ProSoccerTeam proTeam = (ProSoccerTeam) team;
                        System.out.printf("%s %s Games Won: %d Drawn: %d Lost: %d%n", proTeam.getTeamNumber(), proTeam.getTeamName(), proTeam.getGamesWon(), proTeam.getGamesDrawn(), proTeam.getGamesLost());
                    } 
                    else if (team instanceof AmSoccerTeam) 
                    {
                        AmSoccerTeam amTeam = (AmSoccerTeam) team;
                        System.out.printf("%s %s Goals Scored: %d Conceded: %d RankingScore: %d%n",  amTeam.getTeamNumber(),  amTeam.getTeamName(),  amTeam.getGoalsScored(),  amTeam.getGoalsConceded(),((AmSoccerTeam)team).getGoalsConceded());
                    }
                } 
                catch (EOFException e) 
                {
                    System.out.println("End of file reached.");
                    break;
                }
            }
        }
        
        // Print summary of loaded teams
        System.out.printf("%nSummary: %d teams loaded into array.%n", teamCount);
    }
}





//IO 
import java.io.*;
import java.util.*;

public class TeamsList{
    public static void main(String[] args) {
        SoccerTeam[] teams = new SoccerTeam[25];
        int numberOfTeams = 0;

        try (Scanner input = new Scanner(new File("soccerdata.txt"))) {
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String[] fields = line.split("#");

                try 
				{
                    String teamNumber = fields[0];
                    String teamName = fields[1];
                    char type = teamNumber.charAt(0);

                    if (type == 'A') {
                        int goalsScored = Integer.parseInt(fields[2]);
                        int goalsConceded = Integer.parseInt(fields[3]);
                        teams[numberOfTeams++] = new AmSoccerTeam(teamNumber, teamName, goalsScored, goalsConceded);
                    } else if (type == 'P') {
                        int won = Integer.parseInt(fields[2]);
                        int tied = Integer.parseInt(fields[3]);
                        int lost = Integer.parseInt(fields[4]);
                        teams[numberOfTeams++] = new ProSoccerTeam(teamNumber, teamName, won, tied, lost);
                    } else {
                        System.out.println("Incorrect code: " + teamNumber + " for team " + teamName);
                    }
                } catch (InvalidTeamNumberException e) {
                    System.out.println("InvalidTeamNumberException: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error processing line: " + Arrays.toString(fields) + " - " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("soccerdatade not found.");
        }

        // List of teams
        System.out.println("\nList of teams:");
        for (int i = 0; i < numberOfTeams; i++) {
            System.out.println(teams[i]);
        }

        // Ratings
        System.out.println("\nNumber Team name Rating");
        for (int i = 0; i < numberOfTeams; i++) {
            System.out.printf("%s %s %d%n", teams[i].getTeamNumber(), teams[i].getTeamName(), teams[i].getRankingScore());
        }

        // Amateur goal differences
        System.out.println("\nNumber Team name Difference");
        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i] instanceof AmSoccerTeam) {
                AmSoccerTeam am = (AmSoccerTeam) teams[i];
				int goalDif = am.getGoalsScored()- am.getGoalsConceded();
                System.out.printf("%s %s %d%n", am.getTeamNumber(), am.getTeamName(),goalDif );
            }
        }

        // Most games won
        ProSoccerTeam top = null;
        for (int i = 0; i < numberOfTeams; i++) {
            if (teams[i] instanceof ProSoccerTeam) {
                ProSoccerTeam p = (ProSoccerTeam) teams[i];
                if (top == null || p.getGamesWon() > top.getGamesWon()) {
                    top = p;
                }
            }
        }
        if (top != null) {
            System.out.printf("\nThe most team with the games won is: %s with %d wins%n", top.getTeamName(), top.getGamesWon());
        }

        // Sort and print
        Arrays.sort(teams, 0, numberOfTeams);
        System.out.println("\nList of teams after sort:");
        for (int i = 0; i < numberOfTeams; i++) {
            System.out.printf("%s Rating: %d%n", teams[i], teams[i].getRankingScore());
        }

        // Serialize
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("teams.ser"))) 
		{
            for (int i = 0; i < numberOfTeams; i++) 
			{
                out.writeObject(teams[i]);
            }
        } 
		catch (IOException e) 
		{
            System.out.println("Serialization error: " + e);
        }
    }
}




// 

import java.io.*;
public abstract class SoccerTeam implements RankingCalculation, Comparable<SoccerTeam>, Serializable
{
	private String teamName;
	private String teamNumber;
	
	public SoccerTeam()
	{
		this("","");
	}
	
	public SoccerTeam(String teamNumber, String teamName)
	{
		setTeamName(teamName);
		setTeamNumber(teamNumber);
	}
	
	public void setTeamName(String teamName)
	{
		this.teamName = teamName;
	}
	
	public void setTeamNumber(String teamNumber)
	{
		if (teamNumber == null || teamNumber.isEmpty()) 
		{
			throw new InvalidTeamNumberException("Team number must have: Length of 4.");
		}
		
		if (teamNumber.length() != 4) {
			throw new InvalidTeamNumberException("Team number " + teamNumber + " must have: Length of 4.");
		}
		
		char firstChar = teamNumber.charAt(0);
		if (firstChar != 'A' && firstChar != 'P') 
		{
			throw new InvalidTeamNumberException("Team number " + teamNumber + " must have: First character 'A' or 'P'.");
		}
		
		for (int i = 1; i < 4; i++) {
			if (!Character.isDigit(teamNumber.charAt(i))) 
			{
				throw new InvalidTeamNumberException("Team number " + teamNumber + " must have: Only digits in positions 2 to 4.");
			}
		}
		this.teamNumber = teamNumber;
	}
	
	public String getTeamName()
	{
		return teamName;
	}
	
	public String getTeamNumber()
	{
		return teamNumber;
	}
	
	public String toString()
	{
		return "Team number: "+getTeamNumber()+ " Name: "+getTeamName() ;
	}
	
	@Override
    public int compareTo(SoccerTeam other) 
	{
        String key1 = teamNumber.charAt(0) + String.format("%03d", this.getRankingScore());
        String key2 = other.teamNumber.charAt(0) + String.format("%03d", other.getRankingScore());
        return key1.compareTo(key2);
    }
}



//

public class ProSoccerTeam extends SoccerTeam 
{
	private int gamesWon;
	private int gamesDrawn;
	private int gamesLost;
	
	public ProSoccerTeam()
	{
		this("","",0,0,0);
	}
	
	public ProSoccerTeam(String teamName, String teamNumber, int gamesWon, int gamesDrawn, int gamesLost)
	{
		super(teamName, teamNumber);
		setGamesWon(gamesWon);
		setGamesDrawn(gamesDrawn);
		setGamesLost(gamesLost);
	}
	
	public void setGamesWon(int gamesWon)
	{
		this.gamesWon = gamesWon;
	}	
	
	public void setGamesDrawn(int gamesDrawn)
	{
		this.gamesDrawn = gamesDrawn;
	}	
	
	public void setGamesLost(int gamesLost)
	{
		this.gamesLost = gamesLost;
	}	
	
	public int getGamesWon()
	{
		return gamesWon;
	}	

	public int getGamesDrawn()
	{
		return gamesDrawn;
	}
	
	public int getGamesLost()
	{
		return gamesLost;
	}
	
	public int getRankingScore()
	{
		return (int)((getGamesWon()*3+getGamesDrawn()-getGamesLost()*2)*200.0/(getGamesWon()+getGamesDrawn()+getGamesLost()));
	}
// (3 times won + tied minus 2 times lost) times 200 / (won + tied + lost)
	public String toString()
	{
		return String.format("%-41s Games Won:%2d Drawn:%2d Lost:%2d", super.toString() ,getGamesWon(), getGamesDrawn(), getGamesLost());
	}

}




