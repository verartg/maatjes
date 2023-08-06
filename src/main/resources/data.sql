-- created accounts
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1001, 'Lisa de Jong', 39, 'V', '0645123456', 'Cedar Avenue', '24', '3456MN', 'Utrecht', 'Ik lees graag voor en ben ook handig met klussen.', true, false, 'DONDERDAGAVOND', 'EEN_KEER_PER_WEEK');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1002, 'Peter van Dijk', 45, 'M', '0645987654', 'Elm Avenue', '17', '4567OP', 'Amsterdam', 'Ik ben handig met klussen en maak graag tijd voor een gezellig kopje koffie.', true, true, 'WOENSDAGOCHTEND', 'TWEE_KEER_PER_WEEK');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1003, 'Sanne Bakker', 32, 'V', '0645123456', 'Boslaan', '10', '2345QR', 'Rotterdam', 'Ik hou van wandelen in mijn vrije tijd en wil graag anderen helpen.', true, false, 'MAANDAGOCHTEND', 'EENMALIG');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1004, 'Mark Jansen', 28, 'M', '0654789654', 'Willow Street', '8', '5678AB', 'Amsterdam', 'I enjoy gardening and can help with household chores.', true, false, 'VRIJDAGOCHTEND', 'EEN_KEER_PER_MAAND');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1005, 'Sophie van der Meer', 36, 'V', '0612345678', 'Oak Avenue', '12', '6789CD', 'Rotterdam', 'I love walking in nature and would like to help with administrative tasks.', true, false, 'WOENSDAGMIDDAG', 'TWEE_KEER_PER_MAAND');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1006, 'Tom de Vries', 43, 'M', '0654879521', 'Chestnut Street', '6', '7890EF', 'Utrecht', 'I enjoy taking care of pets and can also assist with shopping.', true, true, 'ZATERDAGOCHTEND', 'EEN_KEER_PER_WEEK');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1007, 'Emma Sanders', 31, 'V', '0632145698', 'Maple Avenue', '14', '8901GH', 'Amsterdam', 'I can knit and would love to provide companionship.', false, true, 'MAANDAGMIDDAG', 'TWEE_KEER_PER_WEEK');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1008, 'David van den Berg', 52, 'M', '0645698741', 'Beech Street', '18', '9012IJ', 'Rotterdam', 'I can help with household chores and provide administrative assistance.', true, true, 'VRIJDAGAVOND', 'DRIE_KEER_PER_WEEK');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1009, 'Anna Jacobs', 41, 'V', '0632145698', 'Linden Avenue', '10', '0123KL', 'Utrecht', 'I enjoy reading and can assist with gardening.', true, false, 'WOENSDAGOCHTEND', 'EEN_KEER_PER_MAAND');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1010, 'Lucas de Wit', 29, 'M', '0612345678', 'Cypress Street', '9', '2345MN', 'Amsterdam', 'I can help with household chores and provide companionship.', true, true, 'DONDERDAGOCHTEND', 'TWEE_KEER_PER_WEEK');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1011, 'Eva van der Linden', 35, 'V', '0654879521', 'Chestnut Street', '6', '3456OP', 'Rotterdam', 'I enjoy walking and can assist with administrative tasks.', true, false, 'MAANDAGMIDDAG', 'EEN_KEER_PER_WEEK');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1012, 'Tim Koster', 46, 'M', '0632145698', 'Willow Street', '8', '4567QR', 'Utrecht', 'I can help with gardening and provide companionship.', true, true, 'WOENSDAGAVOND', 'DRIE_KEER_PER_WEEK');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1013, 'Sarah Mulder', 33, 'V', '0645698741', 'Maple Avenue', '14', '5678ST', 'Amsterdam', 'I can knit and provide assistance with shopping.', false, true, 'ZONDAGOCHTEND', 'TWEE_KEER_PER_WEEK');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1014, 'Daniel Peters', 50, 'M', '0632145698', 'Beech Street', '18', '6789UV', 'Rotterdam', 'I can help with household chores and provide administrative assistance.', true, true, 'VRIJDAGMIDDAG', 'TWEE_KEER_PER_WEEK');
INSERT INTO accounts (account_id, name, age, sex, phone_number, street, house_number, postal_code, city, bio, gives_help, needs_help, availability, frequency) VALUES (1015, 'Julia Hendriks', 40, 'V', '0612345678', 'Linden Avenue', '10', '7890WX', 'Utrecht', 'I enjoy reading and can assist with gardening.', true, false, 'ZATERDAGAVOND', 'EEN_KEER_PER_WEEK');

