package servlet.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class UserRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String EMAIL_LABEL="email";
	private final static String CMD_LABEL="cmd";
	private final static String PWD_LABEL="pwd";
	private DatastoreService datastore;
       
    public UserRegistrationServlet() {
        super();
    }
    
   @Override
   public void init() throws ServletException {
	   super.init();
	   //Create a connection to the datastore ONETIME at the init servlet process
		datastore = DatastoreServiceFactory.getDatastoreService();
   }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String cmdValue=request.getParameter(CMD_LABEL);
		if("Register".equals(cmdValue)){
			
			//Retreive Parameter to HTTP request
			String emailValue=request.getParameter(EMAIL_LABEL);
			String pwdValue= request.getParameter(PWD_LABEL);
			
			//Create Entity for the datastore
			Entity employee = new Entity("User");
			
			//Add Properties to the Entity
			employee.setProperty(EMAIL_LABEL, emailValue);
			employee.setProperty(PWD_LABEL, pwdValue);
			Date hireDate = new Date();
			employee.setProperty("hireDate", hireDate);
			
			//Add the new Entity to the dataStore
			datastore.put(employee);
			
			//Format the answer
			response.setContentType("application/json");
			JSONObject jsonToSend;
			jsonToSend=new JSONObject();
			
			jsonToSend.put("userid", employee.getKey().getId());
			
			//Send the Json object to the web browser
			PrintWriter out= response.getWriter();
			out.write(jsonToSend.toString());
			
		}
	}

}
