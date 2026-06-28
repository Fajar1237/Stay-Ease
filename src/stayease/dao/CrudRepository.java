package stayease.dao;

import java.util.List;

/**
 * CrudRepository<T, ID>
 * ---------------------------------------------------------------------------
 * A generic interface that defines the standard CRUD contract for all DAO
 * classes in the StayEase application.
 *
 * This interface contains three types of methods as required by OOP grading:
 *   1. Abstract Methods  - must be implemented by every class that uses this interface
 *   2. Default Method    - has a built-in implementation; can be overridden
 *   3. Static Method     - called directly via CrudRepository.formatResult(...)
 *
 * @param <T>   the entity / model type  (e.g. Hotel, User, Booking)
 * @param <ID>  the primary key type     (e.g. Integer)
 *
 * Currently implemented by:
 *   HotelDAO implements CrudRepository<Hotel, Integer>
 */
public interface CrudRepository<T, ID> {

    // =========================================================================
    // ABSTRACT METHODS
    // Every class that implements this interface MUST override these methods.
    // =========================================================================

    /**
     * Saves a new entity to the database (CREATE).
     *
     * @param entity the object to be saved
     * @return the generated primary key, or -1 if the operation failed
     */
    int insert(T entity);

    /**
     * Updates an existing entity in the database (UPDATE).
     *
     * @param entity the object with updated data (must already have an ID)
     * @return true if the update was successful
     */
    boolean update(T entity);

    /**
     * Deletes an entity by its primary key (DELETE).
     *
     * @param id the primary key of the entity to delete
     * @return true if the deletion was successful
     */
    boolean delete(ID id);

    /**
     * Retrieves one entity by its primary key (READ by ID).
     *
     * @param id the primary key to search for
     * @return the entity object, or null if not found
     */
    T findById(ID id);

    /**
     * Retrieves all entities from the table (READ all).
     *
     * @return a List of all entities; empty list if the table is empty
     */
    List<T> findAll();

    // =========================================================================
    // DEFAULT METHOD
    // Has a built-in implementation. Implementing classes may override it.
    // =========================================================================

    /**
     * Returns the entity name based on the DAO class name.
     * Example: HotelDAO returns "Hotel", UserDAO returns "User".
     *
     * Override this if the DAO class name does not follow the "EntityNameDAO" pattern.
     *
     * @return the entity name as a String
     */
    default String getEntityName() {
        // Remove the "DAO" suffix from the simple class name
        return this.getClass().getSimpleName().replace("DAO", "");
    }

    // =========================================================================
    // STATIC METHOD
    // Called directly as: CrudRepository.formatResult("Hotel", "INSERT", true)
    // =========================================================================

    /**
     * Formats a CRUD operation result into a consistent log message.
     *
     * Example:
     *   CrudRepository.formatResult("Hotel", "INSERT", true)
     *   returns "[Hotel] INSERT: Success"
     *
     * @param entityName  the name of the entity (e.g. "Hotel")
     * @param operation   the operation name    (e.g. "INSERT", "UPDATE", "DELETE")
     * @param success     true if the operation succeeded
     * @return a formatted result message
     */
    static String formatResult(String entityName, String operation, boolean success) {
        return "[" + entityName + "] "
                + operation.toUpperCase()
                + ": " + (success ? "Success" : "Failed");
    }
}
