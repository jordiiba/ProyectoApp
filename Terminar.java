package itcelaya.proyecto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Terminar extends AppCompatActivity {

    @BindView(R.id.txtSubTotal)
    TextView txtSubTotal;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    @BindView(R.id.txtCupon)
    TextView txtCupon;

    String ip;
    String port;
    int id;
    String clave;
    double descuento = 0;
    double total;
    double nuevoTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminar);
        ButterKnife.bind(this);
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ip = myPreferences.getString("ip", "desconocido");
        port = myPreferences.getString("port", "desconocido");
        id = myPreferences.getInt("id",0);
        Intent i = getIntent();
        if(i.getStringExtra("clave")!=null){
            clave = i.getStringExtra("clave");
            descuento = Double.parseDouble(i.getStringExtra("descuento"));
            total = Double.parseDouble(i.getStringExtra("total"));
            txtCupon.setText(clave);
            txtSubTotal.setText("$ "+total);
            double des = total*(descuento/100);
            nuevoTotal = total - des;
            Log.e("Total",total+" "+des);
            txtTotal.setText("$ "+nuevoTotal);
        } else if(i.getStringExtra("total")!=null){
            total = Integer.parseInt(i.getStringExtra("total"));
            txtSubTotal.setText("$ "+total);
            nuevoTotal = total;
            txtTotal.setText("$ "+nuevoTotal);
        }
    }

    @OnClick(R.id.btnCupon)
    public void click1(){
        Intent intCup = new Intent(this, Cupones.class);
        intCup.putExtra("total",total+"");
        startActivity(intCup);
        finish();
    }

    @OnClick(R.id.btnCancelar)
    public void click2(){
        Intent intCan = new Intent(this, Carrito.class);
        startActivity(intCan);
        finish();
    }
}
