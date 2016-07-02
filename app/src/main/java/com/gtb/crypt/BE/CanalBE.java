package com.gtb.crypt.BE;

/**
 * Created by GroverTB on 5/05/2016.
 */
public class CanalBE {
    private Integer codigo;
    private String name;
    private String stream;
    private String colorCard;
    private Boolean estado;
    private Integer tipo;
    private Integer posicion;

    public CanalBE() {
    }

    public CanalBE(Integer codigo, String name, String stream, String colorCard, Boolean estado, Integer tipo, Integer posicion) {
        this.codigo = codigo;
        this.name = name;
        this.stream = stream;
        this.colorCard = colorCard;
        this.estado = estado;
        this.tipo = tipo;
        this.posicion = posicion;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getColorCard() {
        return colorCard;
    }

    public void setColorCard(String colorCard) {
        this.colorCard = colorCard;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }
}
