package itcelaya.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {

    @BindView(R.id.edtUsuario)
    EditText edtUsuario;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    String ip = "192.168.0.6";
    String port = "8000";
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
        myEditor.putInt("id", 0);
        myEditor.commit();
        Intent intMenu = new Intent(this, Carga.class);
        startActivity(intMenu);
        finish();
    }

    private void validarUsuario() {
        if(edtUsuario.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
            Toast.makeText(this,"Complete los campos",Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("usuario", edtUsuario.getText());
                jsonObject.put("contrasena", (edtPassword.getText()));
            } catch (JSONException e) { Log.e("Error", e.toString()); }
            String URL = "http://"+ip+":"+port+"/apiPractica3/valcliente";
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
            requestQueue.add(jsonObjectRequest);
        }
    }
}
