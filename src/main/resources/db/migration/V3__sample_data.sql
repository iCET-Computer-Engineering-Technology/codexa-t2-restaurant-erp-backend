-- =============================================================
-- Smart Restaurant ERP — Sample Data Seed
-- Focus: Order Management (Menu, Portions, Orders, Payments)
-- 5 Categories × 10 Items | 3–4 Portion Variants per Item
-- Prices in Sri Lankan Rupees (LKR)
-- =============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- =============================================================
-- 0. CLEANUP EXISTING DATA
-- Clear order management and related master data to prevent
-- Primary Key collisions before inserting the V3 sample data.
-- =============================================================

-- Clear Order Management Data
DELETE FROM order_assignment;
DELETE FROM order_status_updates;
DELETE FROM order_discounts;
DELETE FROM payments;
DELETE FROM kds_order_items;
DELETE FROM kds_orders;
DELETE FROM kitchen_order;
DELETE FROM order_items;
DELETE FROM loyalty_transactions;
DELETE FROM customer_visits;
DELETE FROM inventory_transactions;
DELETE FROM orders;

-- Clear Master Data populated by this script
DELETE FROM menu_item_price;
DELETE FROM menu_items;
DELETE FROM menu_categories;
DELETE FROM portions;
DELETE FROM waiter;
DELETE FROM tables;
DELETE FROM customers;
DELETE FROM users;
DELETE FROM suppliers;

-- Reset Auto-Increments (Optional but recommended for clean IDs)
ALTER TABLE order_assignment AUTO_INCREMENT = 1;
ALTER TABLE order_status_updates AUTO_INCREMENT = 1;
ALTER TABLE order_discounts AUTO_INCREMENT = 1;
ALTER TABLE payments AUTO_INCREMENT = 1;
ALTER TABLE kds_order_items AUTO_INCREMENT = 1;
ALTER TABLE kds_orders AUTO_INCREMENT = 1;
ALTER TABLE kitchen_order AUTO_INCREMENT = 1;
ALTER TABLE order_items AUTO_INCREMENT = 1;
ALTER TABLE orders AUTO_INCREMENT = 1;

-- =============================================================
-- 1. USERS (Staff)
-- =============================================================
INSERT INTO users (id, username, email, password, role, enabled) VALUES
                                                                     (1, 'admin',        'admin@restaurant.lk',   '$2a$10$hashedpassword1', 'admin',   1),
                                                                     (2, 'manager01',    'manager@restaurant.lk',  '$2a$10$hashedpassword2', 'manager', 1),
                                                                     (3, 'server01',     'server1@restaurant.lk',  '$2a$10$hashedpassword3', 'server',  1),
                                                                     (4, 'server02',     'server2@restaurant.lk',  '$2a$10$hashedpassword4', 'server',  1),
                                                                     (5, 'cashier01',    'cashier@restaurant.lk',  '$2a$10$hashedpassword5', 'cashier', 1);

-- =============================================================
-- 2. CUSTOMERS
-- =============================================================
INSERT INTO customers (id, first_name, last_name, email, phone, loyalty_points, birthday) VALUES
                                                                                              (1, 'Ashan',    'Perera',    'ashan@gmail.com',    '+94771234567', 150, '1990-05-15'),
                                                                                              (2, 'Dilini',   'Silva',     'dilini@gmail.com',   '+94762345678', 320, '1988-11-22'),
                                                                                              (3, 'Ruwantha', 'Fernando',  'ruwan@gmail.com',    '+94753456789', 80,  '1995-03-08'),
                                                                                              (4, 'Nishani',  'Rajapaksa', 'nishani@gmail.com',  '+94744567890', 500, '1992-07-30'),
                                                                                              (5, 'Kasun',    'Wickrama',  'kasun@gmail.com',    '+94735678901', 210, '1985-12-04');

-- =============================================================
-- 3. TABLES (Restaurant Floor)
-- =============================================================
INSERT INTO tables (id, table_number, capacity, pos_x, pos_y, status) VALUES
                                                                          (1,  'T01', 2,  100, 100, 'available'),
                                                                          (2,  'T02', 2,  200, 100, 'available'),
                                                                          (3,  'T03', 4,  300, 100, 'occupied'),
                                                                          (4,  'T04', 4,  400, 100, 'available'),
                                                                          (5,  'T05', 4,  100, 200, 'reserved'),
                                                                          (6,  'T06', 6,  200, 200, 'available'),
                                                                          (7,  'T07', 6,  300, 200, 'occupied'),
                                                                          (8,  'T08', 8,  400, 200, 'available'),
                                                                          (9,  'T09', 8,  100, 300, 'available'),
                                                                          (10, 'T10', 10, 200, 300, 'available');

