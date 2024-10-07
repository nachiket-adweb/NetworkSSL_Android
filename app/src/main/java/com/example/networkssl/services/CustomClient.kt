package com.example.networkssl.services

/*
// Custom HttpURLConnectionClient
class CustomHttpClient(private val context: Context) : retrofit2.client.Client {
    override fun execute(request: retrofit2.client.Request): retrofit2.client.Response {
        val url = URL(request.url)
        val connection = url.openConnection() as HttpURLConnection

        // Apply custom SSLSocketFactory for SSL Pinning
        if (connection is javax.net.ssl.HttpsURLConnection) {
            connection.sslSocketFactory = getPinnedSSLSocketFactory(context)
        }

        connection.requestMethod = request.method
        connection.connectTimeout = 10000
        connection.readTimeout = 10000
        connection.doInput = true

        // Pass headers
        request.headers.forEach {
            connection.setRequestProperty(it.name, it.value)
        }

        // Handle request body for POST/PUT if needed
        if (request.body != null) {
            connection.doOutput = true
            connection.outputStream.use {
                request.body.writeTo(it)
            }
        }

        // Get the response
        val responseCode = connection.responseCode
        val responseStream =
            if (responseCode in 200..299) connection.inputStream else connection.errorStream

        return retrofit2.client.Response(
            request.url,
            responseCode,
            connection.responseMessage,
            request.headers,
            responseStream,
        )
    }
}

 */