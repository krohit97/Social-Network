// Authors :- Ronak Kumar (2015080)
//            Rohit Kumar (2015078)

import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings({})

public class Person {
    
    private String userName;
    private String password;
    private String displayName;

    private String status;

    private ArrayList<String> requests;
    private ArrayList<String> friends;

    public Person(String userName, String password, String displayName) {
        this.userName = userName;
        this.password = password;
        this.displayName = displayName;

        this.status = "";

        this.friends = new ArrayList<String>(0);
        this.requests = new ArrayList<String>(0);
    }

    public Person(String fileLine) throws FileLineFormatException {
        String temp[] = fileLine.split(",");

        if (temp.length < 3) {
            throw new FileLineFormatException("Line does not contain one of (username|pass|display name)");
        }

        this.userName = temp[0];
        this.password = temp[1];
        this.displayName = temp[2];

        if (temp.length < 4) {
            throw new FileLineFormatException("Line does not contain number of friends");
        }

        int numFriends = Integer.parseInt(temp[3]);

        if (temp.length < 4 + numFriends) {
            throw new FileLineFormatException("Not enough friends");
        }

        friends = new ArrayList<String>(numFriends);

        for (int i = 0; i < numFriends; ++i) {
            int index = i + 4;
            friends.add(temp[index]);
        }

        int prevLength = 4 + numFriends;

        if (temp.length < prevLength + 1) {
            throw new FileLineFormatException("Line does not contain number of requests");
        }

        int numPending = Integer.parseInt(temp[prevLength]);

        if (temp.length < prevLength + 1 + numPending) {
            throw new FileLineFormatException("Not enough pending requests");
        }

        requests = new ArrayList<>(numPending);

        for (int i = 0; i < numPending; ++i) {
            int index = i + prevLength + 1;
            requests.add(temp[index]);
        }

        this.status = temp[temp.length - 1];
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getStatus()
    {
        return status;
    }

    public ArrayList<String> getFriends()
    {
        return friends;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public boolean checkPassword(String attempt)
    {
        return password.equals(attempt);
    }

    public ArrayList<String> getMutualFriends(Person User) {
        ArrayList <String> mutaulFriends = new ArrayList<String>(0);

        for(String friend: this.getFriends())
        {
            if(User.getFriends().contains(friend))
                mutaulFriends.add(friend);
        }

        return mutaulFriends;
    }

    public boolean isFriend(String userName)
    {
        return this.friends.contains(userName);
    }

    public boolean hasRequestFrom(String userName)
    {
        return this.requests.contains(userName);
    }

    public void viewProfile(Person asUser, String shortestPath) {
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        while(true)
        {
            if(asUser.isFriend(this.getUserName()))
            {
                System.out.format("You and %s are friends.\n",this.getDisplayName());

                System.out.format("Display Name: %s\n",this.getDisplayName());
                System.out.format("Status: %s\n",this.getStatus());

                System.out.format("Friends: ");

                if(this.getFriends().size() == 0)
                    System.out.format("No friends.");
                else
                    for(String friend: this.getFriends())
                        System.out.format("%s ",friend);

                System.out.format("\n");
                System.out.format("Mutual friends: ");

                ArrayList <String> mutualFriends = this.getMutualFriends(asUser);

                if(mutualFriends.size() == 0)
                    System.out.format("No mutual friends.");
                else
                    for(String mutualFriend: mutualFriends)
                        System.out.format("%s ",mutualFriend);

                System.out.println("");
                System.out.println("\tb. Back");

                System.out.print("Command: ");
                String option = in.next();

                if(option.equals("b"))
                    return;
                else
                    System.out.format("Invalid Choice.\n\n");
            }
            else
            {
                System.out.format("%s is not a friend.\n", this.getDisplayName());

                System.out.format("Mutual friends: ");

                ArrayList <String> mutualFriends = this.getMutualFriends(asUser);

                if(mutualFriends.size() == 0)
                    System.out.format("No mutual friends.");
                else
                    for(String mutualFriend: mutualFriends)
                        System.out.format("%s ",mutualFriend);

                System.out.println("");
                System.out.format("Shortest Path: %s\n",shortestPath);

                if(this.hasRequestFrom(asUser.getUserName()))
                {
                    System.out.println("\t1. Cancel Request");
                    System.out.println("\tb. Back");

                    String option = in.next();

                    switch (option)
                    {
                        case "1":
                            this.requests.remove(asUser.getUserName());
                            System.out.println("Request removed.");
                            break;
                        case "b": return;
                    }
                }
                else
                {
                    System.out.println("\t1. Send Request");
                    System.out.println("\tb. Back");

                    String option = in.next();

                    switch (option)
                    {
                        case "1":
                            this.requests.add(asUser.getUserName());
                            System.out.println("Request Sent.");
                            break;
                        case "b": return;
                    }
                }
            }
        }
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String toString() {
        String fileString = "";

        fileString = fileString.concat(this.userName);
        fileString = fileString.concat(",");

        fileString = fileString.concat(this.password);
        fileString = fileString.concat(",");

        fileString = fileString.concat(this.displayName);
        fileString = fileString.concat(",");

        fileString = fileString.concat(Integer.toString(friends.size()));
        fileString = fileString.concat(",");

        fileString = fileString.concat(arrayListString(this.friends));
        fileString = fileString.concat(",");

        fileString = fileString.concat(Integer.toString(requests.size()));
        fileString = fileString.concat(",");

        fileString = fileString.concat(arrayListString(this.requests));
        fileString = fileString.concat(",");

        fileString = fileString.concat(this.status);

        fileString = fileString.concat("\n");

        return fileString;
    }

    public static String arrayListString(ArrayList <String> list) {
        String listString = "";

        for(int i=0;i<list.size();i++)
        {
            listString = listString.concat(list.get(i));

            if(i != list.size() - 1)
                listString = listString.concat(",");
        }

        return listString;
    }

    public void addFriend(String userName) {
        this.friends.add(userName);
        this.requests.remove(userName);
    }

    public void removeRequest(String requestName)
    {
        this.requests.remove(requestName);
    }

    public class FileLineFormatException extends Exception {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FileLineFormatException(String message) {
            super(message);
        }
    }

    public class FriendException extends Exception {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FriendException(String message) {
            super(message);
        }
    }
}