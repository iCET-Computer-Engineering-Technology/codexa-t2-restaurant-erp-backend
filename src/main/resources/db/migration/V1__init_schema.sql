-- =============================================================
-- Smart Restaurant ERP — Full Schema with Foreign Keys
-- Adapted from user provided script
-- =============================================================
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- Table users
-- (Fixed syntax error from original DDL)

CREATE TABLE IF NOT EXISTS users (
id INT NOT NULL AUTO_INCREMENT,
username VARCHAR(255) NOT NULL UNIQUE,
email VARCHAR(255) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
role VARCHAR(100) NULL DEFAULT NULL,
enabled BOOLEAN NULL DEFAULT 1,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table automated_messages

CREATE TABLE IF NOT EXISTS automated_messages (
id INT NOT NULL AUTO_INCREMENT,
trigger_type ENUM('birthday','anniversary','lapsed','tier_change') NULL DEFAULT NULL,
channel SET('email','sms') NULL DEFAULT NULL,
template_body TEXT NULL DEFAULT NULL,
offer_type ENUM('discount','free_item','none') NULL DEFAULT NULL,
offer_value DECIMAL(10,2) NULL DEFAULT NULL,
send_days_before INT NULL DEFAULT '1',
is_active TINYINT NULL DEFAULT '1',
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- automated_time
CREATE TABLE email_scheduler_config (
id INT AUTO_INCREMENT PRIMARY KEY,
send_time TIME NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table customers

CREATE TABLE IF NOT EXISTS customers (
id INT NOT NULL AUTO_INCREMENT,
first_name VARCHAR(100) NULL DEFAULT NULL,
last_name VARCHAR(100) NULL DEFAULT NULL,
email VARCHAR(255) UNIQUE NULL DEFAULT NULL,
phone VARCHAR(30) UNIQUE NULL DEFAULT NULL,
preferred_language VARCHAR(10) NULL DEFAULT 'en',
dietary_notes TEXT NULL DEFAULT NULL,
communication_email TINYINT NULL DEFAULT '1',
communication_sms TINYINT NULL DEFAULT '1',
gdpr_deleted TINYINT NULL DEFAULT '0',
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
birthday DATE NULL DEFAULT NULL,
loyalty_points INT NOT NULL DEFAULT 0,
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table campaign_analytics

CREATE TABLE IF NOT EXISTS campaign_analytics (
id INT NOT NULL AUTO_INCREMENT,
campaign_id INT NULL DEFAULT NULL,
customer_id INT NULL DEFAULT NULL,
sent_at DATETIME NULL DEFAULT NULL,
opened_at DATETIME NULL DEFAULT NULL,
clicked_at DATETIME NULL DEFAULT NULL,
converted_at DATETIME NULL DEFAULT NULL,
unsubscribed_at DATETIME NULL DEFAULT NULL,
variant CHAR(1) NULL DEFAULT NULL,
PRIMARY KEY (id),
CONSTRAINT fk_campaign_analytics_customer
FOREIGN KEY (customer_id) REFERENCES customers (id)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table tables

CREATE TABLE IF NOT EXISTS tables (
id INT NOT NULL AUTO_INCREMENT,
section_id INT NULL DEFAULT NULL,
table_number VARCHAR(20) NULL DEFAULT NULL,
capacity INT NULL DEFAULT NULL,
pos_x INT NULL DEFAULT NULL,
pos_y INT NULL DEFAULT NULL,
status ENUM('available','occupied','reserved','cleaning') NULL DEFAULT 'available',
updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table orders

CREATE TABLE IF NOT EXISTS orders (
id INT NOT NULL AUTO_INCREMENT,
order_number VARCHAR(50) NULL DEFAULT NULL,
order_type ENUM('dine_in','takeout','booking') NULL DEFAULT NULL,
table_id INT NULL DEFAULT NULL,
customer_id INT NULL DEFAULT NULL,
server_id INT NULL DEFAULT NULL,
status ENUM('open','sent_to_kitchen','partially_ready','ready','paid','voided') NOT NULL DEFAULT 'open',
subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00,
discount_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
tax_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
service_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,
total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
notes TEXT NULL DEFAULT NULL,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_orders_table
FOREIGN KEY (table_id) REFERENCES tables (id)
ON DELETE SET NULL ON UPDATE CASCADE,
CONSTRAINT fk_orders_customer
FOREIGN KEY (customer_id) REFERENCES customers (id)
ON DELETE SET NULL ON UPDATE CASCADE,
CONSTRAINT fk_orders_server
FOREIGN KEY (server_id) REFERENCES users (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table customer_visits

CREATE TABLE IF NOT EXISTS customer_visits (
id INT NOT NULL AUTO_INCREMENT,
customer_id INT NULL DEFAULT NULL,
visit_date DATETIME NULL DEFAULT NULL,
order_id INT NULL DEFAULT NULL,
spend_amount DECIMAL(10,2) NULL DEFAULT NULL,
notes TEXT NULL DEFAULT NULL,
PRIMARY KEY (id),
CONSTRAINT fk_customer_visits_customer
FOREIGN KEY (customer_id) REFERENCES customers (id)
ON DELETE SET NULL ON UPDATE CASCADE,
CONSTRAINT fk_customer_visits_order
FOREIGN KEY (order_id) REFERENCES orders (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table dashboard_widgets

CREATE TABLE IF NOT EXISTS dashboard_widgets (
id INT NOT NULL AUTO_INCREMENT,
user_id INT NULL DEFAULT NULL,
widget_type VARCHAR(100) NULL DEFAULT NULL,
position_x INT NULL DEFAULT NULL,
position_y INT NULL DEFAULT NULL,
width INT NULL DEFAULT NULL,
height INT NULL DEFAULT NULL,
config_json JSON NULL DEFAULT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_dashboard_widgets_user
FOREIGN KEY (user_id) REFERENCES users (id)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table expenses

CREATE TABLE IF NOT EXISTS expenses (
id INT NOT NULL AUTO_INCREMENT,
name VARCHAR(100) NULL DEFAULT NULL,
expense_date DATE NULL DEFAULT NULL,
amount DECIMAL(15,2) NULL DEFAULT NULL,
description TEXT NULL DEFAULT NULL,
receipt_url VARCHAR(500) NULL DEFAULT NULL,
recorded_by INT NULL DEFAULT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_expenses_recorded_by
FOREIGN KEY (recorded_by) REFERENCES users (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table ingredients

CREATE TABLE IF NOT EXISTS ingredients (
id INT NOT NULL AUTO_INCREMENT,
name VARCHAR(200) NULL DEFAULT NULL,
unit VARCHAR(50) NULL DEFAULT NULL,
current_stock DECIMAL(10,3) NULL DEFAULT NULL,
low_stock_threshold DECIMAL(10,3) NULL DEFAULT NULL,
cost_per_unit DECIMAL(10,4) NULL DEFAULT NULL,
barcode VARCHAR(100) NULL DEFAULT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table inventory_alerts

CREATE TABLE IF NOT EXISTS inventory_alerts (
id INT NOT NULL AUTO_INCREMENT,
ingredient_id INT NULL DEFAULT NULL,
alert_type ENUM('low_stock','out_of_stock') NULL DEFAULT NULL,
triggered_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
resolved_at DATETIME NULL DEFAULT NULL,
notified_via VARCHAR(50) NULL DEFAULT NULL,
PRIMARY KEY (id),
CONSTRAINT fk_inventory_alerts_ingredient
FOREIGN KEY (ingredient_id) REFERENCES ingredients (id)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table purchase_orders
-- (declared before inventory_transactions which references it)

CREATE TABLE IF NOT EXISTS purchase_orders (
id INT NOT NULL AUTO_INCREMENT,
supplier_id INT NULL DEFAULT NULL,
po_number VARCHAR(50) NULL DEFAULT NULL,
status ENUM('draft','sent','confirmed','partially_received','received','cancelled') NULL DEFAULT 'draft',
total_amount DECIMAL(15,2) NULL DEFAULT NULL,
sent_at DATETIME NULL DEFAULT NULL,
expected_date DATE NULL DEFAULT NULL,
received_at DATETIME NULL DEFAULT NULL,
created_by INT NULL DEFAULT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table inventory_transactions

CREATE TABLE IF NOT EXISTS inventory_transactions (
id INT NOT NULL AUTO_INCREMENT,
ingredient_id INT NULL DEFAULT NULL,
transaction_type ENUM('deduction','restock','adjustment','waste','count') NULL DEFAULT NULL,
quantity DECIMAL(10,3) NULL DEFAULT NULL,
balance_after DECIMAL(10,3) NULL DEFAULT NULL,
order_id INT NULL DEFAULT NULL,
purchase_order_id INT NULL DEFAULT NULL,
performed_by INT NULL DEFAULT NULL,
notes TEXT NULL DEFAULT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_inv_tx_ingredient
FOREIGN KEY (ingredient_id) REFERENCES ingredients (id)
ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT fk_inv_tx_order
FOREIGN KEY (order_id) REFERENCES orders (id)
ON DELETE SET NULL ON UPDATE CASCADE,
CONSTRAINT fk_inv_tx_purchase_order
FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders (id)
ON DELETE SET NULL ON UPDATE CASCADE,
CONSTRAINT fk_inv_tx_performed_by
FOREIGN KEY (performed_by) REFERENCES users (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table menu_categories

CREATE TABLE IF NOT EXISTS menu_categories (
id INT NOT NULL AUTO_INCREMENT,
name VARCHAR(200) NULL DEFAULT NULL,
is_active TINYINT NOT NULL DEFAULT 1,
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table menu_items

CREATE TABLE IF NOT EXISTS menu_items (
id INT NOT NULL AUTO_INCREMENT,
category_id INT NULL DEFAULT NULL,
name VARCHAR(200) NULL DEFAULT NULL,
description TEXT NULL DEFAULT NULL,
is_available TINYINT NOT NULL DEFAULT 1,
image_url VARCHAR(500) NULL DEFAULT NULL,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_menu_items_category
FOREIGN KEY (category_id) REFERENCES menu_categories (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table portions

CREATE TABLE IF NOT EXISTS portions (
id INT NOT NULL AUTO_INCREMENT,
portion_name VARCHAR(100) NOT NULL,
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;


-- Table order_items
-- (declared before kds_order_items which references it)

CREATE TABLE IF NOT EXISTS order_items (
id INT NOT NULL AUTO_INCREMENT,
order_id INT NULL DEFAULT NULL,
menu_item_id INT NULL DEFAULT NULL,
portion_id INT NOT NULL,
quantity INT NOT NULL DEFAULT 1,
price DECIMAL(10,2) NULL DEFAULT NULL,
status ENUM('pending','fired','ready','served','voided') NOT NULL DEFAULT 'pending',
notes TEXT NULL DEFAULT NULL,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
-- ADD FKs as per script
CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_order_items_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items (id) ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT fk_order_items_portion FOREIGN KEY (portion_id) REFERENCES portions (id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table kds_orders

CREATE TABLE IF NOT EXISTS kds_orders (
id INT NOT NULL AUTO_INCREMENT,
order_id INT NULL DEFAULT NULL,
displayed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
bumped_at DATETIME NULL DEFAULT NULL,
bumped_by INT NULL DEFAULT NULL,
is_rush TINYINT NOT NULL DEFAULT 0,
is_vip TINYINT NOT NULL DEFAULT 0,
color_status ENUM('green','yellow','red') NOT NULL DEFAULT 'green',
PRIMARY KEY (id),
CONSTRAINT fk_kds_orders_order
FOREIGN KEY (order_id) REFERENCES orders (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_kds_orders_bumped_by
FOREIGN KEY (bumped_by) REFERENCES users (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table kds_order_items

CREATE TABLE IF NOT EXISTS kds_order_items (
id INT NOT NULL AUTO_INCREMENT,
kds_order_id INT NULL DEFAULT NULL,
order_item_id INT NULL DEFAULT NULL,
status ENUM('pending','in_progress','done') NOT NULL DEFAULT 'pending',
fired_at DATETIME NULL DEFAULT NULL,
completed_at DATETIME NULL DEFAULT NULL,
PRIMARY KEY (id),
CONSTRAINT fk_kds_order_items_kds_order
FOREIGN KEY (kds_order_id) REFERENCES kds_orders (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_kds_order_items_order_item
FOREIGN KEY (order_item_id) REFERENCES order_items (id)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table kitchen_order
-- (FK already existed in original DDL — preserved as-is)

CREATE TABLE IF NOT EXISTS kitchen_order (
id INT NOT NULL AUTO_INCREMENT,
order_id INT NOT NULL,
status ENUM('pending','in_progress','done','cancelled') NOT NULL DEFAULT 'pending',
get_time DATETIME NULL DEFAULT NULL,
end_time DATETIME NULL DEFAULT NULL,
created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
INDEX order_id (order_id ASC),
CONSTRAINT fk_kitchen_order_order
FOREIGN KEY (order_id) REFERENCES orders (id)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table order_sequence

CREATE TABLE IF NOT EXISTS order_sequence (
sequence_date DATE PRIMARY KEY,
last_sequence INT NOT NULL DEFAULT 0
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;



-- Table menu_item_price
-- (FKs already existed in original DDL — preserved as-is)

CREATE TABLE IF NOT EXISTS menu_item_price (
id INT NOT NULL AUTO_INCREMENT,
item_id INT NOT NULL,
portion_id INT NOT NULL,
price DECIMAL(10,2) NOT NULL,
is_active TINYINT NOT NULL DEFAULT 1,
PRIMARY KEY (id),
UNIQUE KEY uq_item_portion (item_id, portion_id),
CONSTRAINT fk_menu_item_price_item
FOREIGN KEY (item_id) REFERENCES menu_items (id)
ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT fk_menu_item_price_portion
FOREIGN KEY (portion_id) REFERENCES portions (id)
ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS loyalty_tiers (
id INT NOT NULL AUTO_INCREMENT,
tier_name VARCHAR(100) NULL DEFAULT NULL,
points_threshold INT NULL DEFAULT NULL,
discount_pct DECIMAL(5,2) NULL DEFAULT NULL,
free_item_id INT NULL DEFAULT NULL,
priority_seating TINYINT NULL DEFAULT '0',
sort_order INT NULL DEFAULT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_loyalty_tiers_free_item
FOREIGN KEY (free_item_id) REFERENCES menu_items (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table loyalty_accounts

CREATE TABLE IF NOT EXISTS loyalty_accounts (
id INT NOT NULL AUTO_INCREMENT,
customer_id INT NULL DEFAULT NULL,
points_balance INT NULL DEFAULT '0',
tier_id INT NULL DEFAULT NULL,
lifetime_points INT NULL DEFAULT '0',
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_loyalty_accounts_customer
FOREIGN KEY (customer_id) REFERENCES customers (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_loyalty_accounts_tier
FOREIGN KEY (tier_id) REFERENCES loyalty_tiers (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table loyalty_transactions

CREATE TABLE IF NOT EXISTS loyalty_transactions (
id INT NOT NULL AUTO_INCREMENT,
customer_id INT NULL DEFAULT NULL,
order_id INT NULL DEFAULT NULL,
transaction_type ENUM('earn','redeem','adjust','expire') NULL DEFAULT NULL,
points INT NULL DEFAULT NULL,
balance_after INT NULL DEFAULT NULL,
notes TEXT NULL DEFAULT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_loyalty_tx_customer
FOREIGN KEY (customer_id) REFERENCES customers (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_loyalty_tx_order
FOREIGN KEY (order_id) REFERENCES orders (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table waiter

CREATE TABLE IF NOT EXISTS waiter (
id INT NOT NULL AUTO_INCREMENT,
waiter_name VARCHAR(200) NOT NULL,
status ENUM('active','inactive','on_break') NULL DEFAULT 'active',
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table order_assignment
-- (FKs already existed in original DDL — preserved as-is)

CREATE TABLE IF NOT EXISTS order_assignment (
id INT NOT NULL AUTO_INCREMENT,
kitchen_order_id INT NOT NULL,
waiter_id INT NOT NULL,
assigned_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
INDEX kitchen_order_id (kitchen_order_id ASC),
INDEX waiter_id (waiter_id ASC),
CONSTRAINT order_assignment_ibfk_1
FOREIGN KEY (kitchen_order_id) REFERENCES kitchen_order (id),
CONSTRAINT order_assignment_ibfk_2
FOREIGN KEY (waiter_id) REFERENCES waiter (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table order_status_updates

CREATE TABLE IF NOT EXISTS order_status_updates (
id INT NOT NULL AUTO_INCREMENT,
order_id INT NOT NULL,
waiter_id INT NOT NULL,
status ENUM('served','unserved') NOT NULL,
updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
UNIQUE KEY uq_order_waiter (order_id, waiter_id),
CONSTRAINT fk_osu_order
FOREIGN KEY (order_id) REFERENCES orders (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_osu_waiter
FOREIGN KEY (waiter_id) REFERENCES waiter (id)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table order_discounts

CREATE TABLE IF NOT EXISTS order_discounts (
id INT NOT NULL AUTO_INCREMENT,
order_id INT NULL DEFAULT NULL,
order_item_id INT NULL DEFAULT NULL,
discount_type ENUM('percent','fixed','comp') NULL DEFAULT NULL,
discount_value DECIMAL(10,2) NULL DEFAULT NULL,
reason_code VARCHAR(100) NULL DEFAULT NULL,
applied_by INT NULL DEFAULT NULL,
manager_pin_used TINYINT NULL DEFAULT '0',
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_order_discounts_order
FOREIGN KEY (order_id) REFERENCES orders (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_order_discounts_order_item
FOREIGN KEY (order_item_id) REFERENCES order_items (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_order_discounts_applied_by
FOREIGN KEY (applied_by) REFERENCES users (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table payments

CREATE TABLE IF NOT EXISTS payments (
id INT NOT NULL AUTO_INCREMENT,
order_id INT NULL DEFAULT NULL,
payment_method VARCHAR(50) NULL DEFAULT NULL,
amount DECIMAL(10,2) NULL DEFAULT NULL,
tip_amount DECIMAL(10,2) NULL DEFAULT '0.00',
reference_number VARCHAR(100) NULL DEFAULT NULL,
processed_by INT NULL DEFAULT NULL,
processed_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_payments_order
FOREIGN KEY (order_id) REFERENCES orders (id)
ON DELETE RESTRICT ON UPDATE CASCADE,
CONSTRAINT fk_payments_processed_by
FOREIGN KEY (processed_by) REFERENCES users (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table suppliers
-- (declared before purchase_orders FKs are applied)

CREATE TABLE IF NOT EXISTS suppliers (
id INT NOT NULL AUTO_INCREMENT,
name VARCHAR(200) NULL DEFAULT NULL,
contact_name VARCHAR(200) NULL DEFAULT NULL,
email VARCHAR(255) NULL DEFAULT NULL,
phone VARCHAR(30) NULL DEFAULT NULL,
address TEXT NULL DEFAULT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Now that suppliers exists, add FKs on purchase_orders

ALTER TABLE purchase_orders
ADD CONSTRAINT fk_po_supplier
FOREIGN KEY (supplier_id) REFERENCES suppliers (id)
ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT fk_po_created_by
FOREIGN KEY (created_by) REFERENCES users (id)
ON DELETE SET NULL ON UPDATE CASCADE;

-- Table purchase_order_items

CREATE TABLE IF NOT EXISTS purchase_order_items (
id INT NOT NULL AUTO_INCREMENT,
po_id INT NULL DEFAULT NULL,
ingredient_id INT NULL DEFAULT NULL,
ordered_qty DECIMAL(10,3) NULL DEFAULT NULL,
received_qty DECIMAL(10,3) NULL DEFAULT '0.000',
unit_price DECIMAL(10,4) NULL DEFAULT NULL,
line_total DECIMAL(15,2) NULL DEFAULT NULL,
PRIMARY KEY (id),
CONSTRAINT fk_poi_purchase_order
FOREIGN KEY (po_id) REFERENCES purchase_orders (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_poi_ingredient
FOREIGN KEY (ingredient_id) REFERENCES ingredients (id)
ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table recipes

CREATE TABLE IF NOT EXISTS recipes (
id INT NOT NULL AUTO_INCREMENT,
menu_item_id INT NULL DEFAULT NULL,
version_number INT NULL DEFAULT '1',
is_current TINYINT NULL DEFAULT '1',
notes TEXT NULL DEFAULT NULL,
created_by INT NULL DEFAULT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_recipes_menu_item
FOREIGN KEY (menu_item_id) REFERENCES menu_items (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_recipes_created_by
FOREIGN KEY (created_by) REFERENCES users (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table recipe_ingredients

CREATE TABLE IF NOT EXISTS recipe_ingredients (
id INT NOT NULL AUTO_INCREMENT,
recipe_id INT NULL DEFAULT NULL,
ingredient_id INT NULL DEFAULT NULL,
quantity DECIMAL(10,4) NULL DEFAULT NULL,
unit VARCHAR(50) NULL DEFAULT NULL,
PRIMARY KEY (id),
CONSTRAINT fk_recipe_ingredients_recipe
FOREIGN KEY (recipe_id) REFERENCES recipes (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_recipe_ingredients_ingredient
FOREIGN KEY (ingredient_id) REFERENCES ingredients (id)
ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table reservations

CREATE TABLE IF NOT EXISTS reservations (
id INT NOT NULL AUTO_INCREMENT,
customer_id INT NULL DEFAULT NULL,
table_id INT NULL DEFAULT NULL,
party_size INT NULL DEFAULT NULL,
reservation_date DATE NULL DEFAULT NULL,
reservation_time TIME NULL DEFAULT NULL,
status ENUM('pending','confirmed','modified','cancelled','no_show','seated') NULL DEFAULT 'pending',
confirmation_code VARCHAR(20) NULL DEFAULT NULL,
reminder_24h_sent TINYINT NULL DEFAULT '0',
reminder_2h_sent TINYINT NULL DEFAULT '0',
notes TEXT NULL DEFAULT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_reservations_customer
FOREIGN KEY (customer_id) REFERENCES customers (id)
ON DELETE SET NULL ON UPDATE CASCADE,
CONSTRAINT fk_reservations_table
FOREIGN KEY (table_id) REFERENCES tables (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table sales_trends

CREATE TABLE IF NOT EXISTS sales_trends (
id INT NOT NULL AUTO_INCREMENT,
period_type ENUM('hourly','daily','weekly','monthly') NULL DEFAULT NULL,
period_start DATETIME NULL DEFAULT NULL,
period_end DATETIME NULL DEFAULT NULL,
total_revenue DECIMAL(15,2) NULL DEFAULT NULL,
total_orders INT NULL DEFAULT NULL,
avg_order_value DECIMAL(10,2) NULL DEFAULT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table stock_count_sessions

CREATE TABLE IF NOT EXISTS stock_count_sessions (
id INT NOT NULL AUTO_INCREMENT,
counted_by INT NULL DEFAULT NULL,
session_date DATE NULL DEFAULT NULL,
status ENUM('open','completed') NULL DEFAULT 'open',
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_scs_counted_by
FOREIGN KEY (counted_by) REFERENCES users (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table stock_count_items

CREATE TABLE IF NOT EXISTS stock_count_items (
id INT NOT NULL AUTO_INCREMENT,
session_id INT NULL DEFAULT NULL,
ingredient_id INT NULL DEFAULT NULL,
system_quantity DECIMAL(10,3) NULL DEFAULT NULL,
counted_quantity DECIMAL(10,3) NULL DEFAULT NULL,
variance DECIMAL(10,3) NULL DEFAULT NULL,
barcode_scanned TINYINT NULL DEFAULT '0',
PRIMARY KEY (id),
CONSTRAINT fk_sci_session
FOREIGN KEY (session_id) REFERENCES stock_count_sessions (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_sci_ingredient
FOREIGN KEY (ingredient_id) REFERENCES ingredients (id)
ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table supplier_ingredients

CREATE TABLE IF NOT EXISTS supplier_ingredients (
id INT NOT NULL AUTO_INCREMENT,
supplier_id INT NULL DEFAULT NULL,
ingredient_id INT NULL DEFAULT NULL,
supplier_sku VARCHAR(100) NULL DEFAULT NULL,
unit_price DECIMAL(10,4) NULL DEFAULT NULL,
min_order_qty DECIMAL(10,3) NULL DEFAULT NULL,
price_date DATE NULL DEFAULT NULL,
PRIMARY KEY (id),
CONSTRAINT fk_si_supplier
FOREIGN KEY (supplier_id) REFERENCES suppliers (id)
ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT fk_si_ingredient
FOREIGN KEY (ingredient_id) REFERENCES ingredients (id)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table waitlist

CREATE TABLE IF NOT EXISTS waitlist (
id INT NOT NULL AUTO_INCREMENT,
customer_name VARCHAR(200) NULL DEFAULT NULL,
phone VARCHAR(30) NULL DEFAULT NULL,
party_size INT NULL DEFAULT NULL,
added_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
estimated_wait_min INT NULL DEFAULT NULL,
notified_at DATETIME NULL DEFAULT NULL,
seated_at DATETIME NULL DEFAULT NULL,
status ENUM('waiting','notified','seated','left') NULL DEFAULT 'waiting',
PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table waste_logs

CREATE TABLE IF NOT EXISTS waste_logs (
id INT NOT NULL AUTO_INCREMENT,
ingredient_id INT NULL DEFAULT NULL,
menu_item_id INT NULL DEFAULT NULL,
quantity DECIMAL(10,3) NULL DEFAULT NULL,
unit VARCHAR(50) NULL DEFAULT NULL,
cost DECIMAL(10,2) NULL DEFAULT NULL,
reason_code VARCHAR(100) NULL DEFAULT NULL,
logged_by INT NULL DEFAULT NULL,
logged_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
CONSTRAINT fk_waste_logs_ingredient
FOREIGN KEY (ingredient_id) REFERENCES ingredients (id)
ON DELETE SET NULL ON UPDATE CASCADE,
CONSTRAINT fk_waste_logs_menu_item
FOREIGN KEY (menu_item_id) REFERENCES menu_items (id)
ON DELETE SET NULL ON UPDATE CASCADE,
CONSTRAINT fk_waste_logs_logged_by
FOREIGN KEY (logged_by) REFERENCES users (id)
ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Table marketing_campaigns (Added to resolve missing table used in code)
CREATE TABLE IF NOT EXISTS marketing_campaigns (
    id INT NOT NULL AUTO_INCREMENT,
    campaign_name VARCHAR(255) NULL,
    segment_id INT NULL,
    channel VARCHAR(50) NULL,
    subject VARCHAR(255) NULL,
    body_template TEXT NULL,
    ab_test_enabled BOOLEAN NULL,
    variant_b_body TEXT NULL,
    scheduled_at DATETIME NULL,
    sent_at DATETIME NULL,
    status VARCHAR(50) NULL,
    created_by INT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================================
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
-- =============================================================

