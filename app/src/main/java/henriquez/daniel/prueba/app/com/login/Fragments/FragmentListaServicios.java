package henriquez.daniel.prueba.app.com.login.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import henriquez.daniel.prueba.app.com.login.Clases.Servicio;
import henriquez.daniel.prueba.app.com.login.R;
import henriquez.daniel.prueba.app.com.login.Requests.TraerContratoCursoRequest;

public class FragmentListaServicios extends Fragment {

    int cursoId;
    //String rutApo;
    ListView lvServicios;
    ArrayList<Servicio> servicios = new ArrayList<>();
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fragment_lista_servicios, container, false);
        cursoId = getArguments().getInt("idCurso");
        //rutApo = String.valueOf(getArguments().getInt("rutApoderado"));
        lvServicios = rootView.findViewById(R.id.listaServicios);

        try
        {
            Response.Listener<String> respoListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        //JSONObject jo = new JSONObject(response);
                        JSONArray jaContrato = new JSONArray(response);
                        //JSONArray jaContrato = jo.getJSONArray("ArrayOfContrato");

                        for (int i = 0; i < jaContrato.length(); i++){
                            JSONObject jsonContrato = jaContrato.getJSONObject(i);
                            JSONObject jsonCurso = jsonContrato.getJSONObject("Curso");

                            if (jsonCurso.getInt("Id") == cursoId){
                                JSONArray jaServiciosLista = jsonContrato.getJSONArray("ListaServiciosAsociados");
                                for (int j = 0; j < jaServiciosLista.length(); j++){
                                    JSONObject joServicioAsoci = jaServiciosLista.getJSONObject(j);
                                    JSONObject joServicio = joServicioAsoci.getJSONObject("Servicio");
                                    servicios.add(new Servicio(joServicio.getInt("Id"),
                                            joServicio.getString("Descripcion").trim(),
                                            joServicio.getString("Nombre").trim(),
                                            joServicio.getInt("Valor")));
                                }
                                break;
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    AdaptadorServicios adaptador = new AdaptadorServicios(getActivity().getApplicationContext(), servicios);
                    lvServicios.setAdapter(adaptador);
                }
            };
            TraerContratoCursoRequest traerContratoCursoRequest = new TraerContratoCursoRequest(respoListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(traerContratoCursoRequest);

        }catch(Exception e)
        {
            e.printStackTrace();
        }



        return rootView;
    }

    class AdaptadorServicios extends ArrayAdapter<Servicio> {

        private Context context;
        private ArrayList<Servicio> datos;

        //Constructor, parametros: contexto (actividad donde estamos), array de datos.
        public AdaptadorServicios(Context context, ArrayList<Servicio> datos) {
            //llamada al constructor padre. (extends).
            super(context, R.layout.list_item_servicios, datos);
            this.context = context;
            this.datos = datos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View item = inflater.inflate(R.layout.list_item_servicios, null);

            TextView lblId = (TextView)item.findViewById(R.id.LblId);
            lblId.setText("Codigo servicio: " + String.valueOf(datos.get(position).getId()));

            TextView lblServicio = (TextView)item.findViewById(R.id.LblServicio);
            lblServicio.setText("Nombre: " + datos.get(position).getNombre());

            TextView lblServDescrip = (TextView)item.findViewById(R.id.LblServDescrip);
            lblServDescrip.setText("Descripcion: " + datos.get(position).getDescripcion());

            TextView lblValor = (TextView)item.findViewById(R.id.LblValorServi);
            lblValor.setText("Costo: $" + String.valueOf(datos.get(position).getValor()));

            return item;
        }
    }
}
