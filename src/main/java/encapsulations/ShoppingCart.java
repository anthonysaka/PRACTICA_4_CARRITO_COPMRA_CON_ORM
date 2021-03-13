package encapsulations;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity()
public class ShoppingCart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private Product product;

    @NotNull
    private Integer cant;

    @ManyToOne(fetch = FetchType.EAGER)
    private InvoiceProduct invProduct;

    public ShoppingCart(Product product, Integer cant) {
        this.product = product;
        this.cant = cant;
    }

    public ShoppingCart(Product product, Integer cant, InvoiceProduct invProduct) {
        this.product = product;
        this.cant = cant;
        this.invProduct = invProduct;
    }

    public ShoppingCart() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getCant() {
        return cant;
    }

    public void setCant(Integer cant) {
        this.cant = cant;
    }
}
