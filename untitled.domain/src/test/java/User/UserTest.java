package User;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void InstantiateUser_NotNull() {
        User user = new User("test", "firstName", "lastName", "emailAddress");
        assertNotNull(user);
    }

    @Test
    public void InstantiateUser_HasGivenUserName() {
        String username = "test";
        User user = new User(username, "firstName", "lastName", "emailAddress");
        assertEquals(username, user.getUserName());
    }

    @Test
    public void InstantiateUser_HasGivenFirstName() {
        String firstName = "test";
        User user = new User("username", firstName, "lastName", "emailAddress");
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    public void InstantiateUser_HasGivenLastName() {
        String lastName = "test";
        User user = new User("username", "firstName", lastName, "emailAddress");
        assertEquals(lastName, user.getLastName());
    }

    @Test
    public void InstantiateUser_HasGivenEmail() {
        String emailAddress = "test";
        User user = new User("username", "firstName", "lastName", emailAddress);
        assertEquals(emailAddress, user.getEmailAddress());
    }

    @Test
    public void UserEquality_IsEqual() {
        String username = "test";
        String firstName = "test";
        String lastName = "test";
        String email = "test@test.com";
        User user = new User(username, firstName, lastName, email);
        User user2 = new User(username, firstName, lastName, email);
        assertEquals(user, user2);
    }

    @Test
    public void UserEquality_NotEqual() {
        String username = "test";
        String firstName = "test";
        String lastName = "test";
        String email = "test@test.com";
        User user = new User(username, firstName, lastName, email);
        User user2 = new User(username, firstName, lastName, "bob@notTest.com");
        assertNotEquals(user, user2);
    }

    @Test
    public void UserEquality_NotEqualNotUser() {
        String username = "test";
        String firstName = "test";
        String lastName = "test";
        String email = "test@test.com";
        User user = new User(username, firstName, lastName, email);
        assertNotEquals(user, new Object());
    }

    @Test
    public void UserEquality_HashCode() {
        String username = "test";
        String firstName = "test";
        String lastName = "test";
        String email = "test@test.com";
        User user = new User(username, firstName, lastName, email);
        User user2 = new User(username, firstName, lastName, email);
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    public void ChangeUsername_Matches() {
        String newUsername = "bob";
        String username = "username";
        String firstName = "firstName";
        String lastName = "lastName";
        String emailAddress = "email@address.com";
        User user = new User(username,firstName, lastName, emailAddress);

        user.changeUsername(newUsername);

        assertEquals(newUsername, user.getUserName());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(emailAddress, user.getEmailAddress());
    }

    @Test
    public void ChangeFirstName_Matches() {
        String newFirstName = "bob";
        String username = "username";
        String firstName = "firstName";
        String lastName = "lastName";
        String emailAddress = "email@address.com";
        User user = new User(username,firstName, lastName, emailAddress);

        user.changeFirstName(newFirstName);

        assertEquals(username, user.getUserName());
        assertEquals(newFirstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(emailAddress, user.getEmailAddress());
    }

    @Test
    public void ChangeLastName_Matches() {
        String newLastName = "bob";
        String username = "username";
        String firstName = "firstName";
        String lastName = "lastName";
        String emailAddress = "email@address.com";
        User user = new User(username,firstName, lastName, emailAddress);

        user.changeLastName(newLastName);

        assertEquals(username, user.getUserName());
        assertEquals(firstName, user.getFirstName());
        assertEquals(newLastName, user.getLastName());
        assertEquals(emailAddress, user.getEmailAddress());
    }

    @Test
    public void ChangeEmail_Matches() {
        String newEmail = "bob@bob.com";
        String username = "username";
        String firstName = "firstName";
        String lastName = "lastName";
        String emailAddress = "email@address.com";
        User user = new User(username,firstName, lastName, emailAddress);

        user.changeEmailAddress(newEmail);

        assertEquals(username, user.getUserName());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(newEmail, user.getEmailAddress());
    }
}