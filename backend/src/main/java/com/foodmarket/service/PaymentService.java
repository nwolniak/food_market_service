package com.foodmarket.service;

import com.foodmarket.exceptions.EntityNotFoundException;
import com.foodmarket.model.dto.PaymentDto;
import com.foodmarket.model.entity.OrderEntity;
import com.foodmarket.model.entity.PaymentEntity;
import com.foodmarket.model.mapping.PaymentMapper;
import com.foodmarket.repository.PaymentRepository;
import com.foodmarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final PaymentMapper mapper;

    public PaymentDto getPayment(long paymentId) {
        return paymentRepository.findById(paymentId)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Payment with %s id was not found in payments repository", paymentId)));
    }

    public List<PaymentDto> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public List<PaymentDto> getUserPayments(SecurityContext context) {
        Authentication authentication = context.getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .stream()
                .flatMap(userEntity -> paymentRepository.findByUserEntity_Id(userEntity.getId()))
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public PaymentDto addPayment(PaymentDto paymentDto) {
        PaymentEntity paymentEntity = mapper.toEntity(paymentDto);
        OrderEntity orderEntity = orderService.getOrderEntity(paymentDto.orderId());
        orderEntity.setPaid(true);
        orderEntity.setPaymentEntity(paymentEntity);
        paymentEntity.setOrderEntity(orderEntity);
        PaymentEntity saved = paymentRepository.save(paymentEntity);
        return mapper.toDto(saved);
    }

}
