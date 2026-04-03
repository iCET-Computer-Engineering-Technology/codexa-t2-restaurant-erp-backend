-- =============================================================
-- Smart Restaurant ERP — Sample Data (5+ rows per entity)
-- Insert order respects foreign key dependencies
-- =============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- ---------------------------------------------------------------
-- 1. users
-- ---------------------------------------------------------------
INSERT INTO users (id, username, email, password, role, enabled)
VALUES (1, 'admin', 'admin@restaurant.com', '$2a$12$UELqa2vZb5llSBBvlKa1fu9VMhQyN5f7eHWy2bCLcXK4AljvFKoRK', 'ROLE_ADMIN', 1),
       (2, 'waiter', 'waiter@restaurant.com', '$2a$12$l6xZ.LNYXbSE5ciYfyqHQOGMm09ADVwNHra4OaGLBczRcUbqMK7oC', 'ROLE_WAITER', 1),
       (3, 'chef', 'chef@restaurant.com', '$2a$12$HNRF7LE46jWeExo/2i.Q3OT0oSzpRvpS5Y52GY6f7Od8acFIdF/aG', 'ROLE_CHEF', 1),
       (4, 'cashier', 'cashier@restaurant.com', '$2a$12$3Iitmyjq6w53mwKBr0y10e.VtPlQsPQ3nkgQaxdLVgSVdekCgNlV.', 'ROLE_CASHIER', 1),
       (5, 'manager', 'manager@restaurant.com', '$2a$12$1WxWyFCfGsvQZb9dR.WJyO1z4s6wTWZKWUaxB1jGx/sDk3.yL/Gbe', 'ROLE_MANAGER', 1);

# admin → Admin@123
# waiter → Waiter@123
# chef → Chef@123
# cashier → Cashier@123
# manager → Manager@123

-- ---------------------------------------------------------------
-- 2. customers
-- ---------------------------------------------------------------
INSERT INTO customers (id, first_name, last_name, email, phone, preferred_language, dietary_notes, communication_email,
                       communication_sms, birthday, loyalty_points)
VALUES (1, 'Alice', 'Smith', 'alice@gmail.com', '+94771234567', 'en', NULL, 1, 1, '1990-05-14', 150),
       (2, 'Bob', 'Johnson', 'bob@gmail.com', '+94772345678', 'en', 'Vegetarian', 1, 0, '1985-11-22', 320),
       (3, 'Chamari', 'Perera', 'chamari@gmail.com', '+94773456789', 'si', 'No nuts', 1, 1, '1995-03-08', 80),
       (4, 'David', 'Brown', 'david@gmail.com', '+94774567890', 'en', NULL, 0, 1, '1978-07-30', 500),
       (5, 'Emma', 'Wilson', 'emma@gmail.com', '+94775678901', 'en', 'Gluten-free', 1, 1, '2000-12-01', 60),
       (6, 'Fathima', 'Rizvi', 'fathima@gmail.com', '+94776789012', 'ta', 'Halal only', 1, 1, '1992-09-19', 210);

-- ---------------------------------------------------------------
-- 3. automated_messages
-- ---------------------------------------------------------------
INSERT INTO automated_messages (trigger_type, channel, template_body, offer_type, offer_value, send_days_before,
                                is_active)
VALUES ('birthday', 'email', 'Happy Birthday {{first_name}}! Enjoy a special treat on us.', 'discount', 10.00, 1, 1),
       ('birthday', 'sms', 'Hi {{first_name}}, it\'s your birthday! Show this for a free dessert.', 'free_item', 0.00,
        1, 1),
       ('anniversary', 'email', 'One year with us! Thank you {{first_name}} — here\'s a gift.', 'discount', 15.00, 3,
        1),
       ('lapsed', 'email,sms', 'We miss you, {{first_name}}! Come back for 20% off your next visit.', 'discount', 20.00,
        0, 1),
       ('tier_change', 'email', 'Congrats {{first_name}}! You\'ve reached {{tier_name}} status!', 'none', 0.00, 0, 1),
       ('lapsed', 'sms', 'Hey {{first_name}}, it\'s been a while. Visit us this week!', 'discount', 10.00, 0, 0);

-- ---------------------------------------------------------------
-- 3a. email_scheduler_config (DEFAULT SCHEDULER TIME FOR BIRTHDAYS/ANNIVERSARIES)
-- ---------------------------------------------------------------
INSERT INTO email_scheduler_config (id, send_time, created_at)
VALUES (1, '08:00:00', NOW());

