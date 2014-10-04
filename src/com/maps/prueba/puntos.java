package com.maps.prueba;

import java.sql.Date;
import java.text.DateFormat;
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

import sun.misc.GC.LatencyRequest;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

@Path("/puntos/")
@Consumes({"application/json"})
@Produces({"application/json"})
public class puntos {
	
	private static final Logger log = Logger.getLogger(puntos.class.getName());
    private static Map<String, puntosGeoDatosUsuario> mapPuntosGeoDatosUsuario = new LinkedHashMap<String, puntosGeoDatosUsuario>();
    private static Map<String, puntosGeoDatosBloqueoPersistente> mapPuntosGeoDatosPersistentes = new LinkedHashMap<String, puntosGeoDatosBloqueoPersistente>();
    private static Map<puntosGeoDatosBloqueoPersistente,Integer> mapPuntosGeoDatosPersistentesPosibles = new LinkedHashMap<puntosGeoDatosBloqueoPersistente,Integer >();
    private static Map<String, puntosGeoDatosBloqueoAlerta> mapPuntosGeoDatosAlertas = new LinkedHashMap<String, puntosGeoDatosBloqueoAlerta>();

    puntosGeoDatosUsuario puntoNulo = new puntosGeoDatosUsuario("no", "no",1,1);
    
    static 
    {
        puntosGeoDatosUsuario[] puntos = new puntosGeoDatosUsuario[]{ new puntosGeoDatosUsuario("no","no",1,1), new puntosGeoDatosUsuario("se","se",1,1)};
        for (puntosGeoDatosUsuario punto : puntos) {
            mapPuntosGeoDatosUsuario.put(punto.getTipo()+punto.getUsuario(), punto);
        }
    }
    static 
    {
    	puntosGeoDatosBloqueoPersistente[] puntosPersistentes = new puntosGeoDatosBloqueoPersistente[]{ };
        for (puntosGeoDatosBloqueoPersistente punto : puntosPersistentes) {
        	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            mapPuntosGeoDatosPersistentes.put(timeStamp, punto);
        }
    }
    
    static 
    {
    	puntosGeoDatosBloqueoAlerta[] puntosAlertas = new puntosGeoDatosBloqueoAlerta[]{ };
        for (puntosGeoDatosBloqueoAlerta punto : puntosAlertas) {
        	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            mapPuntosGeoDatosAlertas.put(timeStamp, punto);
        }
    }

