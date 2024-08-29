INSERT INTO local_user(
   email, first_name, last_name, password, role, workspace_name)
VALUES ( 'testa@test.com', 'firstName', 'lastName', '$2y$10$d28FoGS7mKUGJqokR3nXTOujCIQw6csxyb8nZCX9dPvRPJ3dJhj6G', 1, 'default');

INSERT INTO local_user(
    email, first_name, last_name, password, role, workspace_name)
VALUES
    ( 'testB@test.com', 'firstName', 'lastName', '$2y$10$d28FoGS7mKUGJqokR3nXTOujCIQw6csxyb8nZCX9dPvRPJ3dJhj6G', 0, 'default1');

-- Insert the first directory
INSERT INTO public.directory(
    created_at, name, is_public, user_id)
VALUES
    (NOW(), 'dir1', TRUE, 1);

-- Insert the second directory
INSERT INTO public.directory(
    created_at, name, is_public, user_id)
VALUES
    (NOW(), 'dir2', TRUE, 1);
