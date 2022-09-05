package br.com.igorbag.githubsearch.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.domain.Repository

class RepositoryAdapter(private val repositories: List<Repository>) :
    RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    var carItemLister: (Repository) -> Unit = {}
    var btnShareLister: (Repository) -> Unit = {}

    // Cria uma nova view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.repository_item, parent, false)
        return ViewHolder(view)
    }

    // Pega o conteudo da view e troca pela informacao de item de uma lista
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.repositorio.text = repositories[position].name


        holder.repositorio.setOnClickListener {
         val repos = repositories[position]
            carItemLister(repos)
        }

        // Exemplo de click no btn Share
        holder.compartilhar.setOnClickListener {
            btnShareLister(repositories[position])
        }
    }

    // Pega a quantidade de repositorios da lista
    override fun getItemCount(): Int = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val repositorio: TextView
        var compartilhar: ImageButton
            get() {
                TODO()
            }

        init {
            view.apply {
                repositorio = findViewById(R.id.tv_repositorio)
                compartilhar = findViewById(R.id.iv_compartilhar)
            }

        }
    }
    }