    @GET
    @Path("/getUserPoints/{usuario}")
    public Collection<puntosGeoDatosUsuario> getUserPoints(@PathParam("usuario") String usuario) {
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
    @Path("/getPersistentLockPoints")
    public Collection<puntosGeoDatosBloqueoPersistente> getPersistentLockPoints() {
        Collection<puntosGeoDatosBloqueoPersistente> result = mapPuntosGeoDatosPersistentes.values();
        log.info("getPointsUser: " + result);
        return result;
    }
    
    @GET
    @Path("/getAlertLockPoints")
    public Collection<puntosGeoDatosBloqueoAlerta> getAlertLockPoints() throws java.text.ParseException {
    	depurarPuntos();
        Collection<puntosGeoDatosBloqueoAlerta> result = mapPuntosGeoDatosAlertas.values();
        log.info("getPointsUser: " + result);
        return result;
    }
    
    @GET
    @Path("/getPersistentPossiblePoints")
    public Collection<puntosGeoDatosBloqueoPersistente> getPersistentPossiblePoints() {
    	Map<String, puntosGeoDatosBloqueoPersistente> resul = new LinkedHashMap<String, puntosGeoDatosBloqueoPersistente>();
    	
    	for (Map.Entry<puntosGeoDatosBloqueoPersistente,Integer> entry : mapPuntosGeoDatosPersistentesPosibles.entrySet()) {
    		resul.put(entry.getKey().toString(),entry.getKey() );
    	}
        Collection<puntosGeoDatosBloqueoPersistente> result = resul.values();
        log.info("getPointsUser: " + result);
        return result;
    }

    @GET
    @Path("/getPoint/{tipo}/{usuario}")
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
    @Path("/setPoint/{tipo}/{usuario}/{latitude}/{longitude}")
    public Collection<puntosGeoDatosUsuario> setPoint(@PathParam("tipo") String tipo,@PathParam("usuario") String usuario,@PathParam("latitude") double latitude,@PathParam("longitude") double longitude) 
    {
    	puntosGeoDatosUsuario punto = new puntosGeoDatosUsuario(tipo,usuario,latitude,longitude);
    	Collection<puntosGeoDatosUsuario> result = new ArrayList<puntosGeoDatosUsuario>();
    	if (punto != null)
        {
    		mapPuntosGeoDatosUsuario.put(tipo+usuario, punto);
        	log.info("getPoint: " + punto);
        	result.add(punto);
        	System.out.println("datos usuario: "+mapPuntosGeoDatosUsuario.size());
        	System.out.println("puntos posibles: "+mapPuntosGeoDatosPersistentesPosibles.size());
        	System.out.println("puntos bloqueo: "+mapPuntosGeoDatosPersistentes.size());
        	System.out.println("puntos alerta: "+mapPuntosGeoDatosAlertas.size());
        	return result;
        }
        log.info("addPoint: " + puntoNulo);
        System.out.println("nuuuuuu");
        return result;
    }
    
    @GET
    @Path("/setLockPoint/{tipo}/{codigoUsuario}/{latitude}/{longitude}")
    public boolean setLockPoint (@PathParam("tipo") String tipo,@PathParam("codigoUsuario") String codigoUsuario,@PathParam("latitude") double latitude,@PathParam("longitude") double longitude) 
    {
    	boolean result = false;
    	puntosGeoDatosBloqueoPersistente punto = new puntosGeoDatosBloqueoPersistente(tipo,codigoUsuario,latitude,longitude);
    	if (punto != null)
        {
    		if (verificarSiPuedoAniadir(punto))//true aniadir 
    		{
    			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        		mapPuntosGeoDatosPersistentes.put(timeStamp, punto);
            	log.info("getPointBloqueo: " + punto);
            	
            	System.out.println("datos usuario: "+mapPuntosGeoDatosUsuario.size());
            	System.out.println("puntos posibles: "+mapPuntosGeoDatosPersistentesPosibles.size());
            	System.out.println("puntos bloqueo: "+mapPuntosGeoDatosPersistentes.size());
            	System.out.println("puntos alerta: "+mapPuntosGeoDatosAlertas.size());
            	
            	result=true;
            	
			}
    		System.out.println("datos usuario: "+mapPuntosGeoDatosUsuario.size());
        	System.out.println("puntos posibles: "+mapPuntosGeoDatosPersistentesPosibles.size());
        	System.out.println("puntos bloqueo: "+mapPuntosGeoDatosPersistentes.size());
        	System.out.println("puntos alerta: "+mapPuntosGeoDatosAlertas.size());
        }
        return false;
    }
    
    @GET
    @Path("/setAlertPoint/{tipo}/{codigoUsuario}/{latitude}/{longitude}")
    public boolean setAlertPoint (@PathParam("tipo") String tipo,@PathParam("codigoUsuario") String codigoUsuario,@PathParam("latitude") double latitude,@PathParam("longitude") double longitude) 
    {
    	boolean result = false;
    	puntosGeoDatosBloqueoAlerta punto = new puntosGeoDatosBloqueoAlerta(tipo,codigoUsuario,latitude,longitude);
    	if (punto != null)
        {
    		
    		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    		mapPuntosGeoDatosAlertas.put(timeStamp, punto);
    		
    		log.info("getPointBloqueo: " + punto);
        	result=true;
        	
        	System.out.println("datos usuario: "+mapPuntosGeoDatosUsuario.size());
        	System.out.println("puntos posibles: "+mapPuntosGeoDatosPersistentesPosibles.size());
        	System.out.println("puntos bloqueo: "+mapPuntosGeoDatosPersistentes.size());
        	System.out.println("puntos alerta: "+mapPuntosGeoDatosAlertas.size());
        	//return result;
        }
        log.info("addPoint: " + puntoNulo);
        return false;
    }
    
    private boolean verificarSiPuedoAniadir(puntosGeoDatosBloqueoPersistente punto)
    {
    	if (mapPuntosGeoDatosPersistentesPosibles!= null && mapPuntosGeoDatosPersistentesPosibles.size()>0) {
    		boolean aux = true;
    		for (Map.Entry<puntosGeoDatosBloqueoPersistente,Integer> entry : mapPuntosGeoDatosPersistentesPosibles.entrySet()) {
    			puntosGeoDatosBloqueoPersistente temporal = entry.getKey();
    			double distancia = CalculationByDistance(punto.getLatitude(),punto.getLongitude(),temporal.getLatitude(),temporal.getLongitude());
    			if (distancia<50)//mayor a 50 metros 
    			{
    				aux = false;
    				if (entry.getValue()==1) //conteo en dos borrar y retornar true para aniadir a la base real 
    				{
    					mapPuntosGeoDatosPersistentesPosibles.remove(entry.getKey());
    					return true;
					}
    				else
    				{
    					entry.setValue(entry.getValue()+1);
    					break;
    				}
        		}
        	}
    		if (aux) {
    			mapPuntosGeoDatosPersistentesPosibles.put(punto, 1);
			}
    	}
    	if (mapPuntosGeoDatosPersistentesPosibles!= null && mapPuntosGeoDatosPersistentesPosibles.size()==0) {
			mapPuntosGeoDatosPersistentesPosibles.put(punto, 1);
		}
    	
    	return false;
    }
    
    public static double CalculationByDistance(double lat1,double lon1,double lat2,double lon2) {
    	
    	double R = 6371; // km
    	double o1 = Math.toRadians(lat1);
    	double o2 = Math.toRadians(lat2);
    	double tri_o = Math.toRadians(lat2-lat1); //
    	double tri_l = Math.toRadians(lon2-lon1); //

    double a = Math.sin(tri_o/2)*Math.sin(tri_o/2)+Math.cos(o1)*Math.cos(o2)*Math.sin(tri_l/2)*Math.sin(tri_l/2);
    	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

    	double d = R * c;
    	return (d*1000);
    }
    private void depurarPuntos() throws java.text.ParseException
	{
		try {
			if (mapPuntosGeoDatosAlertas!= null && mapPuntosGeoDatosAlertas.size()>0) {
	    		for (Map.Entry<String, puntosGeoDatosBloqueoAlerta> entry : mapPuntosGeoDatosAlertas.entrySet()) {
	    			String temporal = entry.getKey();
	    			double diferencia = getDiferenciaTiempo(temporal);
	    			if (diferencia>=1){
	        			mapPuntosGeoDatosAlertas.remove(entry.getKey());
	    			}
	        	}
			}
	    	else
	    	{
	    		System.out.println("elseeeeeeeeeeeeeeeeee");
	    	}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	private double getDiferenciaTiempo(String temporal) throws java.text.ParseException
	{
		DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		java.util.Date date = null;
	 
		try {
			
			date = formatter.parse(temporal);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//resta de la fecha actual
		Calendar cal = Calendar.getInstance();
		
		java.util.Date date2 = cal.getTime();
		
		double result = ((date2.getTime()/60000) - (date.getTime()/60000));
		System.out.println("vamossss:  "+result);
		
		    	
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
