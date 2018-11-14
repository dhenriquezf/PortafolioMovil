package henriquez.daniel.prueba.app.com.login.Requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class TraerContratoCursoRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL="http://portafolio-duoc.centralus.cloudapp.azure.com:82/Api/contrato";

    //Constructor.
    public TraerContratoCursoRequest(Response.Listener<String> listener){
        super(Request.Method.GET,REGISTER_REQUEST_URL,listener,null);
    }
}
