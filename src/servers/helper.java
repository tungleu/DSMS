package servers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class helper {
    public static String current_time(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }
}
