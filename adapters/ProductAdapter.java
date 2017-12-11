package itcelaya.proyecto.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import itcelaya.proyecto.R;
import itcelaya.proyecto.models.Producto;


/**
 * Created by niluxer on 11/12/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    Context context;
    List<Producto> productList = new ArrayList<>();
    int id;
    String ip;
    String port;
    private Bd objbd;
    private SQLiteDatabase objsql;
    private int version = 1;

    public ProductAdapter(Context context, List<Producto> productList){
        this.context = context;
        this.productList = productList;
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        ip = myPreferences.getString("ip", "desconocido");
        port = myPreferences.getString("port", "desconocido");
        id = myPreferences.getInt("id", 0);
        objbd = new Bd(context, "carrito", null, version);
        objsql = objbd.getReadableDatabase();
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false);
        ProductHolder productHolder = new ProductHolder(view);
        return productHolder;
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        final Producto producto = productList.get(position);

        holder.tvProductName.setText(producto.getNombre());
        holder.tvProductPrice.setText(producto.getPrecio_venta() + "");
        holder.tvProductCat.setText(producto.getCategoria());
        holder.tvProductDesc.setText(producto.getDescripcion());
        holder.tvProductStock.setText(producto.getStock()+"");
        holder.btnAgregar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!holder.edtNum.getText().toString().equals("")){
                    //Toast.makeText(context,producto.getNombre(),Toast.LENGTH_SHORT).show();
                    int cantidad = Integer.parseInt(holder.edtNum.getText().toString());
                    if(cantidad <= producto.getStock()){
                        //peticion eliminar de stock
                        ContentValues row = new ContentValues();
                        row.put("id_cliente", id);
                        row.put("id_producto", producto.getId());
                        row.put("cantidad", cantidad);
                        objsql.insert("carrito", null, row);
                        holder.edtNum.setText("0");
                        Toast.makeText(context,"Agregaste "+cantidad+" "+producto.getNombre()+" al carrito.",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context,"No hay productos suficientes",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context,"Ingrese la cantidad",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Picasso.with(context).load("http://"+ip+":"+port+"/imagenes/"+producto.getImagen()).into(holder.ivImage);
        Log.e("Ima",ip+":"+port+"/imagenes/"+producto.getImagen());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder{

        TextView tvProductName,tvProductPrice,tvProductCat,tvProductDesc,tvProductStock;
        EditText edtNum;
        Button btnAgregar;
        ImageView ivImage;
        public ProductHolder(View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductCat = itemView.findViewById(R.id.tvProductCat);
            tvProductDesc = itemView.findViewById(R.id.tvProductDesc);
            tvProductStock = itemView.findViewById(R.id.tvProductStock);
            edtNum = itemView.findViewById(R.id.edtNum);
            btnAgregar = itemView.findViewById(R.id.btnAgregar);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }

}
