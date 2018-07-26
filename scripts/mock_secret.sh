echo 'object Secret {
    const val googleClientId = "";

    const val debugStoreFile = "mocked/store";
    const val releaseStoreFile = "mocked/store";
    const val storePassword = "";

    const val releaseKeyAlias = "";
    const val releaseKeyPassword = "";

    const val releaseXKeyAlias = "";
    const val releaseXKeyPassword = "";
}' >./buildSrc/src/main/java/Secret.kt