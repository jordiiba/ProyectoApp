package itcelaya.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {

    @BindView(R.id.edtUsuario)
    EditText edtUsuario;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    String ip;
    String port;
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ip = myPreferences.getString("ip", "desconocido");
        port = myPreferences.getString("port", "desconocido");
        if(ip.equals("desconocido") || port.equals("desconocido")){
            ip = "127.0.0.1";
            port = "8000";
        }
        myEditor = myPreferences.edit();
        myEditor.putString("ip", ip);
        myEditor.putString("port", port);
    }

    @OnClick(R.id.btnIniciar)
    public void click1(){
        //Toast.makeText(this,"Prueba", Toast.LENGTH_LONG).show();
        validarUsuario();
    }

    @OnClick(R.id.btnInvitado)
    public void click2(){
        myEditor.putInt("id", 5);
        myEditor.commit();
        Intent intMenu = new Intent(this, Carga.class);
        startActivity(intMenu);
        finish();
    }

    @OnClick(R.id.btnConfiguracion)
    public void click3(){
        myEditor.putInt("id", 0);
        myEditor.commit();
        Intent intConf = new Intent(this, Configuracion.class);
        startActivity(intConf);
        finish();
    }

    @OnClick(R.id.btnRegistro)
    public void click4(){
        myEditor.putInt("id", 0);
        myEditor.commit();
        Intent intReg = new Intent(this, Registro.class);
        startActivity(intReg);
        finish();
    }

    private void validarUsuario() {
        if(edtUsuario.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
            Toast.makeText(this,"Complete los campos",Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("usuario", edtUsuario.getText().toString());
                jsonObject.put("password", edtPassword.getText().toString());
                //jsonObject.put("Content-Type","application/x-www-form-urlencoded");
            } catch (JSONException e) { Log.e("Error", e.toString()); }
            String URL = "http://"+ip+":"+port+"/api/valcliente";
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.POST, URL, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean respuesta = response.getBoolean("success");
                                if (respuesta) {
                                    //Toast.makeText(Login.this,response.getString("mensaje"),Toast.LENGTH_LONG).show();
                                    myEditor.putInt("id", response.getInt("id"));
                                    myEditor.commit();
                                    Intent intMenu = new Intent(Login.this, Carga.class);
                                    startActivity(intMenu);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, response.get("mensaje").toString(), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    });
                    /*
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                            myEditor.putInt("id", response.getInt("id"));
                            //myEditor.putInt("id", 2);
                            myEditor.commit();
                            Intent intMenu = new Intent(Login.this, Carga.class);
                            startActivity(intMenu);
                            finish();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            //Log.d("Error.Response", response);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("usuario", edtUsuario.getText().toString());
                    params.put("password", edtPassword.getText().toString());
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/json");
                    return params;
                }
            };
            */
            requestQueue.add(jsonObjectRequest);
        }
    }
}
