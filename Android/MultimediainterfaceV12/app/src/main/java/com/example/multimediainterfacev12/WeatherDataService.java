package com.example.multimediainterfacev12;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataService {

    Context context;
    String lat;
    //Values in the WEATHER ARRAY
    String main, description, icon;
    //Values in the main Object
    String temp, feels_like, temp_min, temp_max, pressure, humidity;
    //Values in the wind Object
    String speed, deg;
    //Values in the clouds Object
    String all;
    //Values in the main JSONObject
    String cityName;

    private static final String TAG = "WeatherDataService";


    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener{
        void onError(String message);

        void onResponse(String main, String description, String icon, String temp, String temp_min, String temp_max, String feels_like, String pressure, String humidity, String cityName);
    }

    public void getCityID(Double latitude, Double longitude, VolleyResponseListener volleyResponseListener){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.i(TAG, "Entered getCityID");

        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=63b3859e68a3572bd20b683f5dde6411" + "&units=metric";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
//                    base = response.getString("base");
//                    JSONObject cityCoord = response.getJSONObject("coord");
//                    lat = cityCoord.getString("lat");

                    JSONArray weatherArray = response.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);
                    main = weather.getString("main");
                    description = weather.getString("description");
                    icon = weather.getString("icon");

                    JSONObject mainObj = response.getJSONObject("main");
                    temp = mainObj.getString("temp");
                    temp_min = mainObj.getString("temp_min");
                    temp_max = mainObj.getString("temp_max");
                    feels_like = mainObj.getString("feels_like");
                    pressure = mainObj.getString("pressure");
                    humidity = mainObj.getString("humidity");

                    cityName = response.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(context, "Lat=" + latitude + " Long=" + longitude, Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Latitude = " + main, Toast.LENGTH_SHORT).show();

//                Toast.makeText(context, "Main=" + main + " Description=" + description + " Icon=" + icon +
//                        "Temp=" + temp + " temp_min=" + temp_min +
//                        " temp_min=" + temp_min + " fells_like=" + feels_like +
//                        " pressure=" + pressure + " humidity=" + humidity +
//                        " cityName=" + cityName, Toast.LENGTH_LONG).show();
                volleyResponseListener.onResponse(main, description, icon, temp, temp_min, temp_max, feels_like, pressure, humidity, cityName);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something wrong!");
            }
        });


/*        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
            }
        });
 */

        // Add the request to the RequestQueue.
        //queue.add(stringRequest);
        MySingleton.getInstance(context).addToRequestQueue(request);

        //return lat;

    }
}
