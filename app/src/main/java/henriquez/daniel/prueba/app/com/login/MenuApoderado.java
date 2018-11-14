package henriquez.daniel.prueba.app.com.login;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import henriquez.daniel.prueba.app.com.login.Clases.Usuario;
import henriquez.daniel.prueba.app.com.login.Fragments.FragmentActividadAp;
import henriquez.daniel.prueba.app.com.login.Fragments.FragmentAgregaActividad;
import henriquez.daniel.prueba.app.com.login.Fragments.FragmentAlumnoAp;
import henriquez.daniel.prueba.app.com.login.Fragments.FragmentContratoAp;
import henriquez.daniel.prueba.app.com.login.Fragments.FragmentDetalleAporte;
import henriquez.daniel.prueba.app.com.login.Fragments.FragmentListaDestinos;
import henriquez.daniel.prueba.app.com.login.Fragments.FragmentListaServicios;
import henriquez.daniel.prueba.app.com.login.Fragments.FragmentIngresaPago;
import henriquez.daniel.prueba.app.com.login.Fragments.FragmentPagoAp;

public class MenuApoderado extends AppCompatActivity implements FragmentContratoAp.comunicaActivity,FragmentAlumnoAp.comunicaAct,FragmentActividadAp.nuevaAct, FragmentPagoAp.pago{

