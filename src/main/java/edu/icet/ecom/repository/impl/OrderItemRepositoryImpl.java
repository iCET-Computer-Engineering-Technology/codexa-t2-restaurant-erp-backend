package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.OrderItem;
import edu.icet.ecom.repository.OrderItemRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id=?";

        return jdbcTemplate.query(sql,(rs,rowNum)->{

            OrderItem item = new OrderItem();

            item.setId(rs.getLong("id"));
            item.setOrderId(rs.getLong("order_id"));
            item.setMenuItemId(rs.getLong("menu_item_id"));
            item.setQuantity(rs.getInt("quantity"));
            item.setUnitPrice(rs.getDouble("unit_price"));
            item.setTotalPrice(rs.getDouble("total_price"));

            return item;

        },orderId);
    }
}
