package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum DeleteOrdini {
    
    TUTTI("Tutti", -1),
    
    EVASI("Evasi", 2),
    
    PARZIALMENTE_EVASI("Parzialmente evasi", 1);
        
    protected String label;
    
    protected int output;
        
    DeleteOrdini(String label, int output){
        this.label = label; 
        this.output = output;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public int getOutput() {
          return this.output;
    }
    
    public static Collection<String> list(){
        List<String> result = new ArrayList<>();
        for(int i=0; i<values().length; i++){
            result.add(values()[i].getLabel());
        }
        return result;
    }
    
    public static int getOutputFromLabel(String label){
        if(label != null && !label.isEmpty()){
            String name = label.toUpperCase().replace(" ", "_");
            return DeleteOrdini.valueOf(DeleteOrdini.class, name).getOutput();
        } 
        return -1;
    }
    
}
