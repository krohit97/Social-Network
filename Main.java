import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static void main(String args[]) throws IOException
    {
        @SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

        System.out.print("Enter database file name: ");
        String fileName = in.next();

        Network myNetwork = new Network(fileName);

        while(true)
        {
            if (!myNetwork.isLoggedIn())
            {
                System.out.print("1. Sign up\n2. Login\n3. Exit\nCommand: ");
                int choice = in.nextInt();


                String username, password, displayName;
                switch(choice)
                {
                    case 1:
                        System.out.print("Enter username: ");
                        username = in.next();

                        System.out.print("Enter password: ");
                        password = in.next();

                        System.out.print("Enter display name: ");
                        displayName = in.next();

                        try
                        {
                            myNetwork.register(username, password, displayName);
                        }
                        catch (Network.UserException e)
                        {
                            System.out.println(e.getMessage());
                        }

                        break;
                    case 2:
                        System.out.print("Enter username: ");
                        username = in.next();

                        System.out.print("Enter password: ");
                        password = in.next();

                        try
                        {
                            myNetwork.login(username, password);
                            System.out.printf("User %s logged in\n", myNetwork.getUserLoggedIn().getDisplayName());
                        }
                        catch (Network.UserException e)
                        {
                            System.out.println(e.getMessage());
                        }

                        break;
                    case 3: System.exit(0); break;
                    default: System.out.println("Invalid Choice!");
                }
            }
            else
            {
                String options = ""
                        + "1. List Friends\n"
                        + "2. Search\n"
                        + "3. Update Status\n"
                        + "4. Pending Requests\n"
                        + "5. Logout\n";

                System.out.print(options);
                System.out.print("Command: ");

                int option = in.nextInt();

                switch(option)
                {
                    case 1: myNetwork.listFriends(); break;
                    case 2:
                        System.out.print("Enter name: ");
                        String searchName = in.next();

                        try
                        {
                            myNetwork.searchUser(searchName);
                        }
                        catch (Network.UserException e)
                        {
                            System.out.println(e.getMessage());
                        }

                        break;
                    case 3:
                        myNetwork.statusUpdate();
                        break;
                    case 4:
                        myNetwork.handleRequests();
                        break;
                    case 5:
                        myNetwork.logout();
                        System.out.println("Logged out successfully!");
                        break;
                    default: System.out.println("Invalid Choice!");
                }
            }
        }
    }
}