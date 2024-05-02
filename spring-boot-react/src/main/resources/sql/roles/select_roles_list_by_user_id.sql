select r.*
from user_roles ur, roles r
where ur.user_id = :id
and ur.role_id = r.id;