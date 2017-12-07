package itcelaya.proyecto;

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

    public Hilo(ProgressBar progressBar, Context con){
        this.progressBar = progressBar;
        this.con = con;
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
        Intent intMenu = new Intent(con,Login.class);
        con.startActivity(intMenu);
    }
}
