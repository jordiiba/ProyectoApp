package itcelaya.proyecto;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Carga extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Hilo objHilo;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);
        //SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String ip = myPreferences.getString("ip", "desconocido");
        //String port = myPreferences.getString("port", "desconocido");
        //int id = myPreferences.getInt("id", 0);
        //Toast.makeText(this, ip+":"+port+" "+id, Toast.LENGTH_LONG).show();
        ButterKnife.bind(this);
        activity = this;
        objHilo = new Hilo(progressBar,this,activity);
        objHilo.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