-- ---------------------------------------------------------------
-- 4. suppliers
-- ---------------------------------------------------------------
INSERT INTO suppliers (id, name, contact_name, email, phone, address)
VALUES (1, 'FreshFarm Produce', 'Nimal Fernando', 'nimal@freshfarm.lk', '+94112233445', '12 Kandy Rd, Colombo'),
       (2, 'SeaBreeze Seafood', 'Amara Silva', 'amara@seabreeze.lk', '+94112233446', '5 Galle Face, Colombo'),
       (3, 'Island Dairy Co.', 'Ravi Jayasinghe', 'ravi@islanddairy.lk', '+94112233447', '34 Negombo Rd, Wennappuwa'),
       (4, 'SpiceMart Lanka', 'Shani Wickrama', 'shani@spicemart.lk', '+94112233448', '7 Pettah Market, Colombo'),
       (5, 'Golden Grain Mills', 'Priya Mendis', 'priya@goldengrain.lk', '+94112233449',
        '22 Kurunegala Rd, Kurunegala'),
       (6, 'CoolBev Distributors', 'Kasun Perera', 'kasun@coolbev.lk', '+94112233450', '9 Industrial Zone, Biyagama');

-- ---------------------------------------------------------------
-- 5. ingredients
-- ---------------------------------------------------------------
INSERT INTO ingredients (id, name, unit, current_stock, low_stock_threshold, cost_per_unit, barcode)
VALUES (1, 'Chicken Breast', 'kg', 25.500, 5.000, 850.0000, '8901234567890'),
       (2, 'Basmati Rice', 'kg', 50.000, 10.000, 180.0000, '8901234567891'),
       (3, 'Fresh Milk', 'L', 20.000, 5.000, 120.0000, '8901234567892'),
       (4, 'Red Chilli Powder', 'kg', 3.000, 0.500, 350.0000, '8901234567893'),
       (5, 'Olive Oil', 'L', 8.000, 2.000, 950.0000, '8901234567894'),
       (6, 'Shrimp (L)', 'kg', 12.000, 3.000, 1400.0000, '8901234567895'),
       (7, 'All-Purpose Flour', 'kg', 30.000, 8.000, 90.0000, '8901234567896'),
       (8, 'Butter', 'kg', 6.000, 1.500, 620.0000, '8901234567897'),
       (9, 'Garlic', 'kg', 4.000, 1.000, 280.0000, '8901234567898'),
       (10, 'Tomatoes', 'kg', 10.000, 3.000, 160.0000, '8901234567899');

-- ---------------------------------------------------------------
-- 6. menu_categories
-- ---------------------------------------------------------------
INSERT INTO menu_categories (id, name, is_active)
VALUES (1, 'Starters', 1),
       (2, 'Main Course', 1),
       (3, 'Seafood', 1),
       (4, 'Desserts', 1),
       (5, 'Beverages', 1),
       (6, 'Rice & Bread', 1);

-- ---------------------------------------------------------------
-- 7. menu_items
-- ---------------------------------------------------------------
INSERT INTO menu_items (id, category_id, name, description, is_available, image_url)
VALUES (1, 1, 'Garlic Bread', 'Toasted bread with garlic butter', 1, '/images/garlic_bread.jpg'),
       (2, 1, 'Spring Rolls', 'Crispy veg spring rolls with sweet chilli dip', 1, '/images/spring_rolls.jpg'),
       (3, 2, 'Chicken Curry', 'Traditional Sri Lankan chicken curry', 1, '/images/chicken_curry.jpg'),
       (4, 2, 'Grilled Chicken', 'Herb-marinated grilled chicken breast', 1, '/images/grilled_chicken.jpg'),
       (5, 3, 'Prawn Stir Fry', 'Jumbo prawns with garlic and peppers', 1, '/images/prawn_stirfry.jpg'),
       (6, 3, 'Fish & Chips', 'Beer-battered fish with seasoned fries', 1, '/images/fish_chips.jpg'),
       (7, 4, 'Chocolate Lava Cake', 'Warm cake with molten chocolate centre', 1, '/images/lava_cake.jpg'),
       (8, 4, 'Ice Cream (3 scoops)', 'Choice of vanilla, chocolate or strawberry', 1, '/images/ice_cream.jpg'),
       (9, 5, 'Fresh Lime Juice', 'Squeezed lime with soda or still water', 1, '/images/lime_juice.jpg'),
       (10, 6, 'Steamed Rice', 'Fluffy long-grain basmati rice', 1, '/images/steamed_rice.jpg'),
       (11, 6, 'Garlic Naan', 'Soft naan bread brushed with garlic butter', 1, '/images/garlic_naan.jpg');

-- ---------------------------------------------------------------
-- 8. portions
-- ---------------------------------------------------------------
INSERT INTO portions (id, portion_name)
VALUES (1, 'Small'),
       (2, 'Regular'),
       (3, 'Large'),
       (4, 'Half'),
       (5, 'Full'),
       (6, 'Single');

