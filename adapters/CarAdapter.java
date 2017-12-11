package itcelaya.proyecto.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import itcelaya.proyecto.Bd;
import itcelaya.proyecto.Carrito;
import itcelaya.proyecto.R;
import itcelaya.proyecto.models.Producto;

/**
 * Created by niluxer on 11/12/17.
 */

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ProductHolder> {

    Context context;
    List<Producto> productList = new ArrayList<>();
    ArrayList<Integer> id_productos = new ArrayList();
    ArrayList<Integer> cantidades = new ArrayList();
    int id;
    String ip;
    String port;
    private Bd objbd;
    private SQLiteDatabase objsql;
    SharedPreferences.Editor myEditor;
    SharedPreferences myPreferences;
    private int version = 1;
    public int total = 0;
    Activity activity;
    public CarAdapter(Context context, List<Producto> productList, ArrayList<Integer> id_productos ,ArrayList<Integer> cantidades, Activity activity){
        this.context = context;
        this.productList = productList;
        this.id_productos = id_productos;
        this.cantidades = cantidades;
        this.activity = activity;
        myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        ip = myPreferences.getString("ip", "desconocido");
        port = myPreferences.getString("port", "desconocido");
        id = myPreferences.getInt("id", 0);
        objbd = new Bd(context, "carrito", null, version);
        objsql = objbd.getReadableDatabase();
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_row, parent, false);
        ProductHolder productHolder = new ProductHolder(view);
        return productHolder;
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, int position) {
        final Producto producto = productList.get(position);
        int cantidad=0;
        holder.tvProductName.setText(producto.getNombre());
        holder.tvProductPrice.setText(producto.getPrecio_venta() + "");
        for (int i = 0; i<id_productos.size(); i++){
            if(producto.getId()==id_productos.get(i)){
                cantidad+=cantidades.get(i);
                holder.tvNum.setText(cantidad+"");
            }
        }
        total += producto.getPrecio_venta()*cantidad;
        myEditor = myPreferences.edit();
        myEditor.putInt("total", total);
        myEditor.commit();
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                objsql.execSQL("delete from carrito where id_cliente = "+id+" and id_producto = "+producto.getId());
                Intent intCarrito = new Intent(context,Carrito.class);
                context.startActivity(intCarrito);
                activity.finish();
            }
        });
        Picasso.with(context).load("http://"+ip+":"+port+"/imagenes/"+producto.getImagen()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder{

        TextView tvProductName,tvProductPrice, tvNum;
        Button btnEliminar;
        ImageView ivImage;
        public ProductHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvNum = itemView.findViewById(R.id.tvNum);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }

}