-- =============================================================
-- 4. WAITERS
-- =============================================================
INSERT INTO waiter (id, waiter_name, status) VALUES
                                                 (1, 'Chamara Bandara',  'active'),
                                                 (2, 'Sanduni Jayawardena', 'active'),
                                                 (3, 'Thilina Rathnayake', 'active'),
                                                 (4, 'Madhavi Gunasekara', 'on_break');

-- =============================================================
-- 5. PORTIONS (Global — 3–4 variants)
-- =============================================================
INSERT INTO portions (id, portion_name) VALUES
                                            (1,  'Regular'),
                                            (2,  'Large'),
                                            (3,  'Small'),
                                            (4,  'Half'),
                                            (5,  'Full'),
                                            (6,  'Single'),
                                            (7,  'Double'),
                                            (8,  'Family'),
                                            (9,  'Combo'),
                                            (10, 'Extra Large');

-- =============================================================
-- 6. MENU CATEGORIES
-- =============================================================
INSERT INTO menu_categories (id, name, is_active) VALUES
                                                      (1, 'Burgers & American Grill', 1),
                                                      (2, 'Italian Classics',         1),
                                                      (3, 'Japanese & Asian Fusion',  1),
                                                      (4, 'Indian Curry House',       1),
                                                      (5, 'Beverages & Desserts',     1);

