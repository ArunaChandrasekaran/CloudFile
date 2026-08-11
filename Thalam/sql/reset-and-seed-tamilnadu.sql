-- Thalam: wipe business data, restart SERIAL ids, seed Tamil Nadu sample data (English text)
-- Keeps OnboardingDetails (login) intact.
-- Run in pgAdmin / psql against database: Thalam

BEGIN;

-- Keep login; unlink any employee reference
UPDATE OnboardingDetails SET employee_id = NULL;

TRUNCATE TABLE
  DailyWorklogMaterials,
  Expenses,
  PurchaseItems,
  DailyWorklogs,
  Purchases,
  Invoices,
  ProjectsAssociatedEmployees,
  Projects,
  Materials,
  Vendors,
  Employees,
  Clients,
  Roles
RESTART IDENTITY CASCADE;

-- ========== Roles ==========
INSERT INTO Roles (name, description) VALUES
  ('Site Engineer', 'Construction site engineer'),
  ('Supervisor', 'Work supervisor'),
  ('Accountant', 'Accounts and billing'),
  ('Mason Lead', 'Masonry team lead'),
  ('Store Keeper', 'Materials store keeper');

-- ========== Employees (Tamil Nadu) ==========
INSERT INTO Employees (name, phone, email, address, role_id) VALUES
  ('Murugan K', '9876543210', 'murugan@thalam.in', 'Anna Nagar, Chennai - 600040', 1),
  ('Kavitha R', '9876543211', 'kavitha@thalam.in', 'RS Puram, Coimbatore - 641002', 2),
  ('Rajesh M', '9876543212', 'rajesh@thalam.in', 'KK Nagar, Madurai - 625020', 3),
  ('Sundar P', '9876543213', 'sundar@thalam.in', 'Thennur, Trichy - 620017', 4),
  ('Anitha S', '9876543214', 'anitha@thalam.in', 'Hasthampatti, Salem - 636007', 5),
  ('Karthik V', '9876543215', 'karthik@thalam.in', 'Perur, Erode - 638001', 1),
  ('Megala T', '9876543216', 'megala@thalam.in', 'Palayamkottai, Tirunelveli - 627002', 2),
  ('Prabhakaran N', '9876543217', 'prabhu@thalam.in', 'Sattur, Virudhunagar - 626203', 4);

-- ========== Clients ==========
INSERT INTO Clients (Name, Phone, Email, AltPhone, Address) VALUES
  ('Chandrasekar R', '9443311001', 'chandru@client.in', '9443311002', 'T Nagar, Chennai - 600017'),
  ('Lakshmi Narayanan', '9443311003', 'lakshmi@client.in', '9443311004', 'Saibaba Colony, Coimbatore - 641011'),
  ('Venkatesh Kumar', '9443311005', 'venkat@client.in', NULL, 'Goripalayam, Madurai - 625001'),
  ('Thenmozhi P', '9443311006', 'thenmozhi@client.in', '9443311007', 'Srirangam, Trichy - 620006'),
  ('Arunachalam S', '9443311008', 'arunachalam@client.in', NULL, 'Alagapuram, Salem - 636501');

-- ========== Projects ==========
INSERT INTO Projects (name, client_id, startdate, enddate, address, contractamount, notes, status) VALUES
  ('Balaji Apartments', 1, '2026-01-15', '2026-12-31', 'Velachery, Chennai', 8500000, 'G+4 apartment', 'Ongoing'),
  ('Green Valley Villa', 2, '2026-03-01', '2026-10-15', 'Vadavalli, Coimbatore', 4200000, 'Independent villa', 'Ongoing'),
  ('Meenakshi Nagar Homes', 3, '2025-08-01', '2026-06-30', 'Thiruparankundram, Madurai', 6100000, 'Twin house', 'Late'),
  ('Kaveri Residency', 4, '2026-05-01', '2027-02-28', 'Kajanamalai, Trichy', 7300000, 'Row houses', 'Not yet started'),
  ('Selvam Complex', 5, '2025-06-01', '2026-04-30', 'Hasthampatti, Salem', 3900000, 'Completed project', 'Completed');

INSERT INTO ProjectsAssociatedEmployees (project_id, employee_id) VALUES
  (1, 1), (1, 2), (1, 4),
  (2, 6), (2, 5),
  (3, 7), (3, 8),
  (4, 1), (4, 3),
  (5, 2), (5, 4);

