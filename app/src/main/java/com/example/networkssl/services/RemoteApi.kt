@file:OptIn(ExperimentalEncodingApi::class)

package com.example.networkssl.services

import android.content.res.Resources
import com.example.networkssl.R
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.security.KeyStore
import java.security.MessageDigest
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class RemoteApi {

    val Base_Url = "https://devapi.adwebtech.com/sample/employees.json"

    fun loadRoots(resources: Resources): Array<X509Certificate> {
        val certFactory = CertificateFactory.getInstance("X.509")
        val inputStream: InputStream = resources.openRawResource(R.raw.digicert_root)
        val certRoot = certFactory.generateCertificate(inputStream) as X509Certificate
        inputStream.close()

        val inputStream2: InputStream = resources.openRawResource(R.raw.thawte_inter)
        val certInter = certFactory.generateCertificate(inputStream2) as X509Certificate
        inputStream2.close()

        val inputStream3: InputStream = resources.openRawResource(R.raw.isrg_root)
        val certRoot1 = certFactory.generateCertificate(inputStream3) as X509Certificate
        inputStream3.close()

        val inputStream4: InputStream = resources.openRawResource(R.raw.r11_inter)
        val certInter1 = certFactory.generateCertificate(inputStream4) as X509Certificate
        inputStream4.close()

//        val inputStreamLast: InputStream = resources.openRawResource(R.raw.devapi)
//        val certFinal = certFactory.generateCertificate(inputStreamLast) as X509Certificate
//        inputStreamLast.close()

        return arrayOf(certRoot, certInter, certRoot1, certInter1)
    }

    fun getSslContext(resources: Resources): SSLSocketFactory {
        val trustedRoots = loadRoots(resources)
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        val systemKeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        systemKeyStore.load(null, null)
        trustedRoots.forEachIndexed { index, certs ->
            systemKeyStore.setCertificateEntry("trustedCert$index", certs)
        }
        trustManagerFactory.init(systemKeyStore)

        val trustManagers = trustManagerFactory.trustManagers
        val defTrustManager = trustManagers[0] as X509TrustManager

        val trustManager = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                defTrustManager.checkServerTrusted(chain, authType)
                // pinning code  // leaf cert should be hash
                // Get the server's leaf certificate (the first in the chain)
                val serverCert =
                    chain?.get(0) ?: throw CertificateException("No server certificate found")

                // Hash the server's leaf certificate
                val serverCertHash = hashCertificate(serverCert)

                // Load and hash the pinned certificate
//                val pinnedCertHash = getPinnedCert(resources)
                val strCertHash = "NU8GyRut/Rm1gNy96Icz/g8Q2a4g0Y5nh6AZ9labqP0="
                val pinnedCertHash = Base64.decode(strCertHash)
                println("B64 Hash :: ${Base64.encode(pinnedCertHash)}")

                // Compare the server's leaf certificate hash with the pinned certificate hash
                if (!serverCertHash.contentEquals(pinnedCertHash)) {
                    println("PINNING FAILED")
                    throw CertificateException("Server certificate pinning validation failed!")
                }
                println("PINNING SUCCCEEDDD")
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return defTrustManager.acceptedIssuers
            }

        }

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)

        return sslContext.socketFactory
    }


//    fun getPinnedCert(resources: Resources): ByteArray {
//        val inputStream = resources.openRawResource(R.raw.devapi)
//        val pinnedCertBytes = inputStream.readBytes()
//        inputStream.close()
//
//        val factory = CertificateFactory.getInstance("X.509")
//        val pinnedCert =
//            factory.generateCertificate(pinnedCertBytes.inputStream()) as X509Certificate
//
//        return hashCertificate(pinnedCert)
//    }

    fun hashCertificate(cert: X509Certificate): ByteArray {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(cert.encoded)
    }


    fun getMyJsonTextSSL(sslSFctry: SSLSocketFactory): String {
        val responseData = StringBuilder()
        with(URL(Base_Url).openConnection() as HttpsURLConnection) {
            sslSocketFactory = sslSFctry
            requestMethod = "GET"
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            connectTimeout = 10000
            readTimeout = 10000
            try {
                val reader = InputStreamReader(inputStream)
                reader.use { input ->
                    val bufferedReader = BufferedReader(input)
                    bufferedReader.forEachLine {
                        responseData.append(it.trim())
                    }
                }
                println("::::")
                println("RESPONSE GOTTT >> $responseData")
                println("::::")
            } catch (e: Exception) {
                println("::::")
                println("ERROR GOTTT >> $e")
                e.printStackTrace()
                println("::::")
            } finally {
                disconnect()
            }
        }
        return responseData.toString()
    }

    fun getMyJsonText(): String {
        val responseData = StringBuilder()
        with(URL(Base_Url).openConnection() as HttpsURLConnection) {
            requestMethod = "GET"
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            connectTimeout = 10000
            readTimeout = 10000
            try {
                val reader = InputStreamReader(inputStream)
                reader.use { input ->
                    val bufferedReader = BufferedReader(input)
                    bufferedReader.forEachLine { responseData.append(it.trim()) }
                }
                println("RESPONSE GOTTT >> $responseData")
            } catch (e: Exception) {
                e.printStackTrace() // Log or handle the exception appropriately
            } finally {
                disconnect()
            }
        }
        return responseData.toString()
    }
}


//        val keyStore = KeyStore.getInstance("PKCS12").apply {
//            load(null, null)
//            setCertificateEntry("trustedCertificate", certificate)
//        }

//        val trustManagerFactory =
//            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
//                init(keyStore) // Pass the loaded keyStore
//            }
//        val trustManager = trustManagerFactory.trustManagers[0] as X509TrustManager

//        return SSLContext.getInstance("TLSv1.2").apply {
//            init(null, trustManagerFactory.trustManagers, null)
//        }
//        val sslContext = SSLContext.getInstance("TLSv1.2")
//        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)

//val Cat_Url = "https://cat-fact.herokuapp.com/facts"