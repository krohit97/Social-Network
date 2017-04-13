// Authors :- Ronak Kumar (2015080)
//            Rohit Kumar (2015078)

import java.io.*;
import java.util.*;

@SuppressWarnings({})

public class Network
{
    private ArrayList<Person> users;
    private boolean loggedIn;
    private int userIndex; 
    private String fileName;

    public Network(String fileName) throws IOException {
        
        this.loggedIn = false;
        this.fileName = fileName;

        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String fileLine;

        users = new ArrayList<>(10);

        while((fileLine = reader.readLine()) != null)
        {
            try
            {
                Person temp = new Person(fileLine);
                users.add(temp);
            }
            catch(Person.FileLineFormatException ex)
            {
                System.out.println(ex.getMessage());
            }
        }

        reader.close();
    }

    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    public Person getUserLoggedIn()
    {
        return users.get(userIndex);
    }

    public void register(String userName, String password, String displayName) throws UserException, IOException {
        
        for(int i=0;i<users.size();i++)
        {
            Person currentUser = users.get(i);

            if(currentUser.getUserName().equals((userName)))
            {
                throw new UserException("Username already exists.");
            }
        }

        Person newUser = new Person(userName, password, displayName);
        users.add(newUser);

        this.saveToFile();
    }

    public void login(String userName, String password) throws UserException {
        
        for(int i=0;i<users.size();i++)
        {
            Person currentUser = users.get(i);

            if(currentUser.getUserName().equals(userName))
            {
                if(currentUser.checkPassword(password))
                {
                    loggedIn = true;
                    userIndex = i;

                    return ;
                }
                else
                {
                    throw new UserException("Incorrect password.");
                }
            }
        }

        throw new UserException("User does not exists");
    }

    public void logout()
    {
        this.loggedIn = false;
    }

    public void listFriends() {
        System.out.print("Your friends are: ");

        if(this.getUserLoggedIn().getFriends().size() == 0)
        {
            System.out.println("You have no friends\n");
            return ;
        }

        for(String friend: this.getUserLoggedIn().getFriends())
            System.out.format("%s ",friend);
        System.out.println("");
    }

    public void searchUser(String searchName) throws UserException, IOException {
        for(int i=0;i<this.users.size();i++)
        {
            Person currentUser = this.users.get(i);

            if(i != userIndex && currentUser.getUserName().equals(searchName))
            {
                currentUser.viewProfile(users.get(userIndex),this.getShortestPath(users.get(userIndex).getUserName(),currentUser.getUserName()));
                this.saveToFile();
                return ;
            }
        }

        throw new UserException("User does not exist.");
    }

    public Person getUser(String userName) throws UserException {
        for(Person user: users)
            if(user.getUserName().equals(userName))
                return user;

        throw new UserException("User not found");
    }

    public void statusUpdate() throws IOException {
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

        System.out.print("Enter new status: ");

        String status = in.next();

        this.users.get(userIndex).setStatus(status);

        System.out.println("Status updated!");

        this.saveToFile();
    }

    public void handleRequests() throws IOException {
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        while(true)
        {
            ArrayList <String> requests= this.users.get(userIndex).getRequests();

            for(int i=0;i<requests.size();i++)
                System.out.format("\t%d. %s\n",i+1,requests.get(i));
            System.out.print("\tb. Back\n");

            System.out.print("Command: ");
            String option = in.next();

            if(option.equals("b"))
            {
                return ;
            }
            else
            {
                int requestIndex = Integer.parseInt(option);

                if(0 < requestIndex && requestIndex <= requests.size())
                {
                    requestIndex--;

                    String requestUserName = requests.get(requestIndex);

                    int requestUserIndex = 0;

                    for(int i=0;i<this.users.size();i++)
                    {
                        if(this.users.get(i).getUserName().equals(requestUserName))
                        {
                            requestUserIndex = i;
                            break;
                        }
                    }

                    System.out.println(this.users.get(requestUserIndex).getDisplayName());
                    System.out.print("\t1. Accept\n\t2. Reject\n\tCommand: ");

                    int choice = in.nextInt();

                    switch (choice)
                    {
                        case 1:
                            this.users.get(userIndex).addFriend(requestUserName);
                            this.users.get(requestUserIndex).addFriend(this.users.get(userIndex).getUserName());
                            System.out.format("You are now friends with %s", this.users.get(requestUserIndex).getDisplayName());
                            break;
                        case 2:
                            this.users.get(requestUserIndex).removeRequest(this.users.get(userIndex).getUserName());
                            System.out.format("Friend request from %s deleted.", this.users.get(requestUserIndex).getDisplayName());
                            break;
                    }

                    System.out.println("");
                }
                else
                    System.out.println("Invalid choice.");
            }

            this.saveToFile();
        }

    }

    public void saveToFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        for(Person user: users)
            writer.write(user.toString());

        writer.close();
    }

    public String getShortestPath(String sourceUser, String destUser) throws UserException {
       
        ArrayDeque <String> Queue = new ArrayDeque<String>(0);
        ArrayDeque <String> Path = new ArrayDeque<String>(0);

        ArrayList <String> Visited = new ArrayList<String>(0);

        Queue.addLast(sourceUser);
        Path.addLast(sourceUser);

        while(Queue.size() != 0)
        {
            String userName = Queue.removeFirst();
            String path = Path.removeFirst();

            if(userName.equals(destUser))
                return path;

            Visited.add(userName);

            Person user = getUser(userName);

            for(String friend: user.getFriends())
            {
                if(!Visited.contains(friend))
                {
                    Queue.addLast(friend);
                    Path.addLast(path.concat(" -> ").concat(friend));
                }
            }
        }

        return "No Path!";
    }

    class UserException extends Exception {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UserException(String message)
        {
            super(message);
        }
    }

}