import java.util.*;

// Клас Товар
class Product {
    private String name;
    private double recommendedPrice;

    public Product(String name, double recommendedPrice) {
        this.name = name;
        this.recommendedPrice = recommendedPrice;
    }

    public String getName() {
        return name;
    }

    public double getRecommendedPrice() {
        return recommendedPrice;
    }

    @Override
    public String toString() {
        return name + " (рек. ціна: " + recommendedPrice + ")";
    }
}

// Клас Інтернет-магазин
class OnlineStore {
    private String storeName;
    private List<ProductPrice> productPrices;

    public OnlineStore(String storeName) {
        this.storeName = storeName;
        this.productPrices = new ArrayList<>();
    }

    public void addProduct(Product product, double price) {
        productPrices.add(new ProductPrice(product, price));
    }

    public String getStoreName() {
        return storeName;
    }

    public List<ProductPrice> getProductPrices() {
        return productPrices;
    }

    // Внутрішній клас для зберігання товару і його ціни
    static class ProductPrice {
        private Product product;
        private double price;

        public ProductPrice(Product product, double price) {
            this.product = product;
            this.price = price;
        }

        public Product getProduct() {
            return product;
        }

        public double getPrice() {
            return price;
        }
    }
}

// Клас Сервіс пошуку товарів
class SearchService {
    private List<OnlineStore> stores;

    public SearchService() {
        this.stores = new ArrayList<>();
    }

    public void addStore(OnlineStore store) {
        stores.add(store);
    }

    // Задача 1: знайти мінімальну ціну на заданий товар
    // Використовуємо типізований ітератор (b)
    public Double findMinPrice(String productName) {
        Double minPrice = null;

        Iterator<OnlineStore> storeIterator = stores.iterator();
        while (storeIterator.hasNext()) {
            OnlineStore store = storeIterator.next();

            Iterator<OnlineStore.ProductPrice> priceIterator = store.getProductPrices().iterator();
            while (priceIterator.hasNext()) {
                OnlineStore.ProductPrice pp = priceIterator.next();

                if (pp.getProduct().getName().equals(productName)) {
                    if (minPrice == null || pp.getPrice() < minPrice) {
                        minPrice = pp.getPrice();
                    }
                }
            }
        }

        return minPrice;
    }

    // Задача 2: скласти список магазинів з мінімальною ціною
    // Використовуємо цикл for-each (c)
    public List<String> findStoresWithMinPrice(String productName) {
        List<String> result = new ArrayList<>();
        Double minPrice = findMinPrice(productName);

        if (minPrice == null) {
            return result;
        }

        for (OnlineStore store : stores) {
            for (OnlineStore.ProductPrice pp : store.getProductPrices()) {
                if (pp.getProduct().getName().equals(productName) &&
                        pp.getPrice() == minPrice) {
                    result.add(store.getStoreName());
                    break;
                }
            }
        }

        return result;
    }

    // Задача 3: чи є магазин з усіма товарами дешевше рек. ціни
    // Використовуємо нетипізований ітератор (a)
    public boolean hasStoreWithAllCheaperThanRecommended() {
        Iterator storeIterator = stores.iterator();

        while (storeIterator.hasNext()) {
            OnlineStore store = (OnlineStore) storeIterator.next();
            boolean allCheaper = true;

            Iterator priceIterator = store.getProductPrices().iterator();
            while (priceIterator.hasNext()) {
                OnlineStore.ProductPrice pp = (OnlineStore.ProductPrice) priceIterator.next();

                if (pp.getPrice() >= pp.getProduct().getRecommendedPrice()) {
                    allCheaper = false;
                    break;
                }
            }

            if (allCheaper && !store.getProductPrices().isEmpty()) {
                return true;
            }
        }

        return false;
    }
}

// Головний клас
public class Main {
    public static void main(String[] args) {
        // Створення товарів
        Product laptop = new Product("Ноутбук", 25000);
        Product phone = new Product("Телефон", 15000);
        Product tablet = new Product("Планшет", 10000);

        // Створення магазинів
        OnlineStore store1 = new OnlineStore("Розетка");
        store1.addProduct(laptop, 24000);
        store1.addProduct(phone, 14500);
        store1.addProduct(tablet, 9500);

        OnlineStore store2 = new OnlineStore("Comfy");
        store2.addProduct(laptop, 23500);
        store2.addProduct(phone, 15500);

        OnlineStore store3 = new OnlineStore("Фокстрот");
        store3.addProduct(laptop, 23500);
        store3.addProduct(tablet, 9800);

        // Створення сервісу
        SearchService service = new SearchService();
        service.addStore(store1);
        service.addStore(store2);
        service.addStore(store3);

        // Тестування задач
        System.out.println("=== Задача 1 ===");
        Double minPrice = service.findMinPrice("Ноутбук");
        System.out.println("Мінімальна ціна на Ноутбук: " + minPrice);

        System.out.println("\n=== Задача 2 ===");
        List<String> stores = service.findStoresWithMinPrice("Ноутбук");
        System.out.println("Магазини з мінімальною ціною: " + stores);

        System.out.println("\n=== Задача 3 ===");
        boolean hasCheaper = service.hasStoreWithAllCheaperThanRecommended();
        System.out.println("Чи є магазин з усіма товарами дешевше рек. ціни: " + hasCheaper);
    }
}