ALTER TABLE menu_items
ADD COLUMN prep_time_minutes INT NOT NULL DEFAULT 10 AFTER description;

INSERT INTO menu_items (category_id, name, description, prep_time_minutes, is_available, image_url) VALUES
    (1, 'Spicy Chicken Kotthu', 'Classic street food chopped roti with chicken and gravy.', 15, 1, 'assets/img/kotthu.jpg'),
    (1, 'Egg Hopper Set', '4 crispy hoppers with one egg hopper and lunu miris.', 12, 1, 'assets/img/hoppers.jpg'),
    (2, 'Crispy Chicken Burger', 'Fried chicken patty with cheese and fries.', 10, 1, 'assets/img/burger.jpg'),
    (3, 'Iced Milo', 'Chilled chocolate malt beverage.', 3, 1, 'assets/img/milo.jpg'),
    (3, 'Fresh Lime Juice', 'Freshly squeezed lime with sugar syrup.', 5, 1, 'assets/img/lime.jpg');