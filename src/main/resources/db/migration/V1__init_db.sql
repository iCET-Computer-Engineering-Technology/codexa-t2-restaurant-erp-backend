-- =============================================================
-- V1 — Full initial schema
-- Tables are ordered so every CREATE TABLE appears only after
-- all tables it references via foreign keys.
-- No SET FOREIGN_KEY_CHECKS tricks required.
-- =============================================================

-- ── No dependencies ──────────────────────────────────────────

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       full_name VARCHAR(200),
                       email VARCHAR(255),
                       role VARCHAR(100),
                       pin_hash VARCHAR(255),
                       is_active TINYINT DEFAULT 1,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE kpi_snapshots (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               snapshot_time DATETIME,
                               metric_key VARCHAR(100),
                               metric_value DECIMAL(15,4),
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sales_trends (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              period_type ENUM('hourly','daily','weekly','monthly'),
                              period_start DATETIME,
                              period_end DATETIME,
                              total_revenue DECIMAL(15,2),
                              total_orders INT,
                              avg_order_value DECIMAL(10,2),
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE demand_forecasts (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  forecast_date DATE,
                                  forecast_period ENUM('morning','afternoon','evening','full_day'),
                                  predicted_covers INT,
                                  predicted_revenue DECIMAL(15,2),
                                  confidence_score DECIMAL(5,2),
                                  model_version VARCHAR(50),
                                  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE revenue_reconciliations (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         reconciliation_date DATE,
                                         pos_total DECIMAL(15,2),
                                         system_total DECIMAL(15,2),
                                         discrepancy DECIMAL(15,2),
                                         status ENUM('matched','discrepancy','reviewed') DEFAULT 'matched',
                                         notes TEXT,
                                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE revenue_by_channel (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    channel ENUM('dine_in','takeout','delivery','online'),
                                    report_date DATE,
                                    total_revenue DECIMAL(15,2),
                                    order_count INT,
                                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pl_statements (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               period_month DATE,
                               revenue DECIMAL(15,2),
                               cogs DECIMAL(15,2),
                               gross_profit DECIMAL(15,2),
                               operating_expenses DECIMAL(15,2),
                               net_profit DECIMAL(15,2),
                               generated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               file_path VARCHAR(500)
);

CREATE TABLE cash_flow_statements (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      period_type ENUM('weekly','monthly'),
                                      period_start DATE,
                                      period_end DATE,
                                      operating_cashflow DECIMAL(15,2),
                                      investing_cashflow DECIMAL(15,2),
                                      financing_cashflow DECIMAL(15,2),
                                      net_cashflow DECIMAL(15,2),
                                      bank_connected TINYINT DEFAULT 0,
                                      generated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           first_name VARCHAR(100),
                           last_name VARCHAR(100),
                           email VARCHAR(255),
                           phone VARCHAR(30),
                           preferred_language VARCHAR(10) DEFAULT 'en',
                           dietary_notes TEXT,
                           communication_email TINYINT DEFAULT 1,
                           communication_sms TINYINT DEFAULT 1,
                           gdpr_deleted TINYINT DEFAULT 0,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE customer_segments (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   segment_name VARCHAR(100),
                                   criteria_json JSON,
                                   auto_segment TINYINT DEFAULT 1,
                                   created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE automated_messages (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    trigger_type ENUM('birthday','anniversary','lapsed','tier_change'),
                                    channel SET('email','sms'),
                                    template_body TEXT,
                                    offer_type ENUM('discount','free_item','none'),
                                    offer_value DECIMAL(10,2),
                                    send_days_before INT DEFAULT 1,
                                    is_active TINYINT DEFAULT 1,
                                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE floor_sections (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                section_name VARCHAR(100),
                                layout_json JSON,
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE kds_stations (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              station_name VARCHAR(100),
                              display_config JSON,
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ingredients (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(200),
                             unit VARCHAR(50),
                             current_stock DECIMAL(10,3),
                             low_stock_threshold DECIMAL(10,3),
                             cost_per_unit DECIMAL(10,4),
                             barcode VARCHAR(100),
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE suppliers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(200),
                           contact_name VARCHAR(200),
                           email VARCHAR(255),
                           phone VARCHAR(30),
                           address TEXT,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE menu_categories (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(200),
                                 sort_order INT DEFAULT 0,
                                 is_active TINYINT DEFAULT 1
);

CREATE TABLE modifier_groups (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(200),
                                 selection_type ENUM('single','multi'),
                                 is_required TINYINT DEFAULT 0
);

-- Self-referencing; parent_id is nullable so no chicken-and-egg problem
CREATE TABLE budget_categories (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   category_name VARCHAR(100),
                                   parent_id INT,
                                   created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                   CONSTRAINT fk_bc_parent FOREIGN KEY (parent_id) REFERENCES budget_categories(id)
);

-- ── Depend on: users ─────────────────────────────────────────

CREATE TABLE dashboard_widgets (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   user_id INT,
                                   widget_type VARCHAR(100),
                                   position_x INT,
                                   position_y INT,
                                   width INT,
                                   height INT,
                                   config_json JSON,
                                   created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                   updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   CONSTRAINT fk_dw_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE kpi_alerts (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            user_id INT,
                            metric_key VARCHAR(100),
                            threshold_value DECIMAL(15,4),
                            condition_type ENUM('above','below'),
                            notify_inapp TINYINT DEFAULT 1,
                            notify_sms TINYINT DEFAULT 0,
                            notify_email TINYINT DEFAULT 0,
                            is_active TINYINT DEFAULT 1,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_ka_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE custom_reports (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                created_by INT,
                                report_name VARCHAR(255),
                                selected_fields JSON,
                                filters_json JSON,
                                schedule_type ENUM('none','daily','weekly','monthly') DEFAULT 'none',
                                schedule_time TIME,
                                recipient_emails JSON,
                                export_format ENUM('pdf','csv','xlsx'),
                                last_run_at DATETIME,
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_cr_user FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE payment_type_summary (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      report_date DATE,
                                      payment_type VARCHAR(100),
                                      total_amount DECIMAL(15,2),
                                      transaction_count INT,
                                      manual_adjustment DECIMAL(15,2) DEFAULT 0,
                                      adjusted_by INT,
                                      adjustment_note TEXT,
                                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                      CONSTRAINT fk_pts_user FOREIGN KEY (adjusted_by) REFERENCES users(id)
);

CREATE TABLE financial_exports (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   exported_by INT,
                                   export_type ENUM('pl','cash_flow','balance_sheet'),
                                   export_format ENUM('csv','pdf','qbo','ofx'),
                                   accounting_target VARCHAR(50),
                                   file_path VARCHAR(500),
                                   created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                   CONSTRAINT fk_fe_user FOREIGN KEY (exported_by) REFERENCES users(id)
);

CREATE TABLE stock_count_sessions (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      counted_by INT,
                                      session_date DATE,
                                      status ENUM('open','completed') DEFAULT 'open',
                                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                      CONSTRAINT fk_scs_user FOREIGN KEY (counted_by) REFERENCES users(id)
);

-- ── Depend on: users + budget_categories ─────────────────────

CREATE TABLE budgets (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         category_id INT,
                         budget_month DATE,
                         budgeted_amount DECIMAL(15,2),
                         created_by INT,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         CONSTRAINT fk_b_cat FOREIGN KEY (category_id) REFERENCES budget_categories(id),
                         CONSTRAINT fk_b_user FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE expenses (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          category_id INT,
                          expense_date DATE,
                          amount DECIMAL(15,2),
                          description TEXT,
                          receipt_url VARCHAR(500),
                          recorded_by INT,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_exp_cat FOREIGN KEY (category_id) REFERENCES budget_categories(id),
                          CONSTRAINT fk_exp_user FOREIGN KEY (recorded_by) REFERENCES users(id)
);

-- ── Depend on: customers + customer_segments ─────────────────

CREATE TABLE customer_segment_members (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          segment_id INT,
                                          customer_id INT,
                                          assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                          CONSTRAINT fk_csm_segment FOREIGN KEY (segment_id) REFERENCES customer_segments(id),
                                          CONSTRAINT fk_csm_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- ── Depend on: floor_sections ────────────────────────────────

CREATE TABLE tables (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        section_id INT,
                        table_number VARCHAR(20),
                        capacity INT,
                        pos_x INT,
                        pos_y INT,
                        status ENUM('available','occupied','reserved','cleaning') DEFAULT 'available',
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        CONSTRAINT fk_t_section FOREIGN KEY (section_id) REFERENCES floor_sections(id)
);

-- ── No FK (walk-in guests may not have a customers record) ────

CREATE TABLE waitlist (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          customer_name VARCHAR(200),
                          phone VARCHAR(30),
                          party_size INT,
                          added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          estimated_wait_min INT,
                          notified_at DATETIME,
                          seated_at DATETIME,
                          status ENUM('waiting','notified','seated','left') DEFAULT 'waiting'
);

-- ── Depend on: suppliers + ingredients ───────────────────────

CREATE TABLE supplier_ingredients (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      supplier_id INT,
                                      ingredient_id INT,
                                      supplier_sku VARCHAR(100),
                                      unit_price DECIMAL(10,4),
                                      min_order_qty DECIMAL(10,3),
                                      price_date DATE,
                                      CONSTRAINT fk_si_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
                                      CONSTRAINT fk_si_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

CREATE TABLE purchase_orders (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 supplier_id INT,
                                 po_number VARCHAR(50),
                                 status ENUM('draft','sent','confirmed','partially_received','received','cancelled') DEFAULT 'draft',
                                 total_amount DECIMAL(15,2),
                                 sent_at DATETIME,
                                 expected_date DATE,
                                 received_at DATETIME,
                                 created_by INT,
                                 created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_po_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
                                 CONSTRAINT fk_po_user FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE inventory_alerts (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  ingredient_id INT,
                                  alert_type ENUM('low_stock','out_of_stock'),
                                  triggered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  resolved_at DATETIME,
                                  notified_via VARCHAR(50),
                                  CONSTRAINT fk_ia_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

CREATE TABLE stock_count_items (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   session_id INT,
                                   ingredient_id INT,
                                   system_quantity DECIMAL(10,3),
                                   counted_quantity DECIMAL(10,3),
                                   variance DECIMAL(10,3),
                                   barcode_scanned TINYINT DEFAULT 0,
                                   CONSTRAINT fk_sci_session FOREIGN KEY (session_id) REFERENCES stock_count_sessions(id),
                                   CONSTRAINT fk_sci_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- ── Depend on: menu_categories ───────────────────────────────

CREATE TABLE menu_items (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            category_id INT,
                            name VARCHAR(200),
                            description TEXT,
                            base_price DECIMAL(10,2),
                            current_price DECIMAL(10,2),
                            is_available TINYINT DEFAULT 1,
                            is_eightysixed TINYINT DEFAULT 0,
                            eightysixed_at DATETIME,
                            food_cost_pct DECIMAL(5,2),
                            image_url VARCHAR(500),
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            CONSTRAINT fk_mi_category FOREIGN KEY (category_id) REFERENCES menu_categories(id)
);

-- ── Depend on: modifier_groups ───────────────────────────────

CREATE TABLE modifiers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           group_id INT,
                           name VARCHAR(200),
                           price_adjustment DECIMAL(10,2) DEFAULT 0,
                           is_active TINYINT DEFAULT 1,
                           CONSTRAINT fk_mod_group FOREIGN KEY (group_id) REFERENCES modifier_groups(id)
);

-- ── Depend on: kpi_alerts ────────────────────────────────────

CREATE TABLE kpi_alert_logs (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                alert_id INT,
                                triggered_at DATETIME,
                                triggered_value DECIMAL(15,4),
                                channel VARCHAR(50),
                                delivered TINYINT DEFAULT 0,
                                CONSTRAINT fk_kal_alert FOREIGN KEY (alert_id) REFERENCES kpi_alerts(id)
);

-- ── Depend on: custom_reports + users ────────────────────────

CREATE TABLE report_exports (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                report_id INT,
                                exported_by INT,
                                export_format ENUM('pdf','csv','xlsx'),
                                file_path VARCHAR(500),
                                row_count INT,
                                export_duration_ms INT,
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_re_report FOREIGN KEY (report_id) REFERENCES custom_reports(id),
                                CONSTRAINT fk_re_user FOREIGN KEY (exported_by) REFERENCES users(id)
);

-- ── Depend on: menu_items ────────────────────────────────────

CREATE TABLE loyalty_tiers (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               tier_name VARCHAR(100),
                               points_threshold INT,
                               discount_pct DECIMAL(5,2),
                               free_item_id INT,
                               priority_seating TINYINT DEFAULT 0,
                               sort_order INT,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_lt_item FOREIGN KEY (free_item_id) REFERENCES menu_items(id)
);

CREATE TABLE menu_item_translations (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        menu_item_id INT,
                                        language_code VARCHAR(10),
                                        name VARCHAR(200),
                                        description TEXT,
                                        allergen_info TEXT,
                                        CONSTRAINT fk_mit_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

CREATE TABLE menu_item_modifier_groups (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           menu_item_id INT,
                                           modifier_group_id INT,
                                           sort_order INT DEFAULT 0,
                                           CONSTRAINT fk_mimg_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
                                           CONSTRAINT fk_mimg_modifier_group FOREIGN KEY (modifier_group_id) REFERENCES modifier_groups(id)
);

CREATE TABLE menu_analytics (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                menu_item_id INT,
                                report_date DATE,
                                units_sold INT,
                                revenue DECIMAL(15,2),
                                food_cost DECIMAL(15,2),
                                contribution_margin DECIMAL(15,2),
                                matrix_category ENUM('star','plowhorse','puzzle','dog'),
                                CONSTRAINT fk_ma_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

CREATE TABLE pricing_rules (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               menu_item_id INT,
                               rule_name VARCHAR(200),
                               rule_type ENUM('time_based','promo'),
                               price DECIMAL(10,2),
                               min_price_guard DECIMAL(10,2),
                               max_price_guard DECIMAL(10,2),
                               start_time TIME,
                               end_time TIME,
                               days_of_week VARCHAR(20),
                               promo_start_date DATE,
                               promo_end_date DATE,
                               is_active TINYINT DEFAULT 1,
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_pr_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

CREATE TABLE recipes (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         menu_item_id INT,
                         version_number INT DEFAULT 1,
                         is_current TINYINT DEFAULT 1,
                         notes TEXT,
                         created_by INT,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         CONSTRAINT fk_rec_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
                         CONSTRAINT fk_rec_user FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE waste_logs (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            ingredient_id INT,
                            menu_item_id INT,
                            quantity DECIMAL(10,3),
                            unit VARCHAR(50),
                            cost DECIMAL(10,2),
                            reason_code VARCHAR(100),
                            logged_by INT,
                            logged_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_wl_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id),
                            CONSTRAINT fk_wl_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
                            CONSTRAINT fk_wl_user FOREIGN KEY (logged_by) REFERENCES users(id)
);

-- ── Depend on: customers + tables + users ────────────────────

CREATE TABLE reservations (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              customer_id INT,
                              table_id INT,
                              party_size INT,
                              reservation_date DATE,
                              reservation_time TIME,
                              status ENUM('pending','confirmed','modified','cancelled','no_show','seated') DEFAULT 'pending',
                              confirmation_code VARCHAR(20),
                              reminder_24h_sent TINYINT DEFAULT 0,
                              reminder_2h_sent TINYINT DEFAULT 0,
                              notes TEXT,
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              CONSTRAINT fk_res_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
                              CONSTRAINT fk_res_table FOREIGN KEY (table_id) REFERENCES tables(id)
);

-- ── Depend on: tables + customers + users ────────────────────

CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        order_number VARCHAR(50),
                        order_type ENUM('dine_in','takeout','delivery','online'),
                        table_id INT,
                        customer_id INT,
                        server_id INT,
                        status ENUM('open','sent_to_kitchen','partially_ready','ready','paid','voided') DEFAULT 'open',
                        subtotal DECIMAL(10,2) DEFAULT 0,
                        discount_amount DECIMAL(10,2) DEFAULT 0,
                        tax_amount DECIMAL(10,2) DEFAULT 0,
                        total_amount DECIMAL(10,2) DEFAULT 0,
                        notes TEXT,
                        source VARCHAR(50),
                        external_order_id VARCHAR(100),
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        CONSTRAINT fk_o_table FOREIGN KEY (table_id) REFERENCES tables(id),
                        CONSTRAINT fk_o_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
                        CONSTRAINT fk_o_server FOREIGN KEY (server_id) REFERENCES users(id)
);

-- ── Depend on: purchase_orders + ingredients ─────────────────

CREATE TABLE purchase_order_items (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      po_id INT,
                                      ingredient_id INT,
                                      ordered_qty DECIMAL(10,3),
                                      received_qty DECIMAL(10,3) DEFAULT 0,
                                      unit_price DECIMAL(10,4),
                                      line_total DECIMAL(15,2),
                                      CONSTRAINT fk_poi_po FOREIGN KEY (po_id) REFERENCES purchase_orders(id),
                                      CONSTRAINT fk_poi_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- ── Depend on: recipes + ingredients ─────────────────────────

CREATE TABLE recipe_ingredients (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    recipe_id INT,
                                    ingredient_id INT,
                                    quantity DECIMAL(10,4),
                                    unit VARCHAR(50),
                                    CONSTRAINT fk_ri_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id),
                                    CONSTRAINT fk_ri_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
);

-- ── Depend on: menu_items + users ────────────────────────────

CREATE TABLE menu_change_log (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 menu_item_id INT,
                                 changed_by INT,
                                 change_type VARCHAR(100),
                                 old_value JSON,
                                 new_value JSON,
                                 changed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_mcl_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
                                 CONSTRAINT fk_mcl_user FOREIGN KEY (changed_by) REFERENCES users(id)
);

-- ── Depend on: kds_stations + orders ─────────────────────────

CREATE TABLE kds_orders (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            order_id INT,
                            station_id INT,
                            displayed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            bumped_at DATETIME,
                            bumped_by INT,
                            is_rush TINYINT DEFAULT 0,
                            is_vip TINYINT DEFAULT 0,
                            color_status ENUM('green','yellow','red') DEFAULT 'green',
                            ticket_time_sec INT,
                            CONSTRAINT fk_ko_order FOREIGN KEY (order_id) REFERENCES orders(id),
                            CONSTRAINT fk_ko_station FOREIGN KEY (station_id) REFERENCES kds_stations(id),
                            CONSTRAINT fk_ko_user FOREIGN KEY (bumped_by) REFERENCES users(id)
);

-- ── Depend on: kds_stations ──────────────────────────────────

CREATE TABLE kitchen_performance (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     station_id INT,
                                     report_date DATE,
                                     avg_ticket_time_sec INT,
                                     orders_completed INT,
                                     peak_hour TINYINT,
                                     report_file_path VARCHAR(500),
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     CONSTRAINT fk_kp_station FOREIGN KEY (station_id) REFERENCES kds_stations(id)
);

-- ── Depend on: customers + loyalty_tiers ─────────────────────

CREATE TABLE loyalty_accounts (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  customer_id INT,
                                  points_balance INT DEFAULT 0,
                                  tier_id INT,
                                  lifetime_points INT DEFAULT 0,
                                  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_la_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
                                  CONSTRAINT fk_la_tier FOREIGN KEY (tier_id) REFERENCES loyalty_tiers(id)
);

-- ── Depend on: customers + orders ────────────────────────────

CREATE TABLE loyalty_transactions (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      customer_id INT,
                                      order_id INT,
                                      transaction_type ENUM('earn','redeem','adjust','expire'),
                                      points INT,
                                      balance_after INT,
                                      notes TEXT,
                                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                      CONSTRAINT fk_ltr_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
                                      CONSTRAINT fk_ltr_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE customer_visits (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 customer_id INT,
                                 visit_date DATETIME,
                                 order_id INT,
                                 spend_amount DECIMAL(10,2),
                                 notes TEXT,
                                 CONSTRAINT fk_cv_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
                                 CONSTRAINT fk_cv_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- ── Depend on: customer_segments + users ─────────────────────

CREATE TABLE marketing_campaigns (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     campaign_name VARCHAR(255),
                                     segment_id INT,
                                     channel SET('email','sms'),
                                     subject VARCHAR(255),
                                     body_template TEXT,
                                     ab_test_enabled TINYINT DEFAULT 0,
                                     variant_b_body TEXT,
                                     scheduled_at DATETIME,
                                     sent_at DATETIME,
                                     status ENUM('draft','scheduled','sent','cancelled') DEFAULT 'draft',
                                     created_by INT,
                                     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                     CONSTRAINT fk_mc_segment FOREIGN KEY (segment_id) REFERENCES customer_segments(id),
                                     CONSTRAINT fk_mc_user FOREIGN KEY (created_by) REFERENCES users(id)
);

-- ── Depend on: ingredients + orders + purchase_orders + users ─

CREATE TABLE inventory_transactions (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        ingredient_id INT,
                                        transaction_type ENUM('deduction','restock','adjustment','waste','count'),
                                        quantity DECIMAL(10,3),
                                        balance_after DECIMAL(10,3),
                                        order_id INT,
                                        purchase_order_id INT,
                                        performed_by INT,
                                        notes TEXT,
                                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                        CONSTRAINT fk_it_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id),
                                        CONSTRAINT fk_it_order FOREIGN KEY (order_id) REFERENCES orders(id),
                                        CONSTRAINT fk_it_po FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),
                                        CONSTRAINT fk_it_user FOREIGN KEY (performed_by) REFERENCES users(id)
);

-- ── Depend on: orders + menu_items ───────────────────────────

CREATE TABLE order_items (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             order_id INT,
                             menu_item_id INT,
                             quantity INT DEFAULT 1,
                             unit_price DECIMAL(10,2),
                             modifier_total DECIMAL(10,2) DEFAULT 0,
                             line_total DECIMAL(10,2),
                             course_number INT DEFAULT 1,
                             status ENUM('pending','fired','ready','served','voided') DEFAULT 'pending',
                             notes TEXT,
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT fk_oi_order FOREIGN KEY (order_id) REFERENCES orders(id),
                             CONSTRAINT fk_oi_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

-- ── Depend on: orders + users ────────────────────────────────

CREATE TABLE payments (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          order_id INT,
                          payment_method VARCHAR(50),
                          amount DECIMAL(10,2),
                          tip_amount DECIMAL(10,2) DEFAULT 0,
                          reference_number VARCHAR(100),
                          processed_by INT,
                          processed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_pay_order FOREIGN KEY (order_id) REFERENCES orders(id),
                          CONSTRAINT fk_pay_user FOREIGN KEY (processed_by) REFERENCES users(id)
);

CREATE TABLE split_bills (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             order_id INT,
                             split_type ENUM('equal','by_item'),
                             total_splits INT,
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT fk_sb_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE delivery_platform_orders (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          order_id INT,
                                          platform VARCHAR(50),
                                          platform_order_id VARCHAR(100),
                                          driver_name VARCHAR(200),
                                          driver_phone VARCHAR(30),
                                          status_pushed_at DATETIME,
                                          last_sync_at DATETIME,
                                          CONSTRAINT fk_dpo_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- ── Depend on: marketing_campaigns + customers ───────────────

CREATE TABLE campaign_analytics (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    campaign_id INT,
                                    customer_id INT,
                                    sent_at DATETIME,
                                    opened_at DATETIME,
                                    clicked_at DATETIME,
                                    converted_at DATETIME,
                                    unsubscribed_at DATETIME,
                                    variant CHAR(1),
                                    CONSTRAINT fk_ca_campaign FOREIGN KEY (campaign_id) REFERENCES marketing_campaigns(id),
                                    CONSTRAINT fk_ca_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- ── Depend on: order_items + modifiers ───────────────────────

CREATE TABLE order_item_modifiers (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      order_item_id INT,
                                      modifier_id INT,
                                      modifier_name VARCHAR(200),
                                      price_adjustment DECIMAL(10,2) DEFAULT 0,
                                      CONSTRAINT fk_oim_order_item FOREIGN KEY (order_item_id) REFERENCES order_items(id),
                                      CONSTRAINT fk_oim_modifier FOREIGN KEY (modifier_id) REFERENCES modifiers(id)
);

-- ── Depend on: orders + order_items + users ──────────────────

CREATE TABLE order_discounts (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 order_id INT,
                                 order_item_id INT,
                                 discount_type ENUM('percent','fixed','comp'),
                                 discount_value DECIMAL(10,2),
                                 reason_code VARCHAR(100),
                                 applied_by INT,
                                 manager_pin_used TINYINT DEFAULT 0,
                                 created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_od_order FOREIGN KEY (order_id) REFERENCES orders(id),
                                 CONSTRAINT fk_od_order_item FOREIGN KEY (order_item_id) REFERENCES order_items(id),
                                 CONSTRAINT fk_od_user FOREIGN KEY (applied_by) REFERENCES users(id)
);

-- ── Depend on: split_bills + order_items + payments ──────────

CREATE TABLE split_bill_items (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  split_bill_id INT,
                                  split_index INT,
                                  order_item_id INT,
                                  amount DECIMAL(10,2),
                                  payment_id INT,
                                  CONSTRAINT fk_sbi_split_bill FOREIGN KEY (split_bill_id) REFERENCES split_bills(id),
                                  CONSTRAINT fk_sbi_order_item FOREIGN KEY (order_item_id) REFERENCES order_items(id),
                                  CONSTRAINT fk_sbi_payment FOREIGN KEY (payment_id) REFERENCES payments(id)
);

-- ── Depend on: kds_orders + order_items + kds_stations ───────

CREATE TABLE kds_order_items (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 kds_order_id INT,
                                 order_item_id INT,
                                 station_id INT,
                                 status ENUM('pending','in_progress','done') DEFAULT 'pending',
                                 fired_at DATETIME,
                                 completed_at DATETIME,
                                 CONSTRAINT fk_koi_kds_order FOREIGN KEY (kds_order_id) REFERENCES kds_orders(id),
                                 CONSTRAINT fk_koi_order_item FOREIGN KEY (order_item_id) REFERENCES order_items(id),
                                 CONSTRAINT fk_koi_station FOREIGN KEY (station_id) REFERENCES kds_stations(id)
);
