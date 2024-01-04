CREATE TABLE test_db.expense (
                                 id BIGINT NOT NULL AUTO_INCREMENT,
                                 name VARCHAR(255),
                                 description VARCHAR(255),
                                 amount LONG,
                                 date DATE,
                                 expense_id VARCHAR(255) NOT NULL UNIQUE,
                                expense_type VARCHAR(255),
                                 user_id BIGINT NOT NULL,
                                 PRIMARY KEY (id),
                                 FOREIGN KEY (user_id) REFERENCES test_db.user_db(id)
);