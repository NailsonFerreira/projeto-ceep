package br.com.alura.ceep.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        ListView listaNotas = findViewById(R.id.listView);
        NotaDAO dao = new NotaDAO();

        for(int i = 1;i < 10000; i++) {
            dao.insere(new Nota("Title" + i, "Body"));
        }


        List<Nota> todasNostas = dao.todos();

        listaNotas.setAdapter(new ListaNotasAdapter(this, todasNostas));
    }
}
