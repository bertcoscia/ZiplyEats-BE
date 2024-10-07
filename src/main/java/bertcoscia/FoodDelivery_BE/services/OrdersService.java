package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.*;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.edit.EditOrdersDTO;
import bertcoscia.FoodDelivery_BE.payloads.newEntities.NewOrderProductsDTO;
import bertcoscia.FoodDelivery_BE.payloads.newEntities.NewOrdersDTO;
import bertcoscia.FoodDelivery_BE.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrdersService {
    @Autowired
    OrdersRepository repository;

    @Autowired
    RestaurantsService restaurantsService;

    @Autowired
    UsersService usersService;

    @Autowired
    RidersService ridersService;

    @Autowired
    OrderStatusesService orderStatusesService;

    @Autowired
    ProductsService productsService;

    @Autowired
    ToppingsService toppingsService;

    public Order save(UUID idUser, NewOrdersDTO body) {
        User userFound = this.usersService.findById(idUser);
        Restaurant restaurantFound = this.restaurantsService.findById(body.idRestaurant());
        OrderStatus orderStatusFound = this.orderStatusesService.findByOrderStatus("CREATED");
        Order newOrder = new Order(userFound, restaurantFound, body.deliveryAddress(), orderStatusFound);
        for (NewOrderProductsDTO orderProductDTO : body.orderProductList()) {
            Product productFound = this.productsService.findById(orderProductDTO.idProduct());
            List<Topping> toppingList = null;
            if (orderProductDTO.toppings() != null && !orderProductDTO.toppings().isEmpty()) {
                toppingList = this.toppingsService.findAllById(orderProductDTO.toppings());
            }
            newOrder.addOrderProduct(productFound, toppingList); //
        }
        return this.repository.save(newOrder);
    }

    public Order findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public Order restaurantAcceptsOrder(UUID id) {
        Order orderFound = this.findById(id);
        OrderStatus orderStatusFound = this.orderStatusesService.findByOrderStatus("RESTAURANT_ACCEPTED");
        orderFound.setOrderStatus(orderStatusFound);
        return this.repository.save(orderFound);
    }

    public Order assignRiderToOrder(UUID idOrder, UUID idRider) {
        Rider riderFound = this.ridersService.findById(idRider);
        if (riderFound.isBusyWithOrder()) throw new BadRequestException("Rider " + riderFound.getUsername() + " is currently busy with an order");
        Order orderFound = this.findById(idOrder);
        if (orderFound.getRider() != null) throw new BadRequestException("The order n. " + orderFound.getIdOrder() + " already has a rider");
        orderFound.setRider(riderFound);
        this.repository.save(orderFound);
        this.ridersService.setRiderAsBusy(riderFound);
        return orderFound;
    }

    public Order finaliseOrder(UUID idOrder) {
        Order orderFound = this.findById(idOrder);
        OrderStatus orderStatusFound = this.orderStatusesService.findByOrderStatus("DELIVERED");
        orderFound.setOrderStatus(orderStatusFound);
        this.repository.save(orderFound);
        Rider riderFound = this.ridersService.findById(orderFound.getRider().getIdUser());
        this.ridersService.setRiderAvailable(riderFound);
        return orderFound;
    }

    public Order cancelOrder(UUID idOrder) {
        Order orderFound = this.findById(idOrder);
        OrderStatus orderStatusFound = this.orderStatusesService.findByOrderStatus("CANCELLED");
        orderFound.setOrderStatus(orderStatusFound);
        this.repository.save(orderFound);
        if (orderFound.getRider() != null) {
            Rider riderFound = this.ridersService.findById(orderFound.getRider().getIdUser());
            this.ridersService.setRiderAvailable(riderFound);
        }
        return orderFound;
    }

    public Order findByIdAndUpdate(UUID idOrder, EditOrdersDTO body) {
        Order found = this.findById(idOrder);
        found.setDeliveryAddress(body.deliveryAddress());
        return this.repository.save(found);
    }

}