-- ========== Vendors ==========
INSERT INTO Vendors (Name, Phone, Email, AltPhone, Address) VALUES
  ('Ramco Cements', '9000010001', 'ramco@vendor.in', '9000010002', 'Alangulam, Virudhunagar'),
  ('Thyagaraja Steels', '9000010003', 'thyagaraja@vendor.in', NULL, 'Courtallam Road, Tirunelveli'),
  ('Chennai Sand Supply', '9000010004', 'chennaisand@vendor.in', '9000010005', 'Ennore, Chennai'),
  ('Kovai Bricks', '9000010006', 'kovaibricks@vendor.in', NULL, 'Pollachi, Coimbatore'),
  ('Madurai TMT Store', '9000010007', 'maduraitmt@vendor.in', '9000010008', 'Mattuthardani, Madurai');

-- ========== Materials ==========
INSERT INTO Materials (name, unit, notes) VALUES
  ('Cement', 'Bag (Bag)', 'OPC 53'),
  ('River Sand', 'Cubic metre (Cum)', 'M-Sand / river sand'),
  ('Bricks', 'Numbers (Nos)', 'Clay bricks'),
  ('TMT Rods', 'Kilogram (Kg)', 'Fe500'),
  ('Blue Metal / Jelly', 'Cubic metre (Cum)', '20mm'),
  ('Waterproof Chemical', 'Litre (L)', 'Super plasticizer');

-- ========== Purchases + items ==========
INSERT INTO Purchases (purchase_date, project_id, vendor_id, grand_total, is_paid, payment_mode, notes) VALUES
  ('2026-06-10', 1, 1, 125000, true, 'Bank Transfer', 'Cement order'),
  ('2026-06-18', 1, 3, 48000, false, NULL, 'Sand - unpaid'),
  ('2026-07-05', 2, 4, 36000, true, 'UPI', 'Bricks'),
  ('2026-07-09', 3, 5, 92000, false, NULL, 'TMT rods'),
  ('2026-07-20', 2, 2, 67500, true, 'Cash', 'Steel'),
  ('2026-07-25', 5, 1, 40000, false, NULL, 'Closing cement');

INSERT INTO PurchaseItems (purchase_id, material_id, qty, unit_cost, amount) VALUES
  (1, 1, 400, 312.5, 125000),
  (2, 2, 12, 4000, 48000),
  (3, 3, 12000, 3, 36000),
  (4, 4, 800, 115, 92000),
  (5, 4, 500, 135, 67500),
  (6, 1, 130, 307.69, 40000);

-- Purchase-linked expenses (same pattern as app)
INSERT INTO Expenses (expense_date, expense_type, project_id, category, amount, is_paid, payment_mode, notes, worklog_id, purchase_id) VALUES
  ('2026-06-10', 'Project Expense', 1, 'Materials Purchase', 125000, true, 'Bank Transfer', 'Vendor: Ramco Cements', NULL, 1),
  ('2026-06-18', 'Project Expense', 1, 'Materials Purchase', 48000, false, NULL, 'Vendor: Chennai Sand Supply', NULL, 2),
  ('2026-07-05', 'Project Expense', 2, 'Materials Purchase', 36000, true, 'UPI', 'Vendor: Kovai Bricks', NULL, 3),
  ('2026-07-09', 'Project Expense', 3, 'Materials Purchase', 92000, false, NULL, 'Vendor: Madurai TMT Store', NULL, 4),
  ('2026-07-20', 'Project Expense', 2, 'Materials Purchase', 67500, true, 'Cash', 'Vendor: Thyagaraja Steels', NULL, 5),
  ('2026-07-25', 'Project Expense', 5, 'Materials Purchase', 40000, false, NULL, 'Vendor: Ramco Cements', NULL, 6);

-- ========== Daily worklogs + materials used ==========
INSERT INTO DailyWorklogs (project_id, work_date, employee_id, work_description, notes) VALUES
  (1, '2026-07-20', 1, 'Foundation - Block A', 'Morning shift'),
  (1, '2026-07-22', 4, 'Column concrete', NULL),
  (2, '2026-07-21', 6, 'Brick work - ground floor', 'No rain delay'),
  (3, '2026-07-15', 7, 'Slab work', 'Late project'),
  (5, '2026-03-10', 2, 'Plastering finish', 'Near handover'),
  (1, '2026-07-28', 2, 'Site supervision', NULL);

