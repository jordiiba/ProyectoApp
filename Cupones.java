package itcelaya.proyecto;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import itcelaya.proyecto.adapters.CuponesAdapter;
import itcelaya.proyecto.models.Cupon;

public class Cupones extends AppCompatActivity {

    @BindView(R.id.rvRecyclerList)
    RecyclerView rvCuponesList;

    String ip;
    String port;
    int id;
    double total;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupones);
        ButterKnife.bind(this);
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ip = myPreferences.getString("ip", "desconocido");
        port = myPreferences.getString("port", "desconocido");
        id = myPreferences.getInt("id",0);
        load_cupones();
        Intent i = getIntent();
        if(i.getStringExtra("total")!=null){
            total = Double.parseDouble(i.getStringExtra("total"));
        }
    }

    private void load_cupones(){
        String URL = "http://"+ip+":"+port+"/api/cupon/"+id;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        genera_lista(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    List<Cupon> cuponesList = new ArrayList();

    private void genera_lista(JSONArray response){
        for (int i = 0; i < response.length() ; i++) {
            try {
                JSONObject jsonObject = response.getJSONObject(i);
                cuponesList.add( new Cupon(
                        jsonObject.getInt("id"),
                        jsonObject.getString("clave"),
                        jsonObject.getString("descripcion"),
                        jsonObject.getInt("descuento")));
            } catch (JSONException e){ }
        }
        rvCuponesList.setAdapter(new CuponesAdapter(this,cuponesList,activity,total));
        rvCuponesList.setLayoutManager(new LinearLayoutManager(this));
    }
}
