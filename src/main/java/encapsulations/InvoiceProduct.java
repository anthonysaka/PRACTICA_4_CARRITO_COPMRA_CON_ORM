package encapsulations;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity()
public class InvoiceProduct implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String clientName;

    @OneToMany(mappedBy = "invProduct", fetch = FetchType.EAGER)
    private List<ShoppingCart> listProduct = new ArrayList<>();

    @NotNull
    private Date date;
    @NotNull
    private Float totalPrice;

    /* Constructor */

    public InvoiceProduct(String clientName, List<ShoppingCart> listProduct, Date date, Float totalPrice) {
        this.clientName = clientName;
        this.listProduct = listProduct;
        this.date = date;
        this.totalPrice = totalPrice;
    }

    public InvoiceProduct() {

    }

    /* Gets and Sets */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<ShoppingCart> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ShoppingCart> listProduct) {
        this.listProduct = listProduct;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    /* Methods */

    public void addProduct(List<ShoppingCart> p) {
        this.listProduct = (ArrayList<ShoppingCart>) p;
    }


}

