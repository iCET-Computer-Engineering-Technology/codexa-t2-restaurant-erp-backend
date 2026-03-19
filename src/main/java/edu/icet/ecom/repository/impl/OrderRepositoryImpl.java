package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Integer saveAndGetId(Order order) {
        String sql ="INSERT INTO orders (order_number, order_type, table_id, customer_id, server_id, status, subtotal, discount_amount, " +
                "tax_amount, service_charge, total_amount, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(Connection ->{
            PreparedStatement ps = Connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, order.getOrderNumber());
            ps.setString(2, order.getOrderType());
            if(order.getTableId() != null){
                ps.setInt(3, order.getTableId());
            }else{
                ps.setNull(3, Types.INTEGER);
            }
            if(order.getCustomerId() != null) {
                ps.setInt(4, order.getCustomerId());
            }else{
                ps.setNull(4, Types.INTEGER);
            }
            if(order.getServerId() != null){
                ps.setInt(5, order.getServerId());
            }else{
                ps.setNull(5, Types.INTEGER);
            }
            ps.setString(6, order.getStatus());
            ps.setBigDecimal(7, order.getSubTotal());
            ps.setBigDecimal(8, order.getDiscountAmount());
            ps.setBigDecimal(9, order.getTaxAmount());
            ps.setBigDecimal(10, order.getServiceCharge());
            ps.setBigDecimal(11, order.getTotalAmount());
            ps.setString(12, order.getNotes());
            return ps;
        }, keyHolder );
        return Optional.ofNullable(keyHolder.getKey())
                .map(Number::intValue).orElseThrow(()-> new DataRetrievalFailureException("Order insert failed - no generated key returned"));
    }

    //Get open orders
    @Override
    public List<Order> findOpenOrders() {
        return null;
    }

    //Update status
    @Override
    public boolean updateStatus(Integer orderId, String status) {
        return jdbcTemplate.update("UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?", status, orderId) > 0;
    }

    @Override
    public Order findById(Integer id) {
        return null;
    }

    @Override
    public List<Order> findAll() {
        return List.of();
    }

    @Override
    public List<Order> findByStatus() {
        return List.of();
    }

    //Get sequence for order number - uses order_sequence table to maintain daily counter
    @Override
    public int upsertAndGetSequence(LocalDate date) {
        String sql = "INSERT INTO order_sequence (sequence_date, last_sequence) VALUES (?, 1) " +
                "ON DUPLICATE KEY UPDATE last_sequence = last_sequence + 1";
        jdbcTemplate.update(sql, java.sql.Date.valueOf(date));

        // Retrieve the updated sequence number
        String selectSql = "SELECT last_sequence FROM order_sequence WHERE sequence_date = ?";
        Integer sequence = jdbcTemplate.queryForObject(selectSql, Integer.class, java.sql.Date.valueOf(date));
        return sequence != null ? sequence : 1;
    }

}
