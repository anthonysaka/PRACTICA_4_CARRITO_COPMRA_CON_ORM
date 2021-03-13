package services;

import encapsulations.Comment;
import encapsulations.Foto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class CommentServices extends ORMDBManage<Comment>{

    private static CommentServices instancia;

    private CommentServices() { super(Comment.class); }

    public static CommentServices getInstancia(){
        if(instancia==null){
            instancia = new CommentServices();
        }

        return instancia;
    }

    /**/

    public List<Comment> getListComments(Long id){
        EntityManager em = CommentServices.getInstancia().getEntityManager();
        String queryString = "SELECT c FROM Comment c " +
                "WHERE c.product.id = :id";
        Query query = em.createQuery(queryString);
        query.setParameter("id", id);

        List<Comment> aux  = query.getResultList();
        return aux;
    }
}
