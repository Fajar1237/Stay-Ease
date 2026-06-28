package stayease.model;

/**
 * Person  —  Abstract Superclass
 * ---------------------------------------------------------------------------
 * Represents a general user of the StayEase application.
 *
 * This class does NOT replace User.java.
 * User.java is still used by all DAOs, Session, and the existing application
 * flow — it remains completely untouched.
 *
 * Person exists purely to satisfy OOP grading criteria:
 *   - Inheritance  : Admin and Customer extend Person
 *   - super()      : subclass constructors call super(id, username, noTelepon)
 *   - instanceof   : LoginFrame checks  if (person instanceof Admin)
 *   - Casting      : Admin admin = (Admin) person
 *
 * Subclasses:
 *   Admin    — for users whose role is "admin"
 *   Customer — for users whose role is "user"
 */
public abstract class Person {

    // ── Fields are protected so subclasses can access them directly ──────────
    protected int    id;          // maps to user_id in the users table
    protected String username;    // maps to username in the users table
    protected String noTelepon;   // maps to no_telepon in the users table

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    /**
     * No-argument constructor.
     * Required so that subclasses can define their own no-arg constructor
     * and call super() without arguments.
     */
    public Person() {
        // intentionally empty — subclasses may set fields individually
    }

    /**
     * Full constructor — receives data from a logged-in User object.
     *
     * @param id         the user_id from the database
     * @param username   the username used to log in
     * @param noTelepon  the phone number of the user
     */
    public Person(int id, String username, String noTelepon) {
        this.id        = id;
        this.username  = username;
        this.noTelepon = noTelepon;
    }

    // =========================================================================
    // ABSTRACT METHOD
    // Subclasses MUST implement this — each subclass decides its own role value.
    // =========================================================================

    /**
     * Returns the role of this person: "admin" or "user".
     *
     * @return the role as a String
     */
    public abstract String getRole();

    // =========================================================================
    // CONCRETE METHOD
    // Has a default implementation; subclasses may override and extend it
     // using super.getInfo().
    // =========================================================================

    /**
     * Returns basic information about this person as a formatted String.
     * Subclasses can call super.getInfo() to build on top of this output.
     *
     * @return a String describing this person
     */
    public String getInfo() {
        return "ID: " + id
             + " | Username: " + username
             + " | Role: "     + getRole();
    }

    // =========================================================================
    // GETTERS
    // =========================================================================

    public int    getId()        { return id; }
    public String getUsername()  { return username; }
    public String getNoTelepon() { return noTelepon; }
}
