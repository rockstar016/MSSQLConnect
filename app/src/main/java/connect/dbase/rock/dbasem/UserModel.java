package connect.dbase.rock.dbasem;

/**
 * Created by RockStar-0116 on 2016.07.17.
 */
public class UserModel {
    private String Title,First_Name,Last_Name,Address,Address_2,Suburb, State,Post_Code, Email, Phone, Work, Mobile, Gender, Birthday,  Referred_By;
    private int SMS_Appointment_Reminders, SMS_Marketing, Mail;
    private int clishop, cliid;

    public int getClishop() {
        return clishop;
    }

    public void setClishop(int clishop) {
        this.clishop = clishop;
    }

    public int getCliid() {
        return cliid;
    }

    public void setCliid(int cliid) {
        this.cliid = cliid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String last_Name) {
        Last_Name = last_Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAddress_2() {
        return Address_2;
    }

    public void setAddress_2(String address_2) {
        Address_2 = address_2;
    }

    public String getSuburb() {
        return Suburb;
    }

    public void setSuburb(String suburb) {
        Suburb = suburb;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPost_Code() {
        return Post_Code;
    }

    public void setPost_Code(String post_Code) {
        Post_Code = post_Code;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getWork() {
        return Work;
    }

    public void setWork(String work) {
        Work = work;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getReferred_By() {
        return Referred_By;
    }

    public void setReferred_By(String referred_By) {
        Referred_By = referred_By;
    }

    public int getSMS_Appointment_Reminders() {
        return SMS_Appointment_Reminders;
    }

    public void setSMS_Appointment_Reminders(int SMS_Appointment_Reminders) {
        this.SMS_Appointment_Reminders = SMS_Appointment_Reminders;
    }

    public int getSMS_Marketing() {
        return SMS_Marketing;
    }

    public void setSMS_Marketing(int SMS_Marketing) {
        this.SMS_Marketing = SMS_Marketing;
    }

    public int getMail() {
        return Mail;
    }

    public void setMail(int mail) {
        Mail = mail;
    }
}
