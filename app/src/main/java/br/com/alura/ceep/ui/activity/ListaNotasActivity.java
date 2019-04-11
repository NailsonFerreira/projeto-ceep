package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.activity.recyclerView.ListaNotasAdapter;
import br.com.alura.ceep.ui.activity.recyclerView.adapter.listener.OnItemClickListener;

import static br.com.alura.ceep.ui.activity.ActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.ActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.ActivityConstantes.CODIGO_RESULT_NOTA_CRIADA;

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
                vaiParaFormularioNotaActivity();
            }
        });
    }

    /*Instancia e chama a Tela FormularioNotaActivity.
    alem da intent, passa tambem um final int CODIGO_REQUISICAO_INSERE_NOTA
     */
    private void vaiParaFormularioNotaActivity() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(ehResultadoComNota(requestCode, resultCode, data)){
            Nota notaRecebida = (Nota)data.getSerializableExtra(CHAVE_NOTA);
            adapter.adiciona(notaRecebida);
            new NotaDAO().insere(notaRecebida);
        }

        if(requestCode == 2 && resultCode == CODIGO_RESULT_NOTA_CRIADA && temNota(data) && data.hasExtra("posicao")){
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            int posicaoRecebida = data.getIntExtra("posicao", -1);
            Toast.makeText(this, notaRecebida.getTitulo(), Toast.LENGTH_SHORT).show();
            new NotaDAO().altera(posicaoRecebida, notaRecebida);
            adapter.altera(posicaoRecebida, notaRecebida);
        }

    }

    private boolean ehResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return ehCodRequisicaoInsereNota(requestCode) && ehCodResultNotaCriada(resultCode) && temNota(data);
    }

    private boolean temNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean ehCodResultNotaCriada(int resultCode) {
        return resultCode == CODIGO_RESULT_NOTA_CRIADA;
    }

    private boolean ehCodRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    //Localiza e instancia um RecyclerView para receber uma lista de notas
    private void configuraRecyclerView(List<Nota> todasNostas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycleview);
        configuraAdapter(todasNostas, listaNotas);
    }

    //Recebe uma Lista e RecyclerView e prepara o atributo adapter de 'ListaNotasAdapter' para interação com a activity
    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int position) { // Responde a um clique em um item da lista de notas
                Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
                abreFormularioComNota.putExtra("posicao", position);
                startActivityForResult(abreFormularioComNota, 2); //Chama a FormularioNotaActivity passando o codigo de requisição com o conteudo extra da intent
            }
        });
    }
}
