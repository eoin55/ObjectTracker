package Adapter;

public class RunPattern{
    public static void main(String [] arguments){
        System.out.println("Example for the Adapter pattern");
        System.out.println();
        System.out.println("This example will demonstrate the Adapter by using the");
        System.out.println(" class ContactAdapter to translate from classes written");
        System.out.println(" in a foreign language (Chovnatlh and ChovnatlhImpl),");
        System.out.println(" enabling their code to satisfy the Contact interface.");
        System.out.println();
        
        System.out.println("Creating a new ContactAdapter. This will, by extension,");
        System.out.println(" create an instance of ChovnatlhImpl which will provide");
        System.out.println(" the underlying Contact implementation.");
        Contact contact = new ContactAdapter();
        System.out.println();
        
        System.out.println("ContactAdapter created. Setting contact data.");
        contact.setFirstName("Thomas");
        contact.setLastName("Williamson");
        contact.setTitle("Science Officer");
        contact.setOrganization("W3C");
        System.out.println();
        
        System.out.println("ContactAdapter data has been set. Printing out Contact data.");
        System.out.println();
        System.out.println(contact.toString());
    }
}