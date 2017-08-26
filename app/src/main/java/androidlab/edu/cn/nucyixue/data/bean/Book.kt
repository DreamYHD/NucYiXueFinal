package androidlab.edu.cn.nucyixue.data.bean

/**
 * Created by MurphySL on 2017/8/26.
 */
data class Book(var id: String,
                var isbn10: String,
                var isbn13: String,
                var title: String,
                var origin_title: String,
                var alt_title: String,
                var subtitle: String,
                var url: String,
                var alt: String,
                var image: String,
                var images: Images,
                var publisher: String,
                var pubdate: String,
                var rating: Rating,
                var binding: String,
                var price: String,
                var series: Series,
                var pages: String,
                var author_intro: String,
                var summary: String,
                var catalog: String,
                var ebook_url: String,
                var ebook_price: String,
                var author: List<String>,
                var translator: List<String>,
                var tags: List<Tags>) {
    data class Images(var small: String,
                      var large: String,
                      var medium: String)

    data class Rating(var max: Int,
                      var numRaters: Int,
                      var average: String,
                      var min: Int)

    data class Series(var id: String,
                      var title: String)

    data class Tags(var count: Int,
                    var name: String)

    override fun toString(): String {
        return "Book(id='$id', isbn10='$isbn10', isbn13='$isbn13', title='$title', origin_title='$origin_title', alt_title='$alt_title', subtitle='$subtitle', url='$url', alt='$alt', image='$image', images=$images, publisher='$publisher', pubdate='$pubdate', rating=$rating, binding='$binding', price='$price', series=$series, pages='$pages', author_intro='$author_intro', summary='$summary', catalog='$catalog', ebook_url='$ebook_url', ebook_price='$ebook_price', author=$author, translator=$translator, tags=$tags)"
    }


}