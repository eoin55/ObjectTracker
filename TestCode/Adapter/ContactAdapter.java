package Adapter;

public class ContactAdapter implements Contact{
    private Chovnatlh contact;
    
    public ContactAdapter(){
        contact = new ChovnatlhImpl();
    }
    public ContactAdapter(Chovnatlh newContact){
        contact = newContact;
    }
    
    public String getFirstName(){
        return contact.tlhapWa$DIchPong();
    }
    public String getLastName(){
        return contact.tlhapQavPong();
    }
    public String getTitle(){
        return contact.tlhapPatlh();
    }
    public String getOrganization(){
        return contact.tlhapGhom();
    }
    
    public void setContact(Chovnatlh newContact){
        contact = newContact;
    }
    public void setFirstName(String newFirstName){
        contact.cherWa$DIchPong(newFirstName);
    }
    public void setLastName(String newLastName){
        contact.cherQavPong(newLastName);
    }
    public void setTitle(String newTitle){
        contact.cherPatlh(newTitle);
    }
    public void setOrganization(String newOrganization){
        contact.cherGhom(newOrganization);
    }
    
    public String toString(){
        return contact.toString();
    }
}