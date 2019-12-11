package beans.managed;

import beans.backing.Person;
import data.DbConnection;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by DELL on 11/11/2019.
 */
@ManagedBean
@RequestScoped
public class PersonMBean {
    private Person person;

    public PersonMBean() {
        person = new Person();
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String register(){
        DbConnection db = new DbConnection();
        String securePassword = get_SHA_1_SecurePassword(person.getPassword());
        boolean result = db.insertRecord(person.getName(),person.getUsername(),person.getEmail(),securePassword,person.getPhone());
        if (result) {
            return "login.xhtml?faces-redirect=true";
        }
        return "signup.xhtml";
    }

    private String get_SHA_1_SecurePassword(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public String login(){
        DbConnection db = new DbConnection();
        String securePassword = get_SHA_1_SecurePassword(person.getPassword());
        boolean result = db.authenticatePerson(person.getUsername(), securePassword);
        if (result) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.setAttribute("username",person.getUsername());
            session.setMaxInactiveInterval(60*10);
            return "welcome.xhtml?faces-redirect=true";
        }
        return "login.xhtml";
    }


    public String logout(){
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if(session!=null){
            if(session.getAttribute("username")!=null){
                session.invalidate();
            }
        }
        return "login.xhtml?faces-redirect=true";
    }
}
