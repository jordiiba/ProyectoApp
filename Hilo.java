package itcelaya.proyecto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;

/**
 * Created by Jordi Ibañez on 20/09/2017.
 */

public class Hilo extends AsyncTask <Void,Float,Void> {

    private Context con;
    private ProgressBar progressBar;
    private Activity activity;

    public Hilo(ProgressBar progressBar, Context con, Activity activity){
        this.progressBar = progressBar;
        this.con = con;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try{
            for (int i=1; i<=25; i++){
                Thread.sleep(100);
                publishProgress(i*4f);
            }
        } catch (InterruptedException e) {
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0].intValue());
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Intent intInicio = new Intent(con,VerDatos.class);
        Intent intInicio = new Intent(con,Inicio.class);
        intInicio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        con.startActivity(intInicio);
        activity.finish();
    }
}
