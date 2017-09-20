package pl.jaceksen.sqllite2;

/**
 * Created by jsen on 19.09.17.
 */

public class AdapterItems {

    public int id;
    public String userName;
    public String password;

    AdapterItems(int id, String userName, String password){
        this.id = id;
        this.userName = userName;
        this.password = password;
    }
}
