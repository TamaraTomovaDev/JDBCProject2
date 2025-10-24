import repositories.BeerRepository;
import repositories.BrewerRepository;
import repositories.CategoryRepository;

import java.util.Arrays;
import java.util.List;

public class DatabaseApp {
    public static void main(String[] args) {
        System.out.println("------ Categories -------");
        CategoryRepository categoryRepository = new CategoryRepository();
        categoryRepository.read();
        categoryRepository.createMultiple("Light1", "Light2", "Light3");
        categoryRepository.update("Light01", "Light1");
        categoryRepository.deleteMultiple( "Light2", "Light3");
        categoryRepository.delete("Light01");

        System.out.println("------ Brewers -------");
        BrewerRepository brewerRepository = new BrewerRepository();
        brewerRepository.read();
        brewerRepository.create("Test", "Testlaan 2", "1000", "Test", 100000);
        List<Object[]> brouwers = Arrays.asList(
                new Object[]{"Brouwerij Zon", "Zonnestraat 1", "1000", "Brussel", 500000},
                new Object[]{"Brouwerij Maan", "Maanplein 2", "2000", "Antwerpen", 750000},
                new Object[]{"Brouwerij Ster", "Sterlaan 3", "3000", "Leuven", 600000});
        brewerRepository.createMultiple(brouwers);
        brewerRepository.update(1, "Brouwerij Zonlicht", "Zonnestraat 1A", "1000", "Brussel", 550000);

        System.out.println("------ Beers -------");
        BeerRepository beerRepository = new BeerRepository();
        beerRepository.read();
        beerRepository.create("Testbier", 1, 34, 2.5f, 100, 5.0f, 1, null);
        List<Object[]> beers = Arrays.asList(
                new Object[]{"Zwijntje (=Augustijn)", 14, 15, 2.86f, 200, 8.0f, 0, null},
                new Object[]{"Zwintje", 74, 6, 2.65f, 200, 6.0f, 0, null},
                new Object[]{"Zwoele Berten (=Bornem dubbel)", 14, 14, 2.04f, 200, 0.0f, 0, null}
        );
        beerRepository.createMultiple(beers);

        beerRepository.update(1557, "Testbier aangepast", 1, 1, 2.8f, 120, 5.2f, 2, null);
        beerRepository.delete(1557);
        beerRepository.deleteMultiple(1558, 1559, 1560);
    }
}