-- ---------------------------------------------------------------
-- 9. menu_item_price
-- ---------------------------------------------------------------
INSERT INTO menu_item_price (item_id, portion_id, price, is_active)
VALUES (1, 6, 350.00, 1), -- Garlic Bread / Single
       (2, 6, 480.00, 1), -- Spring Rolls / Single
       (3, 4, 650.00, 1), -- Chicken Curry / Half
       (3, 5, 950.00, 1), -- Chicken Curry / Full
       (4, 6, 1100.00, 1), -- Grilled Chicken / Single
       (5, 2, 1250.00, 1), -- Prawn Stir Fry / Regular
       (5, 3, 1800.00, 1), -- Prawn Stir Fry / Large
       (6, 6, 950.00, 1), -- Fish & Chips / Single
       (7, 6, 450.00, 1), -- Chocolate Lava Cake / Single
       (8, 6, 380.00, 1), -- Ice Cream / Single
       (9, 1, 180.00, 1), -- Fresh Lime Juice / Small
       (9, 2, 250.00, 1), -- Fresh Lime Juice / Regular
       (10, 2, 150.00, 1), -- Steamed Rice / Regular
       (10, 3, 220.00, 1), -- Steamed Rice / Large
       (11, 6, 200.00, 1);
-- Garlic Naan / Single

-- ---------------------------------------------------------------
-- 10. loyalty_tiers
-- ---------------------------------------------------------------
INSERT INTO loyalty_tiers (id, tier_name, points_threshold, discount_pct, free_item_id, priority_seating, sort_order)
VALUES (1, 'Bronze', 0, 0.00, NULL, 0, 1),
       (2, 'Silver', 200, 5.00, NULL, 0, 2),
       (3, 'Gold', 500, 10.00, 8, 1, 3),
       (4, 'Platinum', 1000, 15.00, 7, 1, 4),
       (5, 'VIP', 2000, 20.00, 5, 1, 5);

-- ---------------------------------------------------------------
-- 11. loyalty_accounts
-- ---------------------------------------------------------------
INSERT INTO loyalty_accounts (customer_id, points_balance, tier_id, lifetime_points)
VALUES (1, 150, 2, 350),
       (2, 320, 3, 820),
       (3, 80, 1, 80),
       (4, 500, 3, 1200),
       (5, 60, 1, 60),
       (6, 210, 2, 410);

-- ---------------------------------------------------------------
-- 12. tables (restaurant floor)
-- ---------------------------------------------------------------
INSERT INTO tables (id, section_id, table_number, capacity, pos_x, pos_y, status)
VALUES (1, 1, 'T01', 2, 50, 50, 'available'),
       (2, 1, 'T02', 4, 150, 50, 'occupied'),
       (3, 1, 'T03', 4, 250, 50, 'reserved'),
       (4, 2, 'T04', 6, 50, 200, 'available'),
       (5, 2, 'T05', 6, 150, 200, 'cleaning'),
       (6, 2, 'T06', 8, 250, 200, 'available'),
       (7, 3, 'T07', 10, 50, 350, 'available');

-- ---------------------------------------------------------------
-- 13. waiter
-- ---------------------------------------------------------------
INSERT INTO waiter (id, waiter_name, status)
VALUES (1, 'John Silva', 'active'),
       (2, 'Priya Fernando', 'active'),
       (3, 'Kamal Perera', 'active'),
       (4, 'Nadeeka Raj', 'on_break'),
       (5, 'Saman Wickrama', 'active'),
       (6, 'Dilani Mendis', 'inactive');

-- ---------------------------------------------------------------
-- 14. orders
-- ---------------------------------------------------------------
INSERT INTO orders (id, order_number, order_type, table_id, customer_id, server_id, status, subtotal, discount_amount,
                    tax_amount, service_charge, total_amount, notes)
VALUES (1, 'ORD-20240601-001', 'dine_in', 2, 1, 2, 'paid', 1450.00, 0.00, 152.25, 72.50, 1674.75, NULL),
       (2, 'ORD-20240601-002', 'dine_in', 3, 2, 2, 'paid', 2200.00, 110.00, 217.35, 104.50, 2511.85, 'No spicy food'),
       (3, 'ORD-20240602-001', 'takeout', NULL, 3, 5, 'paid', 630.00, 0.00, 66.15, 31.50, 727.65, NULL),
       (4, 'ORD-20240602-002', 'dine_in', 4, 4, 6, 'open', 1900.00, 0.00, 199.50, 95.00, 2194.50, 'VIP customer'),
       (5, 'ORD-20240603-001', 'takeout', NULL, 5, 5, 'paid', 950.00, 47.50, 95.41, 47.50, 1045.41, 'Pickup order'),
       (6, 'ORD-20240603-002', 'dine_in', 6, 6, 2, 'sent_to_kitchen', 1600.00, 0.00, 168.00, 80.00, 1848.00,
        'Halal only');

-- ---------------------------------------------------------------
-- 15. order_items
-- ---------------------------------------------------------------
INSERT INTO order_items (order_id, menu_item_id, portion_id, quantity, price, status, notes)
VALUES (1, 1, 6, 1, 350.00, 'served', NULL),
       (1, 3, 5, 1, 950.00, 'served', NULL),
       (1, 10, 2, 1, 150.00, 'served', NULL),
       (2, 5, 3, 1, 1800.00, 'served', NULL),
       (2, 2, 6, 2, 480.00, 'served', NULL),
       (3, 9, 2, 1, 250.00, 'served', NULL),
       (3, 2, 6, 1, 480.00, 'served', NULL),
       (4, 4, 6, 1, 1100.00, 'pending', 'Extra herbs'),
       (4, 10, 3, 2, 220.00, 'pending', NULL),
       (5, 6, 6, 1, 950.00, 'served', NULL),
       (6, 5, 2, 1, 1250.00, 'fired', NULL),
       (6, 11, 6, 2, 200.00, 'fired', NULL);

