
package product;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class ProductHandler {

    private static final String FILE_NAME = "products.xml";

    // Read all products from file
    public static synchronized List<Product> getAllProducts()
            throws Exception {

        File file = new File(FILE_NAME);

        System.out.println("XML LOCATION: " + file.getAbsolutePath());

        if (!file.exists()) {
            return new ArrayList<>();
        }

        JAXBContext context =
                JAXBContext.newInstance(ProductList.class);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        ProductList productList =
                (ProductList) unmarshaller.unmarshal(file);

        return productList.getProducts();
    }

    // Save products to file
    private static synchronized void saveProducts(
            List<Product> products) throws Exception {

        JAXBContext context =
                JAXBContext.newInstance(ProductList.class);

        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(
                Marshaller.JAXB_FORMATTED_OUTPUT, true);

        ProductList productList = new ProductList(products);

        marshaller.marshal(productList, new File(FILE_NAME));
    }

    // Find one product
    public static synchronized Product getProduct(String name)
            throws Exception {

        for (Product product : getAllProducts()) {

            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }

        return null;
    }

    // Add product
    public static synchronized boolean addProduct(Product product)
            throws Exception {

        List<Product> products = getAllProducts();

        if (getProduct(product.getName()) != null) {
            return false;
        }

        products.add(product);

        saveProducts(products);

        return true;
    }

    // Update product
    public static synchronized boolean updateProduct(
            String name, Product updatedProduct) throws Exception {

        List<Product> products = getAllProducts();

        for (int i = 0; i < products.size(); i++) {

            if (products.get(i).getName().equalsIgnoreCase(name)) {

                products.set(i, updatedProduct);

                saveProducts(products);

                return true;
            }
        }

        return false;
    }

    // Delete product
    public static synchronized boolean deleteProduct(String name)
            throws Exception {

        List<Product> products = getAllProducts();

        boolean removed = products.removeIf(
                p -> p.getName().equalsIgnoreCase(name)
        );

        if (removed) {
            saveProducts(products);
        }

        return removed;
    }

    // Find cheapest product
    public static synchronized Product getCheapestProduct()
            throws Exception {

        return getAllProducts().stream()
                .min(Comparator.comparingDouble(
                        Product::getUnitPrice))
                .orElse(null);
    }

    // Find most expensive product
    public static synchronized Product getMostExpensiveProduct()
            throws Exception {

        return getAllProducts().stream()
                .max(Comparator.comparingDouble(
                        Product::getUnitPrice))
                .orElse(null);
    }
}
