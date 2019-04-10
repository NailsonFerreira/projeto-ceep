package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.activity.recyclerView.ListaNotasAdapter;

import static br.com.alura.ceep.ui.activity.ActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.ActivityConstantes.CODIGO_RESULT_NOTA_CRIADA;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);

        configuraBotaoInsereNotas();
    }

    private void configuraBotaoInsereNotas() {
        TextView botaoInsereNotas = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFormularioNotaActivity();
            }
        });
    }

    private void vaiParaFormularioNotaActivity() {
        Intent intent = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(intent, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(ehCodRequisicaoInsereNota(requestCode) && ehCodResultNotaCriada(resultCode) && data.hasExtra("nota")){
            Nota notaRecebida =(Nota)data.getSerializableExtra("nota");
            adapter.adiciona(notaRecebida);
            new NotaDAO().insere(notaRecebida);
        }
    }

    private boolean ehCodResultNotaCriada(int resultCode) {
        return resultCode == CODIGO_RESULT_NOTA_CRIADA;
    }

    private boolean ehCodRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(List<Nota> todasNostas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycleview);
        configuraAdapter(todasNostas, listaNotas);
    }


    private void configuraAdapter(List<Nota> todasNostas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNostas);
        listaNotas.setAdapter(adapter);
    }
}
