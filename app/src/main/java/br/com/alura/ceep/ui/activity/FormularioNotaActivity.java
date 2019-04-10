package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;

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
        if(item.getItemId() == R.id.menu_formulario_ic_nota_salva){
            TextView notaTitulo = findViewById(R.id.formulario_nota_titulo);
            TextView notaDescricao = findViewById(R.id.formulario_nota_descricao);
            Nota notaCriada = new Nota(notaTitulo.getText().toString(), notaDescricao.getText().toString());
            Intent resultadoInsercao = new Intent();
            resultadoInsercao.putExtra("nota", notaCriada);
            setResult(2, resultadoInsercao);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
