package bertcoscia.FoodDelivery_BE.services;

import bertcoscia.FoodDelivery_BE.entities.OrderState;
import bertcoscia.FoodDelivery_BE.exceptions.BadRequestException;
import bertcoscia.FoodDelivery_BE.exceptions.NotFoundException;
import bertcoscia.FoodDelivery_BE.payloads.NewOrderStatesDTO;
import bertcoscia.FoodDelivery_BE.repositories.OrderStatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