-- ---------------------------------------------------------------
-- 16. customer_visits
-- ---------------------------------------------------------------
INSERT INTO customer_visits (customer_id, visit_date, order_id, spend_amount, notes)
VALUES (1, '2024-06-01 19:30:00', 1, 1674.75, 'Anniversary dinner'),
       (2, '2024-06-01 20:00:00', 2, 2511.85, NULL),
       (3, '2024-06-02 12:15:00', 3, 727.65, 'Takeout lunch'),
       (4, '2024-06-02 19:00:00', 4, 2194.50, 'Business dinner'),
       (5, '2024-06-03 18:45:00', 5, 1045.41, 'Delivery order'),
       (6, '2024-06-03 20:30:00', 6, 1848.00, NULL);

-- ---------------------------------------------------------------
-- 17. payments
-- ---------------------------------------------------------------
INSERT INTO payments (order_id, payment_method, amount, tip_amount, reference_number, processed_by)
VALUES (1, 'card', 1674.75, 100.00, 'TXN-CC-001', 5),
       (2, 'cash', 2511.85, 200.00, 'TXN-CS-001', 5),
       (3, 'card', 727.65, 50.00, 'TXN-CC-002', 5),
       (5, 'online', 1045.41, 0.00, 'TXN-ON-001', 5),
       (1, 'loyalty', 50.00, 0.00, 'TXN-LY-001', 5), -- split payment example
       (2, 'card', 300.00, 0.00, 'TXN-CC-003', 5);

-- ---------------------------------------------------------------
-- 18. loyalty_transactions
-- ---------------------------------------------------------------
INSERT INTO loyalty_transactions (customer_id, order_id, transaction_type, points, balance_after, notes)
VALUES (1, 1, 'earn', 167, 317, 'Earned on order ORD-20240601-001'),
       (2, 2, 'earn', 251, 571, 'Earned on order ORD-20240601-002'),
       (3, 3, 'earn', 73, 73, 'Earned on order ORD-20240602-001'),
       (4, 4, 'earn', 219, 719, 'Earned on order ORD-20240602-002'),
       (1, 1, 'redeem', -50, 267, 'Redeemed 50 pts on order ORD-20240601-001'),
       (5, 5, 'earn', 104, 164, 'Earned on order ORD-20240603-001'),
       (6, 6, 'earn', 185, 395, 'Earned on order ORD-20240603-002');

-- ---------------------------------------------------------------
-- 19. reservations
-- ---------------------------------------------------------------
INSERT INTO reservations (customer_id, table_id, party_size, reservation_date, reservation_time, status,
                          confirmation_code, reminder_24h_sent, reminder_2h_sent, notes)
VALUES (1, 3, 2, '2024-06-10', '19:00:00', 'confirmed', 'RES-001', 1, 0, 'Anniversary dinner, request candles'),
       (2, 4, 4, '2024-06-11', '20:00:00', 'confirmed', 'RES-002', 0, 0, NULL),
       (3, 1, 2, '2024-06-12', '12:30:00', 'pending', 'RES-003', 0, 0, 'Vegetarian menu preferred'),
       (4, 7, 8, '2024-06-13', '19:30:00', 'confirmed', 'RES-004', 1, 1, 'Corporate event'),
       (5, 2, 2, '2024-06-14', '18:00:00', 'cancelled', 'RES-005', 0, 0, 'Customer cancelled'),
       (6, 6, 6, '2024-06-15', '20:30:00', 'confirmed', 'RES-006', 0, 0, 'Halal menu requested');

-- ---------------------------------------------------------------
-- 20. waitlist
-- ---------------------------------------------------------------
INSERT INTO waitlist (customer_name, phone, party_size, added_at, estimated_wait_min, notified_at, seated_at, status)
VALUES ('Janith De Silva', '+94771000001', 2, '2024-06-03 18:30:00', 15, '2024-06-03 18:45:00', '2024-06-03 18:47:00',
        'seated'),
       ('Anika Gunawardena', '+94771000002', 4, '2024-06-03 19:00:00', 25, '2024-06-03 19:25:00', NULL, 'notified'),
       ('Ramesh Kumar', '+94771000003', 3, '2024-06-03 19:10:00', 30, NULL, NULL, 'waiting'),
       ('Dilani Jayasena', '+94771000004', 5, '2024-06-03 19:15:00', 35, NULL, NULL, 'waiting'),
       ('Thilini Bandara', '+94771000005', 2, '2024-06-03 18:20:00', 10, '2024-06-03 18:30:00', NULL, 'left'),
       ('Nuwan Rathnayake', '+94771000006', 6, '2024-06-03 19:45:00', 40, NULL, NULL, 'waiting');

