insert into "users_log" ("user_id", "operation", "id", "column_affect", "old_value", "new_value")
values (:userId, :operation, :id, :columnAffect, :oldValue::JSON, :newValue::JSON);