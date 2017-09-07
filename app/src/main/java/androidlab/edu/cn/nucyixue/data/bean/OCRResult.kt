package androidlab.edu.cn.nucyixue.data.bean

/**
 * Created by MurphySL on 2017/9/7.
 */
data class OCRResult(var log_id: Int,
                     var direction: Int,
                     var words_result_num: Int,
                     var words_result: List<WordsResult>) {
    data class WordsResult(var location: Location,
                           var words: String,
                           var chars: List<Chars>,
                           var vertexes_location: List<VertexesLocation>) {
        data class Location(var width: Int,
                            var top: Int,
                            var height: Int,
                            var left: Int)

        data class Chars(var char: String,
                         var location: Location) {
            data class Location(var width: Int,
                                var top: Int,
                                var height: Int,
                                var left: Int)
        }

        data class VertexesLocation(var y: Int,
                                    var x: Int)
    }
}