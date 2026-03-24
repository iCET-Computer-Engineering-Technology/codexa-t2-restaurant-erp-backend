-- Table employee
CREATE TABLE employee (
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

-- Table payroll

CREATE TABLE payroll (
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

-- Table deduction

CREATE TABLE deduction (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT NOT NULL,
type VARCHAR(50), -- EPF / TAX / LOAN / DONATION
amount DOUBLE DEFAULT 0,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- Table allowance

CREATE TABLE allowance (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT NOT NULL,
type VARCHAR(50), -- Transport / Meal / Other
amount DOUBLE DEFAULT 0,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- Table employee leave

CREATE TABLE employee_leave (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT NOT NULL,
leave_type VARCHAR(50), -- ANNUAL / CASUAL / SICK
start_date DATE NOT NULL,
end_date DATE NOT NULL,
total_days INT DEFAULT 0,
status VARCHAR(20) DEFAULT 'PENDING', -- PENDING / APPROVED / REJECTED
reason TEXT,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- Table overtime

CREATE TABLE overtime (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT NOT NULL,
date DATE NOT NULL,
hours DOUBLE DEFAULT 0,
rate_per_hour DOUBLE DEFAULT 0,
total_amount DOUBLE DEFAULT 0,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- Table payroll config

CREATE TABLE payroll_config (
id INT AUTO_INCREMENT PRIMARY KEY,
epf_employee_rate DOUBLE DEFAULT 0.08,
epf_employer_rate DOUBLE DEFAULT 0.12,
etf_rate DOUBLE DEFAULT 0.03,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table basic salary

CREATE TABLE basic_salary (
id INT AUTO_INCREMENT PRIMARY KEY,
role VARCHAR(100) NOT NULL,
amount DOUBLE DEFAULT 0,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table salary request

CREATE TABLE salary_request (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT,
basic_salary DOUBLE,
donation DOUBLE,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- Table salary response

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

-- Table bonus

CREATE TABLE bonus (
id INT AUTO_INCREMENT PRIMARY KEY,
employee_id INT NOT NULL,
amount DOUBLE DEFAULT 0,
created_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (employee_id) REFERENCES employee(id)
);




