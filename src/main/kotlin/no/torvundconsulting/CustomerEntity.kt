package no.torvundconsulting

data class CustomerEntity(
    val telefonnummer: Array<String>,
    val epostadresse: Array<String>,
    val fritekst: String,
    val navn: Array<String>,
    val adresse: Array<String>,
    val etternavn: Array<String>,
    val fornavn: Array<String>,
    val fodselsdato: Array<String>,
    val kundenummer: Array<String>,
    val ssn: Array<String>,
    val postnummer: Array<String>,
    val poststed: Array<String>,
    val medlem: Boolean,
    val beboer: Boolean,
    val objekttype: Array<String>,
    val objektkategori: Array<String>
)