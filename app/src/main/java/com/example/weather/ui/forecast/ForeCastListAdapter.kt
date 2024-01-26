package com.example.weather.ui.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.model.DataList
import com.example.weather.utils.convertKelvinToFarenhit
import com.example.weather.utils.degToCompass

class ForeCastListAdapter(private val dataSet: List<DataList>) :
    RecyclerView.Adapter<ForeCastListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val windSpeed: TextView
        val direction: TextView
        val minTemp: TextView
        val maxTemp: TextView
        val weatherDescription:TextView
        val weatherIcon:ImageView
        val weatherView: CardView

        init {
            // Define  listener for the ViewHolder's View.
            windSpeed = view.findViewById(R.id.wind_speed)
            direction = view.findViewById(R.id.direction)
            minTemp = view.findViewById(R.id.hi_temp)
            maxTemp = view.findViewById(R.id.low_temp)
            weatherDescription = view.findViewById(R.id.wind_description)
            weatherIcon = view.findViewById(R.id.weather_icon)
            weatherView = view.findViewById(R.id.weather_view)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_weather_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.windSpeed.text = "${dataSet[position].wind.speed.toInt()} mph"
        viewHolder.direction.text = degToCompass(dataSet[position].wind.deg)
        viewHolder.weatherDescription.text = dataSet[position].weather.firstOrNull()?.description ?: ""
        viewHolder.minTemp.text = "Hi Temp: ${dataSet[position].main.temp_max.convertKelvinToFarenhit()}"
        viewHolder.maxTemp.text = "Lo Temp: ${dataSet[position].main.temp_min.convertKelvinToFarenhit()}"
        Glide.with(viewHolder.weatherIcon.context)
            .load("https://openweathermap.org/img/wn/${dataSet[position].weather.firstOrNull()?.icon ?: ""}@2x.png")
            .into(viewHolder.weatherIcon)
    }

    override fun getItemCount() = dataSet.size

}