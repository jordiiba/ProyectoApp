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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Registro extends AppCompatActivity {

    @BindView(R.id.edtUsuario2)
    EditText edtUsuario;
    @BindView(R.id.edtPassword2)
    EditText edtPassword;
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

    ArrayList<String> nom_estados = new ArrayList();
    ArrayList<Integer> id_estados = new ArrayList();
    ArrayList<String> nom_ciudades = new ArrayList();
    ArrayList<Integer> id_ciudades = new ArrayList();
    String ip;
    String port;
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        myEditor = myPreferences.edit();
        ip = myPreferences.getString("ip", "desconocido");
        port = myPreferences.getString("port", "desconocido");
        obtenerEstados();
    }

    public void posicion(){
        ArrayAdapter<String> adapterEstados =  new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,nom_estados);
        spEstado.setAdapter(adapterEstados);
        spEstado.setSelection(0);
        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                obtenerCiudades(id_estados.get(pos));
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.btnGuardar2)
    public void click1(){
        //Toast.makeText(this,"Prueba", Toast.LENGTH_LONG).show();
        registrar();
    }

    @OnClick(R.id.btnSalir)
    public void click2(){
        //Toast.makeText(this,"Prueba", Toast.LENGTH_LONG).show();
        Intent intLogin = new Intent(this,Login.class);
        startActivity(intLogin);
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
                        ArrayAdapter<String> adapterCiudades =  new ArrayAdapter(Registro.this,android.R.layout.simple_spinner_dropdown_item,nom_ciudades);
                        spCiudad.setAdapter(adapterCiudades);
                        spCiudad.setSelection(0);
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

                        posicion();
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

    public void registrar(){
        if(edtUsuario.getText().toString().equals("") || edtPassword.getText().toString().equals("")
                || edtNombres.getText().toString().equals("") || edtRfc.getText().toString().equals("")
                || edtTelefono.getText().toString().equals("") || edtDireccion.getText().toString().equals("")
                || edtCorreo.getText().toString().equals("") || edtCp.getText().toString().equals("")
                || edtCompania.getText().toString().equals("") || edtApellidos.getText().toString().equals("")){
            Toast.makeText(this,"Complete los campos",Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("usuario", edtUsuario.getText());
                jsonObject.put("password", (edtPassword.getText()));
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
                //jsonObject.put("rol", "member");
            } catch (JSONException e) { Log.e("Eroor", e.toString()); }
            String URL = "http://"+ip+":"+port+"/api/users/insert";
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, URL, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean respuesta = response.getBoolean("success");
                                if (respuesta) {
                                    //Toast.makeText(Login.this,response.getString("mensaje"),Toast.LENGTH_LONG).show();
                                    Intent intLogin = new Intent(Registro.this, Login.class);
                                    startActivity(intLogin);
                                    finish();
                                } else {
                                    Toast.makeText(Registro.this, response.get("mensaje").toString(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(Registro.this, e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(Registro.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    });

            requestQueue.add(jsonObjectRequest);
        }
    }
}