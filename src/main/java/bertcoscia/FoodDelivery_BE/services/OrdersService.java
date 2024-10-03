package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.*;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.NewOrdersDTO;
import bertcoscia.FoodDelivery_BE.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    ProductsService productsService;

    @Autowired
    OrderStatesService orderStatesService;

    public Order save(NewOrdersDTO body) {
        User userFound = this.usersService.findById(UUID.fromString(body.idUser()));
        Restaurant restaurantFound = this.restaurantsService.findById(UUID.fromString(body.idRestaurant()));
        List<UUID> productIds = body.productList().stream()
                .map(UUID::fromString)
                .toList();
        List<Product> productList = this.productsService.findAllById(productIds);
        OrderState orderStateFound = this.orderStatesService.findByOrderState("order_sent");
        return this.repository.save(new Order(userFound, restaurantFound, productList, orderStateFound));
    }

    public Order findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public Order restaurantAcceptsOrder(UUID id) {
        Order orderFound = this.findById(id);
        OrderState orderStateFound = this.orderStatesService.findByOrderState("accepted");
        orderFound.setOrderState(orderStateFound);
        return this.repository.save(orderFound);
    }

    public Order riderAcceptsOrder(UUID idOrder, UUID idRider) {
        Order orderFound = this.findById(idOrder);
        Rider riderFound = this.ridersService.findById(idRider);
        orderFound.setRider(riderFound);
        return this.repository.save(orderFound);
    }


}
