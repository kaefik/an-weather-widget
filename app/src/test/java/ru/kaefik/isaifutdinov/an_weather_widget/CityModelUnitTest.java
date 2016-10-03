package ru.kaefik.isaifutdinov.an_weather_widget;

import org.junit.Test;

import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel2;
import ru.kaefik.isaifutdinov.an_weather_widget.utils.Utils;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CityModelUnitTest {


    @Test
    public void testgetLike() throws Exception {
//        city.setMYAPPID("76d6de6e46c704733f12c8738307dbb5");
        System.out.println(Utils.getLikeNameCity("Kazan"));

//        assertEquals(4, 2 + 2);
    }


    @Test
    public void testgetMainWeather() throws Exception {
        CityModel2 cc = Utils.getHttpWeather("Kazan");
        System.out.println(cc.getCountry());

    }

    // TODO: сделать проверку в методах доступа CityModel2 на null объект

}