-- =============================================================
-- 7. MENU ITEMS
-- =============================================================
INSERT INTO menu_items (id, category_id, name, description, is_available, image_url) VALUES
                                                                                         (1,  1, 'Classic Cheeseburger', 'Juicy beef patty with aged cheddar, lettuce, tomato, pickles and our house sauce in a brioche bun.', 1, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600'),
                                                                                         (2,  1, 'Smokehouse BBQ Burger', 'Slow-smoked beef patty, crispy bacon, caramelised onions, BBQ sauce and colby jack cheese.', 1, 'https://images.unsplash.com/photo-1606149059549-6042addafc5a?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
                                                                                         (3,  1, 'Crispy Chicken Burger', 'Southern-style buttermilk fried chicken, slaw, jalapeños and sriracha mayo.', 1, 'https://images.unsplash.com/photo-1606755962773-d324e0a13086?w=600'),
                                                                                         (4,  1, 'Mushroom Swiss Burger', 'Grilled beef patty topped with sautéed portobello mushrooms, Swiss cheese and garlic aioli.', 1, 'https://images.unsplash.com/photo-1572802419224-296b0aeee0d9?w=600'),
                                                                                         (5,  1, 'Loaded Nachos', 'Crispy tortilla chips loaded with melted cheddar, jalapeños, sour cream, guacamole and pico de gallo.', 1, 'https://images.unsplash.com/photo-1513456852971-30c0b8199d4d?w=600'),
                                                                                         (6,  1, 'BBQ Pork Ribs', 'Fall-off-the-bone pork ribs slow-cooked and glazed with smoky hickory BBQ sauce.', 1, 'https://images.unsplash.com/photo-1544025162-d76694265947?w=600'),
                                                                                         (7,  1, 'New York Strip Steak', '250g premium New York strip, grilled to order, served with fries and garlic butter.', 1, 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600'),
                                                                                         (8,  1, 'Classic Caesar Salad', 'Romaine lettuce, parmesan, house-made croutons and classic Caesar dressing.', 1, 'https://images.unsplash.com/photo-1546793665-c74683f339c1?w=600'),
                                                                                         (9,  1, 'French Fries', 'Golden crispy fries seasoned with sea salt and served with ketchup and mayo.', 1, 'https://images.unsplash.com/photo-1630384060421-cb20d0e0649d?w=600'),
                                                                                         (10, 1, 'Onion Rings', 'Beer-battered onion rings fried to perfection, served with chipotle dipping sauce.', 1, 'https://images.unsplash.com/photo-1639024471283-03518883512d?w=600'),
                                                                                         (11, 2, 'Spaghetti Carbonara', 'Al dente spaghetti with guanciale, egg yolk, Pecorino Romano and black pepper. True Roman style.', 1, 'https://images.unsplash.com/photo-1612874742237-6526221588e3?w=600'),
                                                                                         (12, 2, 'Margherita Pizza', 'Neapolitan-style pizza with San Marzano tomato sauce, fresh mozzarella and basil on a wood-fired crust.', 1, 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=600'),
                                                                                         (13, 2, 'Penne Arrabbiata', 'Penne pasta in a spicy San Marzano tomato sauce with garlic, chilli flakes and fresh parsley.', 1, 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=600'),
                                                                                         (14, 2, 'Risotto ai Funghi', 'Creamy Arborio rice with wild mushrooms, white wine, parmesan and truffle oil.', 1, 'https://images.unsplash.com/photo-1476124369491-e7addf5db371?w=600'),
                                                                                         (15, 2, 'Chicken Parmigiana', 'Crumbed chicken breast topped with napolitana sauce and melted mozzarella, served with spaghetti.', 1, 'https://images.unsplash.com/photo-1632778149955-e80f8ceca2e8?w=600'),
                                                                                         (16, 2, 'Lasagne Bolognese', 'Layers of fresh pasta, slow-cooked beef ragu, béchamel and Parmigiano-Reggiano.', 1, 'https://images.unsplash.com/photo-1619895092538-128341789043?w=600'),
                                                                                         (17, 2, 'Bruschetta al Pomodoro', 'Toasted sourdough rubbed with garlic, topped with cherry tomatoes, basil and extra virgin olive oil.', 1, 'https://images.unsplash.com/photo-1572695157366-5e585ab2b69f?w=600'),
                                                                                         (18, 2, 'Tiramisu', 'Classic Italian dessert with espresso-soaked ladyfingers, mascarpone cream and cocoa dusting.', 1, 'https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=600'),
                                                                                         (19, 2, 'Quattro Formaggi Pizza', 'Four-cheese pizza with mozzarella, gorgonzola, taleggio and Parmigiano on a thin crust.', 1, 'https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600'),
                                                                                         (20, 2, 'Minestrone Soup', 'Hearty Italian vegetable soup with cannellini beans, pasta, tomatoes and seasonal vegetables.', 1, 'https://images.unsplash.com/photo-1547592166-23ac45744acd?w=600'),
                                                                                         (21, 3, 'Salmon Sashimi', 'Premium Norwegian salmon, thinly sliced and served with pickled ginger, wasabi and soy sauce.', 1, 'https://pixabay.com/photos/sushi-salmon-food-fish-seafood-5439480/'),
                                                                                         (22, 3, 'Chicken Ramen', 'Rich tonkotsu-style broth with ramen noodles, chashu chicken, soft-boiled egg, nori and green onions.', 1, 'https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=600'),
                                                                                         (23, 3, 'Dragon Roll (Sushi)', 'Prawn tempura and cucumber inside, topped with avocado, tobiko and eel sauce.', 1, 'https://pixabay.com/images/download/voxpedro-sushi-2453153_1280.jpg'),
                                                                                         (24, 3, 'Pad Thai', 'Stir-fried rice noodles with prawns, tofu, bean sprouts, egg and crushed peanuts in tamarind sauce.', 1, 'https://images.unsplash.com/photo-1559314809-0d155014e29e?w=600'),
                                                                                         (25, 3, 'Korean BBQ Beef (Bulgogi)', 'Thinly sliced marinated beef grilled at the table, served with steamed rice and banchan sides.', 1, 'https://images.unsplash.com/photo-1614735241165-6756e1df61ab?w=600'),
                                                                                         (26, 3, 'Dim Sum Basket', 'Steamed bamboo basket with a selection of har gow, siu mai and char siu bao.', 1, 'https://images.unsplash.com/photo-1563245372-f21724e3856d?w=600'),
                                                                                         (27, 3, 'Miso Soup', 'Traditional Japanese dashi broth with white miso, silken tofu, wakame and spring onions.', 1, 'https://pixabay.com/images/download/jyleen21-soy-bean-paste-soup-749368_1920.jpg'),
                                                                                         (28, 3, 'Gyoza (Pan-Fried Dumplings)', 'Crispy-bottomed pork and cabbage dumplings served with ponzu dipping sauce.', 1, 'https://images.unsplash.com/photo-1496116218417-1a781b1c416c?w=600'),
                                                                                         (29, 3, 'Teriyaki Salmon Bowl', 'Grilled teriyaki-glazed salmon fillet over steamed Japanese rice with edamame and sesame.', 1, 'https://images.unsplash.com/photo-1617093727343-374698b1b08d?w=600'),
                                                                                         (30, 3, 'Tempura Platter', 'Light, crispy battered prawns and vegetables served with tentsuyu dipping sauce and daikon.', 1, 'https://pixabay.com/images/download/april_kim-shrimp-1621339_1920.jpg'),
                                                                                         (31, 4, 'Butter Chicken (Murgh Makhani)', 'Tender chicken in a velvety tomato-cream sauce with aromatic spices. A North Indian classic.', 1, 'https://images.unsplash.com/photo-1588166524941-3bf61a9c41db?w=600'),
                                                                                         (32, 4, 'Lamb Biryani', 'Fragrant basmati rice slow-cooked with tender lamb, saffron, whole spices and caramelised onions.', 1, 'https://images.unsplash.com/photo-1630409346824-4f0e7b080087?w=600'),
                                                                                         (33, 4, 'Palak Paneer', 'Fresh cottage cheese cubes in a smooth, spiced spinach gravy. Served with naan.', 1, 'https://images.unsplash.com/photo-1618449840665-9ed506d73a34?w=600'),
                                                                                         (34, 4, 'Dal Makhani', 'Black lentils and kidney beans slow-cooked overnight with butter, cream and aromatic spices.', 1, 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600'),
                                                                                         (35, 4, 'Chicken Tikka Masala', 'Chargrilled chicken tikka in a rich, creamy masala sauce with fenugreek and tomato.', 1, 'https://images.unsplash.com/photo-1565557623262-b51c2513a641?w=600'),
                                                                                         (36, 4, 'Garlic Naan', 'Soft leavened flatbread topped with garlic and butter, baked in a tandoor oven.', 1, 'https://images.unsplash.com/photo-1601050690597-df0568f70950?w=600'),
                                                                                         (37, 4, 'Samosa (Fried)', 'Crispy pastry triangles filled with spiced potatoes and peas, served with mint chutney.', 1, 'https://pixabay.com/images/download/kumarsu6745-samosa-10084327_1920.jpg'),
                                                                                         (38, 4, 'Chicken Tandoori', 'Whole leg chicken marinated in yoghurt and spices, charred in the tandoor. Served with mint raita.', 1, 'https://images.unsplash.com/photo-1599487488170-d11ec9c172f0?w=600'),
                                                                                         (39, 4, 'Mango Lassi', 'Chilled yoghurt-based drink blended with ripe Alphonso mangoes and a hint of cardamom.', 1, 'https://images.unsplash.com/photo-1553361371-9b22f78e8b1d?w=600'),
                                                                                         (40, 4, 'Gulab Jamun', 'Soft milk-solid dumplings soaked in rose-scented sugar syrup, served warm.', 1, 'https://pixabay.com/images/download/kumarsu6745-gulab-10142435_1920.jpg'),
                                                                                         (41, 5, 'New York Cheesecake', 'Dense, creamy baked cheesecake on a graham cracker crust, topped with fresh strawberry compote.', 1, 'https://images.unsplash.com/photo-1565958011703-44f9829ba187?w=600'),
                                                                                         (42, 5, 'Belgian Waffles', 'Crispy Belgian waffles served with whipped cream, fresh berries and maple syrup.', 1, 'https://images.unsplash.com/photo-1562376552-0d160a2f238d?w=600'),
                                                                                         (43, 5, 'Chocolate Lava Cake', 'Warm dark chocolate fondant with a molten centre, served with vanilla bean ice cream.', 1, 'https://images.unsplash.com/photo-1606313564200-e75d5e30476c?w=600'),
                                                                                         (44, 5, 'Fresh Fruit Smoothie', 'Blended tropical fruits — mango, pineapple, passionfruit — with coconut milk and honey.', 1, 'https://images.unsplash.com/photo-1610970881699-44a5587cabec?w=600'),
                                                                                         (45, 5, 'Classic Cappuccino', 'Double espresso with velvety steamed milk foam, dusted with premium cocoa powder.', 1, 'https://images.unsplash.com/photo-1572442388796-11668a67e53d?w=600'),
                                                                                         (46, 5, 'Iced Matcha Latte', 'Premium ceremonial-grade matcha whisked with oat milk, poured over ice with a touch of vanilla.', 1, 'https://images.unsplash.com/photo-1536256263959-770b48d82b0a?w=600'),
                                                                                         (47, 5, 'Fresh Lemonade', 'Hand-squeezed lemonade with fresh mint, a pinch of sea salt and a honey drizzle.', 1, 'https://images.unsplash.com/photo-1621263764928-df1444c5e859?w=600'),
                                                                                         (48, 5, 'Crème Brûlée', 'Classic French custard with a crisp caramelised sugar top, infused with Madagascar vanilla.', 1, 'https://images.unsplash.com/photo-1470124182917-cc6e71b22ecc?w=600'),
                                                                                         (49, 5, 'Mango Sorbet', 'Dairy-free frozen dessert made from Alphonso mangoes — light, refreshing and intensely fruity.', 1, 'https://images.unsplash.com/photo-1567206563064-6f60f40a2b57?w=600'),
                                                                                         (50, 5, 'Soft Drink (Canned)', 'Choose from Coca-Cola, Sprite, Fanta, Diet Coke or Ginger Beer. Served chilled with ice.', 1, 'https://images.unsplash.com/photo-1625772299848-391b6a87d7b3?w=600');

-- =============================================================
-- 8. MENU ITEM PRICES
-- =============================================================
INSERT INTO menu_item_price (item_id, portion_id, price, is_active) VALUES
                                                                        (1,  3,   750.00, 1), (1,  1,   950.00, 1), (1,  2, 1200.00, 1), (1,  7, 1500.00, 1),
                                                                        (2,  1,  1100.00, 1), (2,  2, 1400.00, 1), (2,  7, 1750.00, 1),
                                                                        (3,  1,  1050.00, 1), (3,  2, 1300.00, 1), (3,  10,1600.00, 1),
                                                                        (4,  1,  1000.00, 1), (4,  2, 1250.00, 1), (4,  7, 1550.00, 1),
                                                                        (5,  3,   750.00, 1), (5,  1, 1100.00, 1), (5,  2, 1500.00, 1),
                                                                        (6,  1,  2200.00, 1), (6,  2, 3500.00, 1), (6,  8, 5500.00, 1),
                                                                        (7,  1,  2900.00, 1), (7,  2, 3800.00, 1), (7,  10,5200.00, 1),
                                                                        (8,  3,   650.00, 1), (8,  1,  950.00, 1), (8,  2, 1300.00, 1),
                                                                        (9,  3,   300.00, 1), (9,  1,  450.00, 1), (9,  2,  550.00, 1), (9,  10,  700.00, 1),
                                                                        (10, 3,   350.00, 1), (10, 1,  500.00, 1), (10, 2,  700.00, 1),
                                                                        (11, 3,   900.00, 1), (11, 1, 1300.00, 1), (11, 2, 1700.00, 1), (11, 8, 2800.00, 1),
                                                                        (12, 3,  1100.00, 1), (12, 1, 1300.00, 1), (12, 2, 1600.00, 1), (12, 8, 2500.00, 1),
                                                                        (13, 3,   850.00, 1), (13, 1, 1200.00, 1), (13, 2, 1550.00, 1), (13, 8, 2600.00, 1),
                                                                        (14, 1,  1600.00, 1), (14, 2, 2100.00, 1),
                                                                        (15, 1,  1800.00, 1), (15, 2, 2300.00, 1),
                                                                        (16, 3,   900.00, 1), (16, 1, 1500.00, 1), (16, 2, 2000.00, 1), (16, 8, 3200.00, 1),
                                                                        (17, 1,   650.00, 1), (17, 2,  950.00, 1),
                                                                        (18, 1,   900.00, 1), (18, 2, 1400.00, 1),
                                                                        (19, 3,  1200.00, 1), (19, 1, 1450.00, 1), (19, 2, 1800.00, 1), (19, 8, 2800.00, 1),
                                                                        (20, 3,   500.00, 1), (20, 1,  750.00, 1), (20, 2, 1000.00, 1),
                                                                        (21, 3,   950.00, 1), (21, 1, 1800.00, 1), (21, 2, 2600.00, 1), (21, 9, 3500.00, 1),
                                                                        (22, 1,  1400.00, 1), (22, 2, 1900.00, 1),
                                                                        (23, 1,  1600.00, 1), (23, 2, 2800.00, 1), (23, 9, 4200.00, 1),
                                                                        (24, 1,  1250.00, 1), (24, 2, 1700.00, 1),
                                                                        (25, 3,  1500.00, 1), (25, 1, 2000.00, 1), (25, 5, 2800.00, 1), (25, 9, 3800.00, 1),
                                                                        (26, 3,   900.00, 1), (26, 1, 1500.00, 1), (26, 2, 2100.00, 1), (26, 9, 2700.00, 1),
                                                                        (27, 1,   350.00, 1),
                                                                        (28, 1,   900.00, 1), (28, 2, 1600.00, 1),
                                                                        (29, 1,  1800.00, 1), (29, 2, 2400.00, 1),
                                                                        (30, 3,   950.00, 1), (30, 1, 1600.00, 1), (30, 2, 2200.00, 1), (30, 9, 3000.00, 1),
                                                                        (31, 4,  1100.00, 1), (31, 5, 1850.00, 1),
                                                                        (32, 4,  1400.00, 1), (32, 5, 2200.00, 1),
                                                                        (33, 4,   950.00, 1), (33, 5, 1600.00, 1),
                                                                        (34, 4,   800.00, 1), (34, 5, 1400.00, 1),
                                                                        (35, 4,  1100.00, 1), (35, 5, 1900.00, 1),
                                                                        (36, 6,   350.00, 1), (36, 7,  600.00, 1),
                                                                        (37, 6,   250.00, 1), (37, 1,  650.00, 1),
                                                                        (38, 4,  1400.00, 1), (38, 5, 2400.00, 1),
                                                                        (39, 1,   450.00, 1), (39, 2,  750.00, 1),
                                                                        (40, 1,   500.00, 1), (40, 7,  900.00, 1),
                                                                        (41, 3,   650.00, 1), (41, 1,  900.00, 1), (41, 2, 1300.00, 1),
                                                                        (42, 1,   950.00, 1), (42, 2, 1400.00, 1),
                                                                        (43, 1,   950.00, 1),
                                                                        (44, 3,   400.00, 1), (44, 1,  600.00, 1), (44, 2,  850.00, 1),
                                                                        (45, 1,   500.00, 1), (45, 7,  800.00, 1),
                                                                        (46, 3,   400.00, 1), (46, 1,  600.00, 1), (46, 2,  800.00, 1),
                                                                        (47, 3,   300.00, 1), (47, 1,  450.00, 1), (47, 2,  650.00, 1),
                                                                        (48, 1,   900.00, 1),
                                                                        (49, 3,   500.00, 1), (49, 1,  800.00, 1), (49, 2, 1100.00, 1),
                                                                        (50, 6,   250.00, 1), (50, 7,  400.00, 1);

-- =============================================================
-- 9. SAMPLE ORDERS
-- =============================================================
INSERT INTO orders (id, order_number, order_type, table_id, customer_id, server_id, status, subtotal, discount_amount, tax_amount, service_charge, total_amount, notes) VALUES
                                                                                                                                                                            (1, 'ORD-2024-0001', 'dine_in', 3, 1, 3, 'paid', 3450.00, 0.00, 414.00, 172.50, 4036.50, 'Window seat preferred'),
                                                                                                                                                                            (2, 'ORD-2024-0002', 'dine_in', 7, 2, 4, 'ready', 5800.00, 500.00, 636.00, 265.00, 6201.00, 'Birthday celebration — comp dessert applied'),
                                                                                                                                                                            (3, 'ORD-2024-0003', 'takeout', NULL, 3, 3, 'paid', 4100.00, 0.00, 492.00, 0.00, 4592.00, NULL),
                                                                                                                                                                            (4, 'ORD-2024-0004', 'delivery', NULL, 4, NULL, 'sent_to_kitchen', 6750.00, 0.00, 810.00, 0.00, 7560.00, 'Deliver to Colombo 03, apt 5B'),
                                                                                                                                                                            (5, 'ORD-2024-0005', 'dine_in', 5, 5, 4, 'open', 3200.00, 0.00, 384.00, 160.00, 3744.00, NULL),
                                                                                                                                                                            (6, 'ORD-2024-0006', 'online', NULL, 2, NULL, 'paid', 5500.00, 550.00, 594.00, 0.00, 5544.00, '10% loyalty discount applied'),
                                                                                                                                                                            (7, 'ORD-2024-0007', 'dine_in', 7, 1, 3, 'paid', 7200.00, 0.00, 864.00, 360.00, 8424.00, NULL);

INSERT INTO order_items (id, order_id, menu_item_id, portion_id, quantity, price, status, notes) VALUES
                                                                                                     (1,  1, 1,  1, 2, 950.00,  'served', NULL),
                                                                                                     (2,  1, 9,  2, 2, 550.00,  'served', 'extra crispy'),
                                                                                                     (3,  1, 45, 1, 2, 500.00,  'served', NULL),
                                                                                                     (4,  2, 31, 5, 1, 1850.00, 'served',  NULL),
                                                                                                     (5,  2, 32, 5, 1, 2200.00, 'served',  NULL),
                                                                                                     (6,  2, 36, 1, 3, 350.00,  'served',  NULL),
                                                                                                     (7,  2, 41, 1, 1, 900.00,  'served',  'free — birthday comp'),
                                                                                                     (8,  3, 22, 1, 1, 1400.00, 'served', 'extra chashu'),
                                                                                                     (9,  3, 28, 1, 2, 900.00,  'served', NULL),
                                                                                                     (10, 3, 46, 1, 2, 600.00,  'served', 'oat milk'),
                                                                                                     (11, 3, 49, 1, 1, 800.00,  'served', NULL),
                                                                                                     (12, 4, 7,  2, 1, 3800.00, 'fired', 'medium rare'),
                                                                                                     (13, 4, 9,  2, 1, 650.00,  'fired', NULL),
                                                                                                     (14, 4, 5,  2, 1, 1500.00, 'fired', 'extra jalapeños'),
                                                                                                     (15, 4, 50, 6, 2, 400.00,  'pending', 'Coca-Cola'),
                                                                                                     (16, 5, 12, 2, 1, 1600.00, 'pending', NULL),
                                                                                                     (17, 5, 11, 1, 1, 1300.00, 'pending', NULL),
                                                                                                     (18, 5, 47, 1, 2, 450.00,  'pending', 'less sugar'),
                                                                                                     (19, 6, 25, 5, 1, 2800.00, 'served', NULL),
                                                                                                     (20, 6, 26, 9, 1, 1800.00, 'served', NULL),
                                                                                                     (21, 6, 27, 1, 2, 450.00,  'served', NULL),
                                                                                                     (22, 6, 49, 1, 1, 800.00,  'served', NULL),
                                                                                                     (23, 7, 21, 1, 2, 1800.00, 'served', '8 slices'),
                                                                                                     (24, 7, 23, 1, 1, 1600.00, 'served', NULL),
                                                                                                     (25, 7, 30, 9, 1, 2200.00, 'served', NULL),
                                                                                                     (26, 7, 45, 7, 2, 800.00,  'served', 'double shot');

-- =============================================================
-- 10. KDS ORDERS (Kitchen Display)
-- =============================================================
INSERT INTO kds_orders (id, order_id, displayed_at, bumped_at, bumped_by, is_rush, color_status) VALUES
                                                                                                     (1, 4, NOW(), NULL,  NULL, 1, 'yellow'),
                                                                                                     (2, 5, NOW(), NULL,  NULL, 0, 'green'),
                                                                                                     (3, 6, NOW() - INTERVAL 45 MINUTE, NOW() - INTERVAL 10 MINUTE, 1, 0, 'green');

INSERT INTO kds_order_items (id, kds_order_id, order_item_id, status, fired_at, completed_at) VALUES
                                                                                                  (1, 1, 12, 'in_progress', NOW() - INTERVAL 8 MINUTE,  NULL),
                                                                                                  (2, 1, 13, 'in_progress', NOW() - INTERVAL 8 MINUTE,  NULL),
                                                                                                  (3, 1, 14, 'done',        NOW() - INTERVAL 12 MINUTE, NOW() - INTERVAL 2 MINUTE),
                                                                                                  (4, 2, 16, 'pending',     NULL, NULL),
                                                                                                  (5, 2, 17, 'pending',     NULL, NULL),
                                                                                                  (6, 3, 19, 'done',        NOW() - INTERVAL 40 MINUTE, NOW() - INTERVAL 15 MINUTE);

-- =============================================================
-- 11. PAYMENTS
-- =============================================================
INSERT INTO payments (id, order_id, payment_method, amount, tip_amount, reference_number, processed_by) VALUES
                                                                                                            (1, 1, 'card',  4036.50, 200.00, 'TXN-VISA-0001', 5),
                                                                                                            (2, 2, 'cash',  6201.00, 0.00,   NULL,             5),
                                                                                                            (3, 3, 'card',  4592.00, 0.00,   'TXN-MAST-0002',  5),
                                                                                                            (4, 6, 'online',5544.00, 0.00,   'TXN-ONLN-0006',  NULL),
                                                                                                            (5, 7, 'card',  8424.00, 500.00, 'TXN-AMEX-0007',  5);

-- =============================================================
-- 12. ORDER DISCOUNTS
-- =============================================================
INSERT INTO order_discounts (order_id, order_item_id, discount_type, discount_value, reason_code, applied_by, manager_pin_used) VALUES
                                                                                                                                    (2, 7,  'comp',    900.00, 'BIRTHDAY_COMP',    2, 1),
                                                                                                                                    (6, NULL,'percent', 550.00, 'LOYALTY_10PCT',   2, 0);

-- =============================================================
-- 13. WAITER ORDER ASSIGNMENTS
-- =============================================================
INSERT INTO order_assignment (kitchen_order_id, waiter_id, assigned_at) VALUES
                                                                            (1, 1, NOW()),
                                                                            (2, 2, NOW()),
                                                                            (3, 3, NOW() - INTERVAL 45 MINUTE);

-- =============================================================
-- 14. ORDER STATUS UPDATES
-- =============================================================
INSERT INTO order_status_updates (order_id, waiter_id, status) VALUES
                                                                   (1, 1, 'served'),
                                                                   (2, 2, 'served'),
                                                                   (3, 3, 'served'),
                                                                   (6, 1, 'served'),
                                                                   (7, 2, 'served');

-- =============================================================
-- 15. CUSTOMER VISITS
-- =============================================================
INSERT INTO customer_visits (customer_id, visit_date, order_id, spend_amount, notes) VALUES
                                                                                         (1, NOW() - INTERVAL 2 DAY,  1, 4036.50, 'Regular dine-in'),
                                                                                         (2, NOW() - INTERVAL 1 DAY,  2, 6201.00, 'Birthday dinner'),
                                                                                         (3, NOW() - INTERVAL 3 DAY,  3, 4592.00, 'Takeout order'),
                                                                                         (2, NOW() - INTERVAL 5 DAY,  6, 5544.00, 'Online order'),
                                                                                         (1, NOW() - INTERVAL 7 DAY,  7, 8424.00, 'Dinner with guests');

-- =============================================================
-- 16. LOYALTY TRANSACTIONS
-- =============================================================
INSERT INTO loyalty_transactions (customer_id, order_id, transaction_type, points, balance_after, notes) VALUES
                                                                                                             (1, 1, 'earn',   40,  190, '1pt per Rs.100 spent'),
                                                                                                             (2, 2, 'earn',   62,  382, '1pt per Rs.100 spent'),
                                                                                                             (3, 3, 'earn',   45,  125, '1pt per Rs.100 spent'),
                                                                                                             (2, 6, 'redeem', 55,  327, '10% loyalty discount redeemed'),
                                                                                                             (1, 7, 'earn',   84,  274, '1pt per Rs.100 spent');

-- =============================================================
-- 17. SUPPLIERS
-- =============================================================
INSERT INTO suppliers (id, name, contact_name, email, phone, address) VALUES
                                                                          (1, 'Lanka Fresh Produce',     'Ruwan Senaratne',   'ruwan@lankafresh.lk',   '+94112345678', 'No.12, Manning Market, Colombo 10'),
                                                                          (2, 'Ceylon Meat & Dairy',     'Priyanka De Silva', 'priyanka@ceylonmeat.lk','+94113456789', 'No.45, Wellawatte, Colombo 06'),
                                                                          (3, 'Oceanic Seafood Imports', 'Dinesh Perera',     'dinesh@oceanic.lk',     '+94114567890', 'No.7, Negombo Fish Market, Negombo'),
                                                                          (4, 'Spice Garden Imports',    'Amali Gunawardena', 'amali@spicegarden.lk',  '+94115678901', 'No.88, Pettah, Colombo 11'),
                                                                          (5, 'Continental Beverages',   'Lasantha Jayamal',  'lasantha@conbev.lk',    '+94116789012', 'No.33, Orugodawatta, Colombo 12');

-- =============================================================
-- RE-ENABLE FOREIGN KEY CHECKS
-- =============================================================
SET FOREIGN_KEY_CHECKS = 1;