--created users
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('admin', '$2a$12$JcVipE0dj3iS7H79ySeZZeciEKkz0vkNnXyEUsCO82HWcWT6revPy',true,null,'admin@admin.admin' );
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('lisa', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee',true,null,'lisa@user.nl' );
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('sanneb1', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee',true,null,'sanne@user.nl' );
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('peter1991', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee',true,null,'peter@user.nl' );
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('markjansen', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'mark@user.nl');
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('sophievandermeer', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'sophie@user.nl');
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('tomdevries', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'tom@user.nl');
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('emmasanders', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'emma@user.nl');
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('davidvandenberg', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'david@user.nl');
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('annajacobs', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'anna@user.nl');
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('lucasdewit', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'lucas@user.nl');
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('evavanderlinden', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'eva@user.nl');
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('timkoster', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'tim@user.nl');
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('sarahmulder', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'sarah@user.nl');
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('danielpeters', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'daniel@user.nl');
INSERT INTO users(username, password, enabled, apikey, email) VALUES ('juliahendriks', '$2a$12$zR4nKDSL/ob18z.z9nlvg./AM7llclJSb0ujAjUwkHKCeu.Zladee', true, null, 'julia@user.nl');

-- assigning accounts to users
UPDATE users SET account_id = 1001 WHERE username = 'lisa';
UPDATE users SET account_id = 1002 WHERE username = 'peter1991';
UPDATE users SET account_id = 1003 WHERE username = 'sanneb1';
UPDATE users SET account_id = 1004 WHERE username = 'markjansen';
UPDATE users SET account_id = 1005 WHERE username = 'sophievandermeer';
UPDATE users SET account_id = 1006 WHERE username = 'tomdevries';
UPDATE users SET account_id = 1007 WHERE username = 'emmasanders';
UPDATE users SET account_id = 1008 WHERE username = 'davidvandenberg';
UPDATE users SET account_id = 1009 WHERE username = 'annajacobs';
UPDATE users SET account_id = 1010 WHERE username = 'lucasdewit';
UPDATE users SET account_id = 1011 WHERE username = 'evavanderlinden';
UPDATE users SET account_id = 1012 WHERE username = 'timkoster';
UPDATE users SET account_id = 1013 WHERE username = 'sarahmulder';
UPDATE users SET account_id = 1014 WHERE username = 'danielpeters';
UPDATE users SET account_id = 1015 WHERE username = 'juliahendriks';

--assigning authorities to users.
INSERT INTO authorities(username, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authorities(username, authority) VALUES ('admin', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('lisa', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('sanneb1', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('peter1991', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('markjansen', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('sophievandermeer', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('tomdevries', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('emmasanders', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('davidvandenberg', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('annajacobs', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('lucasdewit', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('evavanderlinden', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('timkoster', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('sarahmulder', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('danielpeters', 'ROLE_USER');
INSERT INTO authorities(username, authority) VALUES ('juliahendriks', 'ROLE_USER');

--assigning receiving activities to accounts.
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1001, 'TUINIEREN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1001, 'NEDERLANDS_LEREN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1002, 'KLUSSEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1003, 'SCHOONMAKEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1003, 'GEZELSCHAP');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1004, 'BOODSCHAPPEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1004, 'HOND_UITLATEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1005, 'ADMINISTRATIE');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1005, 'BREIEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1006, 'VOORLEZEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1006, 'WANDELEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1007, 'HOND_UITLATEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1007, 'KLUSSEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1008, 'SCHOONMAKEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1008, 'TUINIEREN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1009, 'GEZELSCHAP');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1009, 'NEDERLANDS_LEREN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1010, 'WANDELEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1010, 'BOODSCHAPPEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1011, 'KLUSSEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1011, 'BREIEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1012, 'VOORLEZEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1012, 'HOND_UITLATEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1013, 'ADMINISTRATIE');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1013, 'SCHOONMAKEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1014, 'NEDERLANDS_LEREN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1014, 'BOODSCHAPPEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1015, 'WANDELEN');
INSERT INTO activities_to_receive (account_account_id, activity) VALUES (1015, 'KLUSSEN');

--assigning giving activities to accounts.
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1001, 'VOORLEZEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1001, 'GEZELSCHAP');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1002, 'TUINIEREN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1002, 'WANDELEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1003, 'HOND_UITLATEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1003, 'KLUSSEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1004, 'KLUSSEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1004, 'SCHOONMAKEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1005, 'SCHOONMAKEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1005, 'VOORLEZEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1006, 'BREIEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1006, 'NEDERLANDS_LEREN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1007, 'TUINIEREN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1007, 'SCHOONMAKEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1008, 'GEZELSCHAP');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1008, 'KLUSSEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1009, 'TUINIEREN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1009, 'WANDELEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1010, 'BREIEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1011, 'VOORLEZEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1011, 'HOND_UITLATEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1012, 'SCHOONMAKEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1012, 'NEDERLANDS_LEREN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1013, 'BREIEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1013, 'WANDELEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1014, 'KLUSSEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1014, 'WANDELEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1015, 'BREIEN');
INSERT INTO activities_to_give (account_account_id, activity) VALUES (1015, 'BOODSCHAPPEN');

--creating matches
INSERT INTO match (match_id, giver_accepted, receiver_accepted, contact_person, start_match, end_match, availability, frequency, help_giver_id, help_receiver_id) VALUES (1001, true, true, 'THOMAS', '2023-08-01', '2023-09-01', 'MAANDAGOCHTEND', 'EEN_KEER_PER_WEEK', 1001, 1002);
INSERT INTO match (match_id, giver_accepted, receiver_accepted, contact_person, start_match, end_match, availability, frequency, help_giver_id, help_receiver_id) VALUES (1002, false, false, 'THOMAS', '2023-08-01', '2023-09-01', 'MAANDAGOCHTEND', 'EEN_KEER_PER_WEEK', 1001, 1003);
INSERT INTO match (match_id, giver_accepted, receiver_accepted, contact_person, start_match, end_match, availability, frequency, help_giver_id, help_receiver_id) VALUES (1003, true, false, 'ESMEE', '2023-08-02', '2023-09-02', 'MAANDAGMIDDAG', 'TWEE_KEER_PER_WEEK', 1003, 1004);
INSERT INTO match (match_id, giver_accepted, receiver_accepted, contact_person, start_match, end_match, availability, frequency, help_giver_id, help_receiver_id) VALUES (1004, false, false, 'KEVIN', '2023-08-03', '2023-09-03', 'MAANDAGAVOND', 'EEN_KEER_PER_MAAND', 1005, 1006);
INSERT INTO match (match_id, giver_accepted, receiver_accepted, contact_person, start_match, end_match, availability, frequency, help_giver_id, help_receiver_id) VALUES (1005, true, true, 'MAAMKE', '2023-08-01', '2023-09-01', 'VRIJDAGMIDDAG', 'TWEE_KEER_PER_WEEK', 1007, 1008);

--assigning sharedActivities to matches
INSERT INTO match_activities (match_match_id, activities) VALUES (1004, 'VOORLEZEN');
INSERT INTO match_activities (match_match_id, activities) VALUES (1005, 'KLUSSEN');
INSERT INTO match_activities (match_match_id, activities) VALUES (1005, 'TUINIEREN');
INSERT INTO match_activities (match_match_id, activities) VALUES (1005, 'SCHOONMAKEN');
INSERT INTO match_activities (match_match_id, activities) VALUES (1003, 'SCHOONMAKEN');
INSERT INTO match_activities (match_match_id, activities) VALUES (1003, 'HOND_UITLATEN');
INSERT INTO match_activities (match_match_id, activities) VALUES (1002, 'GEZELSCHAP');
INSERT INTO match_activities (match_match_id, activities) VALUES (1001, 'TUINIEREN');

-- creating appointments
INSERT INTO appointment (id, date, start_time, end_time, description, created_by_name, created_for_name, match_id) VALUES (1001, '2023-08-01', '09:00:00', '10:00:00', 'Tuinieren', 'lisa', 'peter1991', 1001);
INSERT INTO appointment (id, date, start_time, end_time, description, created_by_name, created_for_name, match_id) VALUES (1002, '2023-08-02', '14:30:00', '15:30:00', 'Boodschappen doen', 'peter1991', 'lisa', 1001);
INSERT INTO appointment (id, date, start_time, end_time, description, created_by_name, created_for_name, match_id) VALUES (1003, '2023-08-05', '11:00:00', '12:00:00', 'Tuinieren', 'lisa', 'peter1991', 1001);
INSERT INTO appointment (id, date, start_time, end_time, description, created_by_name, created_for_name, match_id) VALUES (1004, '2023-08-07', '16:00:00', '17:00:00', 'Voorlezen', 'sophievandermeer', 'tomdevries', 1004);
INSERT INTO appointment (id, date, start_time, end_time, description, created_by_name, created_for_name, match_id) VALUES (1005, '2023-08-01', '13:30:00', '14:30:00', 'Klussen', 'emmasanders', 'davidvandenberg', 1005);
INSERT INTO appointment (id, date, start_time, end_time, description, created_by_name, created_for_name, match_id) VALUES (1006, '2023-08-01', '13:30:00', '14:30:00', 'Tuinieren', 'emmasanders', 'davidvandenberg', 1005);
INSERT INTO appointment (id, date, start_time, end_time, description, created_by_name, created_for_name, match_id) VALUES (1007, '2023-08-01', '13:30:00', '14:30:00', 'Schoonmaken', 'emmasanders', 'davidvandenberg', 1005);

-- creating reviews
INSERT INTO review (id, rating, description, verified, feedback_admin, date, match_id, written_by_id, written_for_id) VALUES (1001, 4.5, 'Geweldige match! Samenwerken was een plezier.', false, null, '2023-07-15', 1001, 1001, 1002);
INSERT INTO review (id, rating, description, verified, feedback_admin, date, match_id, written_by_id, written_for_id) VALUES (1002, 3.8, 'Goede communicatie, maar kan de tijd beter beheren.', false, null, '2023-07-17', 1002, 1003, 1001);
INSERT INTO review (id, rating, description, verified, feedback_admin, date, match_id, written_by_id, written_for_id) VALUES (1003, 5.0, 'Uitstekende match! Een aanrader.', true, null, '2023-07-20', 1003, 1004, 1003);
INSERT INTO review (id, rating, description, verified, feedback_admin, date, match_id, written_by_id, written_for_id) VALUES (1004, 1, 'Wat een @£$%^&*', false, 'Je kunt geen scheldwoorden gebruiken, je review is niet goedgekeurd.', '2023-07-22', 1004, 1005, 1006);
INSERT INTO review (id, rating, description, verified, feedback_admin, date, match_id, written_by_id, written_for_id) VALUES (1005, 4.9, 'Fantastische ervaring! Ging boven verwachting.', true, null, '2023-07-25', 1005, 1007, 1008);

--creating messages
INSERT INTO message (id, written_by_name, content, created_at, created_at_date, match_id) VALUES (1001, 'lisa', 'Hoi, hoe gaat het? Binnenkort weer eens afpsreken?', '09:30:00', '2023-07-26', 1001);
INSERT INTO message (id, written_by_name, content, created_at, created_at_date, match_id) VALUES (1002, 'lisa', 'Hoe is het afgelopen week gegaan?', '09:30:00', '2023-07-26', 1002);
INSERT INTO message (id, written_by_name, content, created_at, created_at_date, match_id) VALUES (1003, 'sanneb1', 'Prima, super bedankt nog voor je hulp. Hoe gaat het met jou?', '09:30:00', '2023-07-26', 1002);
INSERT INTO message (id, written_by_name, content, created_at, created_at_date, match_id) VALUES (1004, 'lisa', 'Goed hoor, bedankt!', '10:00:00', '2023-07-26', 1002);
INSERT INTO message (id, written_by_name, content, created_at, created_at_date, match_id) VALUES (1005, 'markjansen', 'Hoi, hoe gaat het?', '09:30:00', '2023-07-26', 1003);
INSERT INTO message (id, written_by_name, content, created_at, created_at_date, match_id) VALUES (1006, 'sanneb1', 'Goed hoor, bedankt!', '10:00:00', '2023-07-26', 1003);
INSERT INTO message (id, written_by_name, content, created_at, created_at_date, match_id) VALUES (1007, 'sophievandermeer', 'Hoi, heb je nog kunnen oefenen?', '09:30:00', '2023-07-26', 1004);
INSERT INTO message (id, written_by_name, content, created_at, created_at_date, match_id) VALUES (1008, 'tomdevries', 'Nee, geen tijd gehad deze week. Volgende week!', '10:00:00', '2023-07-26', 1004);
INSERT INTO message (id, written_by_name, content, created_at, created_at_date, match_id) VALUES (1009, 'davidvandenberg', 'Hoi, hoe gaat het?', '09:30:00', '2023-07-26', 1005);
INSERT INTO message (id, written_by_name, content, created_at, created_at_date, match_id) VALUES (1010, 'emmasanders', 'Goed hoor, bedankt!', '10:00:00', '2023-07-26', 1005);

