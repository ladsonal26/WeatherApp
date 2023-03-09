package com.example.weatherapp

import android.app.DownloadManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.statusBarColor=Color.parseColor("#1383C3")
        var lat=intent.getStringExtra("lat")
        var long=intent.getStringExtra("long")
        getJasionData(lat,long)
    }

    private fun getJasionData(lat: String?, long: String?) {
            val API_KEY="137754a19db5e85f560d54045b6218f0"
            val queue=Volley.newRequestQueue(this)
            val url="https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"
        val jasonRequest=JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener {
                Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
            })
        queue.add(jasonRequest)

    }
    private fun setValues(response: JSONObject){
        //To Get city name from jason
        txt_cityname.text=response.getString("name")
        var lat=response.getJSONObject("coord").getString("lat")
        var long=response.getJSONObject("coord").getString("lon")
        txt_coordinates.text="${lat},${long}"

        txt_weather.text=response.getJSONArray("weather").getJSONObject(0).getString("main")
        //To Get minimum temprature from jason
        var mintemp=response.getJSONObject("main").getString("temp_min")
        mintemp=((((mintemp).toFloat()-273.15)).toInt()).toString()
        txt_mintemp.text=mintemp+"°C"

        //To Get maximum temprature from jason
        var maxtemp=response.getJSONObject("main").getString("temp_max")
        maxtemp=((ceil((maxtemp).toFloat()-273.15)).toInt()).toString()
        txt_maxtemp.text=maxtemp+"°C"
        txt_pressure.text=response.getJSONObject("main").getString("pressure")
        txt_humidity.text=response.getJSONObject("main").getString("humidity")+"%"

        txt_wind.text=response.getJSONObject("wind").getString("speed")
        txt_degree.text=response.getJSONObject("wind").getString("deg")+"°C"

    }
}