INSERT INTO employee
(id, user_id, first_name, last_name, email, phone, basic_salary, allowance, bonus, donation, department, designation)
VALUES
(1, 1, 'John', 'Doe', 'john@example.com', '0771234567', 50000, 5000, 2000, 1000, 'IT', 'Software Engineer'),
(2, 2, 'Jane', 'Smith', 'jane@example.com', '0779876543', 80000, 8000, 5000, 2000, 'HR', 'Manager');

INSERT INTO payroll_config (id, epf_employee_rate, epf_employer_rate, etf_rate)
VALUES (1, 0.08, 0.12, 0.03);

INSERT INTO allowance (employee_id, type, amount) VALUES
(1, 'Transport', 3000),
(1, 'Meal', 2000),
(2, 'Transport', 4000);

INSERT INTO deduction (employee_id, type, amount) VALUES
(1, 'EPF', 4000),
(1, 'LOAN', 2000),
(2, 'TAX', 5000);

INSERT INTO overtime (employee_id, date, hours, rate_per_hour, total_amount) VALUES
(1, '2026-03-01', 10, 500, 5000),
(2, '2026-03-02', 5, 800, 4000);

INSERT INTO employee_leave
(employee_id, leave_type, start_date, end_date, total_days, status, reason)
VALUES
(1, 'ANNUAL', '2026-03-10', '2026-03-12', 3, 'APPROVED', 'Vacation'),
(2, 'SICK', '2026-03-05', '2026-03-06', 2, 'APPROVED', 'Fever');

INSERT INTO bonus (employee_id, amount) VALUES
(1, 3000),
(2, 5000);

INSERT INTO basic_salary (role, amount) VALUES
('Software Engineer', 50000),
('Manager', 80000);

INSERT INTO salary_request (employee_id, basic_salary, donation)
VALUES
(1, 50000, 1000),
(2, 80000, 2000);

INSERT INTO salary_response
(basic_salary, epf_employee, epf_employer, etf_employer, donation, total_deduction, net_salary, employer_cost)
VALUES
(50000, 4000, 6000, 1500, 1000, 5000, 57000, 61500),
(80000, 6400, 9600, 2400, 2000, 10400, 86600, 92000);

INSERT INTO payroll (
employee_id, basic_salary, epf_employee, epf_employer, etf_employer,
allowance, bonus, donation, overtime_amount, leave_days,
leave_deduction, total_deduction, net_salary, employer_cost, payroll_date
)
VALUES
(1, 50000, 4000, 6000, 1500, 5000, 2000, 1000, 5000, 3, 1500, 6500, 55500, 61500, '2026-03-31'),
(2, 80000, 6400, 9600, 2400, 8000, 5000, 2000, 4000, 2, 2000, 10400, 86600, 92000, '2026-03-31');
