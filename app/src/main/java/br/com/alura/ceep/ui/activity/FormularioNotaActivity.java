package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;

import static br.com.alura.ceep.ui.activity.ActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.ActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.ActivityConstantes.POSICAO_INVALIDA;

/*
    Activity responsável pela tela com campos de preenchimento das notas
 */

public class FormularioNotaActivity extends AppCompatActivity {


    public static final String TITULO_APPBAR = "Nova Nota";
    private int posicaoRecebida = POSICAO_INVALIDA;
    private TextView titulo;
    private TextView descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(TITULO_APPBAR);

        inicializaCampos();

        //Inicializa campos com dados caso tenha a chave da nota
        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA)){
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA); //Recebe um Serialazeble com o dados da nota
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA); //Caso não seja recebido uma posição, srá passado um valor padrão invalido para que nao ocorra um NullPointerException
            preencheCampos(notaRecebida);
        }
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
    }

    private void preencheCampos(Nota notaRecebida) {
        titulo.setText(notaRecebida.getTitulo());
        descricao.setText(notaRecebida.getDescricao());
    }

    @Override
    //Infla as opcões do menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu); //Icone para salvar uma nota

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //Verifica o item de opções selecionado
    public boolean onOptionsItemSelected(MenuItem item) {
        //Cria uma Nota
        if(eMenuSalvaNota(item)){
            Nota notaCriada = criaNota();
            retornaNota(notaCriada);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //Instancia uma Intent que recebe uma Nota
    private void retornaNota(Nota notaCriada) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, notaCriada); //Acrescenta uma chave extra do tipo String a intent
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(RESULT_OK, resultadoInsercao); //setta uma chave do tipo final int para que possa ser identificado no metodo onActivityResult
    }

    @NonNull
    private Nota criaNota() {
        return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private boolean eMenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_ic_nota_salva;
    }
}
