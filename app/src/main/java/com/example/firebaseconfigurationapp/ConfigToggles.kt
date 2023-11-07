package com.example.firebaseconfigurationapp

enum class BooleanConfigToggle {
    NEW_FINANCE_ENABLED,
    NEW_SEARCH_MASK_ENABLED,
    LEASING_ENABLED,
    AFTER_LEAD_ENABLED,
    HOME_SMYLE_ENABLED;

    fun getKey(): String {
        return when (this) {
            NEW_FINANCE_ENABLED -> "newFinanceEnabled"
            NEW_SEARCH_MASK_ENABLED -> "newSearchMaskEnabled"
            LEASING_ENABLED -> "leasingEnabled"
            AFTER_LEAD_ENABLED -> "afterLeadEnabled"
            HOME_SMYLE_ENABLED -> "homeSmyleEnabled"
        }
    }

    fun getDefaultValue(): Any {
        return when (this) {
            NEW_FINANCE_ENABLED, NEW_SEARCH_MASK_ENABLED, LEASING_ENABLED -> false
            AFTER_LEAD_ENABLED -> StringConfigToggle.AFTER_LEAD_VERSION.getBooleanValue()
            HOME_SMYLE_ENABLED -> StringConfigToggle.HOME_SMYLE_BANNER_VERSION.getBooleanValue()
        }
    }

}

enum class StringConfigToggle {
    AFTER_LEAD_VERSION,
    HOME_SMYLE_BANNER_VERSION;

    fun getKey(): String {
        return when (this) {
            AFTER_LEAD_VERSION -> "afterLeadVersion"
            HOME_SMYLE_BANNER_VERSION -> "homeSmyleBannerVersion"
        }
    }

    fun getDefaultValue(): Any {
        return when (this) {
            AFTER_LEAD_VERSION, HOME_SMYLE_BANNER_VERSION -> "v0"
        }
    }

    fun getBooleanValue(): Boolean {
        return this.getDefaultValue() != "v0"
    }
}