    private DrawerLayout drawerLayout;
    private LinearLayout mainLayout;
    private ListView menuLateral;
    FragmentManager fragmentManager = getSupportFragmentManager();
    Bundle args;
    int rutAp;
    String nomApo;
    String perfil;
    String[] opciones;

    Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_apoderado);

        ActionBar aBar = getSupportActionBar();
        aBar.setHomeButtonEnabled(true);
        aBar.setDisplayHomeAsUpEnabled(true);
        aBar.setHomeAsUpIndicator(R.drawable.home_menu);

        //Bundle bundle = getIntent().getExtras();

        Bundle bReceptor = getIntent().getExtras();
        if (bReceptor != null){
            user = (Usuario) bReceptor.getSerializable("usuario");
            perfil = (bReceptor.getString("idPerfil")).toLowerCase();
        }
        //rutAp = bundle.getInt("rutApoderado");
        //nomApo = bundle.getString("nomApoderado");
        args = new Bundle();
        args.putInt("rutUsr",user.getRut());
        args.putString("nomUsr",user.getNombre());
        args.putString("perfilUsr", perfil);


        drawerLayout = findViewById(R.id.drawerLayout);
        mainLayout = findViewById(R.id.mainLayout);
        menuLateral = findViewById(R.id.menuLateral);
        //int rut = getIntent().getIntExtra("rutApoderado",1);

        switch (perfil){
            case "apoderado":
                opciones = new String[]{"Contrato", "Pagos", "Actividades", "Alumno", "Salir"};
                break;
            case "ejecutivo":
                opciones = new String[]{"Contrato", "Pagos", "Actividades", "Alumno", "Salir"};
                break;
            case "encargado curso":
                opciones = new String[]{"Contrato", "Pagos", "Actividades", "Alumno", "Salir"};
                break;
            case "dueño":
                opciones = new String[]{"Contrato", "Pagos", "Actividades", "Alumno", "Salir"};
                break;
            case "administrador":
                opciones = new String[]{"Contrato", "Pagos", "Actividades", "Alumno", "Salir"};
                break;
            default:
                opciones = new String[]{"Contrato", "Pagos", "Actividades", "Alumno", "Salir"};
                break;
        }

        FragmentContratoAp fmContraAp = new FragmentContratoAp();
        fmContraAp.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.mainLayout, fmContraAp).addToBackStack(null)
                .commit();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MenuApoderado.this, android.R.layout.simple_list_item_1, opciones);
        menuLateral.setAdapter(adapter);

        menuLateral.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String opcSeleccionado = (String) menuLateral.getAdapter().getItem(position);
                Toast.makeText(MenuApoderado.this, opcSeleccionado, Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(menuLateral);

                //FragmentManager fragmentManager = getSupportFragmentManager();

                switch (perfil){
                    case "apoderado":
                        if (opcSeleccionado == "Contrato") {
                            FragmentContratoAp fmContraAp = new FragmentContratoAp();
                            fmContraAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmContraAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Pagos"){
                            FragmentPagoAp fmPagAp = new FragmentPagoAp();
                            fmPagAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmPagAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Actividades"){
                            FragmentActividadAp fmActAp = new FragmentActividadAp();
                            fmActAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmActAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Alumno"){
                            FragmentAlumnoAp fmAlumAp = new FragmentAlumnoAp();
                            fmAlumAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmAlumAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Salir"){
                            Intent intent = new Intent(MenuApoderado.this,MainActivity.class);
                            MenuApoderado.this.startActivity(intent);
                        }
                        break;
                    case "ejecutivo":
                        break;
                    case "encargado curso":
                        if (opcSeleccionado == "Contrato") {
                            FragmentContratoAp fmContraAp = new FragmentContratoAp();
                            fmContraAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmContraAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Pagos"){
                            FragmentPagoAp fmPagAp = new FragmentPagoAp();
                            fmPagAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmPagAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Actividades"){
                            FragmentActividadAp fmActAp = new FragmentActividadAp();
                            fmActAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmActAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Alumno"){
                            FragmentAlumnoAp fmAlumAp = new FragmentAlumnoAp();
                            fmAlumAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmAlumAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Salir"){
                            Intent intent = new Intent(MenuApoderado.this,MainActivity.class);
                            MenuApoderado.this.startActivity(intent);
                        }
                        break;
                    case "dueño":
                        break;
                    case "administrador":
                        break;
                    default:
                        if (opcSeleccionado == "Contrato") {
                            FragmentContratoAp fmContraAp = new FragmentContratoAp();
                            fmContraAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmContraAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Pagos"){
                            FragmentPagoAp fmPagAp = new FragmentPagoAp();
                            fmPagAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmPagAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Actividades"){
                            FragmentActividadAp fmActAp = new FragmentActividadAp();
                            fmActAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmActAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Alumno"){
                            FragmentAlumnoAp fmAlumAp = new FragmentAlumnoAp();
                            fmAlumAp.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmAlumAp).addToBackStack(null)
                                    .commit();
                        }else if (opcSeleccionado == "Salir"){
                            int sesion = 1;
                            Bundle argsSalir = new Bundle();
                            argsSalir.putInt("sesion",sesion);
                            Intent intent = new Intent(MenuApoderado.this,MainActivity.class);
                            intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtras(argsSalir);
                            MenuApoderado.this.startActivity(intent);
                            finish();
                        }
                        break;
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            if (drawerLayout.isDrawerOpen(menuLateral)){
                drawerLayout.closeDrawer(menuLateral);
            }else{
                drawerLayout.openDrawer(menuLateral);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void comunicaServicios(int cursoId){
        Bundle argsListServ;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentListaServicios fmListServ = new FragmentListaServicios();

        if(fmListServ instanceof FragmentListaServicios){
            argsListServ = new Bundle();
            argsListServ.putInt("idCurso",cursoId);
            fmListServ.setArguments(argsListServ);
            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmListServ).addToBackStack(null)
                    .commit();

        }
    }

    @Override
    public void comunicaDestinos(int cursoId){
        Bundle argsListDesti;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentListaDestinos fmListDesti = new FragmentListaDestinos();

        if(fmListDesti instanceof FragmentListaDestinos){
            argsListDesti = new Bundle();
            argsListDesti.putInt("idCurso",cursoId);
            fmListDesti.setArguments(argsListDesti);
            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmListDesti).addToBackStack(null)
                    .commit();

        }
    }

    @Override
    public void comunicaAct(String rutAlum){
        Bundle argsDetApor;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentDetalleAporte fmDetApor = new FragmentDetalleAporte();

        if(fmDetApor instanceof FragmentDetalleAporte){
            argsDetApor = new Bundle();
            argsDetApor.putString("rutAlum",rutAlum);
            argsDetApor.putInt("rutApoderado",rutAp);
            fmDetApor.setArguments(argsDetApor);
            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmDetApor).addToBackStack(null)
                    .commit();

        }
    }

    @Override
    public void nueAct(int idCurso){
        Bundle argsNewAct;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentAgregaActividad fmNewAct = new FragmentAgregaActividad();

        if(fmNewAct instanceof FragmentAgregaActividad){
            argsNewAct = new Bundle();
            argsNewAct.putInt("idCurso",idCurso);
            //argsNewAct.putInt("rutApoderado",rutAp);
            fmNewAct.setArguments(argsNewAct);
            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmNewAct).addToBackStack(null)
                    .commit();

        }
    }

    @Override
    public void pagar(String nroCta, String nomCurso, String nomAlum) {
        Bundle argsIngrePag;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentIngresaPago fmIngrePag = new FragmentIngresaPago();

        if(fmIngrePag instanceof FragmentIngresaPago){
            argsIngrePag = new Bundle();
            argsIngrePag.putString("nroCta",nroCta);
            argsIngrePag.putString("nomCurso",nomCurso);
            argsIngrePag.putString("nomAlum",nomAlum);
            argsIngrePag.putString("nomApo",nomApo);
            argsIngrePag.putString("perfilUsr",perfil);
            fmIngrePag.setArguments(argsIngrePag);
            fragmentManager.beginTransaction().replace(R.id.mainLayout, fmIngrePag).addToBackStack(null)
                    .commit();

        }
    }
}
