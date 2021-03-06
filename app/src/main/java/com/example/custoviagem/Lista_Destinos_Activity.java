package com.example.custoviagem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custoviagem.adapter.DestinoAdapter;
import com.example.custoviagem.database.DBOpenHelper;
import com.example.custoviagem.database.dao.CustoViagemDAO;
import com.example.custoviagem.database.model.CustoViagemModel;
import com.example.custoviagem.database.model.UsuarioModel;
import com.example.custoviagem.util.Shared;

import java.util.ArrayList;
import java.util.List;

public class Lista_Destinos_Activity extends AppCompatActivity {

    private ListView listaDestinos;
    private TextView txtPerfil;
    private DestinoAdapter destinoAdapter;
    private Button novoCalculo;
    private Shared shared;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_destinos);


        // pegando o usuario salvo no shared e alterar o nome do perfil
        shared = new Shared(Lista_Destinos_Activity.this);
        txtPerfil = findViewById(R.id.txtPerfil);
        txtPerfil.setText(shared.getString(Shared.COLUNA_USUARIO));


        listaDestinos = (ListView) findViewById(R.id.lista_destinos);
        listaDestinos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                CustoViagemModel custo = (CustoViagemModel) listaDestinos.getItemAtPosition(position);
                AlertDialog dialog = new AlertDialog.Builder(Lista_Destinos_Activity.this)
                        .setTitle("Custo de viagem")
                        .setMessage("ID: " +" \t " +custo.getId()+
                                "\nTotal de Viajante: "+ " \t "+custo.getTotalViajante()+
                                "\nDura????o em Dias: "+ " \t " + custo.getDuracaoViagem()+
                                "\nCusto total da Viagem: "+ " \t " + custo.getCustoTotalViagem()+
                                "\nCusto Por Pessoa: "+ " \t " + custo.getCustoTotalPessoa()+
                                "\nOrigem: "+ " \t " + custo.getOrigem()+
                                "\nDestino: "+ " \t " + custo.getDestino())
                        .setNeutralButton("Sair", null)
                        .show();

                //add opcao editar com as ultimas inform????es salvas no shared preferences
            }
        });

        novoCalculo = (Button) findViewById(R.id.novo_calculo);
        novoCalculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNovoCalculo = new Intent(Lista_Destinos_Activity.this, CustosActivity.class);
                startActivity(intentNovoCalculo);
            }
        });

        registerForContextMenu(listaDestinos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    /**
     * fun????o para criar um menu contexto, ?? ativado assim que for presionado a op????o na lista
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem detalhe = menu.add("Eliminar");
        detalhe.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {


                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                CustoViagemModel custo = (CustoViagemModel) listaDestinos.getItemAtPosition(info.position);

//                Intent intentNovoCalculo = new Intent(Lista_Destinos_Activity.this, CustosActivity.class);
//                startActivity(intentNovoCalculo);

                DBOpenHelper dao = new DBOpenHelper(Lista_Destinos_Activity.this);
                dao.deleta(custo);
                dao.close();

                carregaLista();
                return false;
            }
        });
    }

    private void carregaLista() {
        CustoViagemDAO dao = new CustoViagemDAO(this);
        List<CustoViagemModel> custos = dao.Select();
        //dao.close();

        destinoAdapter = new DestinoAdapter(Lista_Destinos_Activity.this, custos);

//        ArrayAdapter<CustoViagemModel> adapter = new ArrayAdapter<CustoViagemModel>(this, custos);
        listaDestinos.setAdapter(destinoAdapter);
    }

}