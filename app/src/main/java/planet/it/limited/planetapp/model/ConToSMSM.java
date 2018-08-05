package planet.it.limited.planetapp.model;

/**
 * Created by Tarikul on 8/2/2018.
 */

public class ConToSMSM {
    public ConToSMSM(String userName, String contactNumber,boolean isChecked) {
        this.userName = userName;
        this.contactNumber = contactNumber;
        this.checked = isChecked;

    }
    public ConToSMSM( ) {
    }
    public String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String contactNumber;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean checked;


}
