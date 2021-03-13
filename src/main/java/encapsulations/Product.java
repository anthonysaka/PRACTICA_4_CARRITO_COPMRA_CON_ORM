package encapsulations;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity()
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @Lob
    private List<Foto> fotos = new ArrayList<>();




    //Void constructor
    public Product() {

    }

    public Product( String name, Double price, String description /*ArrayList<Foto> fotos*/) {
        this.name = name;
        this.price = price;
        this.description = description;
       // this.fotos = fotos;
    }

    public Product( Long id, String name, Double price, String description /*ArrayList<Foto> fotos*/) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        // this.fotos = fotos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }
}

