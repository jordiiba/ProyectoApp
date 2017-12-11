package itcelaya.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Principal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Modificar extends AppCompatActivity {

    @BindView(R.id.edtNombres)
    EditText edtNombres;
    @BindView(R.id.edtApellidos)
    EditText edtApellidos;
    @BindView(R.id.edtCorreo)
    EditText edtCorreo;
    @BindView(R.id.edtCompania)
    EditText edtCompania;
    @BindView(R.id.edtRfc)
    EditText edtRfc;
    @BindView(R.id.edtTelefono)
    EditText edtTelefono;
    @BindView(R.id.edtDireccion)
    EditText edtDireccion;
    @BindView(R.id.edtCp)
    EditText edtCp;
    @BindView(R.id.spEstado)
    Spinner spEstado;
    @BindView(R.id.spCiudad)
    Spinner spCiudad;

    String ip;
    String port;
    int id;
    public int id_estado2;
    public int id_ciudad2;

    ArrayList<String> nom_estados = new ArrayList();
    ArrayList<Integer> id_estados = new ArrayList();
    ArrayList<String> nom_ciudades = new ArrayList();
    ArrayList<Integer> id_ciudades = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        ButterKnife.bind(this);
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ip = myPreferences.getString("ip", "desconocido");
        port = myPreferences.getString("port", "desconocido");
        id = myPreferences.getInt("id", 0);
        obtenerEstados();
    }

    public void posiciones(){
        ArrayAdapter<String> adapterEstados =  new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,nom_estados);
        spEstado.setAdapter(adapterEstados);
        int pos = obtenerPosicion(id_estados,id_estado2);
        spEstado.setSelection(pos);
        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                obtenerCiudades(id_estados.get(pos));
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        obtenerCiudades(id_estados.get(pos));
    }

    @OnClick(R.id.btnModificar)
    public void click1(){
        //Toast.makeText(this,"Prueba", Toast.LENGTH_LONG).show();
        modificar();
    }

    @OnClick(R.id.btnCancelar)
    public void click2(){
        //Toast.makeText(this,"Prueba", Toast.LENGTH_LONG).show();
        Intent intVer = new Intent(this,VerDatos.class);
        startActivity(intVer);
        finish();
    }

    public void obtenerCiudades(int id_estado){
        //Depende al estado mostrar ciudades
        nom_ciudades.clear();
        id_ciudades.clear();
        String URL = "http://"+ip+":"+port+"/api/ciudad/estado/"+id_estado;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length() ; i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                nom_ciudades.add(jsonObject.getString("nombre"));
                                id_ciudades.add(jsonObject.getInt("id"));
                            }
                        } catch (JSONException e){ }
                        ArrayAdapter<String> adapterCiudades =  new ArrayAdapter(Modificar.this,android.R.layout.simple_spinner_dropdown_item,nom_ciudades);
                        spCiudad.setAdapter(adapterCiudades);
                        spCiudad.setSelection(obtenerPosicion(id_ciudades,id_ciudad2));
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

    public void obtenerEstados(){
        nom_estados.clear();
        id_estados.clear();
        String URL = "http://"+ip+":"+port+"/api/estado";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length() ; i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                nom_estados.add(jsonObject.getString("nombre"));
                                id_estados.add(jsonObject.getInt("id"));
                            }
                        } catch (JSONException e){ }
                        obtenerDatos();
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

    public void modificar(){
        if(edtNombres.getText().toString().equals("") || edtRfc.getText().toString().equals("")
                || edtTelefono.getText().toString().equals("") || edtDireccion.getText().toString().equals("")
                || edtCorreo.getText().toString().equals("") || edtCp.getText().toString().equals("")
                || edtCompania.getText().toString().equals("") || edtApellidos.getText().toString().equals("")){
            Toast.makeText(this,"Complete los campos",Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("nombres", edtNombres.getText());
                jsonObject.put("rfc", (edtRfc.getText()));
                jsonObject.put("telefono", edtTelefono.getText());
                jsonObject.put("direccion", (edtDireccion.getText()));
                jsonObject.put("email", edtCorreo.getText());
                jsonObject.put("cp", (edtCp.getText()));
                jsonObject.put("compania", edtCompania.getText());
                jsonObject.put("apellidos", (edtApellidos.getText()));
                int id_ciudad = id_ciudades.get(spCiudad.getSelectedItemPosition());
                jsonObject.put("id_ciudad", id_ciudad);
            } catch (JSONException e) { Log.e("Eroor", e.toString()); }
            String URL = "http://"+ip+":"+port+"/api/users/update/"+id;
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.PUT, URL, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean respuesta = response.getBoolean("success");
                                if (respuesta) {
                                    //Toast.makeText(Login.this,response.getString("mensaje"),Toast.LENGTH_LONG).show();
                                    Intent intVer = new Intent(Modificar.this, VerDatos.class);
                                    startActivity(intVer);
                                    finish();
                                } else {
                                    Toast.makeText(Modificar.this, response.get("mensaje").toString(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(Modificar.this, e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(Modificar.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    });
            requestQueue.add(jsonObjectRequest);
        }
    }

    public void obtenerDatos(){
        String URL = "http://"+ip+":"+port+"/api/perfil/"+id;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            edtNombres.setText(response.getString("nombres"));
                            edtApellidos.setText(response.getString("apellidos"));
                            edtCorreo.setText(response.getString("email"));
                            edtCompania.setText(response.getString("compania"));
                            edtRfc.setText(response.getString("rfc"));
                            edtTelefono.setText(response.getString("telefono"));
                            edtDireccion.setText(response.getString("direccion"));
                            edtCp.setText(response.getString("cp"));
                            id_estado2 = response.getInt("id_estado");
                            id_ciudad2 = response.getInt("id_ciudad");
                            Log.e("OBTENERDATOD",id_estado2+" "+id_ciudad2);
                            posiciones();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public int obtenerPosicion(ArrayList<Integer> a, int id){
        int pos = 0;
        for (int i=0; i<a.size();i++){
            if(a.get(i)==id){
                pos = i;
            }
        }
        return pos;
    }
}
