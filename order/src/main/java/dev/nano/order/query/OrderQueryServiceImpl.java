package dev.nano.order.query;

import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderViewRepository orderViewRepository;
    private final OrderViewMapper orderViewMapper;

    @Override
    public OrderViewDTO getOrderView(Long orderId) {
        return orderViewRepository.findById(orderId)
                .map(orderViewMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Order view not found for orderId: %d", orderId)
                ));
    }

    @Override
    public List<OrderViewDTO> getAllOrderViews() {
        List<OrderViewEntity> views = orderViewRepository.findAll();
        if (views.isEmpty()) {
            throw new ResourceNotFoundException("No order views found");
        }
        return orderViewMapper.toListDTO(views);
    }

    @Override
    public List<OrderViewDTO> getOrderViewsByCustomer(Long customerId) {
        List<OrderViewEntity> views = orderViewRepository.findByCustomerId(customerId);
        if (views.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("No order views found for customerId: %d", customerId)
            );
        }
        return orderViewMapper.toListDTO(views);
    }
}
