package Misc;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class WarehouseLogger {
    public enum LogType{ WAREHOUSE, EMPLOYEE, ITEMS }
    private static ArrayList<String> logLines = new ArrayList<>();
    public static void addLog(FirebaseUser user, LogType type, String message){
        logLines.add("[" + Timestamp.now().toDate().toString().substring(11).substring(0,8) + "] "+ type.toString() + "=> " + message);
    }
    public static List<String> getLog(){
        return logLines;
    }
}
