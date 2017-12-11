package itcelaya.proyecto.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import itcelaya.proyecto.R;
import itcelaya.proyecto.Terminar;
import itcelaya.proyecto.models.Cupon;


/**
 * Created by niluxer on 11/12/17.
 */

public class CuponesAdapter extends RecyclerView.Adapter<CuponesAdapter.ProductHolder> {

    Context context;
    double total;
    List<Cupon> cuponesList = new ArrayList<>();
    Activity activity;
    public CuponesAdapter(Context context, List<Cupon> cuponesList, Activity activity, double total){
        this.context = context;
        this.cuponesList = cuponesList;
        this.total = total;
        this.activity = activity;
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cupones_row, parent, false);
        ProductHolder productHolder = new ProductHolder(view);
        return productHolder;
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, int position) {
        Cupon cupon = cuponesList.get(position);
        holder.tvClave.setText(cupon.getClave());
        holder.tvDescripcion.setText(cupon.getDescripcion());
        holder.tvDescuento.setText(cupon.getDescuento() + "%");
        final String clave = cupon.getClave();
        final String descuento = cupon.getDescuento()+"";
        holder.btnEste.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intTer = new Intent(context, Terminar.class);
                intTer.putExtra("clave",clave);
                intTer.putExtra("descuento",descuento);
                intTer.putExtra("total",total+"");
                context.startActivity(intTer);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cuponesList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder{

        TextView tvClave, tvDescripcion,tvDescuento;
        Button btnEste;
        public ProductHolder(View itemView) {
            super(itemView);
            tvClave = itemView.findViewById(R.id.tvClave);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvDescuento = itemView.findViewById(R.id.tvDescuento);
            btnEste = itemView.findViewById(R.id.btnEste);
        }
    }

}
