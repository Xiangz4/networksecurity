package Transport;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class nettyTransport implements Serializable {
    private int     factor;
    private String     public_key;

    public nettyTransport(int factor,String public_key){
        this.factor = factor;
        this.public_key = public_key;
    }
}
