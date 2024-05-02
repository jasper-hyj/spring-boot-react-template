select a.*
from user_authorities ua, authorities a
where ua.user_id = :id
and ua.authority_id = a.id;