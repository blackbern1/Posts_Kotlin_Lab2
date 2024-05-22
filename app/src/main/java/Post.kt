class Post (val text: String, val user: String, var likes: Int) {
    var id: Int = 0
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var liked : Boolean = false

    var highlighted: Boolean = false
}