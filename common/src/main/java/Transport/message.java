package Transport;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class message implements Serializable {
    String data;
    String check_bit;

    public message(String data, String check_bit) {
        this.data = data;
        this.check_bit = check_bit;
    }
}
