package dataAccess;


import java.util.ArrayList;

import java.util.Date;

import java.util.List;

import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import domain.UtilDate;
import domain.Driver;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class HibernateDataAccess {
    private EntityManager db;
    private EntityManagerFactory emf;

    public HibernateDataAccess(EntityManagerFactory emf) {
        this.emf = emf;
        if (emf != null) {
            this.db = emf.createEntityManager();
        }
    }
    public void open() {
        if (db == null || !db.isOpen()) {
            if (emf != null) {
                db = emf.createEntityManager();
            } else {
                System.err.println("¡Error grave! 'emf' es null dentro de HibernateDataAccess.open()");
            }
        }
    }
    
    public List<String> getDepartCities() {
        TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.from FROM Ride r ORDER BY r.from", String.class);
        return query.getResultList();
    }

    public List<String> getArrivalCities(String from) {
        TypedQuery<String> query = db.createQuery("SELECT DISTINCT r.to FROM Ride r WHERE r.from=?1 ORDER BY r.to", String.class);
        query.setParameter(1, from);
        return query.getResultList();
    }

    public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
        if (new Date().compareTo(date) > 0) {
            throw new RideMustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.ErrorRideMustBeLaterThanToday"));
        }
        try {
            db.getTransaction().begin();
            Driver driver = db.find(Driver.class, driverEmail);
            if (driver.doesRideExists(from, to, date)) {
                db.getTransaction().commit();
                throw new RideAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
            }
            
            Ride ride = driver.addRide(from, to, date, nPlaces, price);
            db.persist(ride);
            db.persist(driver); 
            db.getTransaction().commit();
            return ride;
           
        } catch (NullPointerException e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            return null;
        } catch (Exception e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            throw e;
        }
    }
    
    public List<Ride> getRides(String from, String to, Date date) {
        TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date=?3", Ride.class);   
        query.setParameter(1, from);
        query.setParameter(2, to);
        query.setParameter(3, date);
        return query.getResultList();
    }
    
    public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
        Date firstDayMonthDate = UtilDate.firstDayMonth(date);
        Date lastDayMonthDate = UtilDate.lastDayMonth(date);
                
        TypedQuery<Date> query = db.createQuery("SELECT DISTINCT r.date FROM Ride r WHERE r.from=?1 AND r.to=?2 AND r.date BETWEEN ?3 and ?4", Date.class);   
        query.setParameter(1, from);
        query.setParameter(2, to);
        query.setParameter(3, firstDayMonthDate);
        query.setParameter(4, lastDayMonthDate);
        return query.getResultList();
    }

    public boolean checkLogin(String email, String password) {
        Driver driver = db.find(Driver.class, email);
        if (driver != null && driver.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public void registerDriver(String email, String password) throws Exception {
        Driver existing = db.find(Driver.class, email);
        if (existing != null) {
            throw new Exception("Erabiltzailea lehendik existitzen da");
        }
        try {
            db.getTransaction().begin();
            Driver newDriver = new Driver(email, password);
            db.persist(newDriver);
            db.getTransaction().commit();
        } catch (Exception e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            throw e;
        }
    }



    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}