-- ---------------------------------------------------------------
-- 21. kitchen_order
-- ---------------------------------------------------------------
INSERT INTO kitchen_order (id, order_id, status, get_time, end_time)
VALUES (1, 1, 'done', '2024-06-01 19:35:00', '2024-06-01 20:00:00'),
       (2, 2, 'done', '2024-06-01 20:05:00', '2024-06-01 20:30:00'),
       (3, 3, 'done', '2024-06-02 12:20:00', '2024-06-02 12:40:00'),
       (4, 4, 'in_progress', '2024-06-02 19:05:00', NULL),
       (5, 5, 'done', '2024-06-03 18:50:00', '2024-06-03 19:10:00'),
       (6, 6, 'in_progress', '2024-06-03 20:35:00', NULL);

-- ---------------------------------------------------------------
-- 22. order_assignment
-- ---------------------------------------------------------------
INSERT INTO order_assignment (kitchen_order_id, waiter_id, assigned_at)
VALUES (1, 1, '2024-06-01 20:01:00'),
       (2, 2, '2024-06-01 20:31:00'),
       (3, 3, '2024-06-02 12:41:00'),
       (4, 1, '2024-06-02 19:06:00'),
       (5, 5, '2024-06-03 19:11:00'),
       (6, 2, '2024-06-03 20:36:00');

-- ---------------------------------------------------------------
-- 23. order_status_updates
-- ---------------------------------------------------------------
INSERT INTO order_status_updates (order_id, waiter_id, status, updated_at)
VALUES (1, 1, 'served', '2024-06-01 20:05:00'),
       (2, 2, 'served', '2024-06-01 20:35:00'),
       (3, 3, 'served', '2024-06-02 12:45:00'),
       (5, 5, 'served', '2024-06-03 19:15:00'),
       (4, 1, 'unserved', '2024-06-02 19:10:00'),
       (6, 2, 'unserved', '2024-06-03 20:40:00');

-- ---------------------------------------------------------------
-- 24. kds_orders
-- ---------------------------------------------------------------
INSERT INTO kds_orders (order_id, displayed_at, bumped_at, bumped_by, is_rush, is_vip, color_status)
VALUES (1, '2024-06-01 19:35:00', '2024-06-01 20:00:00', 4, 0, 0, 'green'),
       (2, '2024-06-01 20:05:00', '2024-06-01 20:30:00', 4, 0, 0, 'green'),
       (3, '2024-06-02 12:20:00', '2024-06-02 12:40:00', 4, 1, 0, 'yellow'),
       (4, '2024-06-02 19:05:00', NULL, NULL, 0, 1, 'red'),
       (5, '2024-06-03 18:50:00', '2024-06-03 19:10:00', 4, 0, 0, 'green'),
       (6, '2024-06-03 20:35:00', NULL, NULL, 1, 0, 'yellow');

-- ---------------------------------------------------------------
-- 25. kds_order_items
-- ---------------------------------------------------------------
INSERT INTO kds_order_items (kds_order_id, order_item_id, status, fired_at, completed_at)
VALUES (1, 1, 'done', '2024-06-01 19:36:00', '2024-06-01 19:50:00'),
       (1, 2, 'done', '2024-06-01 19:36:00', '2024-06-01 19:55:00'),
       (1, 3, 'done', '2024-06-01 19:36:00', '2024-06-01 19:45:00'),
       (2, 4, 'done', '2024-06-01 20:06:00', '2024-06-01 20:25:00'),
       (2, 5, 'done', '2024-06-01 20:06:00', '2024-06-01 20:20:00'),
       (3, 6, 'done', '2024-06-02 12:21:00', '2024-06-02 12:30:00'),
       (4, 8, 'in_progress', '2024-06-02 19:06:00', NULL),
       (4, 9, 'pending', NULL, NULL),
       (6, 11, 'in_progress', '2024-06-03 20:36:00', NULL),
       (6, 12, 'pending', NULL, NULL);

-- ---------------------------------------------------------------
-- 26. order_discounts
-- ---------------------------------------------------------------
INSERT INTO order_discounts (order_id, order_item_id, discount_type, discount_value, reason_code, applied_by,
                             manager_pin_used)
VALUES (2, NULL, 'percent', 5.00, 'LOYALTY_SILVER', 3, 0),
       (5, NULL, 'percent', 5.00, 'ONLINE_PROMO', 5, 0),
       (1, 1, 'fixed', 50.00, 'COMP_STARTER', 3, 1),
       (2, 4, 'comp', 100.00, 'MANAGER_COMP', 3, 1),
       (3, 7, 'percent', 10.00, 'BIRTHDAY_OFFER', 5, 0),
       (4, 8, 'fixed', 50.00, 'VIP_DISCOUNT', 3, 1);

-- ---------------------------------------------------------------
-- 27. purchase_orders
-- ---------------------------------------------------------------
INSERT INTO purchase_orders (id, supplier_id, po_number, status, total_amount, sent_at, expected_date, received_at,
                             created_by)
