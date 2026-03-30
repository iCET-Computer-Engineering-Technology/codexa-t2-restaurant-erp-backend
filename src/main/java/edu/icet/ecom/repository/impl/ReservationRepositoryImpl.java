package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Reservation;
import edu.icet.ecom.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReservationRepositoryImpl implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getInt("id"));
        reservation.setCustomerId(rs.getInt("customer_id"));
        reservation.setCustomerName(rs.getString("customer_name"));
        reservation.setEmail(rs.getString("email"));
        reservation.setPhone(rs.getString("phone"));
        reservation.setTableId(rs.getInt("table_id"));
        reservation.setPartySize(rs.getInt("party_size"));

        java.sql.Date resDate = rs.getDate("reservation_date");
        if (resDate != null) {
            reservation.setReservationDate(resDate.toLocalDate());
        }

        java.sql.Time resTime = rs.getTime("reservation_time");
        if (resTime != null) {
            reservation.setReservationTime(resTime.toLocalTime());
        }

        reservation.setStatus(rs.getString("status"));
        reservation.setConfirmationCode(rs.getString("confirmation_code"));
        reservation.setReminder24hSent(rs.getInt("reminder_24h_sent"));
        reservation.setReminder2hSent(rs.getInt("reminder_2h_sent"));
        reservation.setNotes(rs.getString("notes"));

        java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            reservation.setCreatedAt(createdAt.toLocalDateTime());
        }

        java.sql.Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            reservation.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return reservation;
    };

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservations (customer_id, customer_name, email, phone, table_id, party_size, reservation_date, " +
                "reservation_time, status, confirmation_code, reminder_24h_sent, reminder_2h_sent, notes, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, reservation.getCustomerId());
            ps.setString(2, reservation.getCustomerName());
            ps.setString(3, reservation.getEmail());
            ps.setString(4, reservation.getPhone());
            ps.setInt(5, reservation.getTableId());
            ps.setInt(6, reservation.getPartySize());
            ps.setDate(7, java.sql.Date.valueOf(reservation.getReservationDate()));
            ps.setTime(8, java.sql.Time.valueOf(reservation.getReservationTime()));
            ps.setString(9, reservation.getStatus() != null ? reservation.getStatus() : "pending");
            ps.setString(10, reservation.getConfirmationCode());
            ps.setInt(11, 0);
            ps.setInt(12, 0);
            ps.setString(13, reservation.getNotes());
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKey().intValue();
        reservation.setId(id);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(Integer id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT * FROM reservations ORDER BY reservation_date DESC, reservation_time DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Reservation> findByCustomerId(Integer customerId) {
        String sql = "SELECT * FROM reservations WHERE customer_id = ? ORDER BY reservation_date DESC";
        return jdbcTemplate.query(sql, rowMapper, customerId);
    }

    @Override
    public List<Reservation> findByReservationDate(LocalDate date) {
        String sql = "SELECT * FROM reservations WHERE reservation_date = ? ORDER BY reservation_time ASC";
        return jdbcTemplate.query(sql, rowMapper, java.sql.Date.valueOf(date));
    }

    @Override
    public List<Reservation> findByReservationDateAndTime(LocalDate date, LocalTime time) {
        String sql = "SELECT * FROM reservations WHERE reservation_date = ? AND reservation_time = ?";
        return jdbcTemplate.query(sql, rowMapper,
                java.sql.Date.valueOf(date),
                java.sql.Time.valueOf(time));
    }

    @Override
    public List<Reservation> findConfirmedReservations(LocalDate date, LocalTime time) {
        String sql = "SELECT * FROM reservations WHERE reservation_date = ? AND reservation_time = ? " +
                "AND status IN ('confirmed', 'seated') ORDER BY table_id ASC";
        return jdbcTemplate.query(sql, rowMapper,
                java.sql.Date.valueOf(date),
                java.sql.Time.valueOf(time));
    }

    @Override
    public Reservation update(Reservation reservation) {
        String sql = "UPDATE reservations SET customer_id = ?, customer_name = ?, email = ?, phone = ?, table_id = ?, party_size = ?, " +
                "reservation_date = ?, reservation_time = ?, status = ?, confirmation_code = ?, " +
                "reminder_24h_sent = ?, reminder_2h_sent = ?, notes = ?, updated_at = NOW() WHERE id = ?";

        jdbcTemplate.update(sql,
                reservation.getCustomerId(),
                reservation.getCustomerName(),
                reservation.getEmail(),
                reservation.getPhone(),
                reservation.getTableId(),
                reservation.getPartySize(),
                java.sql.Date.valueOf(reservation.getReservationDate()),
                java.sql.Time.valueOf(reservation.getReservationTime()),
                reservation.getStatus(),
                reservation.getConfirmationCode(),
                reservation.getReminder24hSent(),
                reservation.getReminder2hSent(),
                reservation.getNotes(),
                reservation.getId()
        );
        return reservation;
    }

    @Override
    public boolean updateStatus(Integer id, String status) {
        String sql = "UPDATE reservations SET status = ?, updated_at = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql, status, id) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "UPDATE reservations SET status = 'cancelled', updated_at = NOW() WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public List<Reservation> findUpcomingReservations() {
        String sql = "SELECT * FROM reservations WHERE reservation_date >= CURDATE() " +
                "AND status IN ('pending', 'confirmed') ORDER BY reservation_date ASC, reservation_time ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean existsConflict(Integer tableId, LocalDate date, LocalTime time) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE table_id = ? AND reservation_date = ? " +
                "AND reservation_time = ? AND status IN ('pending', 'confirmed', 'seated')";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
                tableId, java.sql.Date.valueOf(date), java.sql.Time.valueOf(time));
        return count != null && count > 0;
    }

    @Override
    public String generateConfirmationCode() {
        return "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
