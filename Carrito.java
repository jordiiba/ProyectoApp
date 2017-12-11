package itcelaya.proyecto;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import itcelaya.proyecto.adapters.CarAdapter;
import itcelaya.proyecto.models.Producto;

public class Carrito extends AppCompatActivity {

    RecyclerView rvCarList;
    public TextView txtTotal;
    String ip;
    String port;
    int id;
    private Bd objbd;
    private SQLiteDatabase objsql;
    SharedPreferences myPreferences;
    private int version = 1;
    List<Producto> productoList = new ArrayList<Producto>();
    ArrayList<Integer> id_productos = new ArrayList();
    ArrayList<Integer> cantidades = new ArrayList();
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        rvCarList = (RecyclerView) findViewById(R.id.rvCarList);
        txtTotal  = (TextView) findViewById(R.id.txtTotal);
        ButterKnife.bind(this);
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ip = myPreferences.getString("ip", "desconocido");
        port = myPreferences.getString("port", "desconocido");
        id = myPreferences.getInt("id", 0);
        objbd = new Bd(this, "carrito", null, version);
        objsql = objbd.getReadableDatabase();
        obtener_carrito();
        cargar_productos();
    }

    @OnClick(R.id.btnTerminar)
    public void click1(){
        int total = myPreferences.getInt("total", 0);
        Intent intTer = new Intent(this, Terminar.class);
        intTer.putExtra("total",total+"");
        intTer.putExtra("id_productos",id_productos);
        intTer.putExtra("cantidades",cantidades);
        startActivity(intTer);
        finish();
    }

    public void obtener_carrito(){
        Cursor myCursor = objsql.rawQuery("select id_producto, cantidad " +
                "from carrito " +
                "where id_cliente = "+id, null);
        while(myCursor.moveToNext()) {
            int id_producto = myCursor.getInt(0);
            int cantidad = myCursor.getInt(1);
            id_productos.add(id_producto);
            cantidades.add(cantidad);
        }
    }

    private void cargar_productos(){
        String URL = "http://"+ip+":"+port+"/api/producto";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                        genera_lista(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void genera_lista(JSONArray response){
        for (int i = 0; i < response.length() ; i++) {
            try {
                JSONObject jsonObject = response.getJSONObject(i);
                if(existe(jsonObject.getInt("id"))){
                    productoList.add( new Producto(
                            jsonObject.getInt("id"),
                            jsonObject.getInt("stock"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("imagen"),
                            jsonObject.getString("descripcion"),
                            jsonObject.getString("categoria"),
                            jsonObject.getDouble("precio_venta")
                    ));
                }
            } catch (JSONException e){ }
        }
        rvCarList.setAdapter(new CarAdapter(this,productoList,id_productos,cantidades,activity));
        rvCarList.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean existe(int id_p){
        for (int i = 0; i<id_productos.size();i++){
            if(id_productos.get(i)==id_p){
                return true;
            }
        }
        return false;
    }
}
