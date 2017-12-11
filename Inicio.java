package itcelaya.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

import itcelaya.proyecto.adapters.ProductAdapter;
import itcelaya.proyecto.models.Producto;

public class Inicio extends AppCompatActivity {

    RecyclerView rvProductList;
    String ip;
    String port;
    int id;
    List<Producto> productoList = new ArrayList<Producto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        rvProductList = (RecyclerView) findViewById(R.id.rvProductList);
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ip = myPreferences.getString("ip", "desconocido");
        port = myPreferences.getString("port", "desconocido");
        id = myPreferences.getInt("id", 0);
        cargar_productos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_carrito) {
            Intent intCar = new Intent(Inicio.this, Carrito.class);
            startActivity(intCar);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                        //Toast.makeText(Inicio.this,response.toString(),Toast.LENGTH_LONG).show();
                        Log.e("response",response.toString());
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
                productoList.add( new Producto(
                        jsonObject.getInt("id"),
                        jsonObject.getInt("stock"),
                        jsonObject.getString("nombre"),
                        jsonObject.getString("imagen"),
                        jsonObject.getString("descripcion"),
                        jsonObject.getString("categoria"),
                        jsonObject.getDouble("precio_venta")
                ));
            } catch (JSONException e){ }
        }
        rvProductList.setAdapter(new ProductAdapter(this,productoList));
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
    }
}
