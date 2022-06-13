package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class fileReader {
    public static String ReadFile(String filepath) throws Exception{
        FileReader reader = new FileReader(filepath);
        BufferedReader in = new BufferedReader(reader);
        String res = null;
        String str = null;
        in.skip(22);
        while ( (str = in.readLine()) != null){
            in.skip(22);
            res += str +"\n";
        }
        return res;
    }
}
