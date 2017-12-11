package itcelaya.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerDatos extends AppCompatActivity {

    @BindView(R.id.txtUsuario)
    TextView txtUsuario;
    @BindView(R.id.txtNombres)
    TextView txtNombres;
    @BindView(R.id.txtApellidos)
    TextView txtApellidos;
    @BindView(R.id.txtCorreo)
    TextView txtCorreo;
    @BindView(R.id.txtCompania)
    TextView txtCompania;
    @BindView(R.id.txtRfc)
    TextView txtRfc;
    @BindView(R.id.txtTelefono)
    TextView txtTelefono;
    @BindView(R.id.txtDireccion)
    TextView txtDireccion;
    @BindView(R.id.txtCp)
    TextView txtCp;
    @BindView(R.id.txtCiudad)
    TextView txtCiudad;

    String ip;
    String port;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_datos);
        ButterKnife.bind(this);
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ip = myPreferences.getString("ip", "desconocido");
        port = myPreferences.getString("port", "desconocido");
        id = myPreferences.getInt("id", 0);

        obtenerDatos();
    }

    @OnClick(R.id.btnModificar2)
    public void click1(){
        Intent intMod = new Intent(this, Modificar.class);
        startActivity(intMod);
        finish();
    }

    public void obtenerDatos(){
        String URL = "http://"+ip+":"+port+"/api/perfil/"+id;
        Log.e("URL",URL);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject json) {
                        try {
                            txtUsuario.setText(json.getString("usuario"));
                            txtNombres.setText(json.getString("nombres"));
                            txtApellidos.setText(json.getString("apellidos"));
                            txtCorreo.setText(json.getString("email"));
                            txtCompania.setText(json.getString("compania"));
                            txtRfc.setText(json.getString("rfc"));
                            txtTelefono.setText(json.getString("telefono"));
                            txtDireccion.setText(json.getString("direccion"));
                            txtCp.setText(json.getString("cp"));
                            txtCiudad.setText(json.getString("ciudad"));
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
}
