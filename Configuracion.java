package itcelaya.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Configuracion extends AppCompatActivity {

    @BindView(R.id.edtIp)
    EditText edtIp;
    @BindView(R.id.edtPort)
    EditText edtPort;

    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        ButterKnife.bind(this);
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        myEditor = myPreferences.edit();
        String ip = myPreferences.getString("ip", "desconocido");
        String port = myPreferences.getString("port", "desconocido");
        edtIp.setText(ip);
        edtPort.setText(port);
    }

    @OnClick(R.id.btnGuardar)
    public void click1(){
        if(edtIp.getText().toString().equals("") || edtPort.getText().toString().equals("")){
            Toast.makeText(this,"Complete los campos",Toast.LENGTH_SHORT).show();
        } else {
            myEditor.putString("ip",edtIp.getText().toString());
            myEditor.putString("port",edtPort.getText().toString());
            myEditor.commit();
            Intent intLogin = new Intent(this, Login.class);
            startActivity(intLogin);
            finish();
        }
    }
}
