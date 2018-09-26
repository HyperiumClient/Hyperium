package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.triggers.OnRegularTrigger
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.NotAbstract
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*

@External
@NotAbstract
abstract class XMLHttpRequest {
    private val USER_AGENT = "Mozilla/5.0"

    private var conn: HttpURLConnection? = null
    private var async = true

    private lateinit var methodCallback: OnTrigger

    var status: Int? = null
    var statusText: String? = null
    var responseText: String? = null
    var extras = HashMap<String, Any>()

    fun open(method: String, urlStr: String, async: Boolean) {
        try {
            this.async = async
            val url = URL(urlStr)

            this.status = -1
            this.statusText = null
            this.responseText = null

            this.conn = url.openConnection() as HttpURLConnection
            this.conn?.requestMethod = method
        } catch (e: Exception) {
            getLoader().console.printStackTrace(e)
        }
    }

    @Throws(IllegalArgumentException::class)
    fun addRequestHeader(key: String, value: String) {
        if (conn == null) throw IllegalArgumentException("Connection must be opened first!")
        conn?.addRequestProperty(key, value)
    }

    /**
     * Sets the callback method, passes in the XMLHttpRequest object
     *
     * @param method the method to be called back on completion of the request
     */
    fun setCallbackMethod(method: Any) {
        this.methodCallback = OnRegularTrigger(method, TriggerType.OTHER, getLoader())
    }

    /**
     * Send a post request to the currently opened connection
     *
     * @param parameters any number of post data, in the form of `"key", "value", "key", "value"`
     */
    fun send(vararg parameters: String) {
        addRequestHeader("User-Agent", USER_AGENT)

        try {
            if (async) {
                Thread { sendPost(*parameters) }.start()
            } else {
                sendPost(*parameters)
            }
        } catch (e: Exception) {
            getLoader().console.printStackTrace(e)
        }
    }

    /**
     * Send a GET request to the currently opened connection
     */
    fun send() {
        addRequestHeader("User-Agent", USER_AGENT)

        try {
            if (async) {
                Thread(Runnable { this.sendGet() }).start()
            } else {
                sendGet()
            }
        } catch (e: Exception) {
            getLoader().console.printStackTrace(e)
        }
    }

    /**
     * Get the value of the response header with the specified key
     *
     * @param headerName the key for the header
     * @return the value of the response header
     */
    @Throws(IllegalArgumentException::class)
    fun getResponseHeader(headerName: String): String? {
        if (conn == null) throw IllegalStateException("Connection must be opened first!")
        return conn?.getHeaderField(headerName)
    }

    private fun sendPost(vararg parameters: String) {
        try {
            val paramList = Arrays.asList(*parameters)

            val data = StringBuilder()

            var i = 0
            while (i < paramList.size) {
                val key = URLEncoder.encode(paramList[i], "UTF-8")
                val value = URLEncoder.encode(paramList[i + 1], "UTF-8")

                if (i != 0) {
                    data.append("&")
                }

                data.append(key).append("=").append(value)
                i += 2
            }

            conn?.doOutput = true
            val wr = OutputStreamWriter(conn?.outputStream)
            wr.write(data.toString())
            wr.flush()

            sendGet()
        } catch (e: Exception) {
            getLoader().console.printStackTrace(e)
        }
    }

    private fun sendGet() {
        try {
            this.status = conn?.responseCode
            this.statusText = conn?.responseMessage

            val input = BufferedReader(
                    InputStreamReader(conn?.inputStream))
            var inputLine = input.readLine()
            val response = StringBuilder()

            while (inputLine != null) {
                response.append(inputLine)
                inputLine = input.readLine()
            }

            input.close()

            this.responseText = response.toString()

            this.methodCallback.trigger(this)
        } catch (e: Exception) {
            getLoader().console.printStackTrace(e)
        }

    }

    internal abstract fun getLoader(): ILoader
}