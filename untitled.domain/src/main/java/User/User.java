package User;

public class User {
    private UserVO valueObject;

    public User(String username, String firstName, String lastName, String emailAddress) {
        valueObject = new UserVO(username, firstName, lastName, emailAddress);
    }

    public String getUserName() {
        return valueObject.getUserName();
    }

    public String getFirstName() {
        return valueObject.getFirstName();
    }

    public String getLastName() {
        return valueObject.getLastName();
    }

    public String getEmailAddress() {
        return valueObject.getEmailAddress();
    }

    public void changeUsername(String newUsername) {
        valueObject = new UserVO(newUsername, valueObject.getFirstName(), valueObject.getLastName(), valueObject.getEmailAddress());
    }

    public void changeFirstName(String newFirstName) {
        valueObject = new UserVO(valueObject.getUserName(), newFirstName, valueObject.getLastName(), valueObject.getEmailAddress());
    }

    public void changeLastName(String newLastName) {
        valueObject = new UserVO(valueObject.getUserName(), valueObject.getFirstName(), newLastName, valueObject.getEmailAddress());
    }

    public void changeEmailAddress(String newEmailAddress) {
        valueObject = new UserVO(valueObject.getUserName(), valueObject.getFirstName(), valueObject.getLastName(), newEmailAddress);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User user)){
            return false;
        }
        return valueObject.equals(user.valueObject);
    }

    @Override
    public int hashCode() {
        return valueObject.hashCode();
    }
}
