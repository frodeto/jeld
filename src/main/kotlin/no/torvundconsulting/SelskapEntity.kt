package no.torvundconsulting

data class SelskapEntity(
    val adresse: Array<String>?,
    val postnummer: Array<String>?,
    val poststed: Array<String>?,
    val all: Boolean?,
    val stoppet: Boolean?,
    val objekttype: Array<String>?,
    val objektkategori: Array<String>?,
    val fritekst : String?) {
    fun isNotEmpty(name: String) : Boolean {
        val result : Boolean
        when (name) {
            "adresse" -> result = adresse.isNullOrEmpty()
            "postnummer" -> result = postnummer.isNullOrEmpty()
            "poststed" -> result = poststed.isNullOrEmpty()
            "objekttype" -> result = objekttype.isNullOrEmpty()
            "objektkategori" -> result = objektkategori.isNullOrEmpty()
            "all" -> result = all ?: false
            "stoppet" -> result = stoppet ?: false
            "fritekst" -> result = fritekst.isNullOrEmpty()
            else -> {
                println("Could not find member with name $name")
                result = true
            }
        }
        return !result
    }

    @Suppress("DuplicatedCode")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SelskapEntity) return false

        if (adresse != null) {
            if (other.adresse == null) return false
            if (!adresse.contentEquals(other.adresse)) return false
        } else if (other.adresse != null) return false
        if (postnummer != null) {
            if (other.postnummer == null) return false
            if (!postnummer.contentEquals(other.postnummer)) return false
        } else if (other.postnummer != null) return false
        if (poststed != null) {
            if (other.poststed == null) return false
            if (!poststed.contentEquals(other.poststed)) return false
        } else if (other.poststed != null) return false
        if (all != other.all) return false
        if (stoppet != other.stoppet) return false
        if (objekttype != null) {
            if (other.objekttype == null) return false
            if (!objekttype.contentEquals(other.objekttype)) return false
        } else if (other.objekttype != null) return false
        if (objektkategori != null) {
            if (other.objektkategori == null) return false
            if (!objektkategori.contentEquals(other.objektkategori)) return false
        } else if (other.objektkategori != null) return false
        if (fritekst != other.fritekst) return false

        return true
    }

    override fun hashCode(): Int {
        var result = adresse?.contentHashCode() ?: 0
        result = 31 * result + (postnummer?.contentHashCode() ?: 0)
        result = 31 * result + (poststed?.contentHashCode() ?: 0)
        result = 31 * result + (all?.hashCode() ?: 0)
        result = 31 * result + (stoppet?.hashCode() ?: 0)
        result = 31 * result + (objekttype?.contentHashCode() ?: 0)
        result = 31 * result + (objektkategori?.contentHashCode() ?: 0)
        result = 31 * result + (fritekst?.hashCode() ?: 0)
        return result
    }

}
