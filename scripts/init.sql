CREATE TABLE CATEGORY (
                          category VARCHAR(255) PRIMARY KEY
);

INSERT INTO Category (category) VALUES ('Comedy');
INSERT INTO Category (category) VALUES ('Action');
INSERT INTO Category (category) VALUES ('Drama');
INSERT INTO Category (category) VALUES ('Sci-Fi');

CREATE TABLE Regular_User (
                              id BIGSERIAL PRIMARY KEY,
                              email VARCHAR(255) NOT NULL,
                              password VARCHAR(255) NOT NULL,
                              role VARCHAR(255) NOT NULL
);
INSERT INTO Regular_User (email, password, role) VALUES ('user1@example.com', '$2a$10$bS9yNSSIzPOS9zo9pWsBhuNyydU9ojTGyeSDaAIzTIsWbGUgefFaC', 'USER');
INSERT INTO Regular_User (email, password, role) VALUES ('user2@example.com', '$2a$10$bS9yNSSIzPOS9zo9pWsBhuNyydU9ojTGyeSDaAIzTIsWbGUgefFaC', 'ADMIN');


CREATE TABLE Movie (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       release_year INT NOT NULL,
                       synopsis TEXT,
                       created_at DATE DEFAULT CURRENT_DATE,
                       created_by BIGSERIAL NOT NULL,
                       poster VARCHAR(255),
                       FOREIGN KEY (created_by) REFERENCES Regular_User(id)
);

-- Create the junction table for Movie and Category
CREATE TABLE Movie_Category (
                                movie_id BIGSERIAL NOT NULL,
                                category_id VARCHAR(255) NOT NULL,
                                PRIMARY KEY (movie_id, category_id),
                                FOREIGN KEY (movie_id) REFERENCES Movie(id),
                                FOREIGN KEY (category_id) REFERENCES Category(category)
);

-- CREATE TABLE Rating (
--                         id BIGSERIAL PRIMARY KEY,
--                         movie_id BIGINT NOT NULL,
--                         user_id BIGINT NOT NULL,
--                         stars INT NOT NULL,
--                         FOREIGN KEY (movie_id) REFERENCES Movie(id),
--                         FOREIGN KEY (user_id) REFERENCES Regular_User(id)
-- );

CREATE TABLE Rating (
                        user_id BIGINT NOT NULL,
                        movie_id BIGINT NOT NULL,
                        stars INT NOT NULL,
                        PRIMARY KEY (user_id, movie_id),
                        FOREIGN KEY (user_id) REFERENCES Regular_User(id) ON DELETE CASCADE,
                        FOREIGN KEY (movie_id) REFERENCES Movie(id) ON DELETE CASCADE
);

-- Insert sample movies into the Movie table
INSERT INTO Movie (name, release_year, synopsis, created_at, created_by) VALUES
                                                                 ('Inception', 2010, 'A mind-bending thriller about dreams within dreams.', CURRENT_DATE, 1),
                                                                 ('The Matrix', 1999, 'A hacker discovers the truth about his reality.', CURRENT_DATE, 1),
                                                                 ('Interstellar', 2014, 'A journey through space to save humanity.', CURRENT_DATE, 1);

INSERT INTO Movie_Category (movie_id, category_id) VALUES
                                                       (1, 'Sci-Fi'), -- Inception is Sci-Fi
                                                       --(1, 'Thriller'), -- Inception is also a Thriller
                                                       (2, 'Sci-Fi'), -- The Matrix is Sci-Fi
                                                       --(2, 'Action'), -- The Matrix is also Action
                                                       (3, 'Sci-Fi'), -- Interstellar is Sci-Fi
                                                       (3, 'Drama'); -- Interstellar is also Drama