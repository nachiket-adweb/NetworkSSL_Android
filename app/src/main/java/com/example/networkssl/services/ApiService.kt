package com.example.networkssl.services

import android.content.Context
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

interface ApiService {
    @GET("sample/employees.json")
    suspend fun getJsonText(): Response<JsonObject>
}

// Function to get the SSL Socket Factory
//fun getPinnedSSLSocketFactory(context: Context): SSLSocketFactory {
//    val certificateFactory = CertificateFactory.getInstance("X.509")
//    val inputStream = context.resources.openRawResource()
//    val certificate = certificateFactory.generateCertificate(inputStream) as X509Certificate
//    inputStream.close()
//
//    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
//    keyStore.load(null, null)
//    keyStore.setCertificateEntry("server", certificate)
//
//    val trustManagerFactory =
//        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
//    trustManagerFactory.init(keyStore)
//    val trustManagers = trustManagerFactory.trustManagers
//    val trustManager = trustManagers[0] as X509TrustManager
//
//    val sslContext = SSLContext.getInstance("TLS")
//    sslContext.init(null, arrayOf(trustManager), null)
//
//    return sslContext.socketFactory
//}

fun makePinnedRequest(context: Context, urlString: String) {
    val url = URL(urlString)
    val urlConnection = url.openConnection() as HttpsURLConnection

    // Set the custom SSLSocketFactory for SSL Pinning
//    urlConnection.sslSocketFactory = getPinnedSSLSocketFactory(context)

    try {
        val responseCode = urlConnection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = urlConnection.inputStream
            // Process the response...
            print("TRUEEE RESPONSE");
        }
    } finally {
        print("FINALLY RESPONSE");
        urlConnection.disconnect()
    }
}