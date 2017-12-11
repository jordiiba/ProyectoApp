package itcelaya.proyecto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jordi Ibañez on 06/10/2017.
 */

public class Bd extends SQLiteOpenHelper {

    private String carrito = "CREATE TABLE IF NOT EXISTS carrito (id_cliente INT, id_producto INT, cantidad INT)";
    public Bd(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(carrito);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
