package simulator;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import company.customer.Customer;
import company.customer.CustomerStore;
import company.customer.PostalAddress;
import company.main.Company;
import company.main.CompanyType;
import company.order.Delivery;
import company.order.DeliveryStore;
import company.order.Order;
import company.order.OrderStore;
import company.product.Product;
import company.product.ProductStore;
import company.product.ProductType;
import company.product.ProductTypeStore;
import company.transportation.Transportation;
import company.transportation.TransportationMode;
import company.transportation.TransportationStore;
import jedis.JedisManager;
import redis.clients.jedis.GeoCoordinate;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Generates default data for simulation
 */
final class DefaultDataGenerator {

    private static ProductStore productStore = new ProductStore();
    private static ProductTypeStore productTypeStore = new ProductTypeStore();
    private static TransportationStore transportationStore = new TransportationStore();
    private static CustomerStore customerStore = new CustomerStore();
    private static OrderStore orderStore = new OrderStore();
    private static DeliveryStore deliveryStore = new DeliveryStore();
    private static Company company = DefaultDataGenerator.generateRandomCompany();
    private static final Logger logger = Logger.getLogger(DefaultDataGenerator.class.getName());

    private DefaultDataGenerator(){}

    public static void generateDefaultDatabase() {
        deleteAll();
        generateDatabase();
    }

    public static Customer generateRandomCustomer() {
        Faker faker = new Faker();
        Name name = faker.name();
        return new Customer(name.firstName(),name.lastName(),DefaultDataGenerator.generateRandomAddress(),
                faker.internet().emailAddress(),faker.phoneNumber().phoneNumber());
    }

    public static PostalAddress generateRandomAddress() {
        Faker faker = new Faker();
        Address address = faker.address();
        return new PostalAddress(address.streetAddress(),address.cityName(),address.state(),address.country(),
                address.zipCode());
    }


    public static Company generateRandomCompany() {
        CompanyType companyType = CompanyType.DOMESTIC;
        Random random = new Random();
        int rand = random.nextInt(3);
        switch (rand) {
            case 0:
                companyType = CompanyType.DOMESTIC;
                break;
            case 1:
                companyType = CompanyType.INTERNATIONAL;
                break;
            case 2:
                companyType = CompanyType.GLOBAL;
                break;
        }
        Faker faker = new Faker();
        return new Company(companyType,faker.company().name(),DefaultDataGenerator.generateRandomAddress());
    }

    private static void generateDatabase() {
        logger.info("Flushing database...");
        deleteAll();
        logger.info("Done");
        logger.info("Populating database...");
        generateProductTypes();
        generateProducts();
        generateTransportation();
        generateCustomers();
        generateOrders();
        generateDeliveries();
        logger.info("All done !");
    }

    private static void generateProductTypes() {
        productTypeStore.add(new ProductType("Apple","Germany",0.5f,0.2f,false));
        productTypeStore.add(new ProductType("Diamond","Brasil",1000.0f,1.4f,true));
        productTypeStore.add(new ProductType("Basket ball","USA",30.0f,1.0f,false));
        productTypeStore.add(new ProductType("Crystal glass","France",198.4f,1.5f,true));
        productTypeStore.add(new ProductType("Sony QLED TV","Japan",4509.99f,20.4f,true));
    }

    private static void generateProducts() {
        GeoCoordinate coordinate = new GeoCoordinate(100,100);
        Product apple;
        for (int i = 0; i < 20000; i++) {
            apple = new Product(productTypeStore.getStorage().get(0),coordinate);
            productStore.add(apple);
        }
        Product basketBall;
        for (int i = 0; i < 100; i++) {
            basketBall = new Product(productTypeStore.getStorage().get(3),coordinate,20);
            productStore.add(basketBall);
        }
        Product tv;
        for (int i = 0; i < 450; i++) {
            tv = new Product(productTypeStore.getStorage().get(4),coordinate,2499);
            productStore.add(tv);
        }
    }

    private static void generateTransportation() {
        transportationStore.add(new Transportation(450,60, TransportationMode.LAND_RAIL));
        transportationStore.add(new Transportation(259,140,TransportationMode.LAND_ROAD));
        transportationStore.add(new Transportation(30000,30,TransportationMode.WATER));
    }

    private static void generateCustomers() {
        for (int i = 0; i < 10; i++)
            customerStore.add(DefaultDataGenerator.generateRandomCustomer());
    }

    private static void generateOrders() {
        List<Product> products = productStore.getStorage();
        Random randomUtil = new Random();
        for (final Customer customer : customerStore.getStorage()) {
            for (int j = 0; j < randomUtil.nextInt(5)+1; j++) {
                Order order = new Order(customer);
                final int nbrProducts = products.size();
                for (int i = 0; i < randomUtil.nextInt(10)+1; i++)
                    order.getOrderedProducts().add(products.get(randomUtil.nextInt(nbrProducts)));
                orderStore.add(order);
            }
        }
    }

    private static void generateDeliveries() {
        Random randomUtil = new Random();
        for (final Customer customer : customerStore.getStorage()) {
            final int size = transportationStore.getStorage().size();
            deliveryStore.add(new Delivery(orderStore.getByCustomer(customer),transportationStore.getStorage().get(randomUtil.nextInt(size)),company.getHeadquarters(),company.getHeadquarters().asCoordinates(),customer.getPostalAddress()));
        }
    }

    private static void deleteAll() {
        JedisManager.getInstance().getResource().flushDB();
    }
}
