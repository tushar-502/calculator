create database calc;

use calc;

CREATE TABLE calculations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    input VARCHAR(255) NOT NULL,
    result DOUBLE NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
