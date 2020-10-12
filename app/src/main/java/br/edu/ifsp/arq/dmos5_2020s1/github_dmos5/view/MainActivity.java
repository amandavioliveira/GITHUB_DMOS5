package br.edu.ifsp.arq.dmos5_2020s1.github_dmos5.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.edu.ifsp.arq.dmos5_2020s1.github_dmos5.api.GitHub;
import br.edu.ifsp.arq.dmos5_2020s1.github_dmos5.model.NomesRepositorios;
import br.edu.ifsp.arq.dmos5_2020s1.github_dmos5.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PERMISSION = 64;
    private static final String BASE_URL = "https://api.github.com/users/";

    private EditText usuarioEditText;
    private Button buscarButton;
    private TextView nomeAppTextView;
    private TextView nomeRepositorio;

    private RecyclerView mRecyclerView;

    private RepositoriosAdapter mRepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioEditText = findViewById(R.id.edittext_usuario);
        nomeAppTextView = findViewById(R.id.textview_app);
        nomeRepositorio = findViewById(R.id.textview_nome_repositorio);
        buscarButton = findViewById(R.id.button_buscar);
        buscarButton.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.recyclerview_repositorios);

        mRepAdapter = new RepositoriosAdapter(null);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRepAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_buscar:
                if(temPermissao()){
                    buscarRespositorio();
                }else{
                    solicitaPermissao();
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void buscarRespositorio(){
        String usuario = usuarioEditText.getText().toString();
        if(usuario.isEmpty()){
            Toast.makeText(this, getString(R.string.usuario_invalido), Toast.LENGTH_SHORT).show();
        }else{
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

            GitHub service = retrofit.create(GitHub.class);

            Call<List<NomesRepositorios>> repos = service.listarRepositorios(usuario);

            repos.enqueue(new Callback<List<NomesRepositorios>>() {
                @Override
                public void onResponse(Call<List<NomesRepositorios>> call, Response<List<NomesRepositorios>> response) {
                    if (response.isSuccessful()) {
                        List<NomesRepositorios> repositorios = response.body();
                        mRepAdapter.atualizarLista(repositorios, mRecyclerView);
                    }else{
                        Toast.makeText(MainActivity.this,R.string.rep_nao_encontrado,Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<NomesRepositorios>> call, Throwable t) {
                    Toast.makeText(MainActivity.this,R.string.erro_api,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean temPermissao(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private void solicitaPermissao(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
            final Activity activity = this;
            new AlertDialog.Builder(this)
                    .setMessage(R.string.explicacao_permissao)
                    .setPositiveButton(R.string.botao_permitir, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton(R.string.botao_nao_permitir, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.INTERNET
                    },
                    REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {

                if (permissions[i].equalsIgnoreCase(Manifest.permission.INTERNET) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    buscarRespositorio();
                }

            }
        }
    }

    private void updateUI(){
        mRecyclerView.setVisibility(View.GONE);
    }
}