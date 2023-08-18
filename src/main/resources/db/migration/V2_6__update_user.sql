ALTER TABLE clinic_user ADD CONSTRAINT unique_email UNIQUE(email);
ALTER TABLE clinic_user ADD CONSTRAINT unique_username UNIQUE(username);
