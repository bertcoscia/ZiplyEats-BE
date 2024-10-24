package bertcoscia.ZiplyEats_BE.controllers;

import bertcoscia.ZiplyEats_BE.entities.Order;
import bertcoscia.ZiplyEats_BE.entities.OrderProduct;
import bertcoscia.ZiplyEats_BE.entities.Product;
import bertcoscia.ZiplyEats_BE.entities.Topping;
import bertcoscia.ZiplyEats_BE.services.OrderProductsService;
import bertcoscia.ZiplyEats_BE.services.OrdersService;
import bertcoscia.ZiplyEats_BE.services.ProductsService;
import bertcoscia.ZiplyEats_BE.services.ToppingsService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class CheckoutController {
    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    @Autowired
    ProductsService productsService;

    @Autowired
    ToppingsService toppingsService;

    @Autowired
    OrderProductsService orderProductsService;

    @Autowired
    OrdersService ordersService;

    @PostMapping("/create-checkout-session")
    @PreAuthorize("hasAuthority('USER')")
    public Map<String, String> createCheckoutSession(@RequestBody UUID idOrder) throws StripeException {
        Order existingOrder = this.ordersService.findById(idOrder);
        if (existingOrder == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        long totalPriceInCents = (long) (existingOrder.getTotalPrice() * 100);
        if (totalPriceInCents < 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Total amount must be at least €0.50");
        }

        List<SessionCreateParams.LineItem> stripeLineItems = new ArrayList<>();

        for (OrderProduct orderProduct : existingOrder.getOrderProducts()) {
            Long price = (long) (orderProduct.calculatePrice() * 100);
            StringBuilder productName = new StringBuilder();
            if (!orderProduct.getToppings().isEmpty()) {
                productName.append(orderProduct.getProduct().getName()).append(" (").append(getAllToppingsNames(orderProduct.getToppings())).append(")");
            } else {
                productName.append(orderProduct.getProduct().getName());
            }

            SessionCreateParams.LineItem.Builder lineItemBuilder = SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("eur")
                                    .setUnitAmount(price)
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(String.valueOf(productName))
                                                    .build()
                                    )
                                    .build()
                    );

            stripeLineItems.add(lineItemBuilder.build());
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "/success/" + idOrder)
                .setCancelUrl("https://tuo-sito.com/cancel") //TODO: CANCEL PAGE
                .addAllLineItem(stripeLineItems)
                .build();

        Session session = Session.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("id", session.getId());

        return response;
    }

    private void saveOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProductsService.saveAll(orderProducts);
    }

    private List<Topping> getToppingsByIds(List<String> toppings) {
        List<UUID> uuidList = new ArrayList<>();
        for (String idString : toppings) uuidList.add(UUID.fromString(idString));
        return this.toppingsService.findAllById(uuidList);
    }

    private Product getProductById(UUID uuid) {
        return this.productsService.findById(uuid);
    }

    private Long getProductPrice(UUID uuid) {
        Product found = this.productsService.findById(uuid);
        return (long) found.getPrice();
    }

    private String getAllToppingsNames(List<Topping> toppings) {
        List<String> toppingNames = new ArrayList<>();
        for (Topping topping : toppings) {
            toppingNames.add(topping.getName());
        }
        return String.join(", ", toppingNames);
    }
}