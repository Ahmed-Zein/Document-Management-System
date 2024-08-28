INSERT INTO local_user(
    id, email, first_name, last_name, password, role, workspace_name)
VALUES (1, 'testa@test.com', 'firstName', 'lastName', '$2y$10$d28FoGS7mKUGJqokR3nXTOujCIQw6csxyb8nZCX9dPvRPJ3dJhj6G', 1, 'default');

INSERT INTO local_user(
    id, email, first_name, last_name, password, role, workspace_name)
VALUES
    (2, 'testB@test.com', 'firstName', 'lastName', '$2y$10$d28FoGS7mKUGJqokR3nXTOujCIQw6csxyb8nZCX9dPvRPJ3dJhj6G', 0, 'default1');
