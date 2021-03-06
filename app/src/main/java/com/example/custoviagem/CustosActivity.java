package com.example.custoviagem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.custoviagem.database.dao.CustoViagemDAO;
import com.example.custoviagem.database.model.CustoViagemModel;

import java.text.DecimalFormat;

public class CustosActivity extends AppCompatActivity {

    // origem e destino
    private EditText table_origem, table_destino;

    // Variaveis da tabela de de Gasolina
    private EditText table_totalKM, table_mediaKM_LT, table_custoMedioLT, table_total_veiculos;

    // Variaveis da tabela de Tarifa Aerea
    private EditText table_custo_por_pessoa, table_aluguel_veiculo;

    // Variaveis da tabela de Refeições
    private EditText table_custo_por_refeicao, table_custo_refeicao_dia;

    // Variaveis da tabela de Hospedagem
    private EditText table_custo_medio_noite, table_total_noites, table_total_quartos;

    // Variaveis da tabela de Entretenimento / Diversos
    private EditText edt_descricao_diversos1, edt_descricao_diversos2, edt_descricao_diversos3, edt_descricao_diversos4;
    private EditText edt_diversos1, edt_diversos2, edt_diversos3, edt_diversos4, table_totalpessoas, table_duracao_viagem;

    // Variaveis do totalizador geral de todas tabelas
    private TextView total_gasolina, total_tarifaAerea, total_refeicoes, total_hospedagem, total_diversos;

