package bertcoscia.ZiplyEats_BE.services;

import bertcoscia.ZiplyEats_BE.entities.OrderProduct;
import bertcoscia.ZiplyEats_BE.entities.Topping;
import bertcoscia.ZiplyEats_BE.exceptions.NotFoundException;
import bertcoscia.ZiplyEats_BE.payloads.edit.EditOrderProductsDTO;
import bertcoscia.ZiplyEats_BE.repositories.OrderProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderProductsService {
    @Autowired
    OrderProductsRepository repository;

    @Autowired
    OrdersService ordersService;

    @Autowired
    ProductsService productsService;

    @Autowired
    ToppingsService toppingsService;


    public OrderProduct save(OrderProduct orderProduct) {
       return this.repository.save(orderProduct);
    }

    public List<OrderProduct> findAllById(List<UUID> orderProductsIds) {
        return this.repository.findAllById(orderProductsIds);
    }

    public OrderProduct findById(UUID id) {
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

    public List<OrderProduct> findByIdOrder(UUID idOrder) {
        return this.repository.findByOrderIdOrder(idOrder);
    }

    public OrderProduct findByIdAndEditToppings(UUID id, EditOrderProductsDTO body) {
        OrderProduct found = this.findById(id);
        List<Topping> toppingList = null;
        if (body.toppings() != null && !body.toppings().isEmpty()) {
            toppingList = this.toppingsService.findAllById(body.toppings());
        }
        found.setToppings(toppingList);
        return this.repository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        this.repository.delete(this.findById(id));
    }

}
