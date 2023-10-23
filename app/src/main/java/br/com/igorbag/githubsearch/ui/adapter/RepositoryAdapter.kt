package br.com.igorbag.githubsearch.ui.adapter


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.domain.Repository

class RepositoryAdapter(private val context: Context, private val repositories: List<Repository>) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

// Cria uma nova view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view =
        LayoutInflater.from(parent.context).inflate(R.layout.repository_item, parent, false)
    return ViewHolder(view)
}

// Pega o conteudo da view e troca pela informacao de item de uma lista

override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val repository = repositories[position]

    holder.cardRepositorio.setOnClickListener {
        openBrowser(context, repository.htmlUrl)
    }

    holder.repositorio.text = repository.name

    holder.compartilhar.setOnClickListener {
        shareReposLink(context, repository.htmlUrl)
    }
}

fun shareReposLink(context: Context, urlRepository: String){
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, urlRepository)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

// Pega a quantidade de repositorios da lista
override fun getItemCount(): Int = 0

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val repositorio: TextView
    val compartilhar: ImageView
    var cardRepositorio: CardView

    init {
        view.apply {
            repositorio = findViewById(R.id.tv_repositorio)
            compartilhar = findViewById(R.id.iv_compartilhar)
            cardRepositorio = findViewById(R.id.cv_repository)
        }

    }
}
fun openBrowser(context: Context, urlRepository: String) {
    context.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(urlRepository)
        )
    )
}
}


