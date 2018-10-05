package mx.com.core.entity;

import mx.com.core.db.ParamType;
import mx.com.core.db.Parameter;
import mx.com.core.db.StoredProcedure;

@StoredProcedure(name = "USERSLIS")
public class UsuarioLis {
    @Parameter(name = "AUTO_ID", type = ParamType.INT)
    private int autoID;
    @Parameter(name = "USER_ID", type = ParamType.INT)
    private int userID;
    @Parameter(name = "USER_PASS", type = ParamType.VARCHAR)
    private String userPass;
    @Parameter(name = "FIRST_NAME", type = ParamType.VARCHAR)
    private String firstName;
    @Parameter(name = "LAST_NAME", type = ParamType.VARCHAR)
    private String lastName;


    public int getAutoID() {
        return autoID;
    }

    public void setAutoID(int autoID) {
        this.autoID = autoID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
