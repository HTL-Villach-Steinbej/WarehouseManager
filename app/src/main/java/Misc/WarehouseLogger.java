package Misc;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class WarehouseLogger {
    private static ArrayList<String> logLines = new ArrayList<>();
    public static void addLog(FirebaseUser user, String message){
        logLines.add("[" + Timestamp.now().toDate().toString().substring(11).substring(0,8) + "] " + user.getEmail() + "=> " + message);
    }
    public static List<String> getLog(){
        return logLines;
    }
}
