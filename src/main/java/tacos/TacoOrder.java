package tacos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
/* @Table annotation is completely optional. By default, the object is mapped to a table based on the domain class name.
In this case, TacoOrder is mapped to a table named "Taco_Order". If that’s fine, you can leave the @Table annotation
out completely, or use it without parameters. But we can map it to a different table name, by specifying the table name
as a parameter to @Table, like @Table("taco-order") instead */
@Table
public class TacoOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;

    /* All other properties in TacoOrder will be mapped automatically to columns based on their property names
    e.g. deliveryName will be mapped to a column called delivery_name.
    HOWEVER, if you wanted to explicitly define the column name mapping, you would annotate the property with e.g.

    @Column("customer-delivery-name")
    private String deliveryName;
    */
    private Date placedAt;

    // Delivery Information

    @NotBlank(message = "Delivery name is required")
    private String deliveryName;
    @NotBlank(message = "Street is required")
    private String deliveryStreet;
    @NotBlank(message = "City is required")
    private String deliveryCity;
    @NotBlank(message = "State is required")
    private String deliveryState;
    @NotBlank(message = "Zip code is required")
    private String deliveryZip;

    // Payment Information
    @CreditCardNumber(message = "Not a valid Credit Card number") // ensures credit card number passes the Luhn algorithm check
    private String ccNumber;
    @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$",
             message="Must be formatted MM/YY")
    private String ccExpiration;
    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    private List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
