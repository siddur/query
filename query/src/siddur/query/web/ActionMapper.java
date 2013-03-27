package siddur.query.web;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.util.log.Log;

public class ActionMapper{

	private static final String ACTION_CONFIG = "action-list";
	
	private Map<String, Method> methodMap = new HashMap<String, Method>();
	private Map<String, Action> actionMap = new HashMap<String, Action>();
	
	
	public ActionMapper(){
		InputStream is = getClass().getResourceAsStream(ACTION_CONFIG);
		Properties p = new Properties();
		
		try {
			p.load(is);
			is.close();
		} catch (IOException e) {
			Log.warn(e);
		}
			
			for(Object keyObj :p.keySet()){
				String key = (String)keyObj;
				String path = p.getProperty(key);
				Class<?> clz;
				try {
					clz = Class.forName(key);
				} catch (ClassNotFoundException e) {
					Log.warn(e);
					continue;
				}
				if(Action.class.isAssignableFrom(clz)){
					Action action = null;
					try {
						action = (Action)clz.newInstance();
						Log.info("find action:" + clz.getName());
					} catch (Exception e) {
						Log.warn(e);
						continue;
					} 
					if(path == null){
						path = action.getPath();
					}
					path = "/" + path;
					Method[] methods = clz.getDeclaredMethods();
					for(Method m : methods){
						if((m.getModifiers() & Modifier.PUBLIC) == 1){
							Class<?>[] c = m.getParameterTypes();
							if(Result.class.isAssignableFrom(m.getReturnType())
									&& c.length == 2 
									&& HttpServletRequest.class.isAssignableFrom(c[0]) 
									&& HttpServletResponse.class.isAssignableFrom(c[1])){
								String methodName = m.getName().toLowerCase();
								String mpath = path + "/" + methodName;
								mpath = mpath.replace("\\", "/").replace("//", "/");
								methodMap.put(mpath, m);
								actionMap.put(mpath, action);
								Log.info("find method:" + m.getName());
							}
						}
					}
				}
				
			}
		
	}
	
	public Result exec(String path, HttpServletRequest req, HttpServletResponse resp){
		Action action = actionMap.get(path);
		Method method = methodMap.get(path);
		
		if(Log.isDebugEnabled()){
			Log.debug("path:" + path + "\tdealer:" + action.getClass().getName() + "." + method.getName());
		}
		if(action == null || method == null){
			return Result.NotFound;
		}
		try {
			Object obj = method.invoke(action, req, resp);
			Result r = (Result) obj;
			if(r.isRedirect()){
				return exec(r.getMessage(), req, resp);
			}
			return r;
		} catch (Exception e) {
			Log.warn(e);
			return Result.error(e.getMessage());
		}
	}
	
	public enum ResultType{
		ok,error,redirect
	}
	
	
	public static class Result {
		public static Result NotFound = new Result("404 not found", ResultType.error);
		
		private String message;
		private ResultType type;
		
		public Result(String message) {
			this.message = message;
		}
		
		public Result(String message, ResultType type) {
			this.message = message;
			this.type = type;
		}
		

		public static Result ok(String msg){
			return new Result(msg, ResultType.ok);
		}
		
		public static Result error(String msg){
			return new Result(msg, ResultType.error);
		}
		
		public static Result redirect(String msg){
			return new Result(msg, ResultType.redirect);
		}
		
		public static Result ok(){
			return ok(null);
		}
		
		public static Result error(){
			return error(null);
		}
		
		public static Result redirect(){
			return redirect(null);
		}
		
		public boolean isRedirect(){
			return type == ResultType.redirect;
		}
		
		public boolean isOK(){
			return type == ResultType.ok;
		}
		
		public boolean isError(){
			return type == ResultType.error;
		}
		
		public String getMessage(){
			return message;
		}
	}
}
