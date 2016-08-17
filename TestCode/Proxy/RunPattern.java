package Proxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class RunPattern{
    public static void main(String [] arguments){
        System.out.println("Example for the Proxy pattern");
        System.out.println();
        System.out.println("This code will demonstrate the use of a Proxy to");
        System.out.println(" provide functionality in place of its underlying");
        System.out.println(" class.");
        System.out.println();
        
        System.out.println(" Initially, an AddressBookProxy object will provide");
        System.out.println(" address book support without requiring that the");
        System.out.println(" AddressBookImpl be created. This could potentially");
        System.out.println(" make the application run much faster, since the");
        System.out.println(" AddressBookImpl would need to read in all addresses");
        System.out.println(" from a file when it is first created.");
        System.out.println();
        
        if (!(new File("data.ser").exists())){
            DataCreator.serialize("data.ser");
        }
        System.out.println("Creating the AddressBookProxy");
        AddressBookProxy proxy = new AddressBookProxy("data.ser");
        System.out.println("Adding entries to the AddressBookProxy");
        System.out.println("(this operation can be done by the Proxy, without");
        System.out.println(" creating an AddressBookImpl object)");
        proxy.add(new AddressImpl("Sun Education [CO]", "500 El Dorado Blvd.", "Broomfield", "CO", "80020"));
        proxy.add(new AddressImpl("Apple Inc.", "1 Infinite Loop", "Redwood City", "CA", "93741"));
        System.out.println("Addresses created. Retrieving an address");
        System.out.println("(since the address is stored by the Proxy, there is");
        System.out.println(" still no need to create an AddressBookImpl object)");
        System.out.println();
        System.out.println(proxy.getAddress("Sun Education [CO]").getAddress());
        System.out.println();
        
        System.out.println("So far, all operations have been handled by the Proxy,");
        System.out.println(" without any involvement from the AddressBookImpl.");
        System.out.println(" Now, a call to the method getAllAddresses will");
        System.out.println(" force instantiation of AddressBookImpl, and will");
        System.out.println(" retrieve ALL addresses that are stored.");
        System.out.println();
        
        ArrayList addresses = proxy.getAllAddresses();
        System.out.println("Addresses retrieved. Addresses currently stored:");
        System.out.println(addresses);
    }
}