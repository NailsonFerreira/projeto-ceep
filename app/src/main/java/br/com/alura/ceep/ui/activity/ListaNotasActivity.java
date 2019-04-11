package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerView.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerView.adapter.listener.OnItemClickListener;
import br.com.alura.ceep.ui.recyclerView.helper.callback.NotaItemTouchHelperCallback;

import static br.com.alura.ceep.ui.activity.ActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.ActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.ActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.ActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.ActivityConstantes.POSICAO_INVALIDA;

/* Activity Laucher
App exibe e gerencia uma lista de notas com titulo e descrição.
 */
public class ListaNotasActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Notas";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        List<Nota> todasNotas = pegaTodasNotas(); //Recebe uma lista de notas
        configuraRecyclerView(todasNotas); //Configura o RecyclerView

        setTitle(TITULO_APPBAR);

        configuraBotaoInsereNotas();
    }


    //Localiza e instancia a View responsavel por chamar a FormularioNotaActivity
    private void configuraBotaoInsereNotas() {
        TextView botaoInsereNotas = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFormularioNotaInsere();
            }
        });
    }

    /*Instancia e chama a Tela FormularioNotaActivity.
    alem da intent, passa tambem um final int CODIGO_REQUISICAO_INSERE_NOTA
     */
    private void vaiParaFormularioNotaInsere() {
        Intent intent = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(intent, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        for(int i = 0; i<10 ; i++){
            dao.insere(new Nota("Nota " + (i+1), "Descrição " + (i + 1)));
        }

        return dao.todos();
    }

    @Override
    //Compartilhamento de dados entre activitys
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(ehResultadoInsereNota(requestCode, data)){
            if(resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adapter.adiciona(notaRecebida);
                new NotaDAO().insere(notaRecebida);
            }
        }

        if(ehResultadoAlteraNota(requestCode, data)){
            if(resultadoOk(resultCode)){
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if(posicaoRecebida > POSICAO_INVALIDA) {
                    new NotaDAO().altera(posicaoRecebida, notaRecebida);
                    adapter.altera(posicaoRecebida, notaRecebida);
                } else {
                    Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    private boolean ehResultadoAlteraNota(int requestCode, @NonNull Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode)  && temNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean ehResultadoInsereNota(int requestCode, @Nullable Intent data) {
        return ehCodRequisicaoInsereNota(requestCode)  && temNota(data);
    }

    private boolean temNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == RESULT_OK;
    }

    private boolean ehCodRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    //Localiza e instancia um RecyclerView para receber uma lista de notas
    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycleview);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    //Recebe uma Lista e RecyclerView e prepara o atributo adapter de 'ListaNotasAdapter' para interação com a activity
    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int position) { // Responde a um clique em um item da lista de notas
                vaiParaFormularioNotaAltera(nota, position);
            }
        });
    }

    private void vaiParaFormularioNotaAltera(Nota nota, int position) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, position);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA); //Chama a FormularioNotaActivity passando o codigo de requisição com o conteudo extra da intent
    }
}
