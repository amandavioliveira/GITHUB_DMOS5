package br.edu.ifsp.arq.dmos5_2020s1.github_dmos5.api;

import java.util.List;

import br.edu.ifsp.arq.dmos5_2020s1.github_dmos5.model.NomesRepositorios;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHub {
    @GET("{user}/repos")
    Call<List<NomesRepositorios>> listarRepositorios(@Path("user") String user);
}