VALUES (1, 1, 'PO-2024-001', 'received', 21250.00, '2024-05-28 09:00:00', '2024-05-30', '2024-05-30 10:00:00', 3),
       (2, 2, 'PO-2024-002', 'received', 16800.00, '2024-05-29 09:00:00', '2024-05-31', '2024-05-31 11:00:00', 3),
       (3, 3, 'PO-2024-003', 'confirmed', 7440.00, '2024-06-01 09:00:00', '2024-06-03', NULL, 3),
       (4, 4, 'PO-2024-004', 'sent', 4200.00, '2024-06-02 09:00:00', '2024-06-05', NULL, 1),
       (5, 5, 'PO-2024-005', 'draft', 5400.00, NULL, '2024-06-07', NULL, 3),
       (6, 1, 'PO-2024-006', 'partially_received', 12750.00, '2024-06-03 09:00:00', '2024-06-05', NULL, 3);

-- ---------------------------------------------------------------
-- 28. purchase_order_items
-- ---------------------------------------------------------------
INSERT INTO purchase_order_items (po_id, ingredient_id, ordered_qty, received_qty, unit_price, line_total)
VALUES (1, 1, 25.000, 25.000, 850.0000, 21250.00),
       (2, 6, 12.000, 12.000, 1400.0000, 16800.00),
       (3, 3, 62.000, 0.000, 120.0000, 7440.00),
       (4, 4, 12.000, 0.000, 350.0000, 4200.00),
       (5, 2, 60.000, 0.000, 90.0000, 5400.00),
       (6, 1, 15.000, 10.000, 850.0000, 12750.00),
       (6, 9, 10.000, 5.000, 280.0000, 2800.00);

-- ---------------------------------------------------------------
-- 29. inventory_transactions
-- ---------------------------------------------------------------
INSERT INTO inventory_transactions (ingredient_id, transaction_type, quantity, balance_after, order_id,
                                    purchase_order_id, performed_by, notes)
VALUES (1, 'restock', 25.000, 25.500, NULL, 1, 3, 'PO-2024-001 received'),
       (6, 'restock', 12.000, 12.000, NULL, 2, 3, 'PO-2024-002 received'),
       (1, 'deduction', 0.300, 25.200, 1, NULL, 4, 'Used for order ORD-20240601-001'),
       (1, 'deduction', 0.300, 24.900, 2, NULL, 4, 'Used for order ORD-20240601-002'),
       (6, 'deduction', 0.400, 11.600, 2, NULL, 4, 'Prawns for order ORD-20240601-002'),
       (2, 'adjustment', -2.000, 48.000, NULL, NULL, 4, 'Recount adjustment'),
       (1, 'waste', 0.200, 24.700, NULL, NULL, 4, 'Spoilage — exceeded shelf life');

-- ---------------------------------------------------------------
-- 30. inventory_alerts
-- ---------------------------------------------------------------
INSERT INTO inventory_alerts (ingredient_id, alert_type, triggered_at, resolved_at, notified_via)
VALUES (4, 'low_stock', '2024-06-01 08:00:00', '2024-06-02 10:00:00', 'email'),
       (8, 'low_stock', '2024-06-02 07:00:00', NULL, 'sms'),
       (3, 'out_of_stock', '2024-05-30 06:00:00', '2024-05-31 12:00:00', 'email'),
       (7, 'low_stock', '2024-06-03 08:30:00', NULL, 'email'),
       (5, 'low_stock', '2024-06-03 09:00:00', NULL, 'sms'),
       (9, 'low_stock', '2024-06-03 10:00:00', NULL, 'email');

-- ---------------------------------------------------------------
-- 31. recipes
-- ---------------------------------------------------------------
INSERT INTO recipes (id, menu_item_id, version_number, is_current, notes, created_by)
VALUES (1, 3, 1, 0, 'Original chicken curry recipe', 4),
       (2, 3, 2, 1, 'Updated — added coconut milk', 4),
       (3, 4, 1, 1, 'Standard grilled chicken recipe', 4),
       (4, 5, 1, 1, 'Prawn stir-fry base recipe', 4),
       (5, 7, 1, 1, 'Chocolate lava cake — signature', 4),
       (6, 1, 1, 1, 'Garlic bread standard recipe', 4);

-- ---------------------------------------------------------------
-- 32. recipe_ingredients
-- ---------------------------------------------------------------
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit)
VALUES (2, 1, 0.3500, 'kg'), -- Chicken Curry v2 — chicken
       (2, 4, 0.0150, 'kg'), -- Chicken Curry v2 — chilli powder
       (2, 9, 0.0200, 'kg'), -- Chicken Curry v2 — garlic
       (3, 1, 0.2500, 'kg'), -- Grilled Chicken — chicken
       (3, 5, 0.0200, 'L'),  -- Grilled Chicken — olive oil
       (3, 9, 0.0150, 'kg'), -- Grilled Chicken — garlic
       (4, 6, 0.3000, 'kg'), -- Prawn Stir Fry — shrimp
       (4, 5, 0.0200, 'L'),  -- Prawn Stir Fry — olive oil
       (5, 7, 0.1000, 'kg'), -- Lava Cake — flour
       (5, 8, 0.0800, 'kg'), -- Lava Cake — butter
       (6, 7, 0.0500, 'kg'), -- Garlic Bread — flour
       (6, 8, 0.0200, 'kg'), -- Garlic Bread — butter
       (6, 9, 0.0100, 'kg');
