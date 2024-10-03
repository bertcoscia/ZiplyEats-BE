package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.OrderState;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.NewOrderStatesDTO;
import bertcoscia.FoodDelivery_BE.repositories.OrderStatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderStatesService {
    @Autowired
    OrderStatesRepository repository;

    public OrderState save(NewOrderStatesDTO body) {
        if (this.repository.existsByOrderStateIgnoreCase(body.orderState().toLowerCase())) throw new BadRequestException("Order state " + body.orderState() + " already existing");
        return this.repository.save(new OrderState(body.orderState()));
    }

    public OrderState findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public OrderState findByOrderState(String orderState) {
        return this.repository.findByOrderStateIgnoreCase(orderState).orElseThrow(()-> new NotFoundException("Could not find order state " + orderState));
    }

    public List<OrderState> findAll() {
        return this.repository.findAll();
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

    public void findByOrderStateAndDelete(String orderState) {
        this.repository.delete(this.findByOrderState(orderState));
    }

    public OrderState findByIdAndUpdate(UUID id, OrderState body) {
        OrderState found = this.findById(id);
        if (this.repository.existsByOrderStateIgnoreCase(body.getOrderState()) && !found.getIdOrderState().equals(body.getIdOrderState())) throw new BadRequestException("Order state " + body.getOrderState() + " already existing");
        found.setOrderState(body.getOrderState());
        return this.repository.save(found);
    }

}
