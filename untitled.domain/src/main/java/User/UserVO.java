package User;

import java.util.Objects;

public class UserVO {
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;

    protected UserVO(String username, String firstName, String lastName, String emailAddress) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    protected String getUserName(){
        return username;
    }

    protected String getFirstName(){
        return firstName;
    }

    protected String getLastName(){
        return lastName;
    }

    protected String getEmailAddress(){
        return emailAddress;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof UserVO other)) {
            return false;
        }

        if((username == other.username)
            && (firstName == other.firstName)
            && (lastName == other.lastName)
            && (emailAddress == other.emailAddress)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(username, firstName, lastName, emailAddress);
    }
}
