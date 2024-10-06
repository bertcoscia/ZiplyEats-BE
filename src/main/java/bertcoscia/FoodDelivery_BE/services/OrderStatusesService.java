package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.OrderStatus;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.NewOrderStatusesDTO;
import bertcoscia.FoodDelivery_BE.repositories.OrderStatusesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderStatusesService {
    @Autowired
    OrderStatusesRepository repository;

    public OrderStatus save(NewOrderStatusesDTO body) {
        if (this.repository.existsByOrderStatusIgnoreCase(body.orderStatus().toLowerCase())) throw new BadRequestException("Order status " + body.orderStatus() + " already existing");
        return this.repository.save(new OrderStatus(body.orderStatus()));
    }

    public OrderStatus findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public OrderStatus findByOrderStatus(String orderStatus) {
        return this.repository.findByOrderStatusIgnoreCase(orderStatus).orElseThrow(()-> new NotFoundException("Could not find order status " + orderStatus));
    }

    public List<OrderStatus> findAll() {
        return this.repository.findAll();
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public void findByOrderStatusAndDelete(String orderStatus) {
        this.repository.delete(this.findByOrderStatus(orderStatus));
    }

    public OrderStatus findByIdAndUpdate(UUID id, OrderStatus body) {
        OrderStatus found = this.findById(id);
        if (this.repository.existsByOrderStatusIgnoreCase(body.getOrderStatus()) && !found.getIdOrderStatus().equals(UUID.fromString(body.getOrderStatus()))) throw new BadRequestException("Order status " + body.getOrderStatus() + " already existing");
        found.setOrderStatus(body.getOrderStatus());
        return this.repository.save(found);
    }

}