-- Garlic Bread — garlic

-- ---------------------------------------------------------------
-- 33. expenses
-- ---------------------------------------------------------------
INSERT INTO expenses (name, expense_date, amount, description, receipt_url, recorded_by)
VALUES ('Electricity Bill', '2024-06-01', 28500.00, 'Monthly electricity — May 2024', '/receipts/elec_may24.pdf', 3),
       ('Staff Salaries', '2024-06-01', 320000.00, 'June 2024 salary disbursement', '/receipts/salary_jun24.pdf', 1),
       ('Cleaning Supplies', '2024-06-02', 4800.00, 'Detergents and mops', '/receipts/clean_jun24.pdf', 3),
       ('Equipment Repair', '2024-06-03', 12000.00, 'Oven repair — kitchen', '/receipts/oven_repair.pdf', 3),
       ('Marketing Flyers', '2024-06-03', 3500.00, 'Printed promo flyers — June', '/receipts/flyers_jun24.pdf', 1),
       ('Internet Bill', '2024-06-01', 5200.00, 'Fibre internet — June 2024', '/receipts/inet_jun24.pdf', 3);

-- ---------------------------------------------------------------
-- 34. dashboard_widgets
-- ---------------------------------------------------------------
INSERT INTO dashboard_widgets (user_id, widget_type, position_x, position_y, width, height, config_json)
VALUES (1, 'revenue_today', 0, 0, 4, 2, '{
  "currency": "LKR",
  "show_trend": true
}'),
       (1, 'orders_live', 4, 0, 4, 2, '{
         "refresh_sec": 30
       }'),
       (1, 'top_menu_items', 8, 0, 4, 2, '{
         "limit": 5,
         "period": "week"
       }'),
       (3, 'inventory_alerts', 0, 0, 6, 3, '{
         "severity": [
           "low_stock",
           "out_of_stock"
         ]
       }'),
       (3, 'staff_on_duty', 6, 0, 6, 3, '{
         "show_breaks": true
       }'),
       (5, 'payment_summary', 0, 0, 12, 2, '{
         "methods": [
           "cash",
           "card",
           "online",
           "loyalty"
         ]
       }');

-- ---------------------------------------------------------------
-- 35. sales_trends
-- ---------------------------------------------------------------
INSERT INTO sales_trends (period_type, period_start, period_end, total_revenue, total_orders, avg_order_value)
VALUES ('daily', '2024-06-01 00:00:00', '2024-06-01 23:59:59', 4186.60, 2, 2093.30),
       ('daily', '2024-06-02 00:00:00', '2024-06-02 23:59:59', 2922.15, 2, 1461.08),
       ('daily', '2024-06-03 00:00:00', '2024-06-03 23:59:59', 2893.41, 2, 1446.71),
       ('weekly', '2024-05-27 00:00:00', '2024-06-02 23:59:59', 18450.00, 12, 1537.50),
       ('monthly', '2024-05-01 00:00:00', '2024-05-31 23:59:59', 72300.00, 48, 1506.25),
       ('hourly', '2024-06-03 19:00:00', '2024-06-03 19:59:59', 1848.00, 1, 1848.00);

-- ---------------------------------------------------------------
-- 36. stock_count_sessions
-- ---------------------------------------------------------------
INSERT INTO stock_count_sessions (id, counted_by, session_date, status)
VALUES (1, 4, '2024-06-01', 'completed'),
       (2, 4, '2024-06-02', 'completed'),
       (3, 3, '2024-06-03', 'open'),
       (4, 4, '2024-05-25', 'completed'),
       (5, 1, '2024-05-18', 'completed'),
       (6, 4, '2024-06-04', 'open');

-- ---------------------------------------------------------------
-- 37. stock_count_items
-- ---------------------------------------------------------------
INSERT INTO stock_count_items (session_id, ingredient_id, system_quantity, counted_quantity, variance, barcode_scanned)
VALUES (1, 1, 26.000, 25.500, -0.500, 1),
       (1, 2, 50.000, 50.000, 0.000, 1),
       (1, 3, 22.000, 20.000, -2.000, 0),
       (2, 4, 3.500, 3.000, -0.500, 1),
       (2, 5, 8.500, 8.000, -0.500, 1),
       (3, 6, 12.000, 11.600, -0.400, 1),
       (3, 7, 31.000, 30.000, -1.000, 0),
       (4, 8, 7.000, 6.000, -1.000, 1),
       (5, 9, 5.000, 4.000, -1.000, 0),
       (5, 10, 11.000, 10.000, -1.000, 1);

