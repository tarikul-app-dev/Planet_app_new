package planet.it.limited.planetapp.model;

/**
 * Created by Tarikul on 6/7/2018.
 */

public class ContactModel {
    public ContactModel(String userName, String contactNumber) {
        this.userName = userName;
        this.contactNumber = contactNumber;

    }
    public ContactModel( ) {
    }

    public String userName;
    public boolean checked;
    public String contactNumber;


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



    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


}
