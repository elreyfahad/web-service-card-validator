/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import entity.Creditcard;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author fahad
 */
@Stateless
@Path("entity.creditcard")
public class CreditcardFacadeREST extends AbstractFacade<Creditcard> {

    @PersistenceContext(unitName = "ProjectWebServiceRESTCardValidatorPU")
    private EntityManager em;

    public CreditcardFacadeREST() {
        super(Creditcard.class);
    }

    @Path("/validate")
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Boolean validateCreditCard(JAXBElement<Creditcard> crediJAXBElement){
        Creditcard creditcard=crediJAXBElement.getValue();
       
        if(isNumberValide(creditcard.getNumber(),
                creditcard.getControlnumber())&& isTypeValide(creditcard.getType()))
        {
            
            super.create(creditcard);//insertion de la carte valide dans la base de donnees
            return true;
            
        }
        else{
            return false;
        }
        
        
    }
   
    @POST
    @Path("/create")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(JAXBElement<Creditcard> crediJAXBElement) {
        Creditcard creditcard=crediJAXBElement.getValue();
        super.create(creditcard);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Creditcard entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Creditcard find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Path("findall")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Creditcard> findAll() {
        return super.findAll();
    }

    

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
    private Boolean isDateValide(Date date){
     
        Date today=Date.valueOf(LocalDate.MAX);
        
        if(date.after(date)){
            return true;
            
        }
        return false;
    }
    
    private Boolean isNumberValide(String number,int controleNumber ){
       
       if(String.valueOf(controleNumber).length()!=3){
           return false;
       }
       Pattern pattern=Pattern.compile("[0-9]{6}"+String.valueOf(controleNumber));
       Matcher matcher=pattern.matcher(number);
        
        return matcher.find();
    }
    
    private Boolean isTypeValide(String type){
        
        if(type.equalsIgnoreCase("visa") || type.equalsIgnoreCase("mastercards")){
            
            return true;
            
        }
        return false;
    }
    
}