-- ---------------------------------------------------------------
-- 38. supplier_ingredients
-- ---------------------------------------------------------------
INSERT INTO supplier_ingredients (supplier_id, ingredient_id, supplier_sku, unit_price, min_order_qty, price_date)
VALUES (1, 1, 'FF-CHK-001', 850.0000, 5.000, '2024-05-01'),
       (1, 10, 'FF-TOM-001', 160.0000, 5.000, '2024-05-01'),
       (2, 6, 'SB-SHR-L01', 1400.0000, 3.000, '2024-05-01'),
       (3, 3, 'ID-MLK-001', 120.0000, 10.000, '2024-05-01'),
       (4, 4, 'SM-RCP-001', 350.0000, 2.000, '2024-05-01'),
       (5, 2, 'GG-RIC-BAS', 90.0000, 20.000, '2024-05-01'),
       (5, 7, 'GG-FLR-APF', 90.0000, 10.000, '2024-05-01');

-- ---------------------------------------------------------------
-- 39. waste_logs
-- ---------------------------------------------------------------
INSERT INTO waste_logs (ingredient_id, menu_item_id, quantity, unit, cost, reason_code, logged_by)
VALUES (1, 3, 0.200, 'kg', 170.00, 'SPOILAGE', 4),
       (6, 5, 0.100, 'kg', 140.00, 'PREP_WASTE', 4),
       (3, NULL, 1.000, 'L', 120.00, 'EXPIRED', 4),
       (7, 7, 0.150, 'kg', 13.50, 'OVER_PRODUCTION', 4),
       (10, 3, 0.500, 'kg', 80.00, 'DAMAGE', 4),
       (8, 1, 0.050, 'kg', 31.00, 'PREP_WASTE', 4);

-- ---------------------------------------------------------------
-- 40. campaign_analytics
-- ---------------------------------------------------------------
INSERT INTO campaign_analytics (campaign_id, customer_id, sent_at, opened_at, clicked_at, converted_at, unsubscribed_at,
                                variant)
VALUES (1, 1, '2024-06-01 10:00:00', '2024-06-01 10:15:00', '2024-06-01 10:16:00', '2024-06-01 19:30:00', NULL, 'A'),
       (1, 2, '2024-06-01 10:00:00', '2024-06-01 11:00:00', NULL, NULL, NULL, 'B'),
       (1, 3, '2024-06-01 10:00:00', NULL, NULL, NULL, NULL, 'A'),
       (2, 4, '2024-06-02 09:00:00', '2024-06-02 09:30:00', '2024-06-02 09:31:00', '2024-06-02 19:00:00', NULL, 'A'),
       (2, 5, '2024-06-02 09:00:00', '2024-06-02 10:00:00', NULL, NULL, '2024-06-02 10:05:00', 'B'),
       (2, 6, '2024-06-02 09:00:00', '2024-06-02 09:45:00', '2024-06-02 09:46:00', NULL, NULL, 'A');

-- ---------------------------------------------------------------
-- 41. marketing_campaigns
-- ---------------------------------------------------------------
INSERT INTO marketing_campaigns (id, campaign_name, segment_id, channel, subject, body_template, ab_test_enabled,
                                 variant_b_body, scheduled_at, sent_at, status, created_by)
VALUES (1, 'June Birthday Blast', 1, 'email', 'Happy Birthday from us! 🎂',
        'Hi {{first_name}}, enjoy 10% off this month!', 1, 'Hi {{first_name}}, a special gift awaits you!',
        '2024-06-01 09:00:00', '2024-06-01 10:00:00', 'sent', 3),
       (2, 'Lapsed Customer Win-Back', 2, 'email', 'We miss you, {{first_name}}!',
        'It has been a while. Come back for 20% off.', 0, NULL, '2024-06-02 09:00:00', '2024-06-02 09:00:00', 'sent',
        3),
       (3, 'Weekend Special Promo', 3, 'sms', NULL, 'This weekend only: Buy 1 Get 1 on desserts!', 0, NULL,
        '2024-06-07 08:00:00', NULL, 'scheduled', 1),
       (4, 'Loyalty Tier Upgrade', 4, 'email', 'You have been upgraded!',
        'Congrats {{first_name}}, you are now {{tier_name}}!', 0, NULL, NULL, NULL, 'draft', 3),
       (5, 'New Menu Launch', 5, 'email', 'New dishes just dropped 🍽️', 'Discover our new seafood menu today.', 1,
        'Fresh from the ocean — new menu live now!', '2024-06-10 10:00:00', NULL, 'scheduled', 1),
       (6, 'Ramadan Special', 6, 'email', 'Eid Mubarak from our family to yours',
        'Enjoy our special Halal Eid menu this week.', 0, NULL, '2024-05-20 07:00:00', '2024-05-20 07:00:00', 'sent',
        3);

-- ---------------------------------------------------------------
-- 42. order_sequence
-- ---------------------------------------------------------------
INSERT INTO order_sequence (sequence_date, last_sequence)
VALUES ('2024-06-01', 2),
       ('2024-06-02', 2),
       ('2024-06-03', 2),
       ('2024-06-04', 0),
       ('2024-06-05', 0);

-- =============================================================
SET FOREIGN_KEY_CHECKS = 1;
-- =============================================================
