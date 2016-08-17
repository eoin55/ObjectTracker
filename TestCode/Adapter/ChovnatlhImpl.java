package Adapter;

// pong = name
// wa'DIch = first
// Qav = last
// patlh = rank (title)
// ghom = group (organization)
// tlhap = take (get)
// cher = set up (set)
// chu' = new
// chovnatlh = specimen (contact)
    
public class ChovnatlhImpl implements Chovnatlh{
    private String wa$DIchPong;
    private String QavPong;
    private String patlh;
    private String ghom;
    
    public ChovnatlhImpl(){ }
    public ChovnatlhImpl(String chu$wa$DIchPong, String chu$QavPong,
        String chu$patlh, String chu$ghom){
            wa$DIchPong = chu$wa$DIchPong;
            QavPong = chu$QavPong;
            patlh = chu$patlh;
            ghom = chu$ghom;
    }
    
    public String tlhapWa$DIchPong(){ return wa$DIchPong; }
    public String tlhapQavPong(){ return QavPong; }
    public String tlhapPatlh(){ return patlh; }
    public String tlhapGhom(){ return ghom; }
    
    public void cherWa$DIchPong(String chu$wa$DIchPong){ wa$DIchPong = chu$wa$DIchPong; }
    public void cherQavPong(String chu$QavPong){ QavPong = chu$QavPong; }
    public void cherPatlh(String chu$patlh){ patlh = chu$patlh; }
    public void cherGhom(String chu$ghom){ ghom = chu$ghom; }
    
    public String toString(){
        return wa$DIchPong + " " + QavPong + ": " + patlh + ", " + ghom;
    }
}
