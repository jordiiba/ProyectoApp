package itcelaya.proyecto.models;

/**
 * Created by jordiiba on 14/11/17.
 */

public class Producto {

    int id, stock;
    String nombre,imagen,descripcion, categoria;
    double precio_venta;

    public Producto(int id, int stock, String nombre, String imagen, String descripcion, String categoria, double precio_venta) {
        this.id = id;
        this.stock = stock;
        this.nombre = nombre;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio_venta = precio_venta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(double precio_venta) {
        this.precio_venta = precio_venta;
    }
}
