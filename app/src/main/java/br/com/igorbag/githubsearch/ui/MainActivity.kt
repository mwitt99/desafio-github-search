package br.com.igorbag.githubsearch.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.data.GitHubService
import br.com.igorbag.githubsearch.domain.Repository
import br.com.igorbag.githubsearch.ui.adapter.RepositoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var nomeUsuario: EditText
    lateinit var btnConfirmar: Button
    lateinit var listaRepositories: RecyclerView
    lateinit var githubApi: GitHubService
    lateinit var progress: ProgressBar
    lateinit var ivWifiOff: ImageView
    lateinit var txtWifiOff: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Habilitando exibição do ícone do app
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(R.layout.activity_main)
        setupView()
        showUserName()
        setupRetrofit()
        setupListeners()

    }

    // Metodo responsavel por realizar o setup da view e recuperar os Ids do layout
    fun setupView() {
        nomeUsuario = findViewById(R.id.et_nome_usuario)
        btnConfirmar = findViewById(R.id.btn_confirmar)
        listaRepositories = findViewById(R.id.rv_lista_repositories)
        progress = findViewById(R.id.pb_carregamento)
        ivWifiOff = findViewById(R.id.iv_wifi_off)
        txtWifiOff = findViewById(R.id.tv_wifi_off)
    }

    //metodo responsavel por configurar os listeners click da tela
    private fun setupListeners() {
        btnConfirmar.setOnClickListener {
            val conection = isInternetAvailable()

            if(!conection){
                ivWifiOff.isVisible = true
                txtWifiOff.isVisible = true
            }else{
                ivWifiOff.isVisible = false
                txtWifiOff.isVisible = false

                val nomePesquisar = nomeUsuario.text.toString()
                getAllReposByUserName(nomePesquisar)
                saveUserLocal()
                //Para que desapareça quando for fazer nossas pesquisas
                listaRepositories.isVisible = false
            }
        }
    }


    // salvar o usuario preenchido no EditText utilizando uma SharedPreferences
    private fun saveUserLocal() {
        val usuarioInformado = nomeUsuario.text.toString()
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("saved_username", usuarioInformado)
        }
    }

    private fun showUserName() {
        val sharedPreferences = getPreferences(MODE_PRIVATE) ?: return
        val ultimoPesquisado = sharedPreferences.getString("saved_username", null)

        if(!ultimoPesquisado.isNullOrEmpty()){
            nomeUsuario.setText(ultimoPesquisado)
        }
    }

    //Metodo responsavel por fazer a configuracao base do Retrofit
    fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        githubApi = retrofit.create(GitHubService::class.java)


    }

    //Metodo responsavel por buscar todos os repositorios do usuario fornecido


    fun setupAdapter(list: List<Repository>) {
            val repAdapter = RepositoryAdapter(
                this, list)
            listaRepositories.adapter = repAdapter
    }

    fun isInternetAvailable(): Boolean {
        val connectivityManager =
        getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
fun getAllReposByUserName(userName: String) { //Método responsável por buscar todos os repositórios do usuário fornecido

    if (userName.isNotEmpty()) {

        progress.isVisible = true

        githubApi.getAllRepositoriesByUser(userName)
            .enqueue(object : Callback<List<Repository>> {

                override fun onResponse(
                    call: Call<List<Repository>>,
                    response: Response<List<Repository>>
                ) {
                    if (response.isSuccessful) {

                        progress.isVisible = false
                        listaRepositories.isVisible = true

                        val repositories = response.body()

                        repositories?.let {
                            setupAdapter(repositories)
                        }

                    } else {

                        progress.isVisible = false

                        val context = applicationContext
                        Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<List<Repository>>, t: Throwable) {

                    progress.isVisible = false

                    val context = applicationContext
                    Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
                }

            })
        }
    }
}


