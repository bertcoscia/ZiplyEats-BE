package bertcoscia.ZiplyEats_BE.services;

import bertcoscia.ZiplyEats_BE.entities.*;
import bertcoscia.ZiplyEats_BE.exceptions.BadRequestException;
import bertcoscia.ZiplyEats_BE.exceptions.NotFoundException;
import bertcoscia.ZiplyEats_BE.exceptions.UnauthorizedException;
import bertcoscia.ZiplyEats_BE.payloads.edit.EditOrdersDTO;
import bertcoscia.ZiplyEats_BE.payloads.newEntities.NewOrderProductsDTO;
import bertcoscia.ZiplyEats_BE.payloads.newEntities.NewOrdersDTO;
import bertcoscia.ZiplyEats_BE.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Order newOrder = new Order(userFound, restaurantFound, orderStatusFound, body.deliveryAddress(), body.requestedDeliveryDateTime());
        // Aggiunta dei prodotti all'ordine
        for (NewOrderProductsDTO orderProductDTO : body.orderProductList()) {
            Product productFound = this.productsService.findById(orderProductDTO.idProduct());
            if (!productFound.getRestaurant().getIdUser().equals(restaurantFound.getIdUser())) throw new BadRequestException("The product " + productFound.getName() + " is not available at the restaurant " + restaurantFound.getName());
            List<Topping> toppingList = new ArrayList<>();
            if (orderProductDTO.toppings() != null && !orderProductDTO.toppings().isEmpty()) {
                toppingList = this.toppingsService.findAllById(orderProductDTO.toppings());
            }
            newOrder.addOrderProduct(productFound, toppingList);
        }
        newOrder.setTotalPrice(newOrder.calculateTotalPrice());
        return this.repository.save(newOrder);
    }

    public Order findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public Order restaurantAcceptsOrder(UUID idOrder, UUID idRestaurant) {
        Order orderFound = this.findById(idOrder);
        if (!orderFound.getRestaurant().getIdUser().equals(idRestaurant)) throw new UnauthorizedException("You are not authorised to perform the requested action");
        if (!orderFound.getOrderStatus().getOrderStatus().equals("CREATED")) throw new BadRequestException("It is not possible to accept this order");
        OrderStatus orderStatusFound = this.orderStatusesService.findByOrderStatus("RESTAURANT_ACCEPTED");
        orderFound.setOrderStatus(orderStatusFound);
        return this.repository.save(orderFound);
    }

    public Order restaurantRefusesOrder(UUID idOrder, UUID idRestaurant) {
        Order orderFound = this.findById(idOrder);
        if (!orderFound.getRestaurant().getIdUser().equals(idRestaurant)) throw new UnauthorizedException("You are not authorised to perform the requested action");
        if (!orderFound.getOrderStatus().getOrderStatus().equals("CREATED") && !orderFound.getOrderStatus().getOrderStatus().equals("RESTAURANT_ACCEPTED")) throw new BadRequestException("It is not possible to cancel the order");
        OrderStatus orderStatusFound = this.orderStatusesService.findByOrderStatus("CANCELLED");
        orderFound.setOrderStatus(orderStatusFound);
        this.repository.save(orderFound);
        if (orderFound.getRider() != null) {
            Rider riderFound = this.ridersService.findById(orderFound.getRider().getIdUser());
            this.ridersService.setRiderAvailable(riderFound);
        }
        return orderFound;
    }

    public Order assignRiderToOrder(UUID idOrder, UUID idRider) {
        Order orderFound = this.findById(idOrder);
        if (!orderFound.getOrderStatus().getOrderStatus().equals("RESTAURANT_ACCEPTED") && !orderFound.getOrderStatus().getOrderStatus().equals("CANCELLED")) throw new BadRequestException("You cannot accept this order. The restaurant has not accepted it yet");
        if (orderFound.getOrderStatus().getOrderStatus().equals("CANCELLED")) throw new BadRequestException("The selected order has been cancelled");
        if (orderFound.getRider() != null) throw new BadRequestException("The order n. " + orderFound.getIdOrder() + " already has a rider");
        Rider riderFound = this.ridersService.findById(idRider);
        if (riderFound.isBusyWithOrder()) throw new BadRequestException("Rider " + riderFound.getUsername() + " is currently busy with an order");
        orderFound.setRider(riderFound);
        this.repository.save(orderFound);
        this.ridersService.setRiderAsBusy(riderFound);
        return orderFound;
    }

    public Order unassignRiderToOrder(UUID idOrder, UUID idUser) {
        Order orderFound = this.findById(idOrder);
        if (!orderFound.getRider().getIdUser().equals(idUser)) throw new UnauthorizedException("You are not authorised to perform the requested action");
        if (orderFound.getOrderStatus().getOrderStatus().equals("DELIVERED") || orderFound.getOrderStatus().getOrderStatus().equals("IN_TRANSIT")) throw new BadRequestException("It is not possible to cancel this order. Please, contact Support Centre");
        Rider riderFound = this.ridersService.findById(orderFound.getRider().getIdUser());
        orderFound.setRider(null);
        this.repository.save(orderFound);
        this.ridersService.setRiderAvailable(riderFound);
        return orderFound;
    }

    public Order riderPicksUpOrder(UUID idOrder) {
    Order orderFound = this.findById(idOrder);
    if (orderFound.getRider() == null) throw new BadRequestException("The order does not have an assigned rider yet.");
    if (!orderFound.getOrderStatus().getOrderStatus().equals("RESTAURANT_ACCEPTED")) throw new BadRequestException("There was an error. Please, contact Support Centre");
    OrderStatus orderStatusFound = this.orderStatusesService.findByOrderStatus("IN_TRANSIT");
    orderFound.setOrderStatus(orderStatusFound);
    return this.repository.save(orderFound);
    }

    public Order finaliseOrder(UUID idOrder) {
        Order orderFound = this.findById(idOrder);
        if (orderFound.getActualDeliveryDateTime() != null) throw new BadRequestException("There was an error. Please, contact Support Centre");
        OrderStatus orderStatusFound = this.orderStatusesService.findByOrderStatus("DELIVERED");
        orderFound.setOrderStatus(orderStatusFound);
        orderFound.setActualDeliveryDateTime(LocalDateTime.now());
        this.repository.save(orderFound);
        Rider riderFound = this.ridersService.findById(orderFound.getRider().getIdUser());
        this.ridersService.setRiderAvailable(riderFound);
        return orderFound;
    }

    public Order findByIdAndUpdate(UUID idOrder, EditOrdersDTO body) {
        Order found = this.findById(idOrder);
        found.setDeliveryAddress(body.deliveryAddress());
        return this.repository.save(found);
    }

    public Page<Order> findAllByUserId(UUID idUser, int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 20) page = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAllByUserIdUser(idUser, pageable);
    }

    public Page<Order> findAllByRestaurantId(UUID idUser, int page, int size, String sortBy, Sort.Direction direction, Map<String, String> params) {
        if (page > 20) page = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return this.repository.findAllByRestaurantIdUser(idUser, pageable);
    }

    public Order findMyOrderById(UUID idOrder, UUID idUser) {
        Order found = this.findById(idOrder);
        if (!found.getUser().getIdUser().equals(idUser)) throw new UnauthorizedException("You are not authorised to see this order");
        return found;
    }

}
