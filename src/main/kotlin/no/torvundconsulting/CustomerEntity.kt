package no.torvundconsulting

data class CustomerEntity(
    val telefonnummer: Array<String>?,
    val epostadresse: Array<String>?,
    val fritekst: String?,
    val navn: Array<String>?,
    val adresse: Array<String>?,
    val etternavn: Array<String>?,
    val fornavn: Array<String>?,
    val fodselsdato: Array<String>?,
    val kundenummer: Array<String>?,
    val ssn: Array<String>?,
    val postnummer: Array<String>?,
    val poststed: Array<String>?,
    val medlem: Boolean?,
    val beboer: Boolean?,
    val objekttype: Array<String>?,
    val objektkategori: Array<String>?) {

    fun isNotEmpty(name: String) : Boolean {
        val result : Boolean
        when (name) {
            "telefonnummer" -> result = telefonnummer.isNullOrEmpty()
            "epostadresse" -> result = epostadresse.isNullOrEmpty()
            "navn" -> result = navn.isNullOrEmpty()
            "adresse" -> result = adresse.isNullOrEmpty()
            "etternavn" -> result = etternavn.isNullOrEmpty()
            "fornavn" -> result = fornavn.isNullOrEmpty()
            "fodselsdato" -> result = fodselsdato.isNullOrEmpty()
            "kundenummer" -> result = kundenummer.isNullOrEmpty()
            "ssn" -> result = ssn.isNullOrEmpty()
            "postnummer" -> result = postnummer.isNullOrEmpty()
            "poststed" -> result = poststed.isNullOrEmpty()
            "objekttype" -> result = objekttype.isNullOrEmpty()
            "objektkategori" -> result = objektkategori.isNullOrEmpty()
            "medlem" -> result = medlem ?: false
            "beboer" -> result = beboer ?: false
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
        if (other !is CustomerEntity) return false

        if (telefonnummer != null) {
            if (other.telefonnummer == null) return false
            if (!telefonnummer.contentEquals(other.telefonnummer)) return false
        } else if (other.telefonnummer != null) return false
        if (epostadresse != null) {
            if (other.epostadresse == null) return false
            if (!epostadresse.contentEquals(other.epostadresse)) return false
        } else if (other.epostadresse != null) return false
        if (fritekst != other.fritekst) return false
        if (navn != null) {
            if (other.navn == null) return false
            if (!navn.contentEquals(other.navn)) return false
        } else if (other.navn != null) return false
        if (adresse != null) {
            if (other.adresse == null) return false
            if (!adresse.contentEquals(other.adresse)) return false
        } else if (other.adresse != null) return false
        if (etternavn != null) {
            if (other.etternavn == null) return false
            if (!etternavn.contentEquals(other.etternavn)) return false
        } else if (other.etternavn != null) return false
        if (fornavn != null) {
            if (other.fornavn == null) return false
            if (!fornavn.contentEquals(other.fornavn)) return false
        } else if (other.fornavn != null) return false
        if (fodselsdato != null) {
            if (other.fodselsdato == null) return false
            if (!fodselsdato.contentEquals(other.fodselsdato)) return false
        } else if (other.fodselsdato != null) return false
        if (kundenummer != null) {
            if (other.kundenummer == null) return false
            if (!kundenummer.contentEquals(other.kundenummer)) return false
        } else if (other.kundenummer != null) return false
        if (ssn != null) {
            if (other.ssn == null) return false
            if (!ssn.contentEquals(other.ssn)) return false
        } else if (other.ssn != null) return false
        if (postnummer != null) {
            if (other.postnummer == null) return false
            if (!postnummer.contentEquals(other.postnummer)) return false
        } else if (other.postnummer != null) return false
        if (poststed != null) {
            if (other.poststed == null) return false
            if (!poststed.contentEquals(other.poststed)) return false
        } else if (other.poststed != null) return false
        if (medlem != other.medlem) return false
        if (beboer != other.beboer) return false
        if (objekttype != null) {
            if (other.objekttype == null) return false
            if (!objekttype.contentEquals(other.objekttype)) return false
        } else if (other.objekttype != null) return false
        if (objektkategori != null) {
            if (other.objektkategori == null) return false
            if (!objektkategori.contentEquals(other.objektkategori)) return false
        } else if (other.objektkategori != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = telefonnummer?.contentHashCode() ?: 0
        result = 31 * result + (epostadresse?.contentHashCode() ?: 0)
        result = 31 * result + (fritekst?.hashCode() ?: 0)
        result = 31 * result + (navn?.contentHashCode() ?: 0)
        result = 31 * result + (adresse?.contentHashCode() ?: 0)
        result = 31 * result + (etternavn?.contentHashCode() ?: 0)
        result = 31 * result + (fornavn?.contentHashCode() ?: 0)
        result = 31 * result + (fodselsdato?.contentHashCode() ?: 0)
        result = 31 * result + (kundenummer?.contentHashCode() ?: 0)
        result = 31 * result + (ssn?.contentHashCode() ?: 0)
        result = 31 * result + (postnummer?.contentHashCode() ?: 0)
        result = 31 * result + (poststed?.contentHashCode() ?: 0)
        result = 31 * result + (medlem?.hashCode() ?: 0)
        result = 31 * result + (beboer?.hashCode() ?: 0)
        result = 31 * result + (objekttype?.contentHashCode() ?: 0)
        result = 31 * result + (objektkategori?.contentHashCode() ?: 0)
        return result
    }
}
