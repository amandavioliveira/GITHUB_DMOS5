package br.edu.ifsp.arq.dmos5_2020s1.github_dmos5.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.ifsp.arq.dmos5_2020s1.github_dmos5.model.NomesRepositorios;
import br.edu.ifsp.arq.dmos5_2020s1.github_dmos5.R;

public class RepositoriosAdapter extends RecyclerView.Adapter<RepositoriosAdapter.RepositoryViewHolder> {

    private List<NomesRepositorios> repositorios;

    public RepositoriosAdapter(@NonNull List<NomesRepositorios> repositorios) {
        this.repositorios = repositorios;
    }

    @NonNull
    @Override
    public RepositoriosAdapter.RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_repositorios, parent, false);
        RepositoryViewHolder viewHolder = new RepositoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoriosAdapter.RepositoryViewHolder holder, int position) {
        holder.nomeRepTextView.setText(repositorios.get(position).getName());
    }

    class RepositoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView nomeRepTextView;

        public RepositoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeRepTextView = itemView.findViewById(R.id.text_nome);
        }
    }

    public void atualizarLista(List<NomesRepositorios> repositorios, RecyclerView recyclerView) {
        this.repositorios = repositorios;
        recyclerView.setVisibility(View.VISIBLE);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (repositorios != null){
            return repositorios.size();
        }
        return 0;
    }
}
