package ru.kaefik.isaifutdinov.an_weather_widget;

import org.junit.Test;

import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CityModelUnitTest {
    @Test
    public void testgetLike() throws Exception {
        CityModel city= new CityModel("Kazan");
        city.setMYAPPID("76d6de6e46c704733f12c8738307dbb5");
        city.getLikeNameCity("Kazan");
//        city.getHttpWeather();

//        assertEquals(4, 2 + 2);
    }
}