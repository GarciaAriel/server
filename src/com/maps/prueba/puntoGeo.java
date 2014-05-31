package com.maps.prueba;

public class puntoGeo {
	private double latitude;
	private double longitude;
	private String tipo;
    private String usuario;

    public puntoGeo()  {
    }

    public puntoGeo(String tipo, String usuario,double latitude, double longitude) {
        this.tipo = tipo;
        this.usuario = usuario;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUsuario() {
        return usuario;
    }

    public double getLatitude() {
		return latitude;
	}
    
    public double getLongitude(){
    	return longitude;
    }
    
    @Override
    public String toString() {
        return "puntoGeo [tipo=" + tipo + ", usuario=" + usuario + ", latitude=" +latitude+ ", longitude="+longitude+"]";
    }
}
