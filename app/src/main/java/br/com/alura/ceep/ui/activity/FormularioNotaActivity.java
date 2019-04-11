package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;

import static br.com.alura.ceep.ui.activity.ActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.ActivityConstantes.CODIGO_RESULT_NOTA_CRIADA;

/*
    Activity responsável pela tela com campos de preenchimento das notas
 */

public class FormularioNotaActivity extends AppCompatActivity {


    public static final String TITULO_APPBAR = "Nova Nota";
    private int posicaoRecebida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(TITULO_APPBAR);

        //Inicializa campos com dados caso tenha a chave da nota
        Intent dadosRecebidos = getIntent();
        if(dadosRecebidos.hasExtra(CHAVE_NOTA) && dadosRecebidos.hasExtra("posicao")){
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA); //Recebe um Serialazeble com o dados da nota
            posicaoRecebida = dadosRecebidos.getIntExtra("posicao", -1);
            TextView titulo = findViewById(R.id.formulario_nota_titulo);
            titulo.setText(notaRecebida.getTitulo());
            TextView descricao = findViewById(R.id.formulario_nota_descricao);
            descricao.setText(notaRecebida.getDescricao());
        }
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
        resultadoInsercao.putExtra("posicao", posicaoRecebida);
        setResult(CODIGO_RESULT_NOTA_CRIADA, resultadoInsercao); //setta uma chave do tipo final int para que possa ser identificado no metodo onActivityResult
    }

    @NonNull
    private Nota criaNota() {
        TextView notaTitulo = findViewById(R.id.formulario_nota_titulo);
        TextView notaDescricao = findViewById(R.id.formulario_nota_descricao);
        return new Nota(notaTitulo.getText().toString(), notaDescricao.getText().toString());
    }

    private boolean eMenuSalvaNota(MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_ic_nota_salva;
    }
}
