package itcelaya.proyecto.models;

/**
 * Created by jordiiba on 29/11/17.
 */

public class Cupon {
    int id;
    String clave;
    String descripcion;
    int descuento;

    public Cupon(int id, String clave, String descripcion, int descuento) {
        this.id = id;
        this.clave = clave;
        this.descripcion = descripcion;
        this.descuento = descuento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDescuento() {
        return descuento;
    }

    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }
}
