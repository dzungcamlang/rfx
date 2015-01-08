package sample.server.item.track;

import org.vertx.java.core.http.HttpServerRequest;

import server.http.handler.BaseHttpHandler;
import server.http.util.LogHandlerUtil;
import server.http.util.RedirectUtil;

public class ItemTrackHttpHandler extends BaseHttpHandler {
		
	private static final String FAVICON_ICO = "favicon.ico";
	private static final String DIRECT_WRITE = "direct-write";
	private static final String PING = "ping";
	static final String logItemTracking = "tk";
	static final String logUserActivity = "u";
	static final String redirectClickPrefix = "r/";

	@Override
	public boolean handle(HttpServerRequest req) {
		String uri;
		if(req.uri().startsWith("/")){
			uri = req.uri().substring(1);	
		} else {
			uri = req.uri();
		}
		
		System.out.println("URI: " + uri);		
		//common
		if(uri.startsWith(DIRECT_WRITE)){			
			String json = req.params().get("data");
			LogHandlerUtil.trackingResponse(req);
			LogHandlerUtil.logRequestToKafka(json);			
			return true;
		} 
		else if (uri.equalsIgnoreCase(FAVICON_ICO)) {
			LogHandlerUtil.trackingResponse(req);
			return true;
		}
		else if (uri.equalsIgnoreCase(PING)) {
			req.response().end("PONG");
			return true;
		}
		else if (uri.startsWith(redirectClickPrefix)) {
			RedirectUtil.redirect(uri, req);
			return true;
		}		
		//just for dev
		else if(uri.startsWith(logItemTracking)){
			//handle request for ITEM TRACKING				
			LogHandlerUtil.logRequestToKafka(req, logItemTracking);
			return true;
		}	
		else if(uri.startsWith(logUserActivity)){
			//handle request for ITEM TRACKING				
			LogHandlerUtil.logRequestToKafka(req, logUserActivity);
			return true;
		} 
		return false;
	}

}
