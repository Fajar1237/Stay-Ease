package stayease.model;

/**
 * Admin  —  Subclass of Person
 * ---------------------------------------------------------------------------
 * Represents a user with administrator privileges in the StayEase system.
 * An Admin object is created in LoginFrame when login succeeds and the
 * user's role is "admin".
 *
 * OOP concepts demonstrated by this class:
 *
 *   Inheritance  →  extends Person
 *                   Admin inherits id, username, noTelepon and getInfo() from Person.
 *
 *   super()      →  both constructors call super(...) to initialise
 *                   the fields defined in the superclass Person.
 *
 *   Overriding   →  @Override getRole()   — provides the "admin" role value
 *                   @Override getInfo()   — extends Person.getInfo() via super.getInfo()
 *
 *   instanceof   →  used in LoginFrame:
 *                   if (person instanceof Admin) { Admin admin = (Admin) person; }
 *
 *   Casting      →  Admin admin = (Admin) person;
 */
public class Admin extends Person {

    // Admin-specific field — not present in the superclass Person
    private String adminLevel;   // "standard" by default; can be changed to "super"

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    /**
     * No-argument constructor.
     * Calls super() to invoke Person's no-arg constructor, then sets adminLevel.
     */
    public Admin() {
        super();                        // calls Person()
        this.adminLevel = "standard";
    }

    /**
     * Full constructor — receives data from the logged-in User object.
     * Calls super(id, username, noTelepon) to pass the common fields to Person.
     *
     * @param id         the user_id from the users table
     * @param username   the admin's username
     * @param noTelepon  the admin's phone number
     */
    public Admin(int id, String username, String noTelepon) {
        super(id, username, noTelepon); // calls Person(int, String, String)
        this.adminLevel = "standard";
    }

    // =========================================================================
    // OVERRIDING  —  implements the abstract method declared in Person
    // =========================================================================

    /**
     * Returns the role "admin".
     * This is the required implementation of the abstract method in Person.
     *
     * @return "admin"
     */
    @Override
    public String getRole() {
        return "admin";
    }

    // =========================================================================
    // OVERRIDING  —  extends the concrete method from Person using super.getInfo()
    // =========================================================================

    /**
     * Returns extended information about this admin.
     * Calls super.getInfo() to get the base output from Person,
     * then appends the admin-specific adminLevel field.
     *
     * Example output:
     *   "ID: 1 | Username: admin | Role: admin | Level: standard"
     *
     * @return a descriptive String about this admin
     */
    @Override
    public String getInfo() {
        return super.getInfo() + " | Level: " + adminLevel;
        //     ↑ super.getInfo() calls Person.getInfo() to get the base string
    }

    // =========================================================================
    // GETTER / SETTER
    // =========================================================================

    public String getAdminLevel()             { return adminLevel; }
    public void   setAdminLevel(String level) { this.adminLevel = level; }
}
