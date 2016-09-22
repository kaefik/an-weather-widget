package ru.kaefik.isaifutdinov.an_weather_widget.city;

// класс информации о погоде города - вторая редакция специально для разбора json c помощью gson
public class CityModel2 {
     long id; // ID города
     String name;  // название города
//    @SerializedName("temp")


     public CityModel2(long id, String name) {
         this.id = id;
         this.name = name;
     }

     public CityModel2(String name) {
         this.name = name;
     }

     public CityModel2() {
         this.name ="";
         id=0;
     }

     public void setId(long id) {
         this.id = id;
     }

     public void setName(String name) {
         this.name = name;
     }

     public long getId() {
         return id;
     }

     public String getName() {
         return name;
     }
 }
