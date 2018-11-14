package henriquez.daniel.prueba.app.com.login.Requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SelectAllActRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL="http://192.168.0.19/WebServiceApp/listaActividades.php";

    //Constructor.
    public SelectAllActRequest(Response.Listener<String> listener){
        super(Request.Method.POST,REGISTER_REQUEST_URL,listener,null);

    }

}
