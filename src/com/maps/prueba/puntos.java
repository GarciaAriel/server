package com.maps.prueba;

import java.util.ArrayList;
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
    private static Map<String, puntoGeo> puntosGeo = new LinkedHashMap<String, puntoGeo>();

    puntoGeo puntoNulo = new puntoGeo("no", "no",1,1);
    
    static {
        puntoGeo[] puntos = new puntoGeo[]{
            new puntoGeo("casa", "garcia",-17.435477,-66.161418), 
            new puntoGeo("universidad", "garcia",-17.393894,-66.149369), 
        };
        for (puntoGeo punto : puntos) {
            puntosGeo.put(punto.getTipo()+punto.getUsuario(), punto);
        }
    }

    @GET
    @Path("/getPuntos")
    public Collection<puntoGeo> getPuntos() {
        Collection<puntoGeo> result = puntosGeo.values();
        log.info("getPoints: " + result);
        return result;
    }

    @GET
    @Path("/getPunto/{tipo}/{usuario}")
    public Collection<puntoGeo> getPunto(@PathParam("tipo") String tipo , @PathParam("usuario") String usuario) {
        puntoGeo punto = puntosGeo.get(tipo+usuario);
        Collection<puntoGeo> result = new ArrayList<puntoGeo>();
        
        if (punto != null)
        {
        	log.info("getBook: " + punto);
        	result.add(punto);
            return result;
        }
        log.info("getBook: " + puntoNulo);
        return result;
    }
    //
    @GET
    @Path("/setPunto/{tipo}/{usuario}/{latitude}/{longitude}")
    public Collection<puntoGeo> setPunto(@PathParam("tipo") String tipo,@PathParam("usuario") String usuario,@PathParam("latitude") double latitude,@PathParam("longitude") double longitude) {
    	puntoGeo punto = new puntoGeo(tipo,usuario,latitude,longitude);
    	Collection<puntoGeo> result = new ArrayList<puntoGeo>();
    	if (punto != null)
        {
    		puntosGeo.put(tipo+usuario, punto);
        	log.info("getBook: " + punto);
        	result.add(punto);
        	System.out.println("seeeee");
        	return result;
        	
            
        }
        log.info("addBook: " + puntoNulo);
        System.out.println("nuuuuuu");
        return result;
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