INSERT INTO DailyWorklogMaterials (worklog_id, material_id, unit, qty, remarks) VALUES
  (1, 1, 'Bag (Bag)', 40, 'Foundation'),
  (1, 2, 'Cubic metre (Cum)', 2, NULL),
  (2, 1, 'Bag (Bag)', 55, NULL),
  (2, 5, 'Cubic metre (Cum)', 3, 'Jelly'),
  (3, 3, 'Numbers (Nos)', 2500, NULL),
  (4, 4, 'Kilogram (Kg)', 120, NULL),
  (5, 1, 'Bag (Bag)', 20, 'Plaster'),
  (6, 6, 'Litre (L)', 10, NULL);

-- Worklog-linked expenses (paid Cash, like app)
INSERT INTO Expenses (expense_date, expense_type, project_id, category, amount, is_paid, payment_mode, notes, worklog_id, purchase_id) VALUES
  ('2026-07-20', 'Project Expense', 1, 'Labour', 8500, true, 'Cash', 'Foundation labour', 1, NULL),
  ('2026-07-22', 'Project Expense', 1, 'Labour', 12000, true, 'Cash', 'Concrete labour', 2, NULL),
  ('2026-07-21', 'Project Expense', 2, 'Labour', 6500, true, 'Cash', 'Brick labour', 3, NULL),
  ('2026-07-15', 'Project Expense', 3, 'Transport', 3500, true, 'Cash', 'Material transport', 4, NULL),
  ('2026-03-10', 'Project Expense', 5, 'Labour', 9000, true, 'Cash', 'Plaster labour', 5, NULL);

-- Manual / company expenses
INSERT INTO Expenses (expense_date, expense_type, project_id, category, amount, is_paid, payment_mode, notes, worklog_id, purchase_id) VALUES
  ('2026-07-01', 'Company Expense', NULL, 'Office Rent', 25000, true, 'Bank Transfer', 'Chennai office rent', NULL, NULL),
  ('2026-07-12', 'Project Expense', 1, 'Tea Coffee', 1000, true, 'Cash', 'Site tea', NULL, NULL),
  ('2026-07-18', 'Company Expense', NULL, 'Fuel', 4500, false, NULL, 'Vehicle fuel', NULL, NULL),
  ('2026-07-26', 'Project Expense', 2, 'Tools', 2800, true, 'UPI', 'Small tools', NULL, NULL);

-- ========== Invoices ==========
INSERT INTO Invoices (project_id, invoice_purpose, invoice_date, due_date, invoice_amount, payment_date, status, is_paid, payment_mode, notes) VALUES
  (1, 'Foundation billing', '2026-05-01', '2026-05-20', 500000, '2026-05-18', 'Paid', true, 'Bank Transfer', 'First installment'),
  (1, 'Column / slab', '2026-07-01', '2026-07-20', 750000, NULL, 'Pending', false, NULL, 'Second installment'),
  (2, 'Initial billing', '2026-04-10', '2026-04-30', 300000, '2026-04-28', 'Paid', true, 'UPI', NULL),
  (3, 'Late project interim', '2026-06-01', '2026-06-15', 400000, NULL, 'Overdue', false, NULL, 'Pending payment'),
  (5, 'Final billing', '2026-04-01', '2026-04-15', 390000, '2026-04-12', 'Paid', true, 'Cheque', 'Full payment'),
  (4, 'Advance', '2026-07-10', '2026-07-25', 200000, NULL, 'Pending', false, NULL, 'Not started yet');

COMMIT;

-- Quick check
SELECT 'Roles' AS t, COUNT(*) FROM Roles
UNION ALL SELECT 'Employees', COUNT(*) FROM Employees
UNION ALL SELECT 'Clients', COUNT(*) FROM Clients
UNION ALL SELECT 'Projects', COUNT(*) FROM Projects
UNION ALL SELECT 'Vendors', COUNT(*) FROM Vendors
UNION ALL SELECT 'Materials', COUNT(*) FROM Materials
UNION ALL SELECT 'Purchases', COUNT(*) FROM Purchases
UNION ALL SELECT 'Expenses', COUNT(*) FROM Expenses
UNION ALL SELECT 'Invoices', COUNT(*) FROM Invoices
UNION ALL SELECT 'DailyWorklogs', COUNT(*) FROM DailyWorklogs;
