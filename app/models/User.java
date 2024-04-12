package models;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.regex.*;

@Entity
@Table(name = "users")
public class User {


    private static final String[] VALID_COUNTRY_CODES = {"+91", "+93", "+355", "+213", "+376", "+672", "+1-242", "+1-264", "+1-684", "+61", "+994", "+973", "+375" };
    @Id
    //@TableGenerator(name = "empid", table = "empktb", pkColumnName = "empkey", pkColumnValue = "empvalue")
    //@GeneratedValue(strategy = GenerationType.TABLE, generator = "empid")
    //@GeneratedValue(generator = "emp", strategy = GenerationType.AUTO)
    //@SequenceGenerator(name="emp", sequenceName="emp", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "integer")
    private long id;

    @NotNull(message = "Name is required")
    @Pattern(regexp = "[a-zA-Z ]+", message = "Name must contain only letters and spaces")
    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email address")
    @Column(name = "name", columnDefinition = "varchar", unique = true)
    private String email;

    @NotNull(message = "Password is required")
    @Column(name = "password", columnDefinition = "varchar")
    private String password;

    @NotNull(message = "Mobile number is required")
    //@Pattern(regexp = "[0-9]{10}", message = "Contact must contain 10 digits")
    //@Pattern(regexp = "\\+\\d{2} \\d{1,15}", message = "Mobile number format is invalid. It should start with a '+' followed by a valid country code and then a space and 1 to 15 digits.")
    @Column(name = "mobile_no", columnDefinition = "varchar")
    private String mobileNo;

    @Column(name = "address", columnDefinition = "varchar")
    private String address;

    @NotNull(message = "Role is required")
    @Pattern(regexp = "^(ADMIN|CUSTOMER)$", message = "Role must be either 'ADMIN' or 'CUSTOMER'")
    @Column(name = "role", columnDefinition = "varchar")
    private String role;

    // Constructors, getters, and setters

    public User() {
    }

    public User(long id, String name, String email, String password, String mobile_no, String address, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNo = mobile_no;
        this.address = address;
        this.role = role;
    }

    // Getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobile_no) {
        this.mobileNo = mobile_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
   public String validateMobileNumber() {
        if (mobileNo == null || !isValidMobileNumberFormat(mobileNo)) {
            return "Mobile number format is invalid. It should start with a '+' followed by a valid country code and then a space and 1 to 15 digits.";
        }
        return null; // No validation error
    }

    private boolean isValidMobileNumberFormat(String mobileNo) {
        // Check if mobile number starts with a valid country code
        for (String validCode : VALID_COUNTRY_CODES) {
            if (mobileNo.startsWith(validCode + " ")) {
                // Check if the remaining part is a sequence of digits
                String remainingDigits = mobileNo.substring(validCode.length() + 1); // Skip country code and space
                if (remainingDigits.matches("\\d+")) {
                    return true;
                }
            }
        }
        return false;
    }


    // Equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

}
