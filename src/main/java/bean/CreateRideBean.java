package bean;

import java.io.Serializable;

import java.util.Date;
import java.util.ResourceBundle;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import businessLogic.BLFacade;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("createRideBean")
@SessionScoped
public class CreateRideBean implements Serializable {
	private static final long serialVersionUID = 1;

	private String departCity;
	private String arrivalCity;
	private int seats;
	private double price;
	private Date rideDate;

	public String getDepartCity() { return departCity; }
	public void setDepartCity(String departCity) { this.departCity = departCity; }

	public String getArrivalCity() { return arrivalCity; }
	public void setArrivalCity(String arrivalCity) { this.arrivalCity = arrivalCity; }

	public int getSeats() { return seats; }
	public void setSeats(int seats) { this.seats = seats; }

	public double getPrice() { return price; }
	public void setPrice(double price) { this.price = price; }

	public Date getRideDate() { return rideDate; }
	public void setRideDate(Date rideDate) { this.rideDate = rideDate; }

	public void create(String email) {
		
		try {
			 FacadeBean.getBusinessLogic().createRide(departCity, arrivalCity, rideDate, seats, (float)price,
					email);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Ride created succesfully", null));
		} catch (RideAlreadyExistException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ride already exists", null));
		} catch (RideMustBeLaterThanTodayException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ride must be later than today", null));
		}
	}


}