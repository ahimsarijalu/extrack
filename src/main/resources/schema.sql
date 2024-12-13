CREATE TABLE IF NOT EXISTS Expense (
    id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    category VARCHAR(255) NOT NULL,
    expense_date DATETIME NOT NULL,
    PRIMARY KEY (id)
);