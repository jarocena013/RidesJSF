package bean;

import java.io.Serializable;
import java.util.ResourceBundle;

import businessLogic.BLFacade;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String email;
	private String password;

	public String register() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		String msg;
		
		if (context.isValidationFailed()) {
			return null;
		}

		try {
			
			FacadeBean.getBusinessLogic().registerDriver(email, password);
			
			
			msg = "Ongi erregistratuta"; 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", msg));
			return "home"; 
			
		} catch (Exception e) {
			msg = e.getMessage(); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
			return null; 
		}
	}
	
	public String login() {
	    FacesContext context = FacesContext.getCurrentInstance();
	   
	    boolean loginOk = FacadeBean.getBusinessLogic().checkLogin(email, password);
	    
	    if (!loginOk) {
	        String msg = "Errorea login egitean";
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
	        return null; 
	    } else {
	       
	        this.password = null; 
	        
	        String msg = "Login ongi burutua";
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", msg));
	        
	        return "Menua"; 
	    }
	}
	public String logout() {
	    FacesContext context = FacesContext.getCurrentInstance();
	   
	    context.getExternalContext().invalidateSession();
	    
	    String msg = "Saioa ongi itxi da / Sesión cerrada con éxito";
	    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", msg));
	    
	    return "home";
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}