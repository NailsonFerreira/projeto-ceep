package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;

import static br.com.alura.ceep.ui.activity.ActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.ActivityConstantes.CODIGO_RESULT_NOTA_CRIADA;

public class FormularioNotaActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(eMenuSalvaNota(item)){
            Nota notaCriada = criaNota();
            retornaNota(notaCriada);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota notaCriada) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, notaCriada);
        setResult(CODIGO_RESULT_NOTA_CRIADA, resultadoInsercao);
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
