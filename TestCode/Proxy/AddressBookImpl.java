package Proxy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
public class AddressBookImpl implements AddressBook {
    private File file;
    private ArrayList addresses = new ArrayList();
    
    public AddressBookImpl(File newFile) {
        file = newFile;
        open();
    }
    
    public ArrayList getAllAddresses(){ return addresses; }
    
    public Address getAddress(String description){
        Iterator addressIterator = addresses.iterator();
        while (addressIterator.hasNext()){
            AddressImpl address = (AddressImpl)addressIterator.next();
            if (address.getDescription().equalsIgnoreCase(description)){
                return address;
            }
        }
        return null;
    }

    public void add(Address address) {
        if (!addresses.contains(address)){
            addresses.add(address);
        }
    }
    
    public void open(){
        addresses = (ArrayList)FileLoader.loadData(file);
    }
    
    public void save(){
        FileLoader.storeData(file, addresses);
    }
}