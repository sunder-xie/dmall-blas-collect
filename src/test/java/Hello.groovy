/**
 * Created by lrkin on 2017/5/18.
 */
class Hello {
    static def helloworld() {
        def str1 = "编程语言"
        def str2 = "Groovy"
        println "$str1:$str2"
        println "$str2"

        def str5 = "你,好"
        def split = str5.split(",")
        for (item in split) {
            println item
        }
    }
}