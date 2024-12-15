package lada.alex.onlinebookstore.repository.order;

import java.util.List;
import java.util.Optional;
import lada.alex.onlinebookstore.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"user", "orderItems"})
    List<Order> findAllByUserId(Pageable pageable, Long userId);

    @EntityGraph(attributePaths = {"user", "orderItems"})
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
