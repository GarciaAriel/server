package com.maps.prueba;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.ws.rs.Consumes; 
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/puntos/")
@Consumes({"application/json"})
@Produces({"application/json"})
public class puntos {
	private static final Logger log = Logger.getLogger(puntos.class.getName());
    private static Map<String, puntosGeoDatosUsuario> mapPuntosGeoDatosUsuario = new LinkedHashMap<String, puntosGeoDatosUsuario>();
    private static Map<String, puntosGeoDatosBloqueoPersistente> mapPuntosGeoDatosPersistentes = new LinkedHashMap<String, puntosGeoDatosBloqueoPersistente>();

    puntosGeoDatosUsuario puntoNulo = new puntosGeoDatosUsuario("no", "no",1,1);
    
    static 
    {
        puntosGeoDatosUsuario[] puntos = new puntosGeoDatosUsuario[]{
            new puntosGeoDatosUsuario("noooo", "garcia",-17.435477,-66.161418), 
            new puntosGeoDatosUsuario("uni", "garcia",-17.393894,-66.149369)
        };
        for (puntosGeoDatosUsuario punto : puntos) {
            mapPuntosGeoDatosUsuario.put(punto.getTipo()+punto.getUsuario(), punto);
        }
    }
    static 
    {
    	puntosGeoDatosBloqueoPersistente[] puntosPersistentes = new puntosGeoDatosBloqueoPersistente[]{
            new puntosGeoDatosBloqueoPersistente("garcia",-17.435477,-66.161418), 
            new puntosGeoDatosBloqueoPersistente("garcia",-17.393894,-66.149369)
        };
        for (puntosGeoDatosBloqueoPersistente punto : puntosPersistentes) {
        	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            mapPuntosGeoDatosPersistentes.put(timeStamp, punto);
        }
    }

    @GET
    @Path("/getPuntosUsuario/{usuario}")
    public Collection<puntosGeoDatosUsuario> getPuntosUsuario(@PathParam("usuario") String usuario) {
        Collection<puntosGeoDatosUsuario> all = mapPuntosGeoDatosUsuario.values();
        ArrayList<puntosGeoDatosUsuario> result = new ArrayList<puntosGeoDatosUsuario>(); 
        for (puntosGeoDatosUsuario elem : all) {
			if ((elem.getTipo().equals("casa"))||(elem.getTipo().equals("trabajo"))) {
				result.add(elem);	
			}
		}
        log.info("getPointsUser: " + result);
        return result;
    }
    
    @GET
    @Path("/getPuntosBloqueoPersistente")
    public Collection<puntosGeoDatosBloqueoPersistente> getPuntosBloqueoPersistente() {
        Collection<puntosGeoDatosBloqueoPersistente> result = mapPuntosGeoDatosPersistentes.values();
        log.info("getPointsUser: " + result);
        return result;
    }

    @GET
    @Path("/getPunto/{tipo}/{usuario}")
    public Collection<puntosGeoDatosUsuario> getPunto(@PathParam("tipo") String tipo , @PathParam("usuario") String usuario) {
        puntosGeoDatosUsuario punto = mapPuntosGeoDatosUsuario.get(tipo+usuario);
        Collection<puntosGeoDatosUsuario> result = new ArrayList<puntosGeoDatosUsuario>();
        
        if (punto != null)
        {
        	log.info("getPoint: " + punto);
        	result.add(punto);
            return result;
        }
        log.info("getPoint: " + puntoNulo);
        return result;
    }
    //
    @GET
    @Path("/setPunto/{tipo}/{usuario}/{latitude}/{longitude}")
    public Collection<puntosGeoDatosUsuario> setPunto(@PathParam("tipo") String tipo,@PathParam("usuario") String usuario,@PathParam("latitude") double latitude,@PathParam("longitude") double longitude) 
    {
    	puntosGeoDatosUsuario punto = new puntosGeoDatosUsuario(tipo,usuario,latitude,longitude);
    	Collection<puntosGeoDatosUsuario> result = new ArrayList<puntosGeoDatosUsuario>();
    	if (punto != null)
        {
    		mapPuntosGeoDatosUsuario.put(tipo+usuario, punto);
        	log.info("getPoint: " + punto);
        	result.add(punto);
        	System.out.println("ok: "+mapPuntosGeoDatosUsuario.size());
        	System.out.println( mapPuntosGeoDatosUsuario.size());
        	return result;
        }
        log.info("addPoint: " + puntoNulo);
        System.out.println("nuuuuuu");
        return result;
    }
    
    @GET
    @Path("/setPuntoBloqueo/{codigoUsuario}/{latitude}/{longitude}")
    public boolean setPuntoBloqueo(@PathParam("codigoUsuario") String codigoUsuario,@PathParam("latitude") double latitude,@PathParam("longitude") double longitude) 
    {
    	boolean result = false;
    	puntosGeoDatosBloqueoPersistente punto = new puntosGeoDatosBloqueoPersistente(codigoUsuario,latitude,longitude);
    	if (punto != null)
        {
    		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    		mapPuntosGeoDatosPersistentes.put(timeStamp, punto);
        	log.info("getPointBloqueo: " + punto);
        	result=true;
        	System.out.println("datos usuario: "+mapPuntosGeoDatosUsuario.size());
        	System.out.println("puntos bloqueo: "+mapPuntosGeoDatosPersistentes.size());
        	//return result;
        }
        log.info("addPoint: " + puntoNulo);
        return false;
    }
//;
//    @POST
//    @Path("/book/{isbn}")
//    public Book updateBook(@PathParam("isbn") String id, String title) {
//        Book book = books.get(id);
//        if (book != null) {
//            book.setTitle(title);
//        }
//        //log.infof("updateBook: %s", book);
//        log.info("updateBook: " + book);
//        return book;
//    }
//
//    @DELETE
//    @Path("/book/{isbn}")
//    public Book removeBook(@PathParam("isbn") String id) {
//        Book book = books.remove(id);
//        //log.infof("removeBook: %s", book);
//        log.info("removeBook: " + book);
//        return book;
//    }
}
