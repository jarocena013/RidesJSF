package bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import domain.Ride;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("queryRideBean")          
@SessionScoped
public class QueryRidesBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String departingCity; 
    private String arrivalCity;
    private Date rideDate;        
    private List<Ride> results;   

    @PostConstruct
    public void init() {
       
    }

    public void searchRides() {   
        if (FacadeBean.getBusinessLogic() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "DB not available."));
            return;
        }
        this.results = FacadeBean.getBusinessLogic().getRides(departingCity, arrivalCity, rideDate);

        if (this.results == null || this.results.isEmpty()) {
            // SOLUCIÓN: Eliminamos el ResourceBundle y usamos un texto fijo sin dependencias externas
            String msgBody = "Ez dago bidaiarik baldintza horiekin"; 
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, null, msgBody));
        }
    }

    // Getters y Setters
    public String getDepartingCity() { return departingCity; }
    public void setDepartingCity(String departingCity) { this.departingCity = departingCity; }

    public String getArrivalCity() { return arrivalCity; }
    public void setArrivalCity(String arrivalCity) { this.arrivalCity = arrivalCity; }

    public Date getRideDate() { return rideDate; }
    public void setRideDate(Date rideDate) { this.rideDate = rideDate; }

    public List<Ride> getResults() { return results; }
    public void setResults(List<Ride> results) { this.results = results; }
}  
