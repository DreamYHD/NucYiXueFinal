package androidlab.edu.cn.nucyixue.net

import androidlab.edu.cn.nucyixue.data.bean.Book
import androidlab.edu.cn.nucyixue.data.bean.Keyword
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by MurphySL on 2017/8/1.
 */
object Service{
    val api_douban : DoubanService by lazy {
        Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(DoubanService::class.java)
    }

    val api_keyword : KeywordService by lazy {
        Retrofit.Builder()
                .baseUrl("http://route.showapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(KeywordService::class.java)
    }

}

interface KeywordService{
    @GET("941-1?showapi_appid=44822&showapi_sign=15554027327b4a4097f744f93e67f2fd")
    fun getKeyword(@Query("text") text : String, @Query("num") num : Int) : Observable<Keyword>
}

interface DoubanService{
    @GET("book/isbn/:{name}")
    fun getBookInfo(@Path("name") name : String) : Observable<Book>
}