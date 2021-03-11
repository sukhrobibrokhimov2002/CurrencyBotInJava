import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class ConvertorConnections {
    public static List<CurrencyClass> currencyArrayList;
    public static List<CurrencyClass> getCurrencyArrayList() throws IOException {
        URL url=new URL("https://cbu.uz/oz/arkhiv-kursov-valyut/json/");
        URLConnection connection=url.openConnection();
        BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        Gson gson=new Gson();
        CurrencyClass[] currencyClasses=gson.fromJson(reader,CurrencyClass[].class);
        currencyArrayList= Arrays.asList(currencyClasses);

        return currencyArrayList;
    }
    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

}
