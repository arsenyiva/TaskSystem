DROP  TABLE users cascade ;
DROP  TABLE comments cascade ;
DROP  TABLE tasks cascade ;
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT NOT NULL,
                       status VARCHAR(50) NOT NULL,
                       priority VARCHAR(50) NOT NULL,
                       author_id INTEGER NOT NULL,
                       assignee_id INTEGER,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP,
                       CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users(id),
                       CONSTRAINT fk_assignee FOREIGN KEY (assignee_id) REFERENCES users(id)
);

CREATE TABLE comments (
                          id SERIAL PRIMARY KEY,
                          task_id INTEGER NOT NULL,
                          author_id INTEGER NOT NULL,
                          content TEXT NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES tasks(id),
                          CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users(id)
);
