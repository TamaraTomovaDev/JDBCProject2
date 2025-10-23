
import repositories.BeerRepository;
import repositories.BrewerRepository;
import repositories.CategoryRepository;

public class DatabaseApp {
    public static void main(String[] args) {
        System.out.println("------ Categories -------");
        CategoryRepository categoryRepository = new CategoryRepository();
        categoryRepository.read();

        System.out.println("------ Brewers -------");
        BrewerRepository brewerRepository = new BrewerRepository();
        brewerRepository.read();

        System.out.println("------ Beers -------");
        BeerRepository beerRepository = new BeerRepository();
        beerRepository.read();
    }
}
