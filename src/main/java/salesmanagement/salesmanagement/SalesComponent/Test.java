package salesmanagement.salesmanagement.SalesComponent;

import java.util.Locale;

public class Test {

    public static void main(String[] args) {
        Locale[] countries = Locale.getAvailableLocales();
        String[] code = Locale.getISOCountries();
        for (int i = 0; i < code.length; i++) {
            System.out.println(countries[i].getDisplayName() + " " + code[i]);
        }
    }
}
