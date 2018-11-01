<?xml version="1.0"?>
<recipe>

    <instantiate from="src/app_package/layout/fragment_layout.xml.ftl"
                   to="${escapeXmlAttribute(resOut)}/layout/${fragmentLayoutName}.xml" />
	<instantiate from="src/app_package/classes/ViewModel.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${className}ViewModel.kt" />
	<instantiate from="src/app_package/classes/ViewState.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${className}ViewState.kt" />
	<instantiate from="src/app_package/classes/Intent.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${className}Intent.kt" />
	<instantiate from="src/app_package/classes/Fragment.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${className}Fragment.kt" />
	<instantiate from="src/app_package/classes/Module.kt.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${className}Module.kt" />

	<open file="${srcOut}/${className}Fragment.kt"/>
</recipe>