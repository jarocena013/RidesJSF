package bean;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.HibernateDataAccess;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FacadeBean {
    
    private static BLFacade facadeInterface = null;

    private FacadeBean() {
        // Constructor privado para el patrón Singleton
    }

    public static BLFacade getBusinessLogic() {
        if (facadeInterface == null) {
            try {
                System.out.println("--- INICIALIZANDO PERSISTENCIA HIBERNATE (iraunkortasuna) ---");
                
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("iraunkortasuna");
                
                HibernateDataAccess hda = new HibernateDataAccess(emf);
                
                facadeInterface = new BLFacadeImplementation(hda);
                
                System.out.println("--- ¡BLFACADE E HIBERNATE LEVANTADOS CON ÉXITO! ---");
            } catch (Exception e) {
                System.err.println("Error crítico en FacadeBean al inicializar el contexto:");
                e.printStackTrace();
            }
        }
        return facadeInterface;
    }
}