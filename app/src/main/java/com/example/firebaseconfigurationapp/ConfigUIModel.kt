package com.example.firebaseconfigurationapp

data class ConfigUIModel(
    var newFinanceEnabled: String = "",
    var newSearchMaskEnabled: String = "",
    var leasingEnabled: String = "",
    var afterLeadEnabled: String = "",
    var afterLeadVersion: String = "",
    var smyleVersion: String = ""
) {
    constructor(
        newFinanceEnabled: Boolean,
        newSearchMaskEnabled: Boolean,
        leasingEnabled: Boolean,
        afterLeadEnabled: Boolean,
        afterLeadVersion: String,
        smyleVersion: String
    ) : this(
        getPrettyBool(newFinanceEnabled),
        getPrettyBool(newSearchMaskEnabled),
        getPrettyBool(leasingEnabled),
        getPrettyBool(afterLeadEnabled),
        afterLeadVersion,
        getSmyle(smyleVersion)
    )

    companion object {
        private fun getPrettyBool(boolVal: Boolean): String {
            return if (boolVal) "True ✅" else "False ❌"
        }

        private fun getSmyle(stringVal: String): String {
            return when (stringVal) {
                "v1" -> "Smyle v1 🙂"
                "v2" -> "Smyle v2 😀"
                "v3" -> "Smyle v3 🤣"
                else -> "No Smyle 🙁"
            }
        }
    }
}