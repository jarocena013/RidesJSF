package businessLogic;
import java.io.Serializable;
import java.util.Date;

import java.util.List;
import java.util.ResourceBundle;





import dataAccess.HibernateDataAccess;
import domain.Ride;
import domain.Driver;
import exceptions.RideMustBeLaterThanTodayException;
import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */

public class BLFacadeImplementation implements BLFacade, Serializable {
    private static final long serialVersionUID = 1L;
	HibernateDataAccess dbManager;

	
    public BLFacadeImplementation(HibernateDataAccess hibernateDataAccess)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		
		
		dbManager=hibernateDataAccess;		
	}
    
    
    /**
     * {@inheritDoc}
     */
     public List<String> getDepartCities(){
    	dbManager.open();	
		
		 List<String> departLocations=dbManager.getDepartCities();		

		dbManager.close();
		
		return departLocations;
    	
    }
    /**
     * {@inheritDoc}
     */
	 public List<String> getDestinationCities(String from){
		dbManager.open();	
		
		 List<String> targetCities=dbManager.getArrivalCities(from);		

		dbManager.close();
		
		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */
   
	 public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws RideMustBeLaterThanTodayException, RideAlreadyExistException {
		    dbManager.open();
		    Ride ride = dbManager.createRide(from, to, date, nPlaces, price, driverEmail);		
		    dbManager.close();
		    return ride;
		}
	
   /**
    * {@inheritDoc}
    */
	 
	public List<Ride> getRides(String from, String to, Date date){
		dbManager.open();
		List<Ride>  rides=dbManager.getRides(from, to, date);
		dbManager.close();
		return rides;
	}

    
	/**
	 * {@inheritDoc}
	 */
	 
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date){
		dbManager.open();
		List<Date>  dates=dbManager.getThisMonthDatesWithRides(from, to, date);
		dbManager.close();
		return dates;
	}
	
	

	 /**
	     * {@inheritDoc}
	     */
	    @Override
	    public boolean checkLogin(String email, String password) {
	        dbManager.open();
	        boolean loginOk = dbManager.checkLogin(email, password);
	        dbManager.close();
	        return loginOk;
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void registerDriver(String email, String password) throws Exception {
	        dbManager.open();
	        try {
	            dbManager.registerDriver(email, password);
	        } finally {
	            dbManager.close();
	        }
	    }
}

