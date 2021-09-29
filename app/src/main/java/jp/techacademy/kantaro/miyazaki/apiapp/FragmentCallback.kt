package jp.techacademy.kantaro.miyazaki.apiapp

interface FragmentCallback {
    // Itemを押した時の処理
    fun onClickItem(shop: Shop)
    fun onClickItem(favoriteShop: FavoriteShop)
    // お気に入り追加時の処理
    fun onAddFavorite(shop: Shop)
    // お気に入り削除時の処理
    fun onDeleteFavorite(id: String)
}