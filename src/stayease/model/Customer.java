package stayease.model;

/**
 * Customer  —  Subclass of Person
 * ---------------------------------------------------------------------------
 * Represents a regular hotel guest in the StayEase system.
 * A Customer object is created in LoginFrame when login succeeds and the
 * user's role is "user".
 *
 * OOP concepts demonstrated by this class:
 *
 *   Inheritance  →  extends Person
 *                   Customer inherits id, username, noTelepon and getInfo() from Person.
 *
 *   super()      →  both constructors call super(...) to initialise
 *                   the fields defined in the superclass Person.
 *
 *   Overriding   →  @Override getRole()   — provides the "user" role value
 *                   @Override getInfo()   — extends Person.getInfo() via super.getInfo()
 *
 *   instanceof   →  used in LoginFrame:
 *                   else if (person instanceof Customer) { Customer c = (Customer) person; }
 *
 *   Casting      →  Customer customer = (Customer) person;
 */
public class Customer extends Person {

    // Customer-specific field — not present in the superclass Person
    private String memberType;   // "regular" by default; extensible to "premium"

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    /**
     * No-argument constructor.
     * Calls super() to invoke Person's no-arg constructor, then sets memberType.
     */
    public Customer() {
        super();                        // calls Person()
        this.memberType = "regular";
    }

    /**
     * Full constructor — receives data from the logged-in User object.
     * Calls super(id, username, noTelepon) to pass the common fields to Person.
     *
     * @param id         the user_id from the users table
     * @param username   the customer's username
     * @param noTelepon  the customer's phone number
     */
    public Customer(int id, String username, String noTelepon) {
        super(id, username, noTelepon); // calls Person(int, String, String)
        this.memberType = "regular";
    }

    // =========================================================================
    // OVERRIDING  —  implements the abstract method declared in Person
    // =========================================================================

    /**
     * Returns the role "user".
     * This is the required implementation of the abstract method in Person.
     *
     * @return "user"
     */
    @Override
    public String getRole() {
        return "user";
    }

    // =========================================================================
    // OVERRIDING  —  extends the concrete method from Person using super.getInfo()
    // =========================================================================

    /**
     * Returns extended information about this customer.
     * Calls super.getInfo() to get the base output from Person,
     * then appends the customer-specific memberType field.
     *
     * Example output:
     *   "ID: 2 | Username: john | Role: user | Member: regular"
     *
     * @return a descriptive String about this customer
     */
    @Override
    public String getInfo() {
        return super.getInfo() + " | Member: " + memberType;
        //     ↑ super.getInfo() calls Person.getInfo() to get the base string
    }

    // =========================================================================
    // GETTER / SETTER
    // =========================================================================

    public String getMemberType()            { return memberType; }
    public void   setMemberType(String type) { this.memberType = type; }
}
