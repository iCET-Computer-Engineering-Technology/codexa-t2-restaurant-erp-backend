package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Order;
import edu.icet.ecom.dto.OrderWithItemNameResponse;
import edu.icet.ecom.dto.OrderItemsWithNameResponse;
import edu.icet.ecom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Integer saveAndGetId(Order order) {
        String sql ="INSERT INTO orders (order_type_id, order_number, order_type, table_id, customer_id, server_id, status, subtotal, discount_amount, " +
                "tax_amount, service_charge, total_amount, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection ->{
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if(order.getOrderTypeId() != null){
                ps.setInt(1, order.getOrderTypeId());
            }else{
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, order.getOrderNumber());
            ps.setString(3, order.getOrderType());
            if(order.getTableId() != null){
                ps.setInt(4, order.getTableId());
            }else{
                ps.setNull(4, Types.INTEGER);
            }
            if(order.getCustomerId() != null) {
                ps.setInt(5, order.getCustomerId());
            }else{
                ps.setNull(5, Types.INTEGER);
            }
            if(order.getServerId() != null){
                ps.setInt(6, order.getServerId());
            }else{
                ps.setNull(6, Types.INTEGER);
            }
            ps.setString(7, order.getStatus());
            ps.setBigDecimal(8, order.getSubTotal());
            ps.setBigDecimal(9, order.getDiscountAmount());
            ps.setBigDecimal(10, order.getTaxAmount());
            ps.setBigDecimal(11, order.getServiceCharge());
            ps.setBigDecimal(12, order.getTotalAmount());
            ps.setString(13, order.getNotes());
            return ps;
        }, keyHolder );
        return Optional.ofNullable(keyHolder.getKey())
                .map(Number::intValue).orElseThrow(()-> new DataRetrievalFailureException("Order insert failed - no generated key returned"));
    }

    @Override
    public boolean updateStatus(Integer orderId, String status) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("Invalid orderId: " + orderId);
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        int rowsUpdated = jdbcTemplate.update(
                "UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?",
                status, orderId
        );
        return rowsUpdated > 0;
    }

    @Override
    public boolean updateType(Integer orderId, String type) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("Invalid orderId: " + orderId);
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        int rowsUpdated = jdbcTemplate.update(
                "UPDATE orders SET order_type = ?, table_id = CASE WHEN ? = 'takeout' THEN NULL ELSE table_id END, updated_at = NOW() WHERE id = ?",
                type, type, orderId
        );
        return rowsUpdated > 0;
    }

    @Override
    public Order findById(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, order_type_id, order_number, order_type, table_id, customer_id, server_id, " +
                            "status, subtotal, discount_amount, tax_amount, service_charge, " +
                            "total_amount, notes, created_at, updated_at " +
                            "FROM orders WHERE id = ?",
                    (rs, row) -> mapRow(rs), id
            );
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;  // Order not found
        }
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query(
                "SELECT id, order_type_id, order_number, order_type, table_id, customer_id, server_id, " +
                        "status, subtotal, discount_amount, tax_amount, service_charge, " +
                        "total_amount, notes, created_at, updated_at " +
                        "FROM orders ORDER BY created_at DESC",
                (rs, row) -> mapRow(rs)
        );
    }

    @Override
    public List<Order> findByStatus(String status) {
        return jdbcTemplate.query(
                "SELECT id, order_type_id, order_number, order_type, table_id, customer_id, server_id, " +
                        "status, subtotal, discount_amount, tax_amount, service_charge, " +
                        "total_amount, notes, created_at, updated_at " +
                        "FROM orders WHERE status = ? ORDER BY created_at DESC",
                (rs, row) -> mapRow(rs), status
        );
    }

    //Get sequence for order number - uses order_sequence table to maintain daily counter
    @Override
    public int upsertAndGetSequence(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        String sql = "INSERT INTO order_sequence (sequence_date, last_sequence) VALUES (?, 1) " +
                "ON DUPLICATE KEY UPDATE last_sequence = last_sequence + 1";
        jdbcTemplate.update(sql, sqlDate);

        String selectSql = "SELECT last_sequence FROM order_sequence WHERE sequence_date = ?";
        Integer sequence = jdbcTemplate.queryForObject(selectSql, Integer.class, sqlDate);
        return sequence != null ? sequence : 1;
    }


    @Override
    public List<OrderWithItemNameResponse> findAllOrdersWithItemNames() {
        String sql = "SELECT " +
                "o.id, o.order_type_id, o.order_number, o.order_type, o.table_id, " +
                "o.customer_id, o.server_id, o.status, o.subtotal, o.discount_amount, " +
                "o.tax_amount, o.service_charge, o.total_amount, o.notes, " +
                "o.created_at, o.updated_at, " +
                "oi.id as item_id, oi.order_id, oi.menu_item_id, mi.name as item_name, " +
                "oi.portion_id, oi.quantity, oi.price, oi.status as item_status, " +
                "oi.notes as item_notes, oi.created_at as item_created_at " +
                "FROM orders o " +
                "LEFT JOIN order_items oi ON o.id = oi.order_id " +
                "LEFT JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                "ORDER BY o.created_at DESC, oi.id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        return mapOrdersWithItems(rows);
    }

    @Override
    public OrderWithItemNameResponse findOrderWithItemNamesById(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        String sql = "SELECT " +
                "o.id, o.order_type_id, o.order_number, o.order_type, o.table_id, " +
                "o.customer_id, o.server_id, o.status, o.subtotal, o.discount_amount, " +
                "o.tax_amount, o.service_charge, o.total_amount, o.notes, " +
                "o.created_at, o.updated_at, " +
                "oi.id as item_id, oi.order_id, oi.menu_item_id, mi.name as item_name, " +
                "oi.portion_id, oi.quantity, oi.price, oi.status as item_status, " +
                "oi.notes as item_notes, oi.created_at as item_created_at " +
                "FROM orders o " +
                "LEFT JOIN order_items oi ON o.id = oi.order_id " +
                "LEFT JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                "WHERE o.id = ? " +
                "ORDER BY oi.id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, id);
        List<OrderWithItemNameResponse> results = mapOrdersWithItems(rows);
        return results.isEmpty() ? null : results.get(0);
    }

    private List<OrderWithItemNameResponse> mapOrdersWithItems(List<Map<String, Object>> rows) {
        if (rows.isEmpty()) {
            return List.of();
        }

        Map<Integer, OrderWithItemNameResponse> ordersMap = new java.util.LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            Integer orderId = (Integer) row.get("id");

            if (!ordersMap.containsKey(orderId)) {
                OrderWithItemNameResponse orderResponse = new OrderWithItemNameResponse();
                orderResponse.setId(orderId);
                orderResponse.setOrderTypeId((Integer) row.get("order_type_id"));
                orderResponse.setOrderNumber((String) row.get("order_number"));
                orderResponse.setOrderType((String) row.get("order_type"));
                orderResponse.setTableId((Integer) row.get("table_id"));
                orderResponse.setCustomerId((Integer) row.get("customer_id"));
                orderResponse.setServerId((Integer) row.get("server_id"));
                orderResponse.setStatus((String) row.get("status"));
                orderResponse.setSubTotal((java.math.BigDecimal) row.get("subtotal"));
                orderResponse.setDiscountAmount((java.math.BigDecimal) row.get("discount_amount"));
                orderResponse.setTaxAmount((java.math.BigDecimal) row.get("tax_amount"));
                orderResponse.setServiceCharge((java.math.BigDecimal) row.get("service_charge"));
                orderResponse.setTotalAmount((java.math.BigDecimal) row.get("total_amount"));
                orderResponse.setNotes((String) row.get("notes"));

                Object createdAtObj = row.get("created_at");
                if (createdAtObj != null) {
                    if (createdAtObj instanceof java.time.LocalDateTime) {
                        orderResponse.setCreatedAt((java.time.LocalDateTime) createdAtObj);
                    } else if (createdAtObj instanceof Timestamp) {
                        orderResponse.setCreatedAt(((Timestamp) createdAtObj).toLocalDateTime());
                    }
                }

                Object updatedAtObj = row.get("updated_at");
                if (updatedAtObj != null) {
                    if (updatedAtObj instanceof java.time.LocalDateTime) {
                        orderResponse.setUpdatedAt((java.time.LocalDateTime) updatedAtObj);
                    } else if (updatedAtObj instanceof Timestamp) {
                        orderResponse.setUpdatedAt(((Timestamp) updatedAtObj).toLocalDateTime());
                    }
                }

                orderResponse.setItems(new java.util.ArrayList<>());
                ordersMap.put(orderId, orderResponse);
            }

            // Add item if it exists (not null)
            Integer itemId = (Integer) row.get("item_id");
            if (itemId != null) {
                OrderItemsWithNameResponse itemResponse = new OrderItemsWithNameResponse();
                itemResponse.setId(itemId);
                itemResponse.setOrderId((Integer) row.get("order_id"));
                itemResponse.setMenuItemId((Integer) row.get("menu_item_id"));
                itemResponse.setItemName((String) row.get("item_name"));
                itemResponse.setPortionId((Integer) row.get("portion_id"));
                itemResponse.setQuantity((Integer) row.get("quantity"));

                java.math.BigDecimal price = (java.math.BigDecimal) row.get("price");
                itemResponse.setPrice(price);

                Integer quantity = (Integer) row.get("quantity");
                if (price != null && quantity != null) {
                    itemResponse.setLineTotal(price.multiply(java.math.BigDecimal.valueOf(quantity)));
                }

                itemResponse.setStatus((String) row.get("item_status"));
                itemResponse.setNotes((String) row.get("item_notes"));

                Object itemCreatedAtObj = row.get("item_created_at");
                if (itemCreatedAtObj != null) {
                    if (itemCreatedAtObj instanceof java.time.LocalDateTime) {
                        itemResponse.setCreatedAt((java.time.LocalDateTime) itemCreatedAtObj);
                    } else if (itemCreatedAtObj instanceof Timestamp) {
                        itemResponse.setCreatedAt(((Timestamp) itemCreatedAtObj).toLocalDateTime());
                    }
                }

                ordersMap.get(orderId).getItems().add(itemResponse);
            }
        }

        return new java.util.ArrayList<>(ordersMap.values());
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));

        int orderTypeId = rs.getInt("order_type_id");
        order.setOrderTypeId(rs.wasNull() ? null : orderTypeId);

        order.setOrderNumber(rs.getString("order_number"));
        order.setOrderType(rs.getString("order_type"));

        int tableId = rs.getInt("table_id");
        order.setTableId(rs.wasNull() ? null : tableId);

        int customerId = rs.getInt("customer_id");
        order.setCustomerId(rs.wasNull() ? null : customerId);

        int serverId = rs.getInt("server_id");
        order.setServerId(rs.wasNull() ? null : serverId);

        order.setStatus(rs.getString("status"));
        order.setSubTotal(rs.getBigDecimal("subtotal"));
        order.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        order.setTaxAmount(rs.getBigDecimal("tax_amount"));
        order.setServiceCharge(rs.getBigDecimal("service_charge"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setNotes(rs.getString("notes"));

        //Handle null timestamps to prevent NullPointerException
        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            order.setCreatedAt(createdTs.toLocalDateTime());
        }
        Timestamp updatedTs = rs.getTimestamp("updated_at");
        if (updatedTs != null) {
            order.setUpdatedAt(updatedTs.toLocalDateTime());
        }
        return order;
    }
}
