package Prototype;

public class Address implements Copyable{
    private String type;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    public static final String EOL_STRING =
        System.getProperty("line.separator");
    public static final String COMMA = ",";
    public static final String HOME = "home";
    public static final String WORK = "work";
    
    public Address(String initType, String initStreet,
        String initCity, String initState, String initZip){
            type = initType;
            street = initStreet;
            city = initCity;
            state = initState;
            zipCode = initZip;
    }
    
    public Address(String initStreet, String initCity,
        String initState, String initZip){
            this(WORK, initStreet, initCity, initState, initZip);
    }
    public Address(String initType){
        type = initType;
    }
    public Address(){ }
    
    public String getType(){ return type; }
    public String getStreet(){ return street; }
    public String getCity(){ return city; }
    public String getState(){ return state; }
    public String getZipCode(){ return zipCode; }
    
    public void setType(String newType){ type = newType; }
    public void setStreet(String newStreet){ street = newStreet; }
    public void setCity(String newCity){ city = newCity; }
    public void setState(String newState){ state = newState; }
    public void setZipCode(String newZip){ zipCode = newZip; }
    
    public Object copy(){
        return new Address(street, city, state, zipCode);
    }
    
    public String toString(){
        return "\t" + street + COMMA + " " + EOL_STRING +
            "\t" + city + COMMA + " " + state + " " + zipCode;
    }
}