    // Variaveis dos checkbox de todas tabelas
    private CheckBox check_gasolina, check_tarifaAerea, check_refeicoes, check_hospedagem, check_entretenimento1, check_entretenimento2, check_entretenimento3, check_entretenimento4;
    private ImageView imgCalcularCusto, imgGravar, imgListar;
    private CustoViagemDAO CustoDAO;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custos);

        // Instancia o banco de dados.
        CustoDAO = new CustoViagemDAO(CustosActivity.this);

        // Criação dos componentes. DESTINO E ORIGEM
        table_origem = (EditText) findViewById(R.id.table_origem);
        table_destino = (EditText) findViewById(R.id.table_destino);

        // Criação dos componentes. GASOLINA
        table_totalpessoas = findViewById(R.id.table_totalpessoas);
        table_duracao_viagem = findViewById(R.id.table_duracao_viagem);
        check_gasolina = findViewById(R.id.check_gasolina);
        table_totalKM = findViewById(R.id.table_totalKM);
        table_mediaKM_LT = findViewById(R.id.table_mediaKM_LT);
        table_custoMedioLT = findViewById(R.id.table_custoMedioLT);
        table_total_veiculos = findViewById(R.id.table_total_veiculos);
        total_gasolina = (TextView) findViewById(R.id.total_gasolina);

        // Criação dos componentes. TARIFA AEREA
        check_tarifaAerea = findViewById(R.id.check_tarifaAerea);
        table_custo_por_pessoa = findViewById(R.id.table_custo_por_pessoa);
        table_aluguel_veiculo = findViewById(R.id.table_aluguel_veiculo);
        total_tarifaAerea = (TextView) findViewById(R.id.total_tarifaAerea);

        // Criação dos componentes. HOSPEDAGEM
        check_hospedagem = findViewById(R.id.check_hospedagem);
        table_custo_medio_noite = findViewById(R.id.table_custo_medio_noite);
        table_total_noites = findViewById(R.id.table_total_noites);
        table_total_quartos = findViewById(R.id.table_total_quartos);
        total_hospedagem = (TextView) findViewById(R.id.total_hospedagem);

        // Criação dos componentes. REFEIÇÕES
        check_refeicoes = findViewById(R.id.check_refeicoes);
        table_custo_por_refeicao = findViewById(R.id.table_custo_por_refeicao);
        table_custo_refeicao_dia = findViewById(R.id.table_custo_refeicao_dia);
        total_refeicoes = (TextView) findViewById(R.id.total_refeicoes);

        // Criação dos componentes. ENTRETENIMENTO / DIVERSOS
        check_entretenimento1 = findViewById(R.id.check_entretenimento1);
        check_entretenimento2 = findViewById(R.id.check_entretenimento2);
        check_entretenimento3 = findViewById(R.id.check_entretenimento3);
        check_entretenimento4 = findViewById(R.id.check_entretenimento4);
        edt_descricao_diversos1 = findViewById(R.id.edt_descricao_diversos1);
        edt_descricao_diversos2 = findViewById(R.id.edt_descricao_diversos2);
        edt_descricao_diversos3 = findViewById(R.id.edt_descricao_diversos3);
        edt_descricao_diversos4 = findViewById(R.id.edt_descricao_diversos4);
        edt_diversos1 = findViewById(R.id.edt_diversos1);
        edt_diversos2 = findViewById(R.id.edt_diversos2);
        edt_diversos3 = findViewById(R.id.edt_diversos3);
        edt_diversos4 = findViewById(R.id.edt_diversos4);
        total_diversos = (TextView) findViewById(R.id.total_diversos);

        // Insert dos custos no banco de dados
        imgCalcularCusto = findViewById(R.id.btnCalcularCusto);
        imgCalcularCusto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Validação de campos obrigatórios
                if(table_totalpessoas.getText().toString().equals("0")){
                    Toast.makeText(CustosActivity.this, "Campo total de viajantes obrigatório", Toast.LENGTH_LONG).show();
                }else if(table_duracao_viagem.getText().toString().equals("0")){
                    Toast.makeText(CustosActivity.this, "Campo duração de viagem obrigatório", Toast.LENGTH_LONG).show();
                }else if(table_origem.getText().toString().equals("")){
                    Toast.makeText(CustosActivity.this, "Campo origem obrigatório", Toast.LENGTH_LONG).show();
                }else if(table_destino.getText().toString().equals("")){
                    Toast.makeText(CustosActivity.this, "Campo destino obrigatório", Toast.LENGTH_LONG).show();
                }else{

                    // Formatação do campo decimal
                    DecimalFormat df = new DecimalFormat("#.00");

                    // Instancia do model
                    CustoViagemModel model = new CustoViagemModel();

                    // Total de viajantes
                    double total_viajantes = Double.parseDouble(String.valueOf(table_totalpessoas.getText()));

                    //Duração da viagem (DIAS)
                    double qtd_dias_viagem = Double.parseDouble(String.valueOf(table_duracao_viagem.getText()));

                    // Custos da tabela Gasolina
                    double quilometros = Double.parseDouble(String.valueOf(table_totalKM.getText()));
                    double mediaKMLT = Double.parseDouble(String.valueOf(table_mediaKM_LT.getText()));
                    double custo_medio_LT = Double.parseDouble(String.valueOf(table_custoMedioLT.getText()));
                    double total_veiculos = Double.parseDouble(String.valueOf(table_total_veiculos.getText()));

                    // Custos da tabela Tarifa Aerea
                    double custo_estimado_pessoa = Double.parseDouble(String.valueOf(table_custo_por_pessoa.getText()));
                    double aluguel_veiculo = Double.parseDouble(String.valueOf(table_aluguel_veiculo.getText()));

                    // Custos da tabela Refeicoes
                    double custo_estimado_refeicao = Double.parseDouble(String.valueOf(table_custo_por_refeicao.getText()));
                    double refeicoes_dia = Double.parseDouble(String.valueOf(table_custo_refeicao_dia.getText()));

                    // Custos da tabela Hospedagem
                    double custo_medio_noite = Double.parseDouble(String.valueOf(table_custo_medio_noite.getText()));
                    double total_noites = Double.parseDouble(String.valueOf(table_total_noites.getText()));
                    double total_quartos = Double.parseDouble(String.valueOf(table_total_quartos.getText()));

                    // Custos da tabela Diversos
                    double custo_diversos = 0;


                    // Verificação do valor do check para considerar ou não valor total da tabela na soma do total do custo da viagem
                    if(check_entretenimento1.isChecked()){
                        double custo1 = Double.parseDouble(String.valueOf(edt_diversos1.getText()));
                        custo_diversos = custo1;
                    } else {
                        custo_diversos += 0;
                    }
                    if(check_entretenimento2.isChecked()){
                        double custo2 = Double.parseDouble(String.valueOf(edt_diversos2.getText()));
                        custo_diversos += custo2;
                    } else {
                        custo_diversos += 0;
                    }
                    if(check_entretenimento3.isChecked()){
                        double custo3 = Double.parseDouble(String.valueOf(edt_diversos3.getText()));
                        custo_diversos += custo3;
                    } else {
                        custo_diversos += 0;
                    }
                    if(check_entretenimento4.isChecked()){
                        double custo4 = Double.parseDouble(String.valueOf(edt_diversos4.getText()));
                        custo_diversos += custo4;
                    } else {
                        custo_diversos += 0;
                    }

                    //Variaveis para guardar o valor total dos custos das tabelas
                    double custo_gasolina = 0;
                    if(check_gasolina.isChecked()){
                        custo_gasolina = ((quilometros / mediaKMLT) * custo_medio_LT) / total_veiculos;
                    } else {
                        custo_gasolina = 0;
                    }

                    double custo_hospedagem = 0;
                    if(check_hospedagem.isChecked()){
                        custo_hospedagem = (custo_medio_noite * total_noites) * total_quartos;
                    } else{
                        custo_hospedagem = 0;
                    }

                    double custo_tarifaAerea = 0;
                    if(check_tarifaAerea.isChecked()){
                        custo_tarifaAerea = (custo_estimado_pessoa * total_viajantes) + aluguel_veiculo;
                    } else {
                        custo_tarifaAerea = 0;
                    }

                    double custo_refeicoes = 0;
                    if(check_refeicoes.isChecked()){
                        custo_refeicoes = ((refeicoes_dia * total_viajantes) * custo_estimado_refeicao) * qtd_dias_viagem;
                    } else {
                        custo_refeicoes = 0;
                    }

                    // Custo total da viagem -> Convertendo o resultado final para String
                    double custo_total_viagem = custo_diversos + custo_gasolina + custo_refeicoes + custo_hospedagem + custo_tarifaAerea;
                    custo_total_viagem = Double.valueOf(df.format(custo_total_viagem));
                    String TXT_custo_total_viagem = Double.toString(custo_total_viagem);

                    // Custo por pessoa -> Convertendo o resultado final para String
                    double custo_por_pessoa = Double.valueOf(df.format(custo_total_viagem / total_viajantes));
                    String TXT_custo_por_pessoa = Double.toString(custo_por_pessoa);

                    // Insere o valor de todos os custos da tabela Gasolina no textview TOTAL
                    total_gasolina.setText(String.valueOf("R$ " + df.format(custo_gasolina)));
                    total_tarifaAerea.setText((String.valueOf("R$ " + df.format(custo_tarifaAerea))));
                    total_refeicoes.setText((String.valueOf("R$ " + df.format(custo_refeicoes))));
                    total_hospedagem.setText((String.valueOf("R$ " + df.format(custo_hospedagem))));
                    total_diversos.setText((String.valueOf("R$ " + df.format(custo_diversos))));

                    // Mensagem de sucesso da simulação dos custos
                    Toast.makeText(CustosActivity.this, "Custos Calculados com sucesso!", Toast.LENGTH_LONG).show();

                    // Botão que executa a função de INSERT no BD
                    imgGravar = findViewById(R.id.btnGravar);
                    imgGravar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //Set (insert) na tabela TotalViajante
                            model.setTotalViajante(table_totalpessoas.getText().toString());
                            model.setDuracaoViagem(table_duracao_viagem.getText().toString());
                            model.setCustoTotalViagem(TXT_custo_total_viagem);
                            model.setCustoTotalPessoa(TXT_custo_por_pessoa);
                            model.setOrigem(table_origem.getText().toString());
                            model.setDestino(table_destino.getText().toString());

                            // Verificação de erro ao fazer o INSERT no banco de dados
                            if (CustoDAO.Insert(model) != -1) {
                                Toast.makeText(CustosActivity.this, "Custos gravados com sucesso!", Toast.LENGTH_LONG).show();

                                // Chama a activity de lista de simulações ja gravadas no BD (SELECT *)
                                startActivity(new Intent(CustosActivity.this, Lista_Destinos_Activity.class));
                            } else {
                                Toast.makeText(CustosActivity.this, "Ocorreu um erro!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        // Chama activity de Lista de simulações já gravados no BD
        imgListar = findViewById(R.id.btnListar);
        imgListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CustosActivity.this, "Lista de Simulações!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CustosActivity.this, Lista_Destinos_Activity.class));
            }
        });
    }
}
