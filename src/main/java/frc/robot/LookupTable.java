package frc.robot;
public class LookupTable{
    double[] keys;
    double[] values;
    public LookupTable(double[] inputs, double[] outputs) throws Exception {
        if(inputs.length != outputs.length)
            throw new Exception();
        keys = inputs;
        values = outputs;
        
    }
    public double get(double input){
        int low_i = 0; 
        int up_i = 0; 
        for(int i =0; i < keys.length; i++){
            up_i = i; 
            if(keys[up_i]>input)
                break;
            low_i = up_i;
            
        }
        double lowerWeight= 1-(input -keys[low_i])/(keys[up_i]-keys[low_i]);
        double upperWeight = 1 -(keys[up_i]-input)/(keys[up_i]-keys[low_i]);
        return lowerWeight*values[low_i] + upperWeight*values[up_i];
    }
}
