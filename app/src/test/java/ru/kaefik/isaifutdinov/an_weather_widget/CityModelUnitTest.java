package ru.kaefik.isaifutdinov.an_weather_widget;

import org.junit.Test;

import ru.kaefik.isaifutdinov.an_weather_widget.city.CityModel2;
import ru.kaefik.isaifutdinov.an_weather_widget.utils.Utils;

import static java.lang.System.out;
import static ru.kaefik.isaifutdinov.an_weather_widget.utils.Utils.getLikeNameCity;
import static ru.kaefik.isaifutdinov.an_weather_widget.utils.Utils.replaceSpaceForHttpSpace;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CityModelUnitTest {


    @Test
    public void testgetLike() throws Exception {
//        city.setMYAPPID("76d6de6e46c704733f12c8738307dbb5");
        out.println(getLikeNameCity("Kazan"));

//        assertEquals(4, 2 + 2);
    }


    @Test
    public void testgetMainWeather() throws Exception {
        CityModel2 cc = new CityModel2("Kazan", "TR");
        System.out.println(cc.getCountry());
        System.out.println(cc.getName());

    }


    // TODO: сделать проверку в методах доступа CityModel2 на null объект

    @Test
    public void testgetNameCituAndCountry() throws Exception {
        String ss = "Kazan-RU";
        String ss1 = " Kazan - RU ";
        out.println(Utils.getCountry(ss));
        out.println(Utils.getNameCity(ss));
        out.println(Utils.getCountry(ss1));
        out.println(Utils.getNameCity(ss1));
    }

    @Test
    public void testreplaceSpaceForHttpSpace() throws Exception {
//        replaceSpaceForHttpSpace
        String ss ="New York";
        String s1s =" New York ";
        System.out.println(replaceSpaceForHttpSpace(ss));
        System.out.println(replaceSpaceForHttpSpace(s1s));
    }

}