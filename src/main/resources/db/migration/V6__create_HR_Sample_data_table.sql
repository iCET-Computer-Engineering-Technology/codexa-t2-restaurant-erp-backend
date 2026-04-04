-- ============================================================
-- SAMPLE DATA FOR PAYROLL MANAGEMENT SYSTEM
-- ============================================================


-- ============================================================
-- CREATE HR TABLES
-- Ensure HR schema exists before loading HR sample data.
CREATE TABLE IF NOT EXISTS employee (
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
user_id INT NULL UNIQUE,
first_name VARCHAR(100) NOT NULL,
last_name VARCHAR(100) NOT NULL,
email VARCHAR(255) NULL,
phone VARCHAR(30) NULL,
basic_salary DOUBLE NOT NULL,
allowance DOUBLE DEFAULT 0,
bonus DOUBLE DEFAULT 0,
donation DOUBLE DEFAULT 0,
department VARCHAR(100) NULL,
designation VARCHAR(100) NULL,
status VARCHAR(20) DEFAULT 'ACTIVE',
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user_id)
REFERENCES users (id)
ON DELETE SET NULL
ON UPDATE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS payroll (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT NOT NULL,
basic_salary DOUBLE NOT NULL,
epf_employee DOUBLE DEFAULT 0,
epf_employer DOUBLE DEFAULT 0,
etf_employer DOUBLE DEFAULT 0,
allowance DOUBLE DEFAULT 0,
bonus DOUBLE DEFAULT 0,
donation DOUBLE DEFAULT 0,
overtime_amount DOUBLE DEFAULT 0,
leave_days INT DEFAULT 0,
leave_deduction DOUBLE DEFAULT 0,
total_deduction DOUBLE DEFAULT 0,
net_salary DOUBLE DEFAULT 0,
employer_cost DOUBLE DEFAULT 0,
payroll_date DATE NOT NULL,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS deduction (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT NOT NULL,
type VARCHAR(50),
amount DOUBLE DEFAULT 0,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS allowance (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT NOT NULL,
type VARCHAR(50),
amount DOUBLE DEFAULT 0,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS employee_leave (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT NOT NULL,
leave_type VARCHAR(50),
start_date DATE NOT NULL,
end_date DATE NOT NULL,
total_days INT DEFAULT 0,
status VARCHAR(20) DEFAULT 'PENDING',
reason TEXT,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS overtime (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT NOT NULL,
date DATE NOT NULL,
hours DOUBLE DEFAULT 0,
rate_per_hour DOUBLE DEFAULT 0,
total_amount DOUBLE DEFAULT 0,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS payroll_config (
id INT AUTO_INCREMENT PRIMARY KEY,
epf_employee_rate DOUBLE DEFAULT 0.08,
epf_employer_rate DOUBLE DEFAULT 0.12,
etf_rate DOUBLE DEFAULT 0.03,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS basic_salary (
id INT AUTO_INCREMENT PRIMARY KEY,
role VARCHAR(100) NOT NULL,
amount DOUBLE DEFAULT 0,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS salary_request (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT,
basic_salary DOUBLE,
donation DOUBLE,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS salary_response (
id INT AUTO_INCREMENT PRIMARY KEY,
basic_salary DOUBLE,
epf_employee DOUBLE,
epf_employer DOUBLE,
etf_employer DOUBLE,
donation DOUBLE,
total_deduction DOUBLE,
net_salary DOUBLE,
employer_cost DOUBLE,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bonus (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT NOT NULL,
amount DOUBLE DEFAULT 0,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- ============================================================
-- 2. PAYROLL CONFIG
-- ============================================================
INSERT INTO payroll_config (epf_employee_rate, epf_employer_rate, etf_rate) VALUES
    (0.08, 0.12, 0.03);

-- ============================================================
-- 3. BASIC SALARY (by role/designation)
-- ============================================================
INSERT INTO basic_salary (role, amount) VALUES
('ROLE_ADMIN',    95000.00),
('ROLE_WAITER',   55000.00),
('ROLE_CHEF',     75000.00),
('ROLE_CASHIER',  60000.00),
('ROLE_MANAGER', 110000.00);

-- ============================================================
-- 4. EMPLOYEES
-- user_id maps to users: 1=admin, 2=waiter, 3=chef, 4=cashier, 5=manager
-- ============================================================
INSERT INTO employee (id, user_id, first_name, last_name, email, phone, basic_salary, allowance, bonus, donation, department, designation, status) VALUES
(1, 1, 'Ashan',   'Perera',      'admin@restaurant.com',   '0711234501', 95000.00,  18000.00, 12000.00, 2000.00, 'Administration', 'Admin',   'ACTIVE'),
(2, 2, 'Nuwan',   'Fernando',    'waiter@restaurant.com',  '0722345602', 55000.00,  10000.00,  5000.00, 1000.00, 'Floor',          'Waiter',  'ACTIVE'),
(3, 3, 'Kasun',   'Silva',       'chef@restaurant.com',    '0733456703', 75000.00,  14000.00,  9000.00, 1500.00, 'Kitchen',        'Chef',    'ACTIVE'),
(4, 4, 'Dilini',  'Jayawardena', 'cashier@restaurant.com', '0744567804', 60000.00,  11000.00,  6000.00, 1200.00, 'Finance',        'Cashier', 'ACTIVE'),
(5, 5, 'Sampath', 'Bandara',     'manager@restaurant.com', '0755678905', 110000.00, 20000.00, 15000.00, 3000.00, 'Management',     'Manager', 'ACTIVE');

-- ============================================================
-- 5. ALLOWANCES (Transport / Meal / Other per employee)
-- ============================================================
INSERT INTO allowance (employee_id, type, amount) VALUES
-- Emp 1: Ashan (Admin)
(1, 'Transport', 6000.00),
(1, 'Meal',      8000.00),
(1, 'Other',     4000.00),
-- Emp 2: Nuwan (Waiter)
(2, 'Transport', 3500.00),
(2, 'Meal',      5000.00),
(2, 'Other',     1500.00),
-- Emp 3: Kasun (Chef)
(3, 'Transport', 4500.00),
(3, 'Meal',      6500.00),
(3, 'Other',     3000.00),
-- Emp 4: Dilini (Cashier)
(4, 'Transport', 3800.00),
(4, 'Meal',      5500.00),
(4, 'Other',     1700.00),
-- Emp 5: Sampath (Manager)
(5, 'Transport', 7000.00),
(5, 'Meal',      9000.00),
(5, 'Other',     4000.00);

-- ============================================================
-- 6. BONUS (per employee)
-- ============================================================
INSERT INTO bonus (employee_id, amount) VALUES
(1, 12000.00),
(2,  5000.00),
(3,  9000.00),
(4,  6000.00),
(5, 15000.00);

-- ============================================================
-- 7. DEDUCTIONS (EPF / TAX / LOAN / DONATION)
-- EPF Employee = basic_salary * 0.08
-- ============================================================
INSERT INTO deduction (employee_id, type, amount) VALUES
-- Emp 1: Ashan (Admin) | EPF = 95000 * 0.08 = 7600
(1, 'EPF',      7600.00),
(1, 'TAX',      3500.00),
(1, 'DONATION', 2000.00),
-- Emp 2: Nuwan (Waiter) | EPF = 55000 * 0.08 = 4400
(2, 'EPF',      4400.00),
(2, 'LOAN',     2000.00),
(2, 'DONATION', 1000.00),
-- Emp 3: Kasun (Chef) | EPF = 75000 * 0.08 = 6000
(3, 'EPF',      6000.00),
(3, 'LOAN',     3000.00),
(3, 'DONATION', 1500.00),
-- Emp 4: Dilini (Cashier) | EPF = 60000 * 0.08 = 4800
(4, 'EPF',      4800.00),
(4, 'LOAN',     2500.00),
(4, 'DONATION', 1200.00),
-- Emp 5: Sampath (Manager) | EPF = 110000 * 0.08 = 8800
(5, 'EPF',      8800.00),
(5, 'TAX',      4500.00),
(5, 'DONATION', 3000.00);

-- ============================================================
-- 8. EMPLOYEE LEAVE
-- ============================================================
INSERT INTO employee_leave (employee_id, leave_type, start_date, end_date, total_days, status, reason) VALUES
(1, 'ANNUAL',  '2025-01-06', '2025-01-08', 3, 'APPROVED', 'Family vacation'),
(1, 'CASUAL',  '2025-02-14', '2025-02-14', 1, 'APPROVED', 'Personal work'),
(2, 'SICK',    '2025-01-13', '2025-01-14', 2, 'APPROVED', 'Fever and cold'),
(2, 'CASUAL',  '2025-02-28', '2025-02-28', 1, 'APPROVED', 'Bank work'),
(3, 'ANNUAL',  '2025-01-20', '2025-01-22', 3, 'APPROVED', 'Rest days'),
(3, 'SICK',    '2025-03-05', '2025-03-05', 1, 'APPROVED', 'Headache'),
(4, 'CASUAL',  '2025-01-17', '2025-01-17', 1, 'APPROVED', 'Government office'),
(4, 'ANNUAL',  '2025-03-10', '2025-03-12', 3, 'PENDING',  'Short trip'),
(5, 'SICK',    '2025-01-09', '2025-01-09', 1, 'APPROVED', 'Medical checkup'),
(5, 'ANNUAL',  '2025-02-17', '2025-02-19', 3, 'APPROVED', 'Annual leave');

-- ============================================================
-- 9. OVERTIME
-- rate_per_hour = basic_salary / (26 days * 8 hrs)
-- ============================================================
INSERT INTO overtime (employee_id, date, hours, rate_per_hour, total_amount) VALUES
-- Emp 1: Ashan | rate = 95000 / 208 = 456.73
(1, '2025-01-15', 3.0, 457.00, 1371.00),
(1, '2025-01-27', 2.0, 457.00,  914.00),
-- Emp 2: Nuwan | rate = 55000 / 208 = 264.42
(2, '2025-01-11', 4.0, 264.00, 1056.00),
(2, '2025-01-25', 3.0, 264.00,  792.00),
-- Emp 3: Kasun | rate = 75000 / 208 = 360.58
(3, '2025-01-18', 5.0, 361.00, 1805.00),
(3, '2025-01-30', 3.5, 361.00, 1263.50),
-- Emp 4: Dilini | rate = 60000 / 208 = 288.46
(4, '2025-01-16', 2.0, 288.00,  576.00),
(4, '2025-01-29', 3.0, 288.00,  864.00),
-- Emp 5: Sampath | rate = 110000 / 208 = 528.85
(5, '2025-01-10', 4.0, 529.00, 2116.00),
(5, '2025-01-28', 2.5, 529.00, 1322.50);

-- ============================================================
-- 10. SALARY REQUESTS
-- ============================================================
INSERT INTO salary_request (employee_id, basic_salary, donation) VALUES
      (1,  95000.00, 2000.00),
      (2,  55000.00, 1000.00),
      (3,  75000.00, 1500.00),
      (4,  60000.00, 1200.00),
      (5, 110000.00, 3000.00);

-- ============================================================
-- 11. SALARY RESPONSES
-- EPF Employee  = basic_salary * 0.08
-- EPF Employer  = basic_salary * 0.12
-- ETF Employer  = basic_salary * 0.03
-- Total Deduction = EPF Employee + Donation
-- Net Salary    = basic_salary - total_deduction
-- Employer Cost = basic_salary + EPF Employer + ETF Employer
-- ============================================================
INSERT INTO salary_response (basic_salary, epf_employee, epf_employer, etf_employer, donation, total_deduction, net_salary, employer_cost) VALUES
-- Emp 1: Ashan (Admin)
(95000.00,   7600.00, 11400.00, 2850.00, 2000.00,  9600.00,  85400.00, 109250.00),
-- Emp 2: Nuwan (Waiter)
(55000.00,   4400.00,  6600.00, 1650.00, 1000.00,  5400.00,  49600.00,  63250.00),
-- Emp 3: Kasun (Chef)
(75000.00,   6000.00,  9000.00, 2250.00, 1500.00,  7500.00,  67500.00,  86250.00),
-- Emp 4: Dilini (Cashier)
(60000.00,   4800.00,  7200.00, 1800.00, 1200.00,  6000.00,  54000.00,  69000.00),
-- Emp 5: Sampath (Manager)
(110000.00,  8800.00, 13200.00, 3300.00, 3000.00, 11800.00,  98200.00, 126500.00);

-- ============================================================
-- 12. PAYROLL — January 2025
-- Daily rate  = basic_salary / 26
-- Leave deduction = leave_days * daily_rate
-- Net Salary  = basic_salary + allowance + bonus + overtime_amount
--             - epf_employee - leave_deduction - donation
-- Employer Cost = basic_salary + epf_employer + etf_employer
-- ============================================================
INSERT INTO payroll (employee_id, basic_salary, epf_employee, epf_employer, etf_employer, allowance, bonus, donation, overtime_amount, leave_days, leave_deduction, total_deduction, net_salary, employer_cost, payroll_date) VALUES
-- Emp 1: Ashan | Leave 4d | daily=3653.85 | leave_ded=14615.38 | OT=2285
(1,  95000.00, 7600.00, 11400.00, 2850.00, 18000.00, 12000.00, 2000.00,  2285.00, 4, 14615.38, 24215.38, 100069.62, 109250.00, '2025-01-31'),
-- Emp 2: Nuwan | Leave 2d | daily=2115.38 | leave_ded=4230.77 | OT=1848
(2,  55000.00, 4400.00,  6600.00, 1650.00, 10000.00,  5000.00, 1000.00,  1848.00, 2,  4230.77,  9630.77,  62017.23,  63250.00, '2025-01-31'),
-- Emp 3: Kasun | Leave 4d | daily=2884.62 | leave_ded=11538.46 | OT=3068.50
(3,  75000.00, 6000.00,  9000.00, 2250.00, 14000.00,  9000.00, 1500.00,  3068.50, 4, 11538.46, 19038.46,  80530.04,  86250.00, '2025-01-31'),
-- Emp 4: Dilini | Leave 1d | daily=2307.69 | leave_ded=2307.69 | OT=1440
(4,  60000.00, 4800.00,  7200.00, 1800.00, 11000.00,  6000.00, 1200.00,  1440.00, 1,  2307.69,  8307.69,  68132.31,  69000.00, '2025-01-31'),
-- Emp 5: Sampath | Leave 4d | daily=4230.77 | leave_ded=16923.08 | OT=3438.50
(5, 110000.00, 8800.00, 13200.00, 3300.00, 20000.00, 15000.00, 3000.00,  3438.50, 4, 16923.08, 28723.08, 119715.42, 126500.00, '2025-01-31');
