package services;

import encapsulations.Product;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ProductServices extends ORMDBManage<Product>{

    private static ProductServices instancia;

    private ProductServices() {
        super(Product.class);
    }

    public static ProductServices getInstancia(){
        if(instancia==null){
            instancia = new ProductServices();
        }

        return instancia;
    }

    /**/

    public List<Product> getListProductByPage(String page){
        EntityManager em = getEntityManager();

        int pageSize = 10;
        String countQ = "Select count (p.id) from Product p";
        Query countQuery = em.createQuery(countQ);
        Long countResults = (Long) countQuery.getSingleResult();

        int PageTotalNumber = (int) (Math.ceil(countResults / pageSize));
        int lastPageNumber = Integer.parseInt(page);

        Query selectQuery = em.createQuery("From Product");
        selectQuery.setFirstResult((lastPageNumber - 1) * pageSize);
        selectQuery.setMaxResults(pageSize);

        List<Product> list = selectQuery.getResultList();

        return  list;


    }
}
