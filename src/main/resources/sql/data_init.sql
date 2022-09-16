INSERT INTO roflan_user (uuid, username, password, enabled)
VALUES ('173344f6-f727-4daf-979f-1e95f4470c64', 'admin', '$2a$12$i80oeLBmZMJETmiXYPP.E./u0eqKA9l9Zrxpt9yk6UHVOp/CvZMpe', true);

INSERT INTO role (id, name) VALUES (1, 'ADMIN');
INSERT INTO role (id, name) VALUES (2, 'USER');

INSERT INTO roflan_user_roles (roflan_user_uuid, roles_id) VALUES ('173344f6-f727-4daf-979f-1e95f4470c64', 1);

INSERT INTO roflan_user (uuid, username, password, enabled)
VALUES ('68594f21-5bc4-4e57-8d0d-2b9d396a2aef', 'user', '$2a$12$Gz/vb2PDCmIboIqMJsZjNOzohrAVlhMQ3NOCSZJ5gqy8WuhLf/OzO', true);

INSERT INTO roflan_user_roles (roflan_user_uuid, roles_id) VALUES ('68594f21-5bc4-4e57-8d0d-2b9d396a2aef', 2);
