package henriquez.daniel.prueba.app.com.login.Fragments;

import android.content.Context;
import android.net.Uri;
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

import henriquez.daniel.prueba.app.com.login.Clases.Destino;
import henriquez.daniel.prueba.app.com.login.R;
import henriquez.daniel.prueba.app.com.login.Requests.TraerContratoCursoRequest;

public class FragmentListaDestinos extends Fragment {

    int cursoId;
    //String rutApo;
    ListView lvDestinos;
    ArrayList<Destino> destinos = new ArrayList<>();
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fragment_lista_destinos, container, false);
        cursoId = getArguments().getInt("idCurso");
        lvDestinos = rootView.findViewById(R.id.listaDestinos);

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
                                JSONArray jaServiciosLista = jsonContrato.getJSONArray("ListaDestinosAsociados");
                                for (int j = 0; j < jaServiciosLista.length(); j++){
                                    JSONObject joServicioAsoci = jaServiciosLista.getJSONObject(j);
                                    JSONObject joServicio = joServicioAsoci.getJSONObject("Destino");
                                    destinos.add(new Destino(joServicio.getInt("Id"),
                                            joServicio.getString("Nombre").trim(),
                                            joServicio.getInt("Valor")));
                                }
                                break;
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                    AdaptadorDestinos adaptador = new AdaptadorDestinos(getActivity().getApplicationContext(), destinos);
                    lvDestinos.setAdapter(adaptador);
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

    class AdaptadorDestinos extends ArrayAdapter<Destino> {

        private Context context;
        private ArrayList<Destino> datos;

        //Constructor, parametros: contexto (actividad donde estamos), array de datos.
        public AdaptadorDestinos(Context context, ArrayList<Destino> datos) {
            //llamada al constructor padre. (extends).
            super(context, R.layout.list_item_destino, datos);
            this.context = context;
            this.datos = datos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View item = inflater.inflate(R.layout.list_item_destino, null);

            TextView lblId = (TextView)item.findViewById(R.id.LblId);
            lblId.setText("Codigo destino: " + String.valueOf(datos.get(position).getId()));

            TextView lblServicio = (TextView)item.findViewById(R.id.LblDestino);
            lblServicio.setText("Nombre: " + datos.get(position).getNombre());

            TextView lblValor = (TextView)item.findViewById(R.id.LblValorDesti);
            lblValor.setText("Costo: $" + String.valueOf(datos.get(position).getValor()));

            return item;
        }
    }
}
