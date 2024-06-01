package tacos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
    /* When generating the repository implementation, Spring Data examines each method in the repository interface, parses
    the method name, and attempts to understand the method’s purpose in the context of the persisted object. */
    /* In order for Spring Data to do this, Repository methods are composed of a verb, an optional subject, the word By,
    and a predicate. */
    /* The method name, findByDeliveryZip(), makes it clear that this method should find all TacoOrder entities by matching
    their deliveryZip property with the value passed in as a parameter to the method. */
    List<TacoOrder> findByDeliveryZip(String deliveryZip);

    /* Suppose that you need to query for all orders delivered to a given ZIP code within a given date range.
    The following method should prove useful: */
    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);

    /* In addition to an implicit Equals operation and the Between operation, Spring Data method signatures can also
    include any of the following operators:

    - IsAfter, After, IsGreaterThan, GreaterThan
    - IsGreaterThanEqual, GreaterThanEqual
    - IsBefore, Before, IsLessThan, LessThan
    - IsLessThanEqual, LessThanEqual
    - IsBetween, Between
    - IsNull, Null
    - IsNotNull, NotNull
    - IsIn, In
    - IsNotIn, NotIn
    - IsStartingWith, StartingWith, StartsWith
    - IsEndingWith, EndingWith, EndsWith
    - IsContaining, Containing, Contains
    - IsLike, Like
    - IsNotLike, NotLike
    - IsTrue, True
    - IsFalse, False
    - Is, Equals
    - IsNot, Not
    - IgnoringCase, IgnoresCase

    As alternatives for IgnoringCase and IgnoresCase, you can place either AllIgnoring- Case or AllIgnoresCase on the
    method to ignore case for all String comparisons.

    Finally, you can also place OrderBy at the end of the method name to sort the results by a specified column. */

    /* In case you don't want to do this (e.g. coz the naming is too complex), you can name the method anything you
    want and annotate it with @Query to explicitly specify the query to be performed when the method is called, e.g.: */
    @Query("select deliveryName from TacoOrder where deliveryCity = 'Seattle'")
    List<TacoOrder> readOrdersDeliveredInSeattle();
}
