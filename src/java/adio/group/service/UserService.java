package adio.group.service;

import adio.group.controller.UsersBean;
import adio.group.model.Users;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author LEOGOLD
 */
@Service
@Transactional
public class UserService {
    
    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_USER = "user";
    
    @Autowired
    private UsersBean userBean;
    @Autowired 
    private SessionFactory sessionFactory;
    
    private Users user;
    private List<Users> userList;


    HttpSession httpSession;
    private Session session; 
    private Query query;
    int quryResult = 0;
    int role = 0;
    public String save(){
        if (!userBean.getFirstName().isEmpty() && !userBean.getLastName().isEmpty() && !userBean.getPhoneNumber().isEmpty()) {

            user = new Users(
                    userBean.getFirstName(),
                    userBean.getLastName(),
                    userBean.getEmail(),
                    userBean.getPhoneNumber(),
                    userBean.getCoverLetter(),
                    userBean.getResumePath(),
                    userBean.getPassportPath(),
                    ROLE_USER
            );

            session = sessionFactory.getCurrentSession();
            session.save(user);
            return "login";
        } else {
            userBean.setMessage("All fields must be filled");
            return "user_form";
        }
    }
    
    
    public List<Users> viewUser() {

        try {

            session = sessionFactory.getCurrentSession();
            query = session.createQuery("FROM Users");
            userList = query.list();

        } catch (NullPointerException npex) {
            System.err.println("Can not perform " + npex.getMessage());
        }

            return userList;
        
    }
    
    public int totalApplicant(){
        
       return (int) session.createCriteria("Users")
                  .setProjection(Projections.rowCount())
                  .uniqueResult();
    }
}
