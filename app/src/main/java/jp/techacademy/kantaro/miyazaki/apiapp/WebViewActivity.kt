package jp.techacademy.kantaro.miyazaki.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.fragment_favorite.*

class WebViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        var shop: Shop
        var favoriteShop: FavoriteShop

        if(intent.getSerializableExtra(KEY_URL) is Shop){
            shop = intent.getSerializableExtra(KEY_URL) as Shop
            favoriteShop = FavoriteShop().apply {
                id = shop.id
                name = shop.name
                imageUrl = shop.logoImage
                url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
                address = shop.address
            }
        }else{
            favoriteShop = intent.getSerializableExtra(KEY_URL) as FavoriteShop
        }
        webView.loadUrl(favoriteShop.url)
        isFavoriteCheck(favoriteShop)
    }

    private fun isFavoriteCheck(favoriteShop: FavoriteShop){
        val isFavorite = FavoriteShop.findBy(favoriteShop.id) != null
        // 白抜きの星マークの画像を指定
        favoriteImageView.apply {
            setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border) // Picassoというライブラリを使ってImageVIewに画像をはめ込む
            setOnClickListener {
                if (isFavorite) {
                    onDeleteFavorite(favoriteShop)
                } else {
                    onAddFavorite(favoriteShop)
                }
            }
        }
    }

    private fun onDeleteFavorite(favoriteShop: FavoriteShop) { // Favoriteから削除するときのメソッド(Fragment -> Activity へ通知する)
        showConfirmDeleteFavoriteDialog(favoriteShop)
    }

    private fun showConfirmDeleteFavoriteDialog(favoriteShop: FavoriteShop) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                FavoriteShop.delete(favoriteShop.id)
                isFavoriteCheck(favoriteShop)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->}
            .create()
            .show()
    }

    private fun onAddFavorite(favoriteShop: FavoriteShop) { // Favoriteに追加するときのメソッド(Fragment -> Activity へ通知する)
        FavoriteShop.insert(favoriteShop)
        isFavoriteCheck(favoriteShop)
    }

    fun updateData() {
        swipeRefreshLayout.isRefreshing = false
    }

    companion object {
        private const val KEY_URL = "key_url"
        fun start1(activity: Activity, shop: Shop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL, shop))
        }
        fun start2(activity: Activity, favoriteShop: FavoriteShop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL, favoriteShop))
        }
    }
}

