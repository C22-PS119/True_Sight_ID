class DirtyFilter {
    class TextRange(start:Int, end:Int, text: String) {
        var start: Int = 0
            get() = field
        var end: Int = 0
            get() = field
        var text: String = ""
            get() = field
        var value: String = ""
            get() = if ((start == -1) or (end == -1)) "" else text.substring(start, end)
        var length:Int = 0
            get() = end - start

        init {
            this.start = start
            this.end = end
            this.text = text
        }
    }
    companion object {

        val DirtyWords = arrayOf<String>("Kunyuk","Bajingan","Bangsat","Kampret","Kontol","Memek","Ngentot","Pentil","Perek","Pepek","Pecun","Bencong","Banci","Maho","Gila","Sinting","Tolol","Sarap","Setan","Lonte","Hencet","Taptei","Kampang","Pilat","Keparat","Bejad","Gembel","Brengsek","*Tai","Anjrit","Bangsat","Fuck","Tetek","Ngulum","Jembut","Totong","Kolop","Pukimak","Bodat","Heang","Jancuk","Burit","Titit","Nenen","Bejat","Silit","Sempak","Fucking","Asshole","Bitch","Klitoris","Kelentit","Borjong","Dancuk","Pantek","Taek","Itil","Teho","Bejat","Pantat","Bagudung","Babami","Kanciang","Bungul","Idiot","Kimak","Henceut","Kacuk","Blowjob","Pussy","Asu*","Dick*","Damn","*Ass*")
        val WordException = arrayOf<String>("Mengintai","ASUS","Asupan","Kutai","Bantai","Mengatai","Masuk","Asuh")

        fun FilterAlfaNumWords(input: String): String {
            return input
                .replace('0', 'o')
                .replace('1', 'i')
                .replace('3', 'e')
                .replace('4', 'A')
                .replace('5', 's')
                .replace('6', 'b')
                .replace('7', 't')
                .replace('9', 'g')
        }

        private fun BuildRegexDirtyWord(word: String) : String {
            var builder = ""
            if (word.startsWith('*')){
                builder += "(?:\\n|[\\W_]|^)"
            }
            builder += "("
            for (char in word)
                if (char != '*')
                    builder += char + "+(?:[ \\W_]| +\\w)*?"
            builder += ")"
            if (word.endsWith('*')){
                builder += "(?:\\n|[\\W_]|\$)"
            }
            return builder
        }

        fun isContainDirtyWord(input: String, dirtyWords: Array<String>): Boolean {
            for (dirtyWord in dirtyWords) {
                val pattern = BuildRegexDirtyWord(dirtyWord).toRegex(setOf<RegexOption>(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
                if (pattern.containsMatchIn(input))
                    return true
            }
            return false
        }

        fun findDirtyWord(input: String, dirtyWords: Array<String>, startIndex: Int = 0): ArrayList<TextRange> {
            var ranges = ArrayList<TextRange>()
            for (dirtyWord in dirtyWords) {
                val pattern = BuildRegexDirtyWord(dirtyWord).toRegex(setOf<RegexOption>(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
                val matches = pattern.findAll(input, startIndex)
                if (matches.count() > 0){
                    for (match in matches)
                        ranges.add(TextRange(match.groups[1]?.range?.start ?: -1,(match.groups[1]?.range?.last ?: -2) + 1, input))
                }
            }
            return ranges
        }

        fun censored(input: String, dirtyWords: Array<String>, cencoredChar: Char?): String {
            var filtered = input
            for (dirtyWord in dirtyWords) {
                val pattern = BuildRegexDirtyWord(dirtyWord).toRegex(setOf<RegexOption>(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
                val matches = pattern.findAll(input)
                for (match in matches){
                    if (cencoredChar == null){
                        filtered = pattern.replace(filtered, "")
                    }else{
                        filtered = filtered.replaceRange(match.groups[1]?.range?.start ?: 0,(match.groups[1]?.range?.last ?: 0) + 1 ,cencoredChar.toString().repeat(match.groups[1]?.value?.length ?: 0))
                    }
                }
            }
            return filtered
        }
    }
}