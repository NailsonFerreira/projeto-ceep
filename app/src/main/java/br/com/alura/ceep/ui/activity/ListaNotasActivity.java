package br.com.alura.ceep.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.activity.recyclerView.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);


        List<Nota> todasNostas = notasDeExemplo();
        configuraRecyclerView(todasNostas);
    }

    private List<Nota> notasDeExemplo() {
        NotaDAO dao = new NotaDAO();

            dao.insere(new Nota("Titulo 1", "Descrição 1"),new Nota("Titulo 2", "Descrição 2"));

        return dao.todos();
    }

    private void configuraRecyclerView(List<Nota> todasNostas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycleview);
        configuraAdapter(todasNostas, listaNotas);
    }


    private void configuraAdapter(List<Nota> todasNostas, RecyclerView listaNotas) {
        listaNotas.setAdapter(new ListaNotasAdapter(this, todasNostas));
    }
}
