import java.util.Scanner;

/**
 * A framework to run give illustrations for Database
 *
 * @author William Thain, Fox Christiansen, Jackson Shields, Bui Dinh Tuan Anh:
 *         lab sec 12
 * @version Nov 2, 2024
 */
public class SimpleDIYTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Database db = new Database();

        System.out.print("Insert your new username here:\t");
        String newUsername = sc.nextLine();
        System.out.print("Insert your new password here:\t");
        String newPassword = sc.nextLine();
        while (db.validateNewUser(new User(newUsername, newPassword)) == false) {
            System.out.print("That is not a legal password. Try another one:\t");
            newPassword = sc.nextLine();
        }
        System.out.print("Do you want to add a first name? (y/n)\t");
        String newFirstName = null;
        if (sc.nextLine().equals("y")) {
            System.out.print("Enter first name here:\t");
            newFirstName = sc.nextLine();
        }
        System.out.print("Do you want to add a last name? (y/n)\t");
        String newLastName = null;
        if (sc.nextLine().equals("y")) {
            System.out.print("Enter last name here:\t");
            newLastName = sc.nextLine();
        }
        System.out.print("Do you want to add a bio? (y/n)\t");
        String newBio = null;
        if (sc.nextLine().equals("y")) {
            System.out.print("Enter bio here (no newlines):\t");
            newBio = sc.nextLine();
        }
        System.out.print("Do you want to add a birthday? (y/n)\t");
        String newBirthday = null;
        String[] newBirthdayArray = null;
        int[] birthdayIntArray = new int[3];
        if (sc.nextLine().equals("y")) {
            System.out.print("Enter birthday data in mm/dd/yyyy format here(with the forward slashes):\t");
            newBirthday = sc.nextLine();
            newBirthdayArray = newBirthday.split("/");
            birthdayIntArray[0] = Integer.parseInt(newBirthdayArray[0]);
            birthdayIntArray[1] = Integer.parseInt(newBirthdayArray[1]);
            birthdayIntArray[2] = Integer.parseInt(newBirthdayArray[2]);
        }
        System.out.print("Would you like to set friendsOnly mode? (y/n)\t");
        boolean newFriendsOnly = false;
        if (sc.nextLine().equals("y")) {
            System.out.print("Enter a character to represent true or false here (t/f):\t");
            if (sc.nextLine().equals("t"))
                newFriendsOnly = true;
        }
        User exampleUser = new User(newUsername, newPassword, newFirstName,
                newLastName, newBio, birthdayIntArray, null, null, null, newFriendsOnly);

        System.out.println(
                "\n" + "Done, your data has been saved to usersHistory.txt, our database: " + "\n"
                        + exampleUser.toString());

        db.addUser(exampleUser);
        db.saveUsers();

        sc.close();
    }
}
