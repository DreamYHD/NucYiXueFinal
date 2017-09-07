package androidlab.edu.cn.nucyixue.data.bean

/**
 * Keyword
 *
 * Created by MurphySL on 2017/8/26.
 */
data class Keyword(var showapi_res_code: Int,
                   var showapi_res_error: String,
                   var showapi_res_body: ShowapiResBody) {
    data class ShowapiResBody(var ret_code: Int,
                              var list: List<String>)
}