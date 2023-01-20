#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	CREATE USER $DEMO_USER WITH PASSWORD "$DEMO_PASSWORD";
	CREATE DATABASE $POSTGRES_DB;
	GRANT ALL PRIVILEGES ON DATABASE $POSTGRES_DB TO $DEMO_USER;
	create schema if not exists $SCHEMA;
	create table $SCHEMA.users (
	    id serial primary key,
        firstname text not null,
        lastname text not null,
        phone text not null,
        email text not null,
        created_at timestamp not null default now(),
        updated_at timestamp not null default now()
    );
    create or replace procedure update_updated_at_column() as $$ begin new.updated_at = now(); return new; end; $$ language plpgsql;
    create trigger update_users_updated_at before update on $SCHEMA.users for each row execute procedure update_updated_at_column();
EOSQL