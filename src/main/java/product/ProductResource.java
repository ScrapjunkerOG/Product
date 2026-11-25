
package product;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/products")
public class ProductResource {

    // GET all products - XML
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public ProductList getAllProducts() throws Exception {

        return new ProductList(
                ProductHandler.getAllProducts());
        
    }

    // GET one product - JSON
    @GET
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Product getProduct(
            @PathParam("name") String name) throws Exception {

        Product product = ProductHandler.getProduct(name);

        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        return product;
    }

    // ADD product - JSON
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addProduct(Product product) throws Exception {

        boolean added = ProductHandler.addProduct(product);

        if (!added) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Product already exists")
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity("Product added successfully")
                .build();
    }

    // UPDATE product - JSON
    @PUT
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateProduct(
            @PathParam("name") String name,
            Product product) throws Exception {

        boolean updated =
                ProductHandler.updateProduct(name, product);

        if (!updated) {
            throw new NotFoundException("Product not found");
        }

        return Response.ok(
                "Product updated successfully").build();
    }

    // DELETE product - plain text
    @DELETE
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteProduct(
            @PathParam("name") String name) throws Exception {

        boolean deleted = ProductHandler.deleteProduct(name);

        if (!deleted) {
            throw new NotFoundException("Product not found");
        }

        return "Product deleted successfully";
    }

    // GET cheapest product - XML
    @GET
    @Path("/cheapest")
    @Produces(MediaType.APPLICATION_XML)
    public Product getCheapestProduct() throws Exception {

        Product product = ProductHandler.getCheapestProduct();

        if (product == null) {
            throw new NotFoundException(
                    "No products available");
        }

        return product;
    }

    // GET most expensive product - XML
    @GET
    @Path("/most-expensive")
    @Produces(MediaType.APPLICATION_XML)
    public Product getMostExpensiveProduct() throws Exception {

        Product product =
                ProductHandler.getMostExpensiveProduct();

        if (product == null) {
            throw new NotFoundException(
                    "No products available");
        }

        return product;
